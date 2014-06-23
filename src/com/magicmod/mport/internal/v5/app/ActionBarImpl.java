
package com.magicmod.mport.internal.v5.app;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.ActionBar;
import android.app.ActionBar.LayoutParams;
import android.app.ActionBar.OnMenuVisibilityListener;
import android.app.ActionBar.OnNavigationListener;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.Dialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SpinnerAdapter;

import com.android.internal.telephony.CallerInfo;
import com.magicmod.mport.internal.v5.view.ActionModeView;
import com.magicmod.mport.internal.v5.widget.ActionBarContainer;
import com.magicmod.mport.internal.v5.widget.ActionBarOverlayLayout;
import com.magicmod.mport.internal.v5.widget.ActionBarView;
import com.magicmod.mport.internal.v5.widget.ScrollingTabContainerView;
import com.magicmod.mport.internal.v5.widget.SearchActionModeView;
import com.magicmod.mport.v5.app.MiuiActionBar;
import com.magicmod.mport.v5.view.DefaultActionMode;
import com.magicmod.mport.v5.view.DefaultActionMode.ActionModeCallback;
import com.magicmod.mport.v5.view.EditActionMode;
import com.magicmod.mport.v5.view.SearchActionMode;
import com.miui.internal.R;

public class ActionBarImpl extends com.android.internal.app.ActionBarImpl implements MiuiActionBar,
        ActionModeCallback {
    private static final int CONTEXT_DISPLAY_SPLIT = 0x1;
    
    static final ActionBar.TabListener sLisenter = new ActionBar.TabListener() {

        @Override
        public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ActionBarImpl.TabImpl impl = (ActionBarImpl.TabImpl) tab;
            if (impl.mSystemLisenter != null)
                impl.mSystemLisenter.onTabReselected(tab, ft);
            if (impl.mUserLisenter != null)
                impl.mUserLisenter.onTabReselected(tab, ft);
        }

        @Override
        public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
            ActionBarImpl.TabImpl impl = (ActionBarImpl.TabImpl) tab;
            if (impl.mSystemLisenter != null)
                impl.mSystemLisenter.onTabSelected(tab, ft);
            if (impl.mUserLisenter != null)
                impl.mUserLisenter.onTabSelected(tab, ft);
        }

        @Override
        public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
            ActionBarImpl.TabImpl impl = (ActionBarImpl.TabImpl) tab;
            if (impl.mSystemLisenter != null)
                impl.mSystemLisenter.onTabUnselected(tab, ft);
            if (impl.mUserLisenter != null)
                impl.mUserLisenter.onTabUnselected(tab, ft);
        }
    };

    private DefaultActionMode mCurrentActionMode;
    private ActionModeView mCurrentActionModeView;
    private ViewGroup mDecor;
    private View mDimView;
    private SearchActionModeView mSearchActionModeView;
    private ViewPagerController mViewPagerController;
    private Window mWindow;

    public ActionBarImpl(Activity activity) {
        super(activity);
        init(activity.getWindow());
    }

    public ActionBarImpl(Dialog dialog) {
        super(dialog);
        init(dialog.getWindow());
    }

    public static ActionBarImpl getActionBarByView(View view) {
        return ActionBarView.findActionBarViewByView(view).getActionBar();
    }

    private void init(Window window) {
        this.mWindow = window;
        this.mDecor = ((ViewGroup) this.mWindow.getDecorView());
        this.mDimView = this.mDecor.findViewById(/*0x60b00a2*/R.id.v5_icon_menu_bar_dim_container);
        ((ActionBarView) getActionView()).setActionBar(this);
    }

    @Override
    public int addFragmentTab(String tag, ActionBar.Tab tab, int index,
            Class<? extends Fragment> fragment, Bundle args, boolean hasActionMenu) {
        return this.mViewPagerController.addFragmentTab(tag, tab, index, fragment, args,
                hasActionMenu);
    }

    @Override
    public int addFragmentTab(String tag, ActionBar.Tab tab, Class<? extends Fragment> fragment,
            Bundle args, boolean hasActionMenu) {
        return this.mViewPagerController.addFragmentTab(tag, tab, fragment, args, hasActionMenu);
    }

    public void addOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener listener) {
        this.mViewPagerController.addOnFragmentViewPagerChangeListener(listener);
    }

    public void addTab(ActionBar.Tab tab) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(tab);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab tab, int position) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(tab, position);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab tab, int position, boolean setSelected) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(tab, position, setSelected);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab tab, boolean setSelected) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(tab, setSelected);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    /*public DefaultActionMode createActionMode(ActionMode.Callback paramCallback) {
        if ((paramCallback instanceof SearchActionMode.Callback))
            ;
        for (Object localObject = new SearchActionMode(getThemedContext(),
                (SearchActionMode.Callback) paramCallback);; localObject = new EditActionMode(
                getThemedContext(), paramCallback))
            return localObject;
    }*/
    public DefaultActionMode createActionMode(android.view.ActionMode.Callback callback) {
        // Object obj;
        if (callback instanceof SearchActionMode.Callback)
            // obj = new SearchActionMode(getThemedContext(),
            // (SearchActionMode.Callback) callback);
            return ((DefaultActionMode) (new SearchActionMode(getThemedContext(),
                    (SearchActionMode.Callback) callback)));
        else
            // obj = new EditActionMode(getThemedContext(), callback);
            return ((DefaultActionMode) (new EditActionMode(getThemedContext(), callback)));
        // return ((DefaultActionMode) (obj));
    }

    /*public ActionModeView createActionModeView(ActionMode.Callback paramCallback) {
        if ((paramCallback instanceof SearchActionMode.Callback)) {
            if (this.mSearchActionModeView == null)
                this.mSearchActionModeView = createSearchActionModeView();
            if (this.mSearchActionModeView.getParent() != this.mDecor)
                this.mDecor.addView(this.mSearchActionModeView);
        }
        for (Object localObject = this.mSearchActionModeView;; localObject = (ActionModeView) getContextView())
            return localObject;
    }*/
    public ActionModeView createActionModeView(android.view.ActionMode.Callback callback) {
        ActionModeView view;
        if (callback instanceof SearchActionMode.Callback) {
            if (mSearchActionModeView == null)
                mSearchActionModeView = createSearchActionModeView();
            if (mSearchActionModeView.getParent() != mDecor)
                mDecor.addView(mSearchActionModeView);
            view = mSearchActionModeView;
        } else {
            view = (ActionModeView) getContextView();
        }
        return view;
    }

    public SearchActionModeView createSearchActionModeView() {
        SearchActionModeView view = (SearchActionModeView) LayoutInflater.from(getThemedContext())
                .inflate(R.layout.v5_search_action_mode_view,// 0x6030043,
                        mDecor, false);
        view.setOnBackClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mCurrentActionMode != null)
                    mCurrentActionMode.finish();
            }
        });
        return view;
    }

    public DimAnimator getDimAnimator(boolean fromActionBar, View.OnClickListener listener) {
        return new DimAnimator(fromActionBar, listener);
    }

    public Fragment getFragmentAt(int position) {
        return this.mViewPagerController.getFragmentAt(position);
    }

    public int getFragmentTabCount() {
        return this.mViewPagerController.getFragmentTabCount();
    }

    public CharSequence getTertiaryTitle() {
        return ((ActionBarView) getActionView()).getTertiaryTitle();
    }

    public int getViewPagerOffscreenPageLimit() {
        return this.mViewPagerController.getViewPagerOffscreenPageLimit();
    }

    Window getWindow() {
        return this.mWindow;
    }

    /*void internalAddTab(ActionBar.Tab paramTab) {
        if (getTabCount() == 0)
            ;
        for (boolean bool = true;; bool = false) {
            super.addTab(paramTab, bool);
            return;
        }
    }*/
    void internalAddTab(ActionBar.Tab tab) {
        boolean flag;
        if (getTabCount() == 0)
            flag = true;
        else
            flag = false;
        super.addTab(tab, flag);
    }


    /*void internalAddTab(ActionBar.Tab paramTab, int paramInt) {
        if (getTabCount() == paramInt)
            ;
        for (boolean bool = true;; bool = false) {
            super.addTab(paramTab, paramInt, bool);
            return;
        }
    }*/
    void internalAddTab(ActionBar.Tab tab, int position) {
        boolean flag;
        if (getTabCount() == position)
            flag = true;
        else
            flag = false;
        super.addTab(tab, position, flag);
    }

    void internalRemoveAllTabs() {
        super.removeAllTabs();
    }

    void internalRemoveTab(ActionBar.Tab tab) {
        super.removeTab(tab);
    }

    void internalRemoveTabAt(int position) {
        super.removeTabAt(position);
    }

    /*public boolean isFragmentViewPagerMode() {
        if (this.mViewPagerController != null)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }*/
    public boolean isFragmentViewPagerMode() {
        boolean flag;
        if (mViewPagerController != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /*void miuiAnimateToMode(boolean paramBoolean) {
        ScrollingTabContainerView localScrollingTabContainerView = (ScrollingTabContainerView) getTabScrollView();
        if ((localScrollingTabContainerView != null) && (!paramBoolean))
            localScrollingTabContainerView.bringToFront();
        ActionModeView localActionModeView = this.mCurrentActionModeView;
        if (paramBoolean)
            ;
        for (int i = 0;; i = 8) {
            localActionModeView.animateToVisibility(i);
            if (paramBoolean)
                ((View) this.mCurrentActionModeView).bringToFront();
            return;
        }
    }*/
    void miuiAnimateToMode(boolean visible) {
        ScrollingTabContainerView tabScrollView = (ScrollingTabContainerView) getTabScrollView();
        if (tabScrollView != null && !visible)
            tabScrollView.bringToFront();
        //ActionModeView actionmodeview = mCurrentActionModeView;
        int i;
        if (visible)
            i = 0;
        else
            i = 8;
        //actionmodeview.animateToVisibility(i);
        mCurrentActionModeView.animateToVisibility(i);
        if (visible)
            ((View) mCurrentActionModeView).bringToFront();
    }

    public ActionBar.Tab newTab() {
        return new TabImpl();
    }

    @Override
    public void onActionModeFinish(ActionMode mode) {
        miuiAnimateToMode(false);
        if (mode == this.mCurrentActionMode) {
            this.mCurrentActionMode = null;
            this.mCurrentActionModeView = null;
        }
    }

    public void onWindowHide() {
        getActionView().onWindowHide();
    }

    public void onWindowShow() {
        getActionView().onWindowShow();
    }

    public void removeAllFragmentTab() {
        this.mViewPagerController.removeAllFragmentTab();
    }

    public void removeAllTabs() {
        if (!isFragmentViewPagerMode()) {
            super.removeAllTabs();
            return;
        }
        throw new IllegalStateException(
                "Cannot remove tab directly in fragment view pager mode!\n Please using removeAllFragmentTabs().");
    }

    public void removeFragmentTab(ActionBar.Tab tab) {
        this.mViewPagerController.removeFragmentTab(tab);
    }

    @Override
    public void removeFragmentTab(Fragment fragment) {
        this.mViewPagerController.removeFragment(fragment);
    }

    public void removeFragmentTab(String tag) {
        this.mViewPagerController.removeFragmentTab(tag);
    }

    public void removeFragmentTabAt(int position) {
        this.mViewPagerController.removeFragmentAt(position);
    }

    public void removeOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener listener) {
        this.mViewPagerController.removeOnFragmentViewPagerChangeListener(listener);
    }

    public void removeTab(ActionBar.Tab tab) {
        if (!isFragmentViewPagerMode()) {
            super.removeTab(tab);
            return;
        }
        throw new IllegalStateException(
                "Cannot remove tab directly in fragment view pager mode!\n Please using removeFragmentTab().");
    }

    public void removeTabAt(int position) {
        if (!isFragmentViewPagerMode()) {
            super.removeTabAt(position);
            return;
        }
        throw new IllegalStateException(
                "Cannot remove tab directly in fragment view pager mode!\n Please using removeFragmentTab().");
    }

    public void setFragmentActionMenuAt(int position, boolean hasActionMenu) {
        this.mViewPagerController.setFragmentActionMenuAt(position, hasActionMenu);
    }

    @Override
    public void setFragmentViewPagerMode(Context context, FragmentManager fm) {
        setFragmentViewPagerMode(context, fm, true);
    }

    /*public void setFragmentViewPagerMode(Context paramContext,
            FragmentManager paramFragmentManager, boolean paramBoolean) {
        if (isFragmentViewPagerMode())
            ;
        while (true) {
            return;
            removeAllTabs();
            setNavigationMode(2);
            ScrollingTabContainerView localScrollingTabContainerView = (ScrollingTabContainerView) getTabScrollView();
            this.mViewPagerController = new ViewPagerController(this, paramFragmentManager,
                    paramBoolean);
            localScrollingTabContainerView.setFragmentViewPagerMode(isFragmentViewPagerMode());
            addOnFragmentViewPagerChangeListener(localScrollingTabContainerView);
            addOnFragmentViewPagerChangeListener((com.miui.internal.v5.widget.ActionBarContainer) getSplitView());
        }
    }*/
    @Override
    public void setFragmentViewPagerMode(Context context, FragmentManager fm,
            boolean allowListAnimation) {
        if (!isFragmentViewPagerMode()) {
            removeAllTabs();
            setNavigationMode(2);
            ScrollingTabContainerView scrollingTabContainerView = (ScrollingTabContainerView) getTabScrollView();
            mViewPagerController = new ViewPagerController(this, fm, allowListAnimation);
            scrollingTabContainerView.setFragmentViewPagerMode(isFragmentViewPagerMode());
            addOnFragmentViewPagerChangeListener(scrollingTabContainerView);
            addOnFragmentViewPagerChangeListener((ActionBarContainer) getSplitView());
        }
    }

    public void setTertiaryTitle(int resId) {
        setTertiaryTitle(getThemedContext().getResources().getString(resId));
    }

    public void setTertiaryTitle(CharSequence title) {
        ((ActionBarView) getActionView()).setTertiaryTitle(title);
    }

    public void setViewPagerOffscreenPageLimit(int limit) {
        this.mViewPagerController.setViewPagerOffscreenPageLimit(limit);
    }

    /*public void showActionBarShadow(boolean paramBoolean) {
        View localView = this.mDecor.findViewById(101384348);
        if (localView != null)
            if (!paramBoolean)
                break label27;
        label27: for (int i = 0;; i = 8) {
            localView.setVisibility(i);
            return;
        }
    }*/
    public void showActionBarShadow(boolean show) {
        View view = mDecor.findViewById(/*0x60b009c*/R.id.v5_action_bar_shadow);
        if (view != null) {
            int i;
            if (show)
                i = 0;
            else
                i = 8;
            view.setVisibility(i);
        }
    }

    /*public void showSplitActionBar(boolean paramBoolean1, boolean paramBoolean2) {
        com.miui.internal.v5.widget.ActionBarContainer localActionBarContainer;
        if (getActionView().isSplitActionBar()) {
            localActionBarContainer = (com.miui.internal.v5.widget.ActionBarContainer) getSplitView();
            if (!paramBoolean1)
                break label28;
            localActionBarContainer.show(paramBoolean2);
        }
        while (true) {
            return;
            label28: localActionBarContainer.hide(paramBoolean2);
        }
    }*/
    public void showSplitActionBar(boolean show, boolean animation) {
        if (getActionView().isSplitActionBar()) {
            ActionBarContainer split = (ActionBarContainer) getSplitView();
            if (show)
                split.show(animation);
            else
                split.hide(animation);
        }
    }

    /*public ActionMode startActionMode(ActionMode.Callback paramCallback) {
        com.miui.internal.v5.widget.ActionBarContainer localActionBarContainer = (com.miui.internal.v5.widget.ActionBarContainer) getSplitView();
        int i = getContextDisplayMode();
        ActionBarOverlayLayout localActionBarOverlayLayout = getActionBarOverlayLayout();
        if (this.mCurrentActionMode != null)
            this.mCurrentActionMode.finish();
        this.mCurrentActionMode = createActionMode(paramCallback);
        this.mCurrentActionMode.setActionModeCallback(this);
        this.mCurrentActionModeView = createActionModeView(paramCallback);
        this.mCurrentActionMode.setActionModeView(this.mCurrentActionModeView);
        if (this.mCurrentActionMode.dispatchOnCreate()) {
            this.mCurrentActionMode.invalidate();
            miuiAnimateToMode(true);
            if ((localActionBarContainer != null) && (i == 1)
                    && (localActionBarContainer.getVisibility() != 0)) {
                localActionBarContainer.setVisibility(0);
                if (localActionBarOverlayLayout != null)
                    localActionBarOverlayLayout.requestFitSystemWindows();
            }
        }
        for (DefaultActionMode localDefaultActionMode = this.mCurrentActionMode;; localDefaultActionMode = null) {
            return localDefaultActionMode;
            if (this.mCurrentActionMode != null)
                this.mCurrentActionMode.finish();
        }
    }*/
    public ActionMode startActionMode(android.view.ActionMode.Callback callback) {
        ActionBarContainer splitView = (ActionBarContainer) getSplitView();
        int contextDisplayMode = getContextDisplayMode();
        ActionBarOverlayLayout overlayLayout = getActionBarOverlayLayout();
        if (mCurrentActionMode != null)
            mCurrentActionMode.finish();
        mCurrentActionMode = createActionMode(callback);
        mCurrentActionMode.setActionModeCallback(this);
        mCurrentActionModeView = createActionModeView(callback);
        mCurrentActionMode.setActionModeView(mCurrentActionModeView);
        DefaultActionMode defaultactionmode;
        if (mCurrentActionMode.dispatchOnCreate()) {
            mCurrentActionMode.invalidate();
            miuiAnimateToMode(true);
            if (splitView != null && contextDisplayMode == 1 && splitView.getVisibility() != 0) {
                splitView.setVisibility(0);
                if (overlayLayout != null)
                    overlayLayout.requestFitSystemWindows();
            }
            defaultactionmode = mCurrentActionMode;
        } else {
            if (mCurrentActionMode != null)
                mCurrentActionMode.finish();
            defaultactionmode = null;
        }
        return defaultactionmode;
    }

    public class DimAnimator implements AnimatorListener {
        private boolean mDimActionBarOrSplitActionBar;
        private Animator mDimHidingAnimator;
        private Animator mDimShowingAnimator;
        private View.OnClickListener mDimViewClickListener = null;

        public DimAnimator(boolean fromActionBar, View.OnClickListener listener) {
            //Object localObject;
            //this.mDimViewClickListener = localObject;
            this.mDimActionBarOrSplitActionBar = fromActionBar;
            this.mDimShowingAnimator = ObjectAnimator.ofFloat(ActionBarImpl.this.mDimView, "alpha",
                    new float[] {
                            0.0F, 1.0F
                    });
            this.mDimShowingAnimator.addListener(this);
            this.mDimHidingAnimator = ObjectAnimator.ofFloat(ActionBarImpl.this.mDimView, "alpha",
                    new float[] {
                            1.0F, 0.0F
                    });
            this.mDimHidingAnimator.addListener(this);
        }

        public Animator getDimHidingAnimator() {
            return this.mDimHidingAnimator;
        }

        public Animator getDimShowingAnimator() {
            return this.mDimShowingAnimator;
        }

        @Override
        public void onAnimationCancel(Animator animation) {
            if (animation == this.mDimHidingAnimator) {
                if (!this.mDimActionBarOrSplitActionBar)
                    ActionBarImpl.this.getSplitView().bringToFront();
                ActionBarImpl.this.mDimView.setOnClickListener(null);
            }
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            if (animation == this.mDimHidingAnimator) {
                if (!this.mDimActionBarOrSplitActionBar)
                    ActionBarImpl.this.getSplitView().bringToFront();
                ActionBarImpl.this.mDimView.setOnClickListener(null);
                ActionBarImpl.this.mDimView.setVisibility(8);
            }
        }

        @Override
        public void onAnimationRepeat(Animator animation) {
        }

        @Override
        public void onAnimationStart(Animator animation) {
            /*if (paramAnimator == this.mDimShowingAnimator) {
                ActionBarImpl.this.mDimView.setVisibility(0);
                ActionBarImpl.this.mDimView.bringToFront();
                if (!this.mDimActionBarOrSplitActionBar)
                    break label61;
                ActionBarImpl.this.getContainerView().bringToFront();
            }
            while (true) {
                ActionBarImpl.this.mDimView.setOnClickListener(this.mDimViewClickListener);
                return;
                label61: ActionBarImpl.this.getSplitView().bringToFront();
            }*/
            if (animation == mDimShowingAnimator) {
                mDimView.setVisibility(0);
                mDimView.bringToFront();
                if (mDimActionBarOrSplitActionBar)
                    getContainerView().bringToFront();
                else
                    getSplitView().bringToFront();
                mDimView.setOnClickListener(mDimViewClickListener);
            }
        }
    }

    class TabImpl extends com.android.internal.app.ActionBarImpl.TabImpl {
        ActionBar.TabListener mSystemLisenter;
        ActionBar.TabListener mUserLisenter;

        public TabImpl() {
            super();
            super.setTabListener(ActionBarImpl.sLisenter);
        }

        ActionBar.Tab setSystemTabListener(ActionBar.TabListener callback) {
            this.mSystemLisenter = callback;
            return this;
        }

        public ActionBar.Tab setTabListener(ActionBar.TabListener callback) {
            this.mUserLisenter = callback;
            return this;
        }
    }

    @Override
    public void addOnMenuVisibilityListener(OnMenuVisibilityListener paramOnMenuVisibilityListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public View getCustomView() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getDisplayOptions() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getHeight() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNavigationItemCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getNavigationMode() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int getSelectedNavigationIndex() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Tab getSelectedTab() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CharSequence getSubtitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public Tab getTabAt(int paramInt) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public int getTabCount() {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public Context getThemedContext() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public CharSequence getTitle() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    public void hide() {
        // TODO Auto-generated method stub

    }

    @Override
    public boolean isShowing() {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public void removeOnMenuVisibilityListener(
            OnMenuVisibilityListener paramOnMenuVisibilityListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void selectTab(Tab paramTab) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setBackgroundDrawable(Drawable paramDrawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCustomView(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCustomView(View paramView) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setCustomView(View paramView, LayoutParams paramLayoutParams) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayHomeAsUpEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayOptions(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayOptions(int paramInt1, int paramInt2) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayShowCustomEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayShowHomeEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayShowTitleEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setDisplayUseLogoEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setHomeButtonEnabled(boolean paramBoolean) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIcon(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setIcon(Drawable paramDrawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter,
            OnNavigationListener paramOnNavigationListener) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLogo(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setLogo(Drawable paramDrawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setNavigationMode(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSelectedNavigationItem(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSplitBackgroundDrawable(Drawable paramDrawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setStackedBackgroundDrawable(Drawable paramDrawable) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSubtitle(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setSubtitle(CharSequence paramCharSequence) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTitle(int paramInt) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setTitle(CharSequence paramCharSequence) {
        // TODO Auto-generated method stub

    }

    @Override
    public void show() {
        // TODO Auto-generated method stub

    }
}
