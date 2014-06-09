
package com.magicmod.mport.internal.v5.app;

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
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.SpinnerAdapter;

import com.android.internal.telephony.CallerInfo;
import com.magicmod.mport.internal.v5.view.ActionModeView;
import com.magicmod.mport.internal.v5.widget.ActionBarView;
import com.magicmod.mport.internal.v5.widget.SearchActionModeView;
import com.magicmod.mport.v5.app.MiuiActionBar;
import com.magicmod.mport.v5.view.DefaultActionMode;
import com.magicmod.mport.v5.view.DefaultActionMode.ActionModeCallback;

public class ActionBarImpl extends com.android.internal.app.ActionBarImpl implements MiuiActionBar,
        ActionModeCallback {
    private static final int CONTEXT_DISPLAY_SPLIT = 1;
    static final ActionBar.TabListener sLisenter = new ActionBar.TabListener() {
        public void onTabReselected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
            ActionBarImpl.TabImpl localTabImpl = (ActionBarImpl.TabImpl) paramAnonymousTab;
            if (localTabImpl.mSystemLisenter != null)
                localTabImpl.mSystemLisenter.onTabReselected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
            if (localTabImpl.mUserLisenter != null)
                localTabImpl.mUserLisenter.onTabReselected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
        }

        public void onTabSelected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
            ActionBarImpl.TabImpl localTabImpl = (ActionBarImpl.TabImpl) paramAnonymousTab;
            if (localTabImpl.mSystemLisenter != null)
                localTabImpl.mSystemLisenter.onTabSelected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
            if (localTabImpl.mUserLisenter != null)
                localTabImpl.mUserLisenter.onTabSelected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
        }

        public void onTabUnselected(ActionBar.Tab paramAnonymousTab,
                FragmentTransaction paramAnonymousFragmentTransaction) {
            ActionBarImpl.TabImpl localTabImpl = (ActionBarImpl.TabImpl) paramAnonymousTab;
            if (localTabImpl.mSystemLisenter != null)
                localTabImpl.mSystemLisenter.onTabUnselected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
            if (localTabImpl.mUserLisenter != null)
                localTabImpl.mUserLisenter.onTabUnselected(paramAnonymousTab,
                        paramAnonymousFragmentTransaction);
        }
    };
    private DefaultActionMode mCurrentActionMode;
    private ActionModeView mCurrentActionModeView;
    private ViewGroup mDecor;
    private View mDimView;
    private SearchActionModeView mSearchActionModeView;
    private ViewPagerController mViewPagerController;
    private Window mWindow;

    public ActionBarImpl(Activity paramActivity) {
        super(paramActivity);
        init(paramActivity.getWindow());
    }

    public ActionBarImpl(Dialog paramDialog) {
        super(paramDialog);
        init(paramDialog.getWindow());
    }

    public static ActionBarImpl getActionBarByView(View paramView) {
        return ActionBarView.findActionBarViewByView(paramView).getActionBar();
    }

    private void init(Window paramWindow) {
        this.mWindow = paramWindow;
        this.mDecor = ((ViewGroup) this.mWindow.getDecorView());
        this.mDimView = this.mDecor.findViewById(101384354);
        ((com.miui.internal.v5.widget.ActionBarView) getActionView()).setActionBar(this);
    }

    public int addFragmentTab(String paramString, ActionBar.Tab paramTab, int paramInt,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        return this.mViewPagerController.addFragmentTab(paramString, paramTab, paramInt,
                paramClass, paramBundle, paramBoolean);
    }

    public int addFragmentTab(String paramString, ActionBar.Tab paramTab,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        return this.mViewPagerController.addFragmentTab(paramString, paramTab, paramClass,
                paramBundle, paramBoolean);
    }

    public void addOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener) {
        this.mViewPagerController
                .addOnFragmentViewPagerChangeListener(paramFragmentViewPagerChangeListener);
    }

    public void addTab(ActionBar.Tab paramTab) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(paramTab);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab paramTab, int paramInt) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(paramTab, paramInt);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab paramTab, int paramInt, boolean paramBoolean) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(paramTab, paramInt, paramBoolean);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public void addTab(ActionBar.Tab paramTab, boolean paramBoolean) {
        if (!isFragmentViewPagerMode()) {
            super.addTab(paramTab, paramBoolean);
            return;
        }
        throw new IllegalStateException(
                "Cannot add tab directly in fragment view pager mode!\n Please using addFragmentTab().");
    }

    public DefaultActionMode createActionMode(ActionMode.Callback paramCallback) {
        if ((paramCallback instanceof SearchActionMode.Callback))
            ;
        for (Object localObject = new SearchActionMode(getThemedContext(),
                (SearchActionMode.Callback) paramCallback);; localObject = new EditActionMode(
                getThemedContext(), paramCallback))
            return localObject;
    }

    public ActionModeView createActionModeView(ActionMode.Callback paramCallback) {
        if ((paramCallback instanceof SearchActionMode.Callback)) {
            if (this.mSearchActionModeView == null)
                this.mSearchActionModeView = createSearchActionModeView();
            if (this.mSearchActionModeView.getParent() != this.mDecor)
                this.mDecor.addView(this.mSearchActionModeView);
        }
        for (Object localObject = this.mSearchActionModeView;; localObject = (ActionModeView) getContextView())
            return localObject;
    }

    public SearchActionModeView createSearchActionModeView() {
        SearchActionModeView localSearchActionModeView = (SearchActionModeView) LayoutInflater
                .from(getThemedContext()).inflate(100859971, this.mDecor, false);
        localSearchActionModeView.setOnBackClickListener(new View.OnClickListener() {
            public void onClick(View paramAnonymousView) {
                if (ActionBarImpl.this.mCurrentActionMode != null)
                    ActionBarImpl.this.mCurrentActionMode.finish();
            }
        });
        return localSearchActionModeView;
    }

    public DimAnimator getDimAnimator(boolean paramBoolean,
            View.OnClickListener paramOnClickListener) {
        return new DimAnimator(paramBoolean, paramOnClickListener);
    }

    public Fragment getFragmentAt(int paramInt) {
        return this.mViewPagerController.getFragmentAt(paramInt);
    }

    public int getFragmentTabCount() {
        return this.mViewPagerController.getFragmentTabCount();
    }

    public CharSequence getTertiaryTitle() {
        return ((com.miui.internal.v5.widget.ActionBarView) getActionView()).getTertiaryTitle();
    }

    public int getViewPagerOffscreenPageLimit() {
        return this.mViewPagerController.getViewPagerOffscreenPageLimit();
    }

    Window getWindow() {
        return this.mWindow;
    }

    void internalAddTab(ActionBar.Tab paramTab) {
        if (getTabCount() == 0)
            ;
        for (boolean bool = true;; bool = false) {
            super.addTab(paramTab, bool);
            return;
        }
    }

    void internalAddTab(ActionBar.Tab paramTab, int paramInt) {
        if (getTabCount() == paramInt)
            ;
        for (boolean bool = true;; bool = false) {
            super.addTab(paramTab, paramInt, bool);
            return;
        }
    }

    void internalRemoveAllTabs() {
        super.removeAllTabs();
    }

    void internalRemoveTab(ActionBar.Tab paramTab) {
        super.removeTab(paramTab);
    }

    void internalRemoveTabAt(int paramInt) {
        super.removeTabAt(paramInt);
    }

    public boolean isFragmentViewPagerMode() {
        if (this.mViewPagerController != null)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    void miuiAnimateToMode(boolean paramBoolean) {
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
    }

    public ActionBar.Tab newTab() {
        return new TabImpl();
    }

    public void onActionModeFinish(ActionMode paramActionMode) {
        miuiAnimateToMode(false);
        if (paramActionMode == this.mCurrentActionMode) {
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

    public void removeFragmentTab(ActionBar.Tab paramTab) {
        this.mViewPagerController.removeFragmentTab(paramTab);
    }

    public void removeFragmentTab(Fragment paramFragment) {
        this.mViewPagerController.removeFragment(paramFragment);
    }

    public void removeFragmentTab(String paramString) {
        this.mViewPagerController.removeFragmentTab(paramString);
    }

    public void removeFragmentTabAt(int paramInt) {
        this.mViewPagerController.removeFragmentAt(paramInt);
    }

    public void removeOnFragmentViewPagerChangeListener(
            MiuiActionBar.FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener) {
        this.mViewPagerController
                .removeOnFragmentViewPagerChangeListener(paramFragmentViewPagerChangeListener);
    }

    public void removeTab(ActionBar.Tab paramTab) {
        if (!isFragmentViewPagerMode()) {
            super.removeTab(paramTab);
            return;
        }
        throw new IllegalStateException(
                "Cannot remove tab directly in fragment view pager mode!\n Please using removeFragmentTab().");
    }

    public void removeTabAt(int paramInt) {
        if (!isFragmentViewPagerMode()) {
            super.removeTabAt(paramInt);
            return;
        }
        throw new IllegalStateException(
                "Cannot remove tab directly in fragment view pager mode!\n Please using removeFragmentTab().");
    }

    public void setFragmentActionMenuAt(int paramInt, boolean paramBoolean) {
        this.mViewPagerController.setFragmentActionMenuAt(paramInt, paramBoolean);
    }

    public void setFragmentViewPagerMode(Context paramContext, FragmentManager paramFragmentManager) {
        setFragmentViewPagerMode(paramContext, paramFragmentManager, true);
    }

    public void setFragmentViewPagerMode(Context paramContext,
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
    }

    public void setTertiaryTitle(int paramInt) {
        setTertiaryTitle(getThemedContext().getResources().getString(paramInt));
    }

    public void setTertiaryTitle(CharSequence paramCharSequence) {
        ((com.miui.internal.v5.widget.ActionBarView) getActionView())
                .setTertiaryTitle(paramCharSequence);
    }

    public void setViewPagerOffscreenPageLimit(int paramInt) {
        this.mViewPagerController.setViewPagerOffscreenPageLimit(paramInt);
    }

    public void showActionBarShadow(boolean paramBoolean) {
        View localView = this.mDecor.findViewById(101384348);
        if (localView != null)
            if (!paramBoolean)
                break label27;
        label27: for (int i = 0;; i = 8) {
            localView.setVisibility(i);
            return;
        }
    }

    public void showSplitActionBar(boolean paramBoolean1, boolean paramBoolean2) {
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
    }

    public ActionMode startActionMode(ActionMode.Callback paramCallback) {
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
    }

    public class DimAnimator implements Animator.AnimatorListener {
        private boolean mDimActionBarOrSplitActionBar;
        private Animator mDimHidingAnimator;
        private Animator mDimShowingAnimator;
        private View.OnClickListener mDimViewClickListener = null;

        public DimAnimator(boolean paramOnClickListener, View.OnClickListener arg3) {
            Object localObject;
            this.mDimViewClickListener = localObject;
            this.mDimActionBarOrSplitActionBar = paramOnClickListener;
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

        public void onAnimationCancel(Animator paramAnimator) {
            if (paramAnimator == this.mDimHidingAnimator) {
                if (!this.mDimActionBarOrSplitActionBar)
                    ActionBarImpl.this.getSplitView().bringToFront();
                ActionBarImpl.this.mDimView.setOnClickListener(null);
            }
        }

        public void onAnimationEnd(Animator paramAnimator) {
            if (paramAnimator == this.mDimHidingAnimator) {
                if (!this.mDimActionBarOrSplitActionBar)
                    ActionBarImpl.this.getSplitView().bringToFront();
                ActionBarImpl.this.mDimView.setOnClickListener(null);
                ActionBarImpl.this.mDimView.setVisibility(8);
            }
        }

        public void onAnimationRepeat(Animator paramAnimator) {
        }

        public void onAnimationStart(Animator paramAnimator) {
            if (paramAnimator == this.mDimShowingAnimator) {
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

        ActionBar.Tab setSystemTabListener(ActionBar.TabListener paramTabListener) {
            this.mSystemLisenter = paramTabListener;
            return this;
        }

        public ActionBar.Tab setTabListener(ActionBar.TabListener paramTabListener) {
            this.mUserLisenter = paramTabListener;
            return this;
        }
    }

    @Override
    public int addFragmentTab(String paramString, Tab paramTab, int paramInt,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    public int addFragmentTab(String paramString, Tab paramTab,
            Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean) {
        // TODO Auto-generated method stub
        return 0;
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
    public void removeFragmentTab(Fragment paramFragment) {
        // TODO Auto-generated method stub

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
    public void setFragmentViewPagerMode(Context paramContext, FragmentManager paramFragmentManager) {
        // TODO Auto-generated method stub

    }

    @Override
    public void setFragmentViewPagerMode(Context paramContext,
            FragmentManager paramFragmentManager, boolean paramBoolean) {
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

    @Override
    public void onActionModeFinish(ActionMode paramActionMode) {
        // TODO Auto-generated method stub

    }

}
