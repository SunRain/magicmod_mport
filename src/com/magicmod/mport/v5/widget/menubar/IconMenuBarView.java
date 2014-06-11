
package com.magicmod.mport.v5.widget.menubar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

public class IconMenuBarView extends FrameLayout implements MenuBar.ItemInvoker, MenuBarView,
        View.OnClickListener {
    private static final int DEFAULT_EXPAND_DURATION = 0x12c;//300;
    private float mAnimationPercent;
    private Animator.AnimatorListener mAnimatorListener;
    private View mDimContainer;
    private ObjectAnimator mExpandAnimator;/* = ObjectAnimator.ofFloat(this, "AnimationPercent",
            new float[] {
                0.0F
            });*/
    private int mExpandDuration;// = 300;
    private boolean mIsExpended;
    private int mMaxItems;
    private MenuBar mMenu;
    private View mMoreIconView;
    private int mPreLayerType;
    private LinearLayout mPrimaryContainer;
    private Drawable mPrimaryContainerCollapsedBackground;
    private Drawable mPrimaryContainerExpanedBackground;
    private int mPrimaryContainerHeight;
    private LinearLayout mSecondaryContainer;

    public IconMenuBarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        
        mExpandAnimator = ObjectAnimator.ofFloat(this, "AnimationPercent", new float[] {
                0.0F
            });
            mExpandDuration = 0x12c;//300;
    }

    private Animator.AnimatorListener getAnimatorListener() {
        if (this.mAnimatorListener == null)
            this.mAnimatorListener = new Animator.AnimatorListener() {
                public void onAnimationCancel(Animator paramAnonymousAnimator) {
                }

                public void onAnimationEnd(Animator paramAnonymousAnimator) {
                    IconMenuBarView.this.setLayerType(IconMenuBarView.this.mPreLayerType, null);
                    if (!IconMenuBarView.this.mIsExpended) {
                        IconMenuBarView.this.mDimContainer.setVisibility(8);
                        if (IconMenuBarView.this.mPrimaryContainerExpanedBackground != null)
                            IconMenuBarView.this.mPrimaryContainer
                                    .setBackground(IconMenuBarView.this.mPrimaryContainerCollapsedBackground);
                    }
                }

                public void onAnimationRepeat(Animator paramAnonymousAnimator) {
                }

                public void onAnimationStart(Animator paramAnonymousAnimator) {
                    if (IconMenuBarView.this.mIsExpended) {
                        IconMenuBarView.this.mDimContainer.setVisibility(0);
                        IconMenuBarView.this.mDimContainer.setAlpha(0.0F);
                        if (IconMenuBarView.this.mPrimaryContainerExpanedBackground != null)
                            IconMenuBarView.this.mPrimaryContainer
                                    .setBackground(IconMenuBarView.this.mPrimaryContainerExpanedBackground);
                    }
                    IconMenuBarView.this.updateMenuState();
                }
            };
        return this.mAnimatorListener;
    }

    private void updateMenuState() {
        //MenuBar localMenuBar = this.mMenu;
        //if (this.mIsExpended)
        //    ;
        //for (int i = 0;; i = 1) {
        //    localMenuBar.dispatchMenuModeChange(i);
        //    this.mMoreIconView.setSelected(this.mIsExpended);
        //    return;
        //}
        MenuBar menubar = mMenu;
        int i;
        if(mIsExpended)
            i = 0;
        else
            i = 1;
        menubar.dispatchMenuModeChange(i);
        mMoreIconView.setSelected(mIsExpended);
    }

    /*protected float computeAlpha(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
        float f = 1.0F;
        if ((paramBoolean1) && (paramBoolean2))
            ;
        while (true) {
            return f;
            if (paramBoolean1)
                f = (int) (10.0F * (1.0F - paramFloat)) / 10.0F;
            else if (paramBoolean2)
                f = (int) (paramFloat * 10.0F) / 10.0F;
        }
    }*/

    protected float computeAlpha(float percent, boolean fromHasMenuBar, boolean toHasMenuBar) {
        float alpha = 1.0F;
        if (!fromHasMenuBar || !toHasMenuBar) {
            if (fromHasMenuBar) {
                alpha = (float) (int) (10F * (1.0F - percent)) / 10F;
            } else if (toHasMenuBar) {
                alpha = (float) (int) (percent * 10F) / 10F;
            }
            return alpha;
            // if(true) goto _L1; else goto _L3
        } else { // goto _L1
            return alpha;
        }
    }

    /*protected float computeTranslationY(float paramFloat, boolean paramBoolean1,
            boolean paramBoolean2) {
        float f = this.mPrimaryContainerHeight;
        if ((paramBoolean1) && (paramBoolean2))
            if (paramFloat < 0.5D)
                paramFloat *= 2.0F;
        while (true) {
            return paramFloat * f;
            paramFloat = 2.0F * (1.0F - paramFloat);
            continue;
            if (paramBoolean2)
                paramFloat = 1.0F - paramFloat;
        }
    }*/
    protected float computeTranslationY(float percent, boolean fromHasMenuBar, boolean toHasMenubar) {
        
        float height = mPrimaryContainerHeight;
        float translationY;
        if (!fromHasMenuBar || !toHasMenubar) { // goto _L2;
            if (toHasMenubar) {
                percent = 1.0F - percent;
            }
            // if(true) goto _L4; else goto _L3
            //return percent * height;
            translationY = percent * height;
        } else {// goto _L1 
            if ((double) percent < 0.5D)
                percent *= 2.0F;
            else
                percent = 2.0F * (1.0F - percent);
            translationY = percent * height;
        }
        return translationY;
/*
_L1:
        if((double)f < 0.5D)
            f *= 2.0F;
        else
            f = 2.0F * (1.0F - f);
_L4:
        return f * f1;
_L2:
        if(flag1)
            f = 1.0F - f;
        if(true) goto _L4; else goto _L3
_L3:*/
    }

    protected IconMenuBarPrimaryItemView createMoreItemView(
            IconMenuBarPrimaryItemView paramIconMenuBarPrimaryItemView, int paramInt,
            Drawable paramDrawable1, Drawable paramDrawable2) {
        if (paramIconMenuBarPrimaryItemView != null)
            ;
        for (IconMenuBarPrimaryItemView localIconMenuBarPrimaryItemView = paramIconMenuBarPrimaryItemView;; localIconMenuBarPrimaryItemView = (IconMenuBarPrimaryItemView) View
                .inflate(this.mContext, paramInt, null)) {
            localIconMenuBarPrimaryItemView
                    .setText(this.mContext.getResources().getText(101450236));
            localIconMenuBarPrimaryItemView.setCompoundDrawablesWithIntrinsicBounds(null,
                    paramDrawable1, null, null);
            localIconMenuBarPrimaryItemView.setEnabled(true);
            localIconMenuBarPrimaryItemView.setCheckable(true);
            localIconMenuBarPrimaryItemView.setOnClickListener(this);
            localIconMenuBarPrimaryItemView.setItemInvoker(this);
            this.mMoreIconView = localIconMenuBarPrimaryItemView;
            if (paramDrawable2 != null)
                localIconMenuBarPrimaryItemView.setBackground(paramDrawable2);
            return localIconMenuBarPrimaryItemView;
        }
    }

    protected void ensureVisibility() {
        if (getVisibility() != 0)
            setVisibility(0);
    }

    float getAnimationPercent() {
        return this.mAnimationPercent;
    }

    public int getMaxItems() {
        return this.mMaxItems;
    }

    public LinearLayout getPrimaryContainer() {
        return this.mPrimaryContainer;
    }

    int getPrimaryContainerHeight() {
        return this.mPrimaryContainerHeight;
    }

    public LayoutAnimationController getPrimaryContainerLayoutAnimation() {
        return this.mPrimaryContainer.getLayoutAnimation();
    }

    public LinearLayout getSecondaryContainer() {
        return this.mSecondaryContainer;
    }

    public LayoutAnimationController getSecondaryContainerLayoutAnimation() {
        return this.mSecondaryContainer.getLayoutAnimation();
    }

    public int getWindowAnimations() {
        return 0;
    }

    public void initialize(MenuBar paramMenuBar) {
        this.mMenu = paramMenuBar;
    }

    public boolean invokeItem(MenuBarItem paramMenuBarItem) {
        boolean bool = this.mMenu.performItemAction(paramMenuBarItem, 0);
        if (bool)
            requestExpand(false);
        return bool;
    }

    public boolean isExpanded() {
        return this.mIsExpended;
    }

    public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
            case 101384354:
            case 101384355:
        }
        while (true) {
            return;
            onMoreItemClick();
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mDimContainer = findViewById(101384354);
        this.mDimContainer.setOnClickListener(this);
        this.mPrimaryContainer = ((LinearLayout) findViewById(101384356));
        this.mPrimaryContainer.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View paramAnonymousView, MotionEvent paramAnonymousMotionEvent) {
                if (paramAnonymousMotionEvent.getY() > IconMenuBarView.this.mPrimaryContainer
                        .getPaddingTop())
                    ;
                for (boolean bool = true;; bool = false)
                    return bool;
            }
        });
        this.mPrimaryContainer.setLayoutAnimation(null);
        this.mPrimaryContainerCollapsedBackground = this.mPrimaryContainer.getBackground();
        Rect localRect = new Rect();
        if (this.mPrimaryContainerCollapsedBackground != null) {
            this.mPrimaryContainerCollapsedBackground.getPadding(localRect);
            this.mPrimaryContainerHeight = (this.mPrimaryContainerCollapsedBackground
                    .getIntrinsicHeight() - localRect.bottom - localRect.top);
        }
        this.mPrimaryContainerExpanedBackground = UiUtils.getDrawable(getContext(), 100728958);
        if (this.mPrimaryContainerExpanedBackground != null)
            this.mPrimaryContainerExpanedBackground.setAlpha(0);
        this.mSecondaryContainer = ((LinearLayout) findViewById(101384357));
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        int i = paramInt4 - paramInt2;
        this.mDimContainer.layout(paramInt1, paramInt2, paramInt3, paramInt4);
        this.mPrimaryContainer.layout(paramInt1, i - this.mPrimaryContainer.getMeasuredHeight(),
                paramInt3, paramInt4);
        this.mSecondaryContainer.layout(paramInt1, paramInt4, paramInt3, paramInt4
                + this.mSecondaryContainer.getMeasuredHeight());
    }

    void onMoreItemClick() {
        if (this.mMoreIconView != null)
            if (this.mMoreIconView.isSelected())
                break label26;
        label26: for (boolean bool = true;; bool = false) {
            requestExpand(bool);
            return;
        }
    }

    public void onScroll(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
        if (paramBoolean2)
            ensureVisibility();
        setAlpha(computeAlpha(paramFloat, paramBoolean1, paramBoolean2));
        float f = computeTranslationY(paramFloat, paramBoolean1, paramBoolean2);
        if ((!paramBoolean1) || (!paramBoolean2))
            this.mPrimaryContainer.setTranslationY(f);
        int i = this.mPrimaryContainer.getChildCount();
        for (int j = 0; j < i; j++)
            this.mPrimaryContainer.getChildAt(j).setTranslationY(f);
    }

    public void onScrollStateChanged(int paramInt) {
    }

    boolean requestExpand(boolean paramBoolean) {
        boolean bool = false;
        if ((this.mExpandAnimator != null) && (this.mExpandAnimator.isRunning())
                && (this.mIsExpended)) {
            if (!this.mIsExpended)
                bool = true;
            this.mIsExpended = bool;
            updateMenuState();
            this.mExpandAnimator.reverse();
            bool = true;
        }
        while (this.mIsExpended == paramBoolean)
            return bool;
        this.mIsExpended = paramBoolean;
        if (paramBoolean)
            this.mExpandAnimator.setFloatValues(new float[] {
                1.0F
            });
        while (true) {
            this.mPreLayerType = getLayerType();
            setLayerType(2, null);
            this.mExpandAnimator.setDuration(this.mExpandDuration);
            this.mExpandAnimator.addListener(getAnimatorListener());
            this.mExpandAnimator.start();
            break;
            this.mExpandAnimator.setFloatValues(new float[] {
                    1.0F, 0.0F
            });
        }
    }

    void setAnimationPercent(float paramFloat) {
        this.mAnimationPercent = paramFloat;
        float f = paramFloat * -this.mSecondaryContainer.getHeight();
        this.mPrimaryContainer.setTranslationY(f);
        this.mSecondaryContainer.setTranslationY(f);
        this.mDimContainer.setAlpha(paramFloat);
    }

    public void setPrimaryContainerLayoutAnimation(
            LayoutAnimationController paramLayoutAnimationController) {
        this.mPrimaryContainer.setLayoutAnimation(paramLayoutAnimationController);
    }

    public void setPrimaryMaskDrawable(Drawable paramDrawable) {
        if (this.mPrimaryContainer.getBackground() == this.mPrimaryContainerExpanedBackground)
            this.mPrimaryContainer.setBackground(paramDrawable);
        this.mPrimaryContainerExpanedBackground = paramDrawable;
    }

    public void setSecondaryContainerLayoutAnimation(
            LayoutAnimationController paramLayoutAnimationController) {
        this.mSecondaryContainer.setLayoutAnimation(paramLayoutAnimationController);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {
        public LayoutParams(int paramInt1, int paramInt2) {
            super(paramInt2);
        }

        public LayoutParams(Context paramContext, AttributeSet paramAttributeSet) {
            super(paramAttributeSet);
        }
    }
}
