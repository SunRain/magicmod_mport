
package com.magicmod.mport.internal.v5.app;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

import com.magicmod.mport.internal.v5.widget.ActionBarContainer;
import com.magicmod.mport.v5.app.MiuiActionBar;
import com.magicmod.mport.v5.view.ViewPager;
import com.magicmod.mport.v5.view.ViewPager.OnPageChangeListener;
import com.miui.internal.R;
import java.util.ArrayList;
import java.util.Iterator;

public class ViewPagerController {
    private ActionBarImpl mActionBar;
    private ObjectAnimator mActionMenuChangeAnimator;
    private ActionMenuChangeAnimatorObject mActionMenuChangeAnimatorObject;
    private ArrayList<MiuiActionBar.FragmentViewPagerChangeListener> mListeners;
    private DynamicFragmentPagerAdapter mPagerAdapter;
    
    private ActionBar.TabListener mTabListener;
    private ViewPager mViewPager;

    ViewPagerController(ActionBarImpl actionBar, FragmentManager fm, boolean allowListAnimation) {

        mTabListener = new ActionBar.TabListener() {
            @Override
            public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }

            @Override
            public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
                int size = mPagerAdapter.getCount();
                for (int i = 0; i < size; i++)
                    if (mPagerAdapter.getTabAt(i) == tab) {
                        mViewPager.setCurrentItem(i, true);
                        // break;
                    }
            }

            @Override
            public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            }
        };

        mActionBar = actionBar;
        Context context = mActionBar.getWindow().getContext();
        View view = mActionBar.getWindow().findViewById(/* 0x60b00b5 */R.id.view_pager);
        if (view instanceof ViewPager) {
            mViewPager = (ViewPager) view;
        } else {
            mViewPager = new ViewPager(context);
            mViewPager.setId(/* 0x60b00b5 */R.id.view_pager);
            mActionBar.getWindow().setContentView(mViewPager);
        }

        mPagerAdapter = new DynamicFragmentPagerAdapter(context, fm, mViewPager);

        mViewPager.setInternalPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                mActionBar.setSelectedNavigationItem(position);
                mPagerAdapter.setPrimaryItem(mViewPager, position,
                        mPagerAdapter.getFragment(position, true));
                if (mListeners != null) { // cond_0 in
                    for (Iterator i = mListeners.iterator(); i.hasNext(); i.next()) {
                        MiuiActionBar.FragmentViewPagerChangeListener listener = (MiuiActionBar.FragmentViewPagerChangeListener) i;
                        listener.onPageSelected(position);
                    }
                }
            }

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                boolean fromHasActionMenu = mPagerAdapter.hasActionMenu(position); // v0
                boolean toHasActionMenu;
                if (position + 1 < mPagerAdapter.getCount()) { // cond_0 in
                    toHasActionMenu = mPagerAdapter.hasActionMenu(position + 1); // v3
                } else {
                    toHasActionMenu = false;
                }
                // goto_0
                if (mListeners != null) { // cond_1 in
                    // .local v1, i$:Ljava/util/Iterator;
                    for (Iterator i = mListeners.iterator(); i.hasNext(); i.next()) {
                        MiuiActionBar.FragmentViewPagerChangeListener listener = (MiuiActionBar.FragmentViewPagerChangeListener) i;
                        listener.onPageScrolled(position, positionOffset, fromHasActionMenu,
                                toHasActionMenu);
                    }
                } // cond_1 after
                  // } //cond_0 after
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                if (mListeners != null) {
                    for (Iterator i = mListeners.iterator(); i.hasNext(); i.next()) {
                        MiuiActionBar.FragmentViewPagerChangeListener listener = (MiuiActionBar.FragmentViewPagerChangeListener) i;
                        listener.onPageScrollStateChanged(state);
                    }
                }
            }
        });

        if (allowListAnimation)
            addOnFragmentViewPagerChangeListener(new ViewPagerScrollEffect(mViewPager,
                    mPagerAdapter));
    }
    
    int addFragmentTab(String tag, ActionBar.Tab tab, int index,
            Class<? extends Fragment> fragment, Bundle args, boolean hasActionMenu) {
        ((ActionBarImpl.TabImpl) tab).setSystemTabListener(mTabListener);
        mActionBar.internalAddTab(tab, index);
        return mPagerAdapter.addFragment(tag, index, fragment, args, tab, hasActionMenu);
    }

    int addFragmentTab(String tag, ActionBar.Tab tab, Class<? extends Fragment> fragment,
            Bundle args, boolean hasActionMenu) {
        ((ActionBarImpl.TabImpl) tab).setSystemTabListener(mTabListener);
        mActionBar.internalAddTab(tab);
        return mPagerAdapter.addFragment(tag, fragment, args, tab, hasActionMenu);
    }

    void addOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener listener) {
        if (mListeners == null)
            mListeners = new ArrayList<MiuiActionBar.FragmentViewPagerChangeListener>();
        mListeners.add(listener);
    }

    Fragment getFragmentAt(int position) {
        return this.mPagerAdapter.getFragment(position, true);
    }

    int getFragmentTabCount() {
        return this.mPagerAdapter.getCount();
    }

    int getViewPagerOffscreenPageLimit() {
        return this.mViewPager.getOffscreenPageLimit();
    }

    void removeAllFragmentTab() {
        this.mActionBar.internalRemoveAllTabs();
        this.mPagerAdapter.removeAllFragment();
    }

    void removeFragment(Fragment fragment) {
        int index = this.mPagerAdapter.removeFragment(fragment);
        if (index >= 0)
            this.mActionBar.internalRemoveTabAt(index);
    }

    void removeFragmentAt(int position) {
        this.mPagerAdapter.removeFragmentAt(position);
        this.mActionBar.internalRemoveTabAt(position);
    }

    void removeFragmentTab(ActionBar.Tab tab) {
        this.mActionBar.internalRemoveTab(tab);
        this.mPagerAdapter.removeFragment(tab);
    }

    void removeFragmentTab(String tag) {
        int i = this.mPagerAdapter.findPositionByTag(tag);
        if (i >= 0)
            removeFragmentAt(i);
    }

    void removeOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener listener) {
        if (this.mListeners != null)
            this.mListeners.remove(listener);
    }

    void setFragmentActionMenuAt(int position, boolean hasActionMenu) {
        this.mPagerAdapter.setFragmentActionMenuAt(position, hasActionMenu);
        if (position == mViewPager.getCurrentItem()) {
            if (mActionMenuChangeAnimatorObject == null) {
                mActionMenuChangeAnimatorObject = new ActionMenuChangeAnimatorObject();
                mActionMenuChangeAnimator = ObjectAnimator.ofFloat(
                        this.mActionMenuChangeAnimatorObject, "Value", new float[] {
                                0.0F, 1.0F
                        });
                mActionMenuChangeAnimator.setDuration(this.mViewPager.getContext()
                        .getResources().getInteger(/*0x10e0000*/com.android.internal.R.integer.config_shortAnimTime));
            }
            mActionMenuChangeAnimatorObject.reset(position, hasActionMenu);
            mActionMenuChangeAnimator.start();
        }
    }

    void setViewPagerOffscreenPageLimit(int limit) {
        this.mViewPager.setOffscreenPageLimit(limit);
    }

    class ActionMenuChangeAnimatorObject {
        private int mPosition;
        private boolean mShowActionMenu;

        ActionMenuChangeAnimatorObject() {
        }

        void reset(int position, boolean showActionMenu) {
            this.mPosition = position;
            this.mShowActionMenu = showActionMenu;
        }

        public void setValue(float value) {
            if (mListeners != null) {
                for (Iterator i = mListeners.iterator(); i.hasNext(); i.next()) {
                    MiuiActionBar.FragmentViewPagerChangeListener listener = (MiuiActionBar.FragmentViewPagerChangeListener) i;
                    if (listener instanceof ActionBarContainer) {
                        float positionOffset = 1.0F - value;
                        boolean toHasActionMenu;
                        if (!mShowActionMenu) {
                            toHasActionMenu = true;
                        } else {
                            toHasActionMenu = false;
                        }
                        listener.onPageScrolled(mPosition, positionOffset, mShowActionMenu,
                                toHasActionMenu);
                    }
                }
            }
        }
    }
}
