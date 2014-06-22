
package com.magicmod.mport.internal.v5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.android.internal.R;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;

public class ActionBarLayout extends FrameLayout implements IActionBarLayout {
    protected ActionBarContainer mActionBarContainer;
    protected ActionBarContextView mActionBarContextView;
    protected ActionBarView mActionBarView;
    protected ViewGroup mContentView;
    protected ActionBarContainer mSplitActionBarContainer;
    private boolean mUpdateContentMargin = true;

    public ActionBarLayout(Context context) {
        this(context, null);
    }

    public ActionBarLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionBarLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    /*public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
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
    }*/
    public boolean dispatchKeyEvent(KeyEvent event) {
        boolean flag = true;
        boolean handled = false;
        if (event.getAction() == 1 && event.getKeyCode() == 4) {
            handled = false | mActionBarView.hideOverflowMenu()
                    | mActionBarContextView.hideOverflowMenu();
        }
        if (!handled && !super.dispatchKeyEvent(event))
            flag = false;
        return flag;
    }

    /*protected int getActionBarHeight() {
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
    }*/
    protected int getActionBarHeight() {
        int height = 0;
        if (mActionBarContainer.getVisibility() == 0) { // goto _L2;

            height = mActionBarView.getMeasuredHeight();
            View tabContainer = mActionBarContainer.getTabContainer();
            if (tabContainer != null && tabContainer.getVisibility() == 0)
                height += mActionBarContainer.getTabContainer().getMeasuredHeight();
            if (height > 0)
                height = Math.max(height - UiUtils.getActionBarOverlayHeight(mContext), 0);
        }
        return height;
    }

   /* protected int getSplitActionBarHeight() {
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
    }*/
    protected int getSplitActionBarHeight() {
        int height = 0;
        if (mSplitActionBarContainer.getVisibility() == 0) { // goto _L2;
            height = 0;
            if (mActionBarView.isSplitActionBar()) {
                if (!mActionBarView.getActionBar().isFragmentViewPagerMode()) {
                    if (mSplitActionBarContainer.getVisibility() == 0) {
                        for (int j = 0; j < mSplitActionBarContainer.getChildCount(); j++) {
                            View view = mSplitActionBarContainer.getChildAt(j);
                            if ((view instanceof ActionMenuView)
                                    && ((ActionMenuView) view).getMenuItems() > 0
                                    && view.getVisibility() == 0)
                                height = Math.max(height, ((ActionMenuView) view).getPrimaryContainerHeight());
                        }
                    }
                } else {
                    ActionMenuView actionMenuView = mActionBarView.getActionMenuView();
                    boolean isContextViewVisible;
                    if (mActionBarContextView.getAnimatedVisibility() == 0)
                        isContextViewVisible = true;
                    else
                        isContextViewVisible = false;
                    if (isContextViewVisible && actionMenuView != null && actionMenuView.getMenuItems() == 0)
                        height = actionMenuView.getPrimaryContainerHeight();
                }
                if (height > 0)
                    height = Math.max(height - UiUtils.getSplitActionBarOverlayHeight(mContext), 0);
            }

        } else {
            height = 0;
        }
        return height;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        mActionBarContainer = (ActionBarContainer) findViewById(ResourceMapper.resolveReference(
                mContext, 
                com.miui.internal.R.id.android_action_bar_container//0x60b0007
                ));
        mSplitActionBarContainer = (ActionBarContainer) findViewById(ResourceMapper
                .resolveReference(
                        mContext, 
                        com.miui.internal.R.id.android_split_action_bar//0x60b0027
                        ));
        mActionBarContextView = (ActionBarContextView) mActionBarContainer
                .findViewById(ResourceMapper.resolveReference(
                        mContext, 
                        com.miui.internal.R.id.android_action_context_bar//0x60b0009
                        ));
        mActionBarView = (ActionBarView) mActionBarContainer.findViewById(ResourceMapper
                .resolveReference(
                        mContext, 
                        com.miui.internal.R.id.android_action_bar//0x60b0006
                        ));
        mContentView = (ViewGroup) findViewById(
                R.id.content//0x1020002
                );
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int maxHeight = 0;
        int maxWidth = 0;
        int mchildState = 0;
        for (int n = 0; n < count; n++) {
            View child = getChildAt(n);
            if ((child.getVisibility() != 8) && (child != this.mContentView)) {
                measureChildWithMargins(child, widthMeasureSpec, 0, heightMeasureSpec, 0);
                FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) child
                        .getLayoutParams();
                maxWidth = Math.max(maxWidth, child.getMeasuredWidth() + lp.leftMargin
                        + lp.rightMargin);
                maxHeight = Math.max(maxHeight, child.getMeasuredHeight() + lp.topMargin
                        + lp.bottomMargin);
                mchildState = combineMeasuredStates(mchildState, child.getMeasuredState());
            }
        }
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) mContentView
                .getLayoutParams();
        if (mUpdateContentMargin) {
            lp.topMargin = getActionBarHeight();
            lp.bottomMargin = getSplitActionBarHeight();
        }
        measureChildWithMargins(mContentView, widthMeasureSpec, 0, heightMeasureSpec, 0);
        int i1 = Math.max(maxWidth, mContentView.getMeasuredWidth() + lp.leftMargin
                + lp.rightMargin);
        int i2 = Math.max(maxHeight, mContentView.getMeasuredHeight() + lp.topMargin
                + lp.bottomMargin);
        int i3 = combineMeasuredStates(mchildState, this.mContentView.getMeasuredState());
        int i4 = Math.max(i2, getSuggestedMinimumHeight());
        setMeasuredDimension(
                resolveSizeAndState(Math.max(i1, getSuggestedMinimumWidth()), widthMeasureSpec, i3),
                resolveSizeAndState(i4, heightMeasureSpec, i3 << 16));
    }

    public void setUpdateContentMarginEnabled(boolean enable) {
        mUpdateContentMargin = enable;
    }
}
