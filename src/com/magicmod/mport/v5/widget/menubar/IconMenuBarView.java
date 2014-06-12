
package com.magicmod.mport.v5.widget.menubar;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.LayoutAnimationController;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.magicmod.mport.util.UiUtils;
import com.miui.internal.R;

import miui.R.menu;

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

    /*protected IconMenuBarPrimaryItemView createMoreItemView(
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
    }*/
    
    protected IconMenuBarPrimaryItemView createMoreItemView(IconMenuBarPrimaryItemView convertView,
            int iconMenuBarPrimaryItemResId, Drawable moreIconDrawable,
            Drawable moreIconBackgroundDrawable) {
        IconMenuBarPrimaryItemView itemView;
        if (convertView != null)
            itemView = convertView;
        else
            itemView = (IconMenuBarPrimaryItemView) View.inflate(mContext,
                    iconMenuBarPrimaryItemResId, null);
        itemView.setText(mContext.getResources().getText(/*0x60c01fc*/R.string.v5_icon_menu_bar_more_item_label));
        itemView.setCompoundDrawablesWithIntrinsicBounds(null, moreIconDrawable,
                null, null);
        itemView.setEnabled(true);
        itemView.setCheckable(true);
        itemView.setOnClickListener(this);
        itemView.setItemInvoker(this);
        mMoreIconView = itemView;
        if (moreIconBackgroundDrawable != null)
            itemView.setBackground(moreIconBackgroundDrawable);
        return itemView;
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

    public void initialize(MenuBar menu) {
        this.mMenu = menu;
    }

    public boolean invokeItem(MenuBarItem item) {
        boolean bool = this.mMenu.performItemAction(item, 0);
        if (bool)
            requestExpand(false);
        return bool;
    }

    public boolean isExpanded() {
        return this.mIsExpended;
    }

    @Override
    public void onClick(View v) {
        /*switch (v.getId()) {
            default:
            case 101384354:
            case 101384355:
        }
        while (true) {
            return;
            onMoreItemClick();
        }*/
        switch (v.getId()) {
            case R.id.v5_icon_menu_bar_dim_container: //0x60b00a2
            case R.id.v5_icon_menu_bar_primary_item: //0x60b00a3
                onMoreItemClick();
                break;
        }
    }

    protected void onFinishInflate() {
        /*super.onFinishInflate();
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
        this.mSecondaryContainer = ((LinearLayout) findViewById(101384357));*/
        super.onFinishInflate();
        mDimContainer = findViewById(/*0x60b00a2*/R.id.v5_icon_menu_bar_dim_container);
        mDimContainer.setOnClickListener(this);
        mPrimaryContainer = (LinearLayout) findViewById(/*0x60b00a4*/R.id.v5_icon_menu_bar_real_primary_container);
        mPrimaryContainer.setOnTouchListener(new android.view.View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionevent) {
                boolean flag;
                if (motionevent.getY() > (float) mPrimaryContainer.getPaddingTop())
                    flag = true;
                else
                    flag = false;
                return flag;
            }
        });
        mPrimaryContainer.setLayoutAnimation(null);
        mPrimaryContainerCollapsedBackground = mPrimaryContainer.getBackground();
        Rect padding = new Rect();
        if (mPrimaryContainerCollapsedBackground != null) {
            mPrimaryContainerCollapsedBackground.getPadding(padding);
            mPrimaryContainerHeight = mPrimaryContainerCollapsedBackground.getIntrinsicHeight()
                    - padding.bottom - padding.top;
        }
        mPrimaryContainerExpanedBackground = UiUtils.getDrawable(getContext(), /*0x601007e*/R.attr.v5_menu_primary_mask_bg);
        if (mPrimaryContainerExpanedBackground != null)
            mPrimaryContainerExpanedBackground.setAlpha(0);
        mSecondaryContainer = (LinearLayout) findViewById(/*0x60b00a5*/R.id.v5_icon_menu_bar_secondary_container);
    }

    protected void onLayout(boolean changed, int l, int t, int r,
            int b) {
        int height = b - t;
        mDimContainer.layout(l, t, r, b);
        mPrimaryContainer.layout(l, height - mPrimaryContainer.getMeasuredHeight(),
                r, b);
        mSecondaryContainer.layout(l, b, r, b
                + mSecondaryContainer.getMeasuredHeight());
    }

    void onMoreItemClick() {
        if (mMoreIconView != null) {
            boolean flag;
            if (!mMoreIconView.isSelected())
                flag = true;
            else
                flag = false;
            requestExpand(flag);
        }
    }

    public void onScroll(float percent, boolean fromHasMenuBar, boolean toHasMenuBar) {
        if (toHasMenuBar)
            ensureVisibility();
        setAlpha(computeAlpha(percent, fromHasMenuBar, toHasMenuBar));
        float translationY = computeTranslationY(percent, fromHasMenuBar, toHasMenuBar);
        if ((!fromHasMenuBar) || (!toHasMenuBar))
            mPrimaryContainer.setTranslationY(translationY);
        int count = mPrimaryContainer.getChildCount();
        for (int j = 0; j < count; j++)
            mPrimaryContainer.getChildAt(j).setTranslationY(translationY);
    }

    public void onScrollStateChanged(int state) {
    }

    /*boolean requestExpand(boolean paramBoolean) {
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
    }*/
    boolean requestExpand(boolean expand) {
        boolean flag1 = false;
        if (mExpandAnimator == null || !mExpandAnimator.isRunning() || !mIsExpended) { // goto
                                                                                       // _L2
            if (mIsExpended == expand) { // goto _L4
                return flag1;
            } else { // goto _L3
                mIsExpended = expand;
                if (expand)
                    mExpandAnimator.setFloatValues(new float[] {
                        1.0F
                    });
                else
                    mExpandAnimator.setFloatValues(new float[] {
                            1.0F, 0.0F
                    });
                mPreLayerType = getLayerType();
                setLayerType(2, null);
                mExpandAnimator.setDuration(mExpandDuration);
                mExpandAnimator.addListener(getAnimatorListener());
                mExpandAnimator.start();
                // if(true) goto _L6; else goto _L5
                flag1 = true;
            }
            // goto _L4; else goto _L3
        } else { // goto _L1
            if (!mIsExpended)
                flag1 = true;
            mIsExpended = flag1;
            updateMenuState();
            mExpandAnimator.reverse();
        }
        return flag1;
         
            //goto _L2; else goto _L1
        /*
_L1:
        if(!mIsExpended)
            flag1 = true;
        mIsExpended = flag1;
        updateMenuState();
        mExpandAnimator.reverse();
_L6:
        flag1 = true;
_L4:
        return flag1;
_L2:
        if(mIsExpended == flag) goto _L4; else goto _L3
_L3:
        mIsExpended = flag;
        if(flag)
            mExpandAnimator.setFloatValues(new float[] {
                1.0F
            });
        else
            mExpandAnimator.setFloatValues(new float[] {
                1.0F, 0.0F
            });
        mPreLayerType = getLayerType();
        setLayerType(2, null);
        mExpandAnimator.setDuration(mExpandDuration);
        mExpandAnimator.addListener(getAnimatorListener());
        mExpandAnimator.start();
        if(true) goto _L6; else goto _L5
_L5:*/
    }  

    void setAnimationPercent(float percent) {
        mAnimationPercent = percent;
        float transition = percent *(float)( -mSecondaryContainer.getHeight());
        mPrimaryContainer.setTranslationY(transition);
        mSecondaryContainer.setTranslationY(transition);
        mDimContainer.setAlpha(percent);
    }

    public void setPrimaryContainerLayoutAnimation(
            LayoutAnimationController paramLayoutAnimationController) {
        this.mPrimaryContainer.setLayoutAnimation(paramLayoutAnimationController);
    }

    public void setPrimaryMaskDrawable(Drawable drawable) {
        if (mPrimaryContainer.getBackground() == mPrimaryContainerExpanedBackground)
            mPrimaryContainer.setBackground(drawable);
        mPrimaryContainerExpanedBackground = drawable;
    }

    public void setSecondaryContainerLayoutAnimation(
            LayoutAnimationController animation) {
        this.mSecondaryContainer.setLayoutAnimation(animation);
    }

    public static class LayoutParams extends ViewGroup.MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }
    }
}
