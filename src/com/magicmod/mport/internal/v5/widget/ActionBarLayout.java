
package com.magicmod.mport.internal.v5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.internal.widget.ActionBarView;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;

public class ActionBarLayout extends FrameLayout implements IActionBarLayout {
    protected ActionBarContainer mActionBarContainer;
    protected ActionBarContextView mActionBarContextView;
    protected ActionBarView mActionBarView;
    protected ViewGroup mContentView;
    protected ActionBarContainer mSplitActionBarContainer;
    private boolean mUpdateContentMargin = true;

    public ActionBarLayout(Context paramContext) {
        this(paramContext, null);
    }

    public ActionBarLayout(Context paramContext, AttributeSet paramAttributeSet) {
        this(paramContext, paramAttributeSet, 0);
    }

    public ActionBarLayout(Context paramContext, AttributeSet paramAttributeSet, int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        int i = 1;
        boolean bool = false;
        if ((paramKeyEvent.getAction() == i) && (paramKeyEvent.getKeyCode() == 4))
            bool = false | this.mActionBarView.hideOverflowMenu()
                    | this.mActionBarContextView.hideOverflowMenu();
        if ((bool) || (super.dispatchKeyEvent(paramKeyEvent)))
            ;
        while (true) {
            return i;
            int j = 0;
        }
    }

    protected int getActionBarHeight() {
        int i;
        if (this.mActionBarContainer.getVisibility() != 0)
            i = 0;
        while (true) {
            return i;
            i = this.mActionBarView.getMeasuredHeight();
            View localView = this.mActionBarContainer.getTabContainer();
            if ((localView != null) && (localView.getVisibility() == 0))
                i += this.mActionBarContainer.getTabContainer().getMeasuredHeight();
            if (i > 0)
                i = Math.max(i - UiUtils.getActionBarOverlayHeight(this.mContext), 0);
        }
    }

    protected int getSplitActionBarHeight() {
        int i;
        if (this.mSplitActionBarContainer.getVisibility() != 0)
            i = 0;
        do {
            return i;
            i = 0;
        } while (!this.mActionBarView.isSplitActionBar());
        ActionMenuView localActionMenuView;
        if (!this.mActionBarView.getActionBar().isFragmentViewPagerMode()) {
            if (this.mSplitActionBarContainer.getVisibility() == 0)
                for (int k = 0; k < this.mSplitActionBarContainer.getChildCount(); k++) {
                    View localView = this.mSplitActionBarContainer.getChildAt(k);
                    if (((localView instanceof ActionMenuView))
                            && (((ActionMenuView) localView).getMenuItems() > 0)
                            && (localView.getVisibility() == 0))
                        i = Math.max(i, ((ActionMenuView) localView).getPrimaryContainerHeight());
                }
        } else {
            localActionMenuView = this.mActionBarView.getActionMenuView();
            if (this.mActionBarContextView.getAnimatedVisibility() != 0)
                break label182;
        }
        label182: for (int j = 1;; j = 0) {
            if ((j != 0) && (localActionMenuView != null)
                    && (localActionMenuView.getMenuItems() == 0))
                i = localActionMenuView.getPrimaryContainerHeight();
            if (i <= 0)
                break;
            i = Math.max(i - UiUtils.getSplitActionBarOverlayHeight(this.mContext), 0);
            break;
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mActionBarContainer = ((ActionBarContainer) findViewById(ResourceMapper
                .resolveReference(this.mContext, 101384199)));
        this.mSplitActionBarContainer = ((ActionBarContainer) findViewById(ResourceMapper
                .resolveReference(this.mContext, 101384231)));
        this.mActionBarContextView = ((ActionBarContextView) this.mActionBarContainer
                .findViewById(ResourceMapper.resolveReference(this.mContext, 101384201)));
        this.mActionBarView = ((ActionBarView) this.mActionBarContainer.findViewById(ResourceMapper
                .resolveReference(this.mContext, 101384198)));
        this.mContentView = ((ViewGroup) findViewById(16908290));
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        int i = getChildCount();
        int j = 0;
        int k = 0;
        int m = 0;
        for (int n = 0; n < i; n++) {
            View localView = getChildAt(n);
            if ((localView.getVisibility() != 8) && (localView != this.mContentView)) {
                measureChildWithMargins(localView, paramInt1, 0, paramInt2, 0);
                FrameLayout.LayoutParams localLayoutParams2 = (FrameLayout.LayoutParams) localView
                        .getLayoutParams();
                k = Math.max(k, localView.getMeasuredWidth() + localLayoutParams2.leftMargin
                        + localLayoutParams2.rightMargin);
                j = Math.max(j, localView.getMeasuredHeight() + localLayoutParams2.topMargin
                        + localLayoutParams2.bottomMargin);
                m = combineMeasuredStates(m, localView.getMeasuredState());
            }
        }
        FrameLayout.LayoutParams localLayoutParams1 = (FrameLayout.LayoutParams) this.mContentView
                .getLayoutParams();
        if (this.mUpdateContentMargin) {
            localLayoutParams1.topMargin = getActionBarHeight();
            localLayoutParams1.bottomMargin = getSplitActionBarHeight();
        }
        measureChildWithMargins(this.mContentView, paramInt1, 0, paramInt2, 0);
        int i1 = Math.max(k, this.mContentView.getMeasuredWidth() + localLayoutParams1.leftMargin
                + localLayoutParams1.rightMargin);
        int i2 = Math.max(j, this.mContentView.getMeasuredHeight() + localLayoutParams1.topMargin
                + localLayoutParams1.bottomMargin);
        int i3 = combineMeasuredStates(m, this.mContentView.getMeasuredState());
        int i4 = Math.max(i2, getSuggestedMinimumHeight());
        setMeasuredDimension(
                resolveSizeAndState(Math.max(i1, getSuggestedMinimumWidth()), paramInt1, i3),
                resolveSizeAndState(i4, paramInt2, i3 << 16));
    }

    public void setUpdateContentMarginEnabled(boolean paramBoolean) {
        this.mUpdateContentMargin = paramBoolean;
    }
}
