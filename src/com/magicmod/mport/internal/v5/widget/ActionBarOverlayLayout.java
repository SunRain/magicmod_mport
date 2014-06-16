
package com.magicmod.mport.internal.v5.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Rect;
import android.renderscript.Element;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;

import com.android.internal.R;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;

public class ActionBarOverlayLayout extends com.android.internal.widget.ActionBarOverlayLayout
        implements IActionBarLayout {
    static final int[] mActionBarSizeAttr = {
        R.attr.actionBarSize//0x10102eb , 16843499
    };
    private int mActionBarHeight;
    private View mDimView;
    private boolean mUpdateContentMargin = true;

    public ActionBarOverlayLayout(Context context) {
        super(context);
        init(context);
    }

    public ActionBarOverlayLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private boolean applyInsets(View view, Rect insets, boolean left,
            boolean top, boolean bottom, boolean right) {
        boolean changed = false;
        ActionBarOverlayLayout.LayoutParams lp = (ActionBarOverlayLayout.LayoutParams) view
                .getLayoutParams();
        if ((left) && (lp.leftMargin != insets.left)) {
            changed = true;
            lp.leftMargin = insets.left;
        }
        if ((top) && (lp.topMargin != insets.top)) {
            changed = true;
            lp.topMargin = insets.top;
        }
        if ((right) && (lp.rightMargin != insets.right)) {
            changed = true;
            lp.rightMargin = insets.right;
        }
        if ((bottom) && (lp.bottomMargin != insets.bottom)) {
            changed = true;
            lp.bottomMargin = insets.bottom;
        }
        return changed;
    }

    /*private ActionBarContextView getActionBarContextView() {
        if (getContainerView() != null)
            ;
        for (ActionBarContextView localActionBarContextView = (ActionBarContextView) getContainerView()
                .findViewById(ResourceMapper.resolveReference(this.mContext, 101384201));; localActionBarContextView = null)
            return localActionBarContextView;
    }*/
    private ActionBarContextView getActionBarContextView() {
        ActionBarContextView actionbarcontextview;
        if (getContainerView() != null)
            actionbarcontextview = (ActionBarContextView) getContainerView().findViewById(
                    ResourceMapper.resolveReference(mContext, /* 0x60b0009 */
                            com.miui.internal.R.id.android_action_context_bar));
        else
            actionbarcontextview = null;
        return actionbarcontextview;
    }

    private void init(Context context) {
        TypedArray ta = getContext().getTheme().obtainStyledAttributes(
                mActionBarSizeAttr);
        mActionBarHeight = ta.getDimensionPixelSize(0, 0);
        ta.recycle();
    }

    /*public boolean dispatchKeyEvent(KeyEvent paramKeyEvent) {
        int i = 1;
        boolean bool = false;
        if ((paramKeyEvent.getAction() == i) && (paramKeyEvent.getKeyCode() == 4))
            bool = false | getActionView().hideOverflowMenu()
                    | getActionBarContextView().hideOverflowMenu();
        if ((bool) || (super.dispatchKeyEvent(paramKeyEvent)))
            ;
        while (true) {
            return i;
            int j = 0;
        }
    }*/
    public boolean dispatchKeyEvent(KeyEvent event) {
        //int flag = 0x1;
        boolean handled = false;
        if (event.getAction() == flag && event.getKeyCode() == 4) {
            handled = false | getActionView().hideOverflowMenu()
                    | getActionBarContextView().hideOverflowMenu();
        }
        if (!handled && !super.dispatchKeyEvent(event)) {
            //flag = 0x0;
            return false
        }
        return true;//flag;
    }

    /*protected int getActionBarHeight() {
        View localView1 = getActionBarTop();
        ActionBarView localActionBarView = (ActionBarView) getActionView();
        ActionBarContainer localActionBarContainer = (ActionBarContainer) getContainerView();
        int i;
        if (localView1.getVisibility() != 0)
            i = 0;
        while (true) {
            return i;
            i = localActionBarView.getMeasuredHeight();
            View localView2 = localActionBarContainer.getTabContainer();
            if ((localView2 != null) && (localView2.getVisibility() == 0))
                i += localActionBarContainer.getTabContainer().getMeasuredHeight();
            if (i > 0)
                i = Math.max(i - UiUtils.getActionBarOverlayHeight(this.mContext), 0);
        }
    }*/
    protected int getActionBarHeight() {
        View actionBarTop;
        ActionBarView actionView;
        ActionBarContainer containerView;
        actionBarTop = getActionBarTop();
        actionView = (ActionBarView) getActionView();
        containerView = (ActionBarContainer) getContainerView();
        int height = 0;
        if (actionBarTop.getVisibility() == 0) {
            height = actionView.getMeasuredHeight();
            View tabContainer = containerView.getTabContainer();
            if (tabContainer != null && tabContainer.getVisibility() == 0)
                height += containerView.getTabContainer().getMeasuredHeight();
            if (height > 0)
                height = Math.max(height - UiUtils.getActionBarOverlayHeight(mContext), 0);
            // if(true) goto _L4; else goto _L3
        }
        return height;
    }

    /*protected int getSplitActionBarHeight() {
        ActionBarContainer localActionBarContainer = (ActionBarContainer) getActionBarBottom();
        ActionBarView localActionBarView = (ActionBarView) getActionView();
        int i;
        if (localActionBarContainer.getVisibility() != 0)
            i = 0;
        do {
            return i;
            i = 0;
        } while (!localActionBarView.isSplitActionBar());
        ActionMenuView localActionMenuView;
        int j;
        if (!localActionBarView.getActionBar().isFragmentViewPagerMode()) {
            if (localActionBarContainer.getVisibility() == 0)
                for (int k = 0; k < localActionBarContainer.getChildCount(); k++) {
                    View localView = localActionBarContainer.getChildAt(k);
                    if (((localView instanceof ActionMenuView))
                            && (((ActionMenuView) localView).getMenuItems() > 0)
                            && (localView.getVisibility() == 0))
                        i = Math.max(i, ((ActionMenuView) localView).getPrimaryContainerHeight());
                }
        } else {
            localActionMenuView = localActionBarView.getActionMenuView();
            ActionBarContextView localActionBarContextView = getActionBarContextView();
            if (localActionBarContextView == null)
                break label198;
            if (localActionBarContextView.getAnimatedVisibility() != 0)
                break label192;
            j = 1;
        }
        while (true) {
            if ((j != 0) && (localActionMenuView != null)
                    && (localActionMenuView.getMenuItems() == 0))
                i = localActionMenuView.getPrimaryContainerHeight();
            if (i <= 0)
                break;
            i = Math.max(i - UiUtils.getSplitActionBarOverlayHeight(this.mContext), 0);
            break;
            label192: j = 0;
            continue;
            label198: j = 0;
        }
    }*/
    protected int getSplitActionBarHeight() {
        ActionBarContainer actionBarBottom;
        ActionBarView actionView;
        actionBarBottom = (ActionBarContainer) getActionBarBottom();
        actionView = (ActionBarView) getActionView();
        int height = 0;
        if (actionBarBottom.getVisibility() == 0) { // goto _L2
            height = 0;
            if (actionView.isSplitActionBar()) {
                if (!actionView.getActionBar().isFragmentViewPagerMode()) {
                    if (actionBarBottom.getVisibility() == 0) {
                        for (int j = 0; j < actionBarBottom.getChildCount(); j++) {
                            View view = actionBarBottom.getChildAt(j);
                            if ((view instanceof ActionMenuView)
                                    && ((ActionMenuView) view).getMenuItems() > 0
                                    && view.getVisibility() == 0)
                                height = Math.max(height, ((ActionMenuView) view).getPrimaryContainerHeight());
                        }

                    }
                } else {
                    ActionMenuView actionMenuView = actionView.getActionMenuView();
                    ActionBarContextView actionBarContextView = getActionBarContextView();
                    boolean isContextViewVisible;
                    if (actionBarContextView != null) {
                        if (actionBarContextView.getAnimatedVisibility() == 0)
                            isContextViewVisible = true;
                        else
                            isContextViewVisible = false;
                    } else {
                        isContextViewVisible = false;
                    }
                    if (isContextViewVisible && actionMenuView != null && actionMenuView.getMenuItems() == 0)
                        height = actionMenuView.getPrimaryContainerHeight();
                }
                if (height > 0)
                    height = Math.max(height - UiUtils.getSplitActionBarOverlayHeight(mContext), 0);
            }
        }
        return height;
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDimView = findViewById(com.miui.internal.R.id.v5_icon_menu_bar_dim_container);//0x60b00a2(101384354);
    }

    /*protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3, int paramInt4)
{
  int i = getChildCount();
  int j = getPaddingLeft();
  (paramInt3 - paramInt1 - getPaddingRight());
  int k = getPaddingTop();
  int m = paramInt4 - paramInt2 - getPaddingBottom();
  int n = 0;
  if (n < i)
  {
    View localView = getChildAt(n);
    ActionBarOverlayLayout.LayoutParams localLayoutParams;
    int i1;
    int i2;
    int i3;
    if (localView.getVisibility() != 8)
    {
      localLayoutParams = (ActionBarOverlayLayout.LayoutParams)localView.getLayoutParams();
      i1 = localView.getMeasuredWidth();
      i2 = localView.getMeasuredHeight();
      i3 = j + localLayoutParams.leftMargin;
      if (localView != getActionBarBottom())
        break label148;
    }
    label148: for (int i4 = m - i2 - localLayoutParams.bottomMargin; ; i4 = k + localLayoutParams.topMargin)
    {
      localView.layout(i3, i4, i3 + i1, i4 + i2);
      n++;
      break;
    }
  }
}*/
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        int count = getChildCount();
        int parentLeft = getPaddingLeft();
        int parentRight = right - left - getPaddingRight();
        int parentTop = getPaddingTop();
        int parentBottom = bottom - top - getPaddingBottom();
        int i = 0;
        while (i < count) {
            View child = getChildAt(i);
            if (child.getVisibility() != 8) {
                LayoutParams lp = (LayoutParams) child.getLayoutParams();
                int width = child.getMeasuredWidth();
                int height = child.getMeasuredHeight();
                int childLeft = parentLeft
                        + ((android.view.ViewGroup.MarginLayoutParams) (lp)).leftMargin;
                int childTop;
                if (child == getActionBarBottom())
                    childTop = parentBottom - height
                            - ((android.view.ViewGroup.MarginLayoutParams) (lp)).bottomMargin;
                else
                    childTop = parentTop
                            + ((android.view.ViewGroup.MarginLayoutParams) (lp)).topMargin;
                child.layout(childLeft, childTop, childLeft + width, childTop + height);
            }
            i++;
        }
    }

    /*protected void onMeasure(int paramInt1, int paramInt2)
{
  findViews();
  int i = 0;
  int j = 0;
  View localView1 = getActionBarTop();
  View localView2 = getActionBarBottom();
  ActionBarImpl localActionBarImpl = (ActionBarImpl)getActionBar();
  ActionBarView localActionBarView = (ActionBarView)getActionView();
  ActionBarContainer localActionBarContainer = (ActionBarContainer)getContainerView();
  View localView3 = getContent();
  measureChildWithMargins(localView1, paramInt1, 0, paramInt2, 0);
  ActionBarOverlayLayout.LayoutParams localLayoutParams1 = (ActionBarOverlayLayout.LayoutParams)localView1.getLayoutParams();
  int k = Math.max(0, localView1.getMeasuredWidth() + localLayoutParams1.leftMargin + localLayoutParams1.rightMargin);
  int m = Math.max(0, localView1.getMeasuredHeight() + localLayoutParams1.topMargin + localLayoutParams1.bottomMargin);
  int n = combineMeasuredStates(0, localView1.getMeasuredState());
  if (localView2 != null)
  {
    measureChildWithMargins(localView2, paramInt1, 0, paramInt2, 0);
    ActionBarOverlayLayout.LayoutParams localLayoutParams3 = (ActionBarOverlayLayout.LayoutParams)localView2.getLayoutParams();
    int i11 = localView2.getMeasuredWidth() + localLayoutParams3.leftMargin + localLayoutParams3.rightMargin;
    k = Math.max(k, i11);
    int i12 = localView2.getMeasuredHeight() + localLayoutParams3.topMargin + localLayoutParams3.bottomMargin;
    m = Math.max(m, i12);
    int i13 = localView2.getMeasuredState();
    n = combineMeasuredStates(n, i13);
  }
  int i1;
  label280: label304: Rect localRect1;
  Rect localRect2;
  Rect localRect5;
  if ((0x100 & getWindowSystemUiVisibility()) != 0)
  {
    i1 = 1;
    if (i1 == 0)
      break label657;
    i = this.mActionBarHeight;
    if ((localActionBarImpl != null) && (localActionBarImpl.hasNonEmbeddedTabs()) && (localActionBarContainer.getTabContainer() != null))
      i += this.mActionBarHeight;
    if ((localActionBarView.isSplitActionBar()) && (localView2 != null))
    {
      if (i1 == 0)
        break label673;
      j = getSplitActionBarHeight();
    }
    localRect1 = getContentInsets();
    localRect2 = getInnerInsets();
    Rect localRect3 = getBaseInnerInsets();
    Rect localRect4 = getBaseContentInsets();
    localRect5 = getLastInnerInsets();
    localRect1.set(localRect4);
    localRect2.set(localRect3);
    if (this.mUpdateContentMargin)
    {
      if ((isOverlayMode()) || (i1 != 0))
        break label682;
      localRect1.top = (i + localRect1.top);
    }
  }
  for (localRect1.bottom = (j + localRect1.bottom); ; localRect2.bottom = (j + localRect2.bottom))
  {
    applyInsets(localView3, localRect1, true, true, this.mUpdateContentMargin, true);
    if (!localRect5.equals(localRect2))
    {
      localRect5.set(localRect2);
      superFitSystemWindows(localRect2);
    }
    measureChildWithMargins(localView3, paramInt1, 0, paramInt2, 0);
    ActionBarOverlayLayout.LayoutParams localLayoutParams2 = (ActionBarOverlayLayout.LayoutParams)localView3.getLayoutParams();
    int i2 = localView3.getMeasuredWidth() + localLayoutParams2.leftMargin + localLayoutParams2.rightMargin;
    int i3 = Math.max(k, i2);
    int i4 = localView3.getMeasuredHeight() + localLayoutParams2.topMargin + localLayoutParams2.bottomMargin;
    int i5 = Math.max(m, i4);
    int i6 = localView3.getMeasuredState();
    int i7 = combineMeasuredStates(n, i6);
    if ((this.mDimView != null) && (this.mDimView.getVisibility() == 0))
    {
      applyInsets(this.mDimView, localRect1, true, true, true, true);
      measureChildWithMargins(this.mDimView, paramInt1, 0, paramInt2, 0);
      int i10 = this.mDimView.getMeasuredState();
      i7 = combineMeasuredStates(i7, i10);
    }
    int i8 = i3 + (getPaddingLeft() + getPaddingRight());
    int i9 = Math.max(i5 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
    setMeasuredDimension(resolveSizeAndState(Math.max(i8, getSuggestedMinimumWidth()), paramInt1, i7), resolveSizeAndState(i9, paramInt2, i7 << 16));
    return;
    i1 = 0;
    break;
    label657: if (localView1.getVisibility() != 0)
      break label280;
    i = getActionBarHeight();
    break label280;
    label673: j = getSplitActionBarHeight();
    break label304;
    label682: localRect2.top = (i + localRect2.top);
  }
}*/
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        findViews();
        int k = 0;
        int l = 0;
        View actionBarTop = getActionBarTop();
        View actionBarBottom = getActionBarBottom();
        ActionBarImpl actionbarimpl = (ActionBarImpl) getActionBar();
        ActionBarView actionbarview = (ActionBarView) getActionView();
        ActionBarContainer actionbarcontainer = (ActionBarContainer) getContainerView();
        View content = getContent();
        measureChildWithMargins(actionBarTop, widthMeasureSpec, 0, heightMeasureSpec, 0);
        LayoutParams lp = (LayoutParams) actionBarTop
                .getLayoutParams();
        int i1 = Math.max(0, actionBarTop.getMeasuredWidth()
                + ((android.view.ViewGroup.MarginLayoutParams) (lp)).leftMargin
                + ((android.view.ViewGroup.MarginLayoutParams) (lp)).rightMargin);
        int j1 = Math.max(0, actionBarTop.getMeasuredHeight()
                + ((android.view.ViewGroup.MarginLayoutParams) (lp)).topMargin
                + ((android.view.ViewGroup.MarginLayoutParams) (lp)).bottomMargin);
        int k1 = combineMeasuredStates(0, actionBarTop.getMeasuredState());
        if (actionBarBottom != null) {
            measureChildWithMargins(actionBarBottom, widthMeasureSpec, 0, heightMeasureSpec, 0);
            LayoutParams layoutparams2 = (LayoutParams) actionBarBottom
                    .getLayoutParams();
            int i4 = actionBarBottom.getMeasuredWidth()
                    + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams2)).leftMargin
                    + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams2)).rightMargin;
            i1 = Math.max(i1, i4);
            int j4 = actionBarBottom.getMeasuredHeight()
                    + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams2)).topMargin
                    + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams2)).bottomMargin;
            j1 = Math.max(j1, j4);
            int k4 = actionBarBottom.getMeasuredState();
            k1 = combineMeasuredStates(k1, k4);
        }
        boolean stable;
        Rect contentInsets;
        Rect innerInsets;
        Rect baseInnerInsets;
        Rect baseContentInsets;
        Rect lastInnerInsets;
        LayoutParams layoutparams1;
        int l1;
        int i2;
        int j2;
        int k2;
        int l2;
        int i3;
        int j3;
        int k3;
        if ((0x100 & getWindowSystemUiVisibility()) != 0)
            stable = true;
        else
            stable = false;
        if (stable) {
            k = mActionBarHeight;
            if (actionbarimpl != null && actionbarimpl.hasNonEmbeddedTabs()
                    && actionbarcontainer.getTabContainer() != null)
                k += mActionBarHeight;
        } else if (actionBarTop.getVisibility() == 0)
            k = getActionBarHeight();
        if (actionbarview.isSplitActionBar() && actionBarBottom != null)
            if (stable)
                l = getSplitActionBarHeight();
            else
                l = getSplitActionBarHeight();
        contentInsets = getContentInsets();
        innerInsets = getInnerInsets();
        baseInnerInsets = getBaseInnerInsets();
        baseContentInsets = getBaseContentInsets();
        lastInnerInsets = getLastInnerInsets();
        contentInsets.set(baseContentInsets);
        innerInsets.set(baseInnerInsets);
        if (mUpdateContentMargin)
            if (!isOverlayMode() && !stable) {
                contentInsets.top = k + contentInsets.top;
                contentInsets.bottom = l + contentInsets.bottom;
            } else {
                innerInsets.top = k + innerInsets.top;
                innerInsets.bottom = l + innerInsets.bottom;
            }
        applyInsets(content, contentInsets, true, true, mUpdateContentMargin, true);
        if (!lastInnerInsets.equals(innerInsets)) {
            lastInnerInsets.set(innerInsets);
            superFitSystemWindows(innerInsets);
        }
        measureChildWithMargins(content, widthMeasureSpec, 0, heightMeasureSpec, 0);
        layoutparams1 = (LayoutParams) content.getLayoutParams();
        l1 = content.getMeasuredWidth()
                + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams1)).leftMargin
                + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams1)).rightMargin;
        i2 = Math.max(i1, l1);
        j2 = content.getMeasuredHeight()
                + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams1)).topMargin
                + ((android.view.ViewGroup.MarginLayoutParams) (layoutparams1)).bottomMargin;
        k2 = Math.max(j1, j2);
        l2 = content.getMeasuredState();
        i3 = combineMeasuredStates(k1, l2);
        if (mDimView != null && mDimView.getVisibility() == 0) {
            applyInsets(mDimView, contentInsets, true, true, true, true);
            measureChildWithMargins(mDimView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            int l3 = mDimView.getMeasuredState();
            i3 = combineMeasuredStates(i3, l3);
        }
        j3 = i2 + (getPaddingLeft() + getPaddingRight());
        k3 = Math.max(k2 + (getPaddingTop() + getPaddingBottom()), getSuggestedMinimumHeight());
        setMeasuredDimension(resolveSizeAndState(Math.max(j3, getSuggestedMinimumWidth()), widthMeasureSpec, i3),
                resolveSizeAndState(k3, heightMeasureSpec, i3 << 16));
    }


    public void setUpdateContentMarginEnabled(boolean enable) {
        this.mUpdateContentMargin = enable;
    }
}
