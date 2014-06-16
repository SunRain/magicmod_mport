
package com.magicmod.mport.internal.v5.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.widget.ImageView;

import com.magicmod.mport.v5.app.MiuiActionBar;

public class ScrollingTabContainerView extends
        com.android.internal.widget.ScrollingTabContainerView implements
        MiuiActionBar.FragmentViewPagerChangeListener {
    private static final int DEFAULT_TAB_INDICATOR_ANIMATOR_DURATION = 300;
    private static final String TAB_INDICATOR_ANIMATOR_PROPERTY = "TranslationX";
    private boolean mIsFragmentViewPagerMode;
    private ImageView mTabIndicator;
    private ObjectAnimator mTabIndicatorAnimator;

    public ScrollingTabContainerView(Context paramContext) {
        super(paramContext);
        createTabIndicator();
    }

    private void setTabIndicatorTranslation(int paramInt, float paramFloat) {
        View localView = getTabLayout().getChildAt(paramInt);
        float f = localView.getLeft()
                + (localView.getWidth() - this.mTabIndicator.getDrawable().getIntrinsicWidth()) / 2
                + paramFloat * localView.getWidth();
        this.mTabIndicator.setTranslationX(f);
    }

    void animateIndicatorToTab(int paramInt) {
        animateIndicatorToTab(getTabLayout().getChildAt(paramInt));
    }

    public void animateIndicatorToTab(View paramView) {
        if ((paramView == null) || (paramView.getWidth() <= 0) || (this.mIsFragmentViewPagerMode))
            ;
        while (true) {
            return;
            if (this.mTabIndicatorAnimator == null) {
                this.mTabIndicatorAnimator = new ObjectAnimator();
                this.mTabIndicatorAnimator.setTarget(this.mTabIndicator);
                this.mTabIndicatorAnimator.setDuration(300L);
                this.mTabIndicatorAnimator.setPropertyName("TranslationX");
            }
            float f = paramView.getLeft()
                    + (paramView.getWidth() - this.mTabIndicator.getDrawable().getIntrinsicWidth())
                    / 2;
            this.mTabIndicatorAnimator.setFloatValues(new float[] {
                f
            });
            this.mTabIndicatorAnimator.cancel();
            this.mTabIndicatorAnimator.start();
        }
    }

    public void animateToTab(int paramInt) {
        super.animateToTab(paramInt);
        animateIndicatorToTab(paramInt);
    }

    public void createTabIndicator() {
        if (this.mTabIndicator != null)
            ;
        while (true) {
            return;
            this.mTabIndicator = new ImageView(this.mContext, null, 100728986);
            FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -2);
            localLayoutParams.gravity = 80;
            this.mTabIndicator.setLayoutParams(localLayoutParams);
            int i = View.MeasureSpec.makeMeasureSpec(0, 0);
            this.mTabIndicator.measure(i, i);
        }
    }

    protected int getSelectedTabIndex() {
        int i = -1;
        LinearLayout localLinearLayout = getTabLayout();
        for (int j = 0;; j++)
            if (j < localLinearLayout.getChildCount()) {
                if (localLinearLayout.getChildAt(j).isSelected())
                    i = j;
            } else
                return i;
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        super.onLayout(paramBoolean, paramInt1, paramInt2, paramInt3, paramInt4);
        LinearLayout localLinearLayout = getTabLayout();
        if (localLinearLayout.getChildCount() > 1) {
            View localView = localLinearLayout.getChildAt(getSelectedTabIndex());
            if (localView != null) {
                if (this.mTabIndicator.getParent() != this) {
                    addViewInLayout(this.mTabIndicator, 1, this.mTabIndicator.getLayoutParams());
                    this.mTabIndicator.layout(0,
                            paramInt4 - paramInt2 - this.mTabIndicator.getMeasuredHeight(),
                            this.mTabIndicator.getMeasuredWidth(), paramInt4 - paramInt2);
                }
                int i = localView.getLeft()
                        + (localView.getWidth() - this.mTabIndicator.getDrawable()
                                .getIntrinsicWidth()) / 2;
                if (this.mTabIndicatorAnimator != null)
                    this.mTabIndicatorAnimator.cancel();
                this.mTabIndicator.setTranslationX(i);
            }
        }
        while (true) {
            return;
            if (this.mTabIndicator.getParent() == this)
                removeView(this.mTabIndicator);
        }
    }

    public void onPageScrollStateChanged(int paramInt) {
    }

    public void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1,
            boolean paramBoolean2) {
        setTabIndicatorTranslation(paramInt, paramFloat);
    }

    public void onPageSelected(int paramInt) {
        View localView = getTabLayout().getChildAt(paramInt);
        if (localView != null)
            localView.sendAccessibilityEvent(4);
        setTabIndicatorTranslation(paramInt, 0.0F);
    }

    public boolean onRequestSendAccessibilityEvent(View paramView,
            AccessibilityEvent paramAccessibilityEvent) {
        if ((paramView == getTabLayout()) && (paramAccessibilityEvent.getEventType() == 1))
            ;
        for (boolean bool = false;; bool = super.onRequestSendAccessibilityEvent(paramView,
                paramAccessibilityEvent))
            return bool;
    }

    protected void onViewRemoved(View paramView) {
        super.onViewRemoved(paramView);
        if ((paramView != this.mTabIndicator) && (this.mTabIndicator != null)
                && (this.mTabIndicator.getParent() == this))
            removeView(this.mTabIndicator);
    }

    public void setFragmentViewPagerMode(boolean paramBoolean) {
        this.mIsFragmentViewPagerMode = paramBoolean;
    }
}
