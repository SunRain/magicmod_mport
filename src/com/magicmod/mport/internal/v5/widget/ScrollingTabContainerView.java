
package com.magicmod.mport.internal.v5.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.view.View;
import android.view.accessibility.AccessibilityEvent;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.magicmod.mport.v5.app.MiuiActionBar;
import com.miui.internal.R;

public class ScrollingTabContainerView extends
        com.android.internal.widget.ScrollingTabContainerView implements
        MiuiActionBar.FragmentViewPagerChangeListener {
    private static final int DEFAULT_TAB_INDICATOR_ANIMATOR_DURATION = 300;
    private static final String TAB_INDICATOR_ANIMATOR_PROPERTY = "TranslationX";
    private boolean mIsFragmentViewPagerMode;
    private ImageView mTabIndicator;
    private ObjectAnimator mTabIndicatorAnimator;

    public ScrollingTabContainerView(Context context) {
        super(context);
        createTabIndicator();
    }

    private void setTabIndicatorTranslation(int position, float ratio) {
        View tabView = getTabLayout().getChildAt(position);
        float translation = tabView.getLeft()
                + (tabView.getWidth() - this.mTabIndicator.getDrawable().getIntrinsicWidth()) / 2
                + ratio * tabView.getWidth();
        this.mTabIndicator.setTranslationX(translation);
    }

    void animateIndicatorToTab(int position) {
        animateIndicatorToTab(getTabLayout().getChildAt(position));
    }

    public void animateIndicatorToTab(View tabView) {
        if (tabView != null && tabView.getWidth() > 0 && !mIsFragmentViewPagerMode) {
            if (mTabIndicatorAnimator == null) {
                mTabIndicatorAnimator = new ObjectAnimator();
                mTabIndicatorAnimator.setTarget(mTabIndicator);
                //mTabIndicatorAnimator.setDuration(300L);
                mTabIndicatorAnimator.setDuration(DEFAULT_TAB_INDICATOR_ANIMATOR_DURATION);
                //mTabIndicatorAnimator.setPropertyName("TranslationX");
                mTabIndicatorAnimator.setPropertyName(TAB_INDICATOR_ANIMATOR_PROPERTY);
            }
            float endTranslationX = tabView.getLeft()
                    + (tabView.getWidth() - mTabIndicator.getDrawable().getIntrinsicWidth()) / 2;
            mTabIndicatorAnimator.setFloatValues(new float[] {
                endTranslationX
            });
            mTabIndicatorAnimator.cancel();
            mTabIndicatorAnimator.start();
        }
    }

    public void animateToTab(int position) {
        super.animateToTab(position);
        animateIndicatorToTab(position);
    }

    public void createTabIndicator() {
        if (mTabIndicator == null) {
            mTabIndicator = new ImageView(mContext, null, /*0x601009a*/R.attr.v5_tab_indicator_arrow_style);
            android.widget.FrameLayout.LayoutParams lParams = new android.widget.FrameLayout.LayoutParams(
                    -2, -2);
            lParams.gravity = 80;
            mTabIndicator.setLayoutParams(lParams);
            int measureSpec = android.view.View.MeasureSpec.makeMeasureSpec(0, 0);
            mTabIndicator.measure(measureSpec, measureSpec);
        }
    }

    protected int getSelectedTabIndex() {
        /*int i = -1;
        LinearLayout localLinearLayout = getTabLayout();
        for (int j = 0;; j++) {
            if (j < localLinearLayout.getChildCount()) {
                if (localLinearLayout.getChildAt(j).isSelected()) {
                    i = j;
                }
            } else {
                return i;
            }
        }*/
        int index = -1;// v1
        LinearLayout tabLayout = getTabLayout(); //v2
        /*int i = 0; //v0
        //goto_0
        if ( i < tabLayout.getChildCount()) { //cond_0 in
            if (tabLayout.getChildAt(i).isSelected() == true) { //cond_1 in
                index = i;
                return index;
            }
            i++;
            //goto :goto_0
        }
        //cond_0 after
        return index;*/
        
        for (int i=0; i<tabLayout.getChildCount(); i++) {
            if (tabLayout.getChildAt(i).isSelected()) {
                index = i;
                break;
            }
        }
        return index;
    }

    /*protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3,
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
    }*/
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        LinearLayout tabLayout;
        super.onLayout(changed, left, top, right, bottom);
        tabLayout = getTabLayout();
        if (tabLayout.getChildCount() <= 1) { // goto _L2;
            if (mTabIndicator.getParent() == this)
                removeView(mTabIndicator);
            return;
        } else { // goto _L1
            View child = tabLayout.getChildAt(getSelectedTabIndex());
            if (child != null) {
                if (mTabIndicator.getParent() != this) {
                    addViewInLayout(mTabIndicator, 1, mTabIndicator.getLayoutParams());
                    mTabIndicator.layout(0, bottom - top - mTabIndicator.getMeasuredHeight(),
                            mTabIndicator.getMeasuredWidth(), bottom - top);
                }
                int translationX = child.getLeft()
                        + (child.getWidth() - mTabIndicator.getDrawable().getIntrinsicWidth()) / 2;
                if (mTabIndicatorAnimator != null)
                    mTabIndicatorAnimator.cancel();
                mTabIndicator.setTranslationX(translationX);
            }
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int position, float ratio, boolean fromHasActionMenu,
            boolean toHasActionMenu) {
        setTabIndicatorTranslation(position, ratio);
    }

    public void onPageSelected(int position) {
        View tabView = getTabLayout().getChildAt(position);
        if (tabView != null)
            tabView.sendAccessibilityEvent(4);
        setTabIndicatorTranslation(position, 0.0F);
    }

    /*public boolean onRequestSendAccessibilityEvent(View paramView,
            AccessibilityEvent paramAccessibilityEvent) {
        if ((paramView == getTabLayout()) && (paramAccessibilityEvent.getEventType() == 1))
            ;
        for (boolean bool = false;; bool = super.onRequestSendAccessibilityEvent(paramView,
                paramAccessibilityEvent))
            return bool;
    }*/
    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        if (child == getTabLayout() && event.getEventType() == 1) {
            return false;
        } else {
            return super.onRequestSendAccessibilityEvent(child, event);
        }
    }

    protected void onViewRemoved(View child) {
        super.onViewRemoved(child);
        if ((child != mTabIndicator) && (mTabIndicator != null)
                && (mTabIndicator.getParent() == this))
            removeView(mTabIndicator);
    }

    public void setFragmentViewPagerMode(boolean isFragmentViewPagerMode) {
        mIsFragmentViewPagerMode = isFragmentViewPagerMode;
    }
}
