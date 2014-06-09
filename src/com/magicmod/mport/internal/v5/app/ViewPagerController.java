
package com.magicmod.mport.internal.v5.app;

import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.os.Bundle;
import android.view.View;

public class ViewPagerController {
    private ActionBarImpl mActionBar;
    private ObjectAnimator mActionMenuChangeAnimator;
    private ActionMenuChangeAnimatorObject mActionMenuChangeAnimatorObject;
    private ArrayList<MiuiActionBar.FragmentViewPagerChangeListener> mListeners;
    private DynamicFragmentPagerAdapter mPagerAdapter;
    private ActionBar.TabListener mTabListener = new ActionBar.TabListener() {
        public void onTabReselected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
        }

        public void onTabSelected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
            int i = ViewPagerController.this.mPagerAdapter.getCount();
            for (int j = 0;; j++)
                if (j < i) {
                    if (ViewPagerController.this.mPagerAdapter.getTabAt(j) == paramAnonymousTab)
                        ViewPagerController.this.mViewPager.setCurrentItem(j, true);
                } else
                    return;
        }

        public void onTabUnselected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
        }
    };
    private ViewPager mViewPager;

    ViewPagerController(ActionBarImpl paramActionBarImpl, FragmentManager paramFragmentManager,
            boolean paramBoolean) {
        this.mActionBar = paramActionBarImpl;
        Context localContext = this.mActionBar.getWindow().getContext();
        View localView = this.mActionBar.getWindow().findViewById(101384373);
        if ((localView instanceof ViewPager))
            this.mViewPager = ((ViewPager) localView);
        while (true) {
            this.mPagerAdapter = new DynamicFragmentPagerAdapter(localContext,
                    paramFragmentManager, this.mViewPager);
            this.mViewPager.setInternalPageChangeListener(new ViewPager.OnPageChangeListener() {
                public void onPageScrollStateChanged(int paramAnonymousInt) {
                    if (ViewPagerController.this.mListeners != null) {
                        Iterator localIterator = ViewPagerController.this.mListeners.iterator();
                        while (localIterator.hasNext())
                            ((MiuiActionBar.FragmentViewPagerChangeListener) localIterator.next())
                                    .onPageScrollStateChanged(paramAnonymousInt);
                    }
                }

                public void onPageScrolled(int paramAnonymousInt1, float paramAnonymousFloat,
                        int paramAnonymousInt2) {
                    boolean bool1 = ViewPagerController.this.mPagerAdapter
                            .hasActionMenu(paramAnonymousInt1);
                    if (paramAnonymousInt1 + 1 < ViewPagerController.this.mPagerAdapter.getCount())
                        ;
                    for (boolean bool2 = ViewPagerController.this.mPagerAdapter
                            .hasActionMenu(paramAnonymousInt1 + 1); ViewPagerController.this.mListeners != null; bool2 = false) {
                        Iterator localIterator = ViewPagerController.this.mListeners.iterator();
                        while (localIterator.hasNext())
                            ((MiuiActionBar.FragmentViewPagerChangeListener) localIterator.next())
                                    .onPageScrolled(paramAnonymousInt1, paramAnonymousFloat, bool1,
                                            bool2);
                    }
                }

                public void onPageSelected(int paramAnonymousInt) {
                    ViewPagerController.this.mActionBar
                            .setSelectedNavigationItem(paramAnonymousInt);
                    ViewPagerController.this.mPagerAdapter.setPrimaryItem(
                            ViewPagerController.this.mViewPager, paramAnonymousInt,
                            ViewPagerController.this.mPagerAdapter.getFragment(paramAnonymousInt,
                                    true));
                    if (ViewPagerController.this.mListeners != null) {
                        Iterator localIterator = ViewPagerController.this.mListeners.iterator();
                        while (localIterator.hasNext())
                            ((MiuiActionBar.FragmentViewPagerChangeListener) localIterator.next())
                                    .onPageSelected(paramAnonymousInt);
                    }
                }
            });
            if (paramBoolean)
                addOnFragmentViewPagerChangeListener(new ViewPagerScrollEffect(this.mViewPager,
                        this.mPagerAdapter));
            return;
            this.mViewPager = new ViewPager(localContext);
            this.mViewPager.setId(101384373);
            this.mActionBar.getWindow().setContentView(this.mViewPager);
        }
    }

    int addFragmentTab(String paramString, ActionBar.Tab paramTab, int paramInt,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        ((ActionBarImpl.TabImpl) paramTab).setSystemTabListener(this.mTabListener);
        this.mActionBar.internalAddTab(paramTab, paramInt);
        return this.mPagerAdapter.addFragment(paramString, paramInt, paramClass, paramBundle,
                paramTab, paramBoolean);
    }

    int addFragmentTab(String paramString, ActionBar.Tab paramTab,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        ((ActionBarImpl.TabImpl) paramTab).setSystemTabListener(this.mTabListener);
        this.mActionBar.internalAddTab(paramTab);
        return this.mPagerAdapter.addFragment(paramString, paramClass, paramBundle, paramTab,
                paramBoolean);
    }

    void addOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener) {
        if (this.mListeners == null)
            this.mListeners = new ArrayList();
        this.mListeners.add(paramFragmentViewPagerChangeListener);
    }

    Fragment getFragmentAt(int paramInt) {
        return this.mPagerAdapter.getFragment(paramInt, true);
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

    void removeFragment(Fragment paramFragment) {
        int i = this.mPagerAdapter.removeFragment(paramFragment);
        if (i >= 0)
            this.mActionBar.internalRemoveTabAt(i);
    }

    void removeFragmentAt(int paramInt) {
        this.mPagerAdapter.removeFragmentAt(paramInt);
        this.mActionBar.internalRemoveTabAt(paramInt);
    }

    void removeFragmentTab(ActionBar.Tab paramTab) {
        this.mActionBar.internalRemoveTab(paramTab);
        this.mPagerAdapter.removeFragment(paramTab);
    }

    void removeFragmentTab(String paramString) {
        int i = this.mPagerAdapter.findPositionByTag(paramString);
        if (i >= 0)
            removeFragmentAt(i);
    }

    void removeOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener) {
        if (this.mListeners != null)
            this.mListeners.remove(paramFragmentViewPagerChangeListener);
    }

    void setFragmentActionMenuAt(int paramInt, boolean paramBoolean) {
        this.mPagerAdapter.setFragmentActionMenuAt(paramInt, paramBoolean);
        if (paramInt == this.mViewPager.getCurrentItem()) {
            if (this.mActionMenuChangeAnimatorObject == null) {
                this.mActionMenuChangeAnimatorObject = new ActionMenuChangeAnimatorObject();
                this.mActionMenuChangeAnimator = ObjectAnimator.ofFloat(
                        this.mActionMenuChangeAnimatorObject, "Value", new float[] {
                                0.0F, 1.0F
                        });
                this.mActionMenuChangeAnimator.setDuration(this.mViewPager.getContext()
                        .getResources().getInteger(17694720));
            }
            this.mActionMenuChangeAnimatorObject.reset(paramInt, paramBoolean);
            this.mActionMenuChangeAnimator.start();
        }
    }

    void setViewPagerOffscreenPageLimit(int paramInt) {
        this.mViewPager.setOffscreenPageLimit(paramInt);
    }

    class ActionMenuChangeAnimatorObject {
        private int mPosition;
        private boolean mShowActionMenu;

        ActionMenuChangeAnimatorObject() {
        }

        void reset(int paramInt, boolean paramBoolean) {
            this.mPosition = paramInt;
            this.mShowActionMenu = paramBoolean;
        }

        public void setValue(float paramFloat) {
            if (ViewPagerController.this.mListeners != null) {
                Iterator localIterator = ViewPagerController.this.mListeners.iterator();
                while (localIterator.hasNext()) {
                    MiuiActionBar.FragmentViewPagerChangeListener localFragmentViewPagerChangeListener = (MiuiActionBar.FragmentViewPagerChangeListener) localIterator
                            .next();
                    if ((localFragmentViewPagerChangeListener instanceof ActionBarContainer)) {
                        int i = this.mPosition;
                        float f = 1.0F - paramFloat;
                        boolean bool1 = this.mShowActionMenu;
                        if (!this.mShowActionMenu)
                            ;
                        for (boolean bool2 = true;; bool2 = false) {
                            localFragmentViewPagerChangeListener.onPageScrolled(i, f, bool1, bool2);
                            break;
                        }
                    }
                }
            }
        }
    }
}
