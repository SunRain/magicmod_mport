
package com.magicmod.mport.v5.widget;

import android.app.ActionBar;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import com.google.android.collect.Lists;
import com.magicmod.mport.v5.android.support.app.FragmentPagerAdapter;
import com.magicmod.mport.v5.android.support.view.ViewPager;
import com.magicmod.mport.v5.app.MiuiViewPagerItem;
import com.magicmod.mport.v5.widget.TabController.TabEditCommand;

import java.lang.ref.WeakReference;
import java.util.Iterator;
import java.util.List;

public class TabController implements TabContainerLayout.TransactionFractory {
    static final String TAG = TabController.class.getName();
    final DynamicPagerAdapter mAdapter;
    PagerAdapterChangedListener mAdapterChangedListener;
    ActionBar.TabListener mInternalTabListener;
    final TabContainerLayout mTabContainer;
    TabIndicator mTabIndicator;
    final CooperativeViewPager mViewPager;
    ViewPager.OnPageChangeListener mViewPagerListener;
    
    
    private final ViewPager.OnPageChangeListener mViewPagerListenerDecorator = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrollStateChanged(int state) {
            mViewPagerState = state;
            if (mViewPagerListener != null)
                mViewPagerListener.onPageScrollStateChanged(state);
        }

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            if (mTabIndicator != null)
                mTabIndicator.apply(position, positionOffset);
            if (mViewPagerListener != null)
                mViewPagerListener.onPageScrolled(position, positionOffset, positionOffsetPixels);
        }

        @Override
        public void onPageSelected(int position) {
            if ((position >= 0) && (position < mTabContainer.getTabCount())
                    && (position != mTabContainer.findCurrentTabPos())) {
                selectTab(position);
            }
            invalideFragmentMenu(position);
            if (mViewPagerListener != null)
                mViewPagerListener.onPageSelected(position);
        }
    };
    
    int mViewPagerState = 0;

    public TabController(Activity a, TabContainerLayout container, CooperativeViewPager pager,
            TabIndicator indicator) {
        this.mTabContainer = container;
        this.mTabContainer.setTransactionFractory(this);
        
        container.setTabWidthChangedListener(new TabContainerLayout.TabWidthChangedListener() {
            @Override
            public void onTabWidthChanged(TabContainerLayout tabContainer) {
                mTabIndicator.setTabWidth(tabContainer.getTabWidth());
                if (mViewPagerState == 0)
                    mTabIndicator.apply(getSelectedPosition(), 0.0F);
            }
        });
        
        setTabIndicator(indicator);
        this.mAdapter = new DynamicPagerAdapter(a.getFragmentManager());
        this.mViewPager = pager;
        this.mViewPager.setAdapter(this.mAdapter);
        this.mViewPager.setOnPageChangeListener(this.mViewPagerListenerDecorator);
    }

    public void addTab(ActionBar.Tab tab, Fragment fragment) {
        addTab(tab, fragment, false);
    }

    public void addTab(ActionBar.Tab tab, Fragment fragment, boolean isMenuBarEnabled) {
        newTabEditor().addTab(tab, fragment, isMenuBarEnabled).commit();
    }

    public FragmentTransaction beginTransaction() {
        return this.mAdapter.getFragmentManager().beginTransaction().disallowAddToBackStack();
    }

    public Fragment findFragmentByTag(String tag) {
        return this.mAdapter.findFragmentByTag(tag);
    }

    public Fragment getFragment(int position) {
        return this.mAdapter.getItem(position);
    }

    public int getFragmentCount() {
        return this.mAdapter.getCount();
    }

    public Fragment getSelectedFragment() {
        return this.mAdapter.getItem(getSelectedPosition());
    }

    public int getSelectedPosition() {
        return this.mViewPager.getCurrentItem();
    }

    public ActionBar.Tab getSelectedTab() {
        return this.mTabContainer.getSelectedTab();
    }

    public ActionBar.Tab getTabAt(int position) {
        return this.mTabContainer.getTabAt(position);
    }

    public TabContainerLayout getTabContainer() {
        return this.mTabContainer;
    }

    public ViewPager getViewPager() {
        return this.mViewPager;
    }

    public int getViewPagerState() {
        return this.mViewPagerState;
    }

    public void invalideFragmentMenu() {
        invalideFragmentMenu(getSelectedPosition());
    }

    /*void invalideFragmentMenu(int paramInt) {
        int i = 0;
        Iterator localIterator = this.mAdapter.mFragments.iterator();
        if (localIterator.hasNext()) {
            Fragment localFragment = (Fragment) localIterator.next();
            if (i == paramInt)
                ;
            for (boolean bool = true;; bool = false) {
                localFragment.setMenuVisibility(bool);
                i++;
                break;
            }
        }
    }*/
    void invalideFragmentMenu(int selected) {
        int i = 0;
        Iterator<Fragment> iterator = mAdapter.mFragments.iterator();
        while (iterator.hasNext()) {
            Fragment f = (Fragment) iterator.next();
            boolean flag;
            if (i == selected)
                flag = true;
            else
                flag = false;
            f.setMenuVisibility(flag);
            i++;
        }
    }

    public ActionBar.Tab newTab() {
        return this.mTabContainer.newTab(new TabListenerProxy(null));
    }

    public ActionBar.Tab newTab(ActionBar.TabListener l) {
        return this.mTabContainer.newTab(new TabListenerProxy(l));
    }

    public TabEditor newTabEditor() {
        return new TabEditorImpl(this);
    }

    /*protected void onTabSelected(ActionBar.Tab tab) {
        if (tab == null)
            ;
        while (true) {
            return;
            int i = this.mTabContainer.findTabPosition(tab);
            if ((i >= 0) && (i < this.mAdapter.getCount())
                    && (i != this.mViewPager.getCurrentItem()))
                this.mViewPager.setCurrentItem(i, true);
        }
    }*/
    protected void onTabSelected(android.app.ActionBar.Tab tab) {
        if (tab != null) {
            int pos = mTabContainer.findTabPosition(tab);
            if (pos >= 0 && pos < mAdapter.getCount() && pos != mViewPager.getCurrentItem())
                mViewPager.setCurrentItem(pos, true);
        }
    }

    public void release() {
        this.mViewPager.setAdapter(null);
        this.mTabContainer.detachIndicator(this.mTabIndicator);
    }

    public void removeAll() {
        TabEditor editor = newTabEditor();
        int count = this.mTabContainer.getTabCount();
        for (int i = 0; i < count; i++)
            editor.removeTab(this.mTabContainer.getTabAt(i));
        editor.commit();
    }

    public void removeTab(ActionBar.Tab tab) {
        if (tab != null)
            newTabEditor().removeTab(tab).commit();
    }

    public void removeTabAt(int position) {
        removeTab(this.mTabContainer.getTabAt(position));
    }

    public boolean selectTab(int position) {
        return selectTab(this.mTabContainer.getTabAt(position));
    }

    public boolean selectTab(ActionBar.Tab tab) {
        return this.mTabContainer.selectTab(tab);
    }

    /*public void setCooperative(boolean paramBoolean) {
        boolean bool1 = true;
        TabContainerLayout localTabContainerLayout = this.mTabContainer;
        boolean bool2;
        CooperativeViewPager localCooperativeViewPager;
        if (!paramBoolean) {
            bool2 = bool1;
            localTabContainerLayout.setInteractive(bool2);
            localCooperativeViewPager = this.mViewPager;
            if (paramBoolean)
                break label43;
        }
        while (true) {
            localCooperativeViewPager.setDraggable(bool1);
            return;
            bool2 = false;
            break;
            label43: bool1 = false;
        }
    }*/
    public void setCooperative(boolean enabled) {
        if (!enabled) {
            mTabContainer.setInteractive(true);
            mViewPager.setDraggable(true);
        } else {
            mTabContainer.setInteractive(false);
            mViewPager.setDraggable(false);
        }

    }

    public void setInternalTabListener(ActionBar.TabListener listener) {
        this.mInternalTabListener = listener;
    }

    public void setPagerAdapterChangedListener(PagerAdapterChangedListener l) {
        this.mAdapterChangedListener = l;
    }

    public void setTabIndicator(TabIndicator indicator) {
        if (mTabIndicator != null)
            mTabContainer.detachIndicator(mTabIndicator);
        mTabIndicator = indicator;
        if (indicator != null)
            mTabContainer.attachIndicator(indicator);
    }

    public void setTabIndicatorImage(Drawable d) {
        this.mTabIndicator.setIndicator(d);
    }

    public void setViewPagerBackground(Drawable d) {
        this.mViewPager.setBackground(d);
    }

    public void setViewPagerListener(ViewPager.OnPageChangeListener l) {
        this.mViewPagerListener = l;
    }

    private static class DynamicPagerAdapter extends FragmentPagerAdapter {
        private final FragmentManager mFragmentManager;
        final List<Fragment> mFragments = Lists.newArrayList();

        public DynamicPagerAdapter(FragmentManager fm) {
            super(fm);
            this.mFragmentManager = fm;
        }

        void addFragment(Fragment fragment) {
            this.mFragments.add(fragment);
        }

        /*Fragment findFragmentByTag(String paramString) {
            Fragment localFragment;
            if (paramString != null) {
                Iterator<Fragment> localIterator = this.mFragments.iterator();
                do {
                    if (!localIterator.hasNext())
                        break;
                    localFragment = (Fragment) localIterator.next();
                } while (!paramString.equals(localFragment.getTag()));
            }
            while (true) {
                return localFragment;
                localFragment = null;
            }
        }*/
        Fragment findFragmentByTag(String tag) {
            if (tag != null) { //cond_1 in
                Iterator<Fragment> i = mFragments.iterator(); //v1
                while (i.hasNext()) {
                    Fragment f = (Fragment) i.next(); //v0
                    if (f.getTag().equals(tag)) {
                        return f;
                    }
                }
            } //cond_1 after
            return null;
        }

        public int getCount() {
            return this.mFragments.size();
        }

        FragmentManager getFragmentManager() {
            return this.mFragmentManager;
        }

        public Fragment getItem(int position) {
            Fragment fragment;
            if (position >= 0 && position < mFragments.size())
                fragment = (Fragment) mFragments.get(position);
            else
                fragment = null;
            return fragment;
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public boolean isViewFromObject(View view, Object object) {
            boolean flag;
            if (((Fragment) object).getView() == view)
                flag = true;
            else
                flag = false;
            return flag;
        }

        boolean removeFragment(Fragment fragment) {
            return this.mFragments.remove(fragment);
        }
    }

    public static abstract interface PagerAdapterChangedListener {
        public abstract void onAdapterChanged(ViewPager paramViewPager, boolean paramBoolean);
    }

    private static class TabEditAdd implements TabController.TabEditCommand {
        private final Fragment mFragment;
        private final boolean mIsMenuBarEnabled;
        private final ActionBar.Tab mTab;
        private final String mTag;

        public TabEditAdd(ActionBar.Tab tab, Fragment fragment, String tag, boolean isMenuBarEnabled) {
            this.mTab = tab;
            this.mFragment = fragment;
            this.mTag = tag;
            this.mIsMenuBarEnabled = isMenuBarEnabled;
        }

        public boolean execute(TabContainerLayout container, ViewPager pager,
                DynamicPagerAdapter adapter, FragmentTransaction transaction, FragmentManager fm) {
            
            container.addTab(mTab, false);
            if (mFragment != null) {
                if (mFragment instanceof MiuiViewPagerItem)
                    ((MiuiViewPagerItem) mFragment).setMenuBarEnabled(mIsMenuBarEnabled);
                adapter.addFragment(mFragment);
                Fragment old = null;
                if (mTag != null)
                    old = fm.findFragmentByTag(mTag);
                if (old == null)
                    transaction.add(pager.getId(), mFragment, mTag);
                else if (old != mFragment) {
                    Log.w(TabController.TAG,
                            (new StringBuilder())
                                    .append("Fragment with same tag or id, but different instance, already exists! ")
                                    .append(mFragment).toString());
                    transaction.add(pager.getId(), mFragment, mTag);
                } else {
                    Log.v(TabController.TAG, (new StringBuilder()).append("Fragment is exists! ")
                            .append(mFragment).toString());
                }
            }
            return true;
        }
    }

    public static abstract interface TabEditCommand {
        public abstract boolean execute(TabContainerLayout container, ViewPager pager,
                TabController.DynamicPagerAdapter adapter, FragmentTransaction transaction,
                FragmentManager fm);
    }

    private static class TabEditRemove implements TabController.TabEditCommand {
        private final ActionBar.Tab mTab;

        public TabEditRemove(ActionBar.Tab tab) {
            this.mTab = tab;
        }

        public boolean execute(TabContainerLayout container, ViewPager pager,
                TabController.DynamicPagerAdapter adapter, FragmentTransaction transaction,
                FragmentManager fm) {
            int ipos = container.findTabPosition(this.mTab);
            container.removeTab(this.mTab);
            Fragment fragment = adapter.getItem(ipos);
            if (fragment != null) {
                adapter.removeFragment(fragment);
                transaction.remove(fragment);
            }
            return true;
        }
    }

    public static abstract interface TabEditor {
        public abstract TabEditor add(TabController.TabEditCommand command);

        public abstract TabEditor addTab(ActionBar.Tab tab, Fragment fragment);

        public abstract TabEditor addTab(ActionBar.Tab tab, Fragment fragment, String fragmentTag);

        public abstract TabEditor addTab(ActionBar.Tab tab, Fragment fragment, String fragmentTag,
                boolean isMenuBarEnabled);

        public abstract TabEditor addTab(ActionBar.Tab tab, Fragment fragment,
                boolean isMenuBarEnabled);

        public abstract boolean commit();

        public abstract TabEditor removeTab(ActionBar.Tab tab);
    }

    private static class TabEditorImpl implements TabController.TabEditor {
        private final List<TabController.TabEditCommand> mCommands = Lists.newArrayList();
        private final WeakReference<TabController> mTabControllerRef;

        public TabEditorImpl(TabController controller) {
            this.mTabControllerRef = new WeakReference<TabController>(controller);
        }

        @Override
        public TabController.TabEditor add(TabController.TabEditCommand command) {
            if (command != null)
                this.mCommands.add(command);
            return null;
        }

        @Override
        public TabController.TabEditor addTab(ActionBar.Tab tab, Fragment fragment) {
            return addTab(tab, fragment, null, false);
        }

        @Override
        public TabController.TabEditor addTab(ActionBar.Tab tab, Fragment fragment,
                String fragmentTag) {
            return addTab(tab, fragment, fragmentTag, false);
        }

        @Override
        public TabController.TabEditor addTab(ActionBar.Tab tab, Fragment fragment,
                String fragmentTag, boolean isMenuBarEnabled) {
            if (tab == null)
                throw new NullPointerException("tab and fragment cannot be null! tab=" + tab
                        + " fragment=" + fragment);
            add(new TabController.TabEditAdd(tab, fragment, fragmentTag, isMenuBarEnabled));
            return this;
        }

        @Override
        public TabController.TabEditor addTab(ActionBar.Tab tab, Fragment fragment,
                boolean isMenuBarEnabled) {
            return addTab(tab, fragment, null, isMenuBarEnabled);
        }

        @Override
        /*public boolean commit() {
            boolean bool1 = false;
            TabController localTabController = (TabController) this.mTabControllerRef.get();
            if (localTabController == null)
                ;
            while (true) {
                return bool1;
                if (this.mCommands.size() <= 0) {
                    bool1 = true;
                } else {
                    CooperativeViewPager localCooperativeViewPager = localTabController.mViewPager;
                    TabController.DynamicPagerAdapter localDynamicPagerAdapter = localTabController.mAdapter;
                    FragmentManager localFragmentManager = localDynamicPagerAdapter
                            .getFragmentManager();
                    TabContainerLayout localTabContainerLayout = localTabController.mTabContainer;
                    FragmentTransaction localFragmentTransaction = localFragmentManager
                            .beginTransaction();
                    Iterator<TabEditCommand> localIterator = this.mCommands.iterator();
                    while (localIterator.hasNext())
                        ((TabController.TabEditCommand) localIterator.next()).execute(
                                localTabContainerLayout, localCooperativeViewPager,
                                localDynamicPagerAdapter, localFragmentTransaction,
                                localFragmentManager);
                    localFragmentTransaction.commitAllowingStateLoss();
                    localFragmentManager.executePendingTransactions();
                    localDynamicPagerAdapter.notifyDataSetChanged();
                    boolean bool2 = false;
                    if (localTabContainerLayout.getSelectedTab() == null) {
                        localTabContainerLayout.selectTabAt(0);
                        bool2 = true;
                    }
                    TabController.PagerAdapterChangedListener localPagerAdapterChangedListener = localTabController.mAdapterChangedListener;
                    if (localPagerAdapterChangedListener != null)
                        localPagerAdapterChangedListener.onAdapterChanged(
                                localCooperativeViewPager, bool2);
                    localTabController.invalideFragmentMenu();
                    localTabContainerLayout.updateTabPosition();
                    this.mCommands.clear();
                    bool1 = true;
                }
            }
        }*/
        public boolean commit() {
            boolean flag = false;
            TabController controller = (TabController) mTabControllerRef.get();
            if (controller != null)
                if (mCommands.size() <= 0) {
                    flag = true;
                } else {
                    CooperativeViewPager pager = controller.mViewPager;
                    DynamicPagerAdapter adapter = controller.mAdapter;
                    FragmentManager manager = adapter.getFragmentManager();
                    TabContainerLayout containter = controller.mTabContainer;
                    FragmentTransaction transaction = manager.beginTransaction();
                    /*
                     * for (Iterator<TabEditCommand> iterator =
                     * mCommands.iterator(); iterator.hasNext();
                     * ((TabEditCommand) iterator .next()).execute(containter,
                     * pager, adapter, transaction, manager)) ;
                     */
                    Iterator<TabEditCommand> i = mCommands.iterator();
                    while (i.hasNext()) {
                        TabController.TabEditCommand command = (TabController.TabEditCommand) i
                                .next();
                        command.execute(containter, pager, adapter, transaction, manager);

                    }
                    transaction.commitAllowingStateLoss();
                    manager.executePendingTransactions();
                    adapter.notifyDataSetChanged();
                    boolean selectedChanged = false;
                    if (containter.getSelectedTab() == null) {
                        containter.selectTabAt(0);
                        selectedChanged = true;
                    }
                    PagerAdapterChangedListener l = controller.mAdapterChangedListener;
                    if (l != null)
                        l.onAdapterChanged(pager, selectedChanged);
                    controller.invalideFragmentMenu();
                    containter.updateTabPosition();
                    mCommands.clear();
                    flag = true;
                }
            return flag;
        }

        @Override
        public TabController.TabEditor removeTab(ActionBar.Tab tab) {
            if (tab == null)
                throw new NullPointerException("tab and fragment cannot be null! tab=" + tab);
            add(new TabController.TabEditRemove(tab));
            return this;
        }
    }

    private class TabListenerProxy implements ActionBar.TabListener {
        private final ActionBar.TabListener mTabListener;

        public TabListenerProxy(android.app.ActionBar.TabListener l)
        {
            super();
            mTabListener = l;
        }

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mInternalTabListener != null)
                mInternalTabListener.onTabReselected(tab, ft);
            if (this.mTabListener != null)
                this.mTabListener.onTabReselected(tab, ft);
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            TabController.this.onTabSelected(tab); 
            if (mInternalTabListener != null)
                mInternalTabListener.onTabSelected(tab, ft);
            if (this.mTabListener != null)
                this.mTabListener.onTabSelected(tab, ft);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            if (mInternalTabListener != null)
                mInternalTabListener.onTabUnselected(tab, ft);
            if (this.mTabListener != null)
                this.mTabListener.onTabUnselected(tab, ft);
        }
    }
}
