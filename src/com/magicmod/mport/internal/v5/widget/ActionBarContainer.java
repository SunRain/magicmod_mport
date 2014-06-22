
package com.magicmod.mport.internal.v5.widget;

import android.R;
import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.android.internal.widget.ActionBarView;
import com.android.internal.widget.ScrollingTabContainerView;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;
import com.magicmod.mport.v5.app.MiuiActionBar.FragmentViewPagerChangeListener;

public class ActionBarContainer extends com.android.internal.widget.ActionBarContainer implements
        FragmentViewPagerChangeListener {
    // private static final int[] ATTRS = { 16842964 };
    private static final int[] ATTRS = {R.attr.background};
    private Animator mCurrentShowAnim;
    private AnimatorListenerAdapter mHideListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator animation) {
            setVisibility(8);
            //ActionBarContainer.access$002(ActionBarContainer.this, null);
            mCurrentShowAnim = null;
        }
    };
    private ActionMenuView mMenuView;
    private boolean mRequestAnimation = true;
    
    private Runnable mShowActionMenuViewRunnable = new Runnable() {
        @Override
        public void run() {
            ensureActionMenuView();
            if ((mMenuView != null) && (mMenuView.getMenuItems() > 0))
                mMenuView.playOpenAnimator();
        }
    };
    
    private AnimatorListenerAdapter mShowListener = new AnimatorListenerAdapter() {
        @Override
        public void onAnimationEnd(Animator paramAnonymousAnimator) {
            //ActionBarContainer.access$002(ActionBarContainer.this, null);
            mCurrentShowAnim = null;
        }
    };

    public ActionBarContainer(Context context) {
        this(context, null);
    }

    public ActionBarContainer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void draw(Canvas canvas) {
        super.draw(canvas);
        if (mRequestAnimation) {
            post(new Runnable() {
                @Override
                public void run() {
                    show(true);
                }
            });
            mRequestAnimation = false;
        }
    }

    /*protected void ensureActionMenuView()
    {
      if (this.mMenuView == null)
      {
        if (!isSplit())
          break label27;
        this.mMenuView = ((ActionMenuView)getChildAt(0));
      }
      while (true)
      {
        return;
        label27: ActionBarView localActionBarView = (ActionBarView)getActionBarView();
        if (localActionBarView != null)
          this.mMenuView = localActionBarView.getActionMenuView();
      }
    }*/
    protected void ensureActionMenuView() {
        if (mMenuView != null) { // goto _L2;
            return;
        } else { // goto _L1
            if (!isSplit()) { // goto _L4;
                ActionBarView actionBarView = (ActionBarView) getActionBarView();
                if (actionBarView != null) {
                    mMenuView = actionBarView.getActionMenuView();
                }
                // if(true) goto _L2; else goto _L5
                return;
            } else { // //goto _L3
                mMenuView = (ActionMenuView) getChildAt(0);
            }
        }
    }

    public void hide(boolean anim) {
        if (mCurrentShowAnim != null)
            mCurrentShowAnim.cancel();
        if (anim && isSplit()) {
            float af[] = new float[2];
            af[0] = 0.0F;
            af[1] = getHeight();
            mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", af);
            mCurrentShowAnim.setDuration(getContext().getResources().getInteger(/*0x10e0000*/R.integer.config_shortAnimTime));
            mCurrentShowAnim.addListener(mHideListener);
            mCurrentShowAnim.start();
        } else {
            setVisibility(8);
        }
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if (!isSplit()) {
            TypedArray a = getContext().obtainStyledAttributes(null, ATTRS, /*0x10102ce*/R.attr.actionBarStyle, 0);
            setPrimaryBackground(a.getDrawable(0));
            a.recycle();
        }
    }

    public void onPageScrollStateChanged(int state) {
    }

    public void onPageScrolled(int position, float ratio, boolean fromHasActionMenu,
            boolean toHasActionMenu) {
        ensureActionMenuView();
        if ((mMenuView != null) && ((fromHasActionMenu) || (toHasActionMenu)))
            mMenuView.onPageScrolled(position, ratio, fromHasActionMenu, toHasActionMenu);
    }

    public void onPageSelected(int position) {
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean result = super.onTouchEvent(ev);
        if (isSplit())
            result = false;
        return result;
    }

    public void onWindowHide() {
        removeCallbacks(mShowActionMenuViewRunnable);
        ensureActionMenuView();
        if ((mMenuView != null) && (mMenuView.getMenuItems() > 0))
            mMenuView.playCloseAnimator();
    }

    public void onWindowShow() {
        post(mShowActionMenuViewRunnable);
    }

    /*public void setTabContainer(ScrollingTabContainerView paramScrollingTabContainerView) {
        if (paramScrollingTabContainerView == getTabContainer())
            ;
        while (true) {
            return;
            super.setTabContainer(paramScrollingTabContainerView);
            updateTabContainerBackground();
        }
    }*/
    public void setTabContainer(ScrollingTabContainerView tabView) {
        if (tabView != getTabContainer()) {
            super.setTabContainer(tabView);
            updateTabContainerBackground();
        }
    }

   /* public void show(boolean paramBoolean) {
        if (this.mCurrentShowAnim != null)
            this.mCurrentShowAnim.cancel();
        setVisibility(0);
        if (paramBoolean) {
            if (isSplit()) {
                float[] arrayOfFloat = new float[2];
                arrayOfFloat[0] = getHeight();
                arrayOfFloat[1] = 0.0F;
                this.mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", arrayOfFloat);
                this.mCurrentShowAnim.setDuration(getContext().getResources().getInteger(17694720));
                this.mCurrentShowAnim.addListener(this.mShowListener);
                this.mCurrentShowAnim.start();
            }
            ensureActionMenuView();
            if ((this.mMenuView != null) && (this.mMenuView.getMenuItems() > 0))
                this.mMenuView.startPrimaryContainerLayoutAnimation();
        }
        while (true) {
            return;
            setTranslationY(0.0F);
        }
    }*/
    public void show(boolean anim) {
        if (mCurrentShowAnim != null) {
            mCurrentShowAnim.cancel();
        }
        setVisibility(0);
        if (anim) {
            if (isSplit()) {
                float af[] = new float[2];
                af[0] = getHeight();
                af[1] = 0.0F;
                mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", af);
                mCurrentShowAnim.setDuration(getContext().getResources().getInteger(/*0x10e0000*/R.integer.config_shortAnimTime));
                mCurrentShowAnim.addListener(mShowListener);
                mCurrentShowAnim.start();
            }
            ensureActionMenuView();
            if (mMenuView != null && mMenuView.getMenuItems() > 0) {
                mMenuView.startPrimaryContainerLayoutAnimation();
            }
        } else {
            setTranslationY(0.0F);
        }
    }

    /*public void updateTabContainerBackground() {
        View localView = getTabContainer();
        if ((localView != null) && (!isSplit()))
            if (!((ActionBarView) getActionBarView()).hasTitle())
                break label45;
        label45: for (Drawable localDrawable = UiUtils.getDrawable(this.mContext, 100728987);; localDrawable = UiUtils
                .getDrawable(this.mContext, 100728988)) {
            localView.setBackground(localDrawable);
            return;
        }
    }*/
    public void updateTabContainerBackground() {
        View tabView = getTabContainer();
        if (tabView != null && !isSplit()) {
            Drawable background;
            if (((ActionBarView) getActionBarView()).hasTitle())
                background = UiUtils.getDrawable(mContext, /*0x601009b*/com.miui.internal.R.attr.v5_tab_indicator_bg);
            else
                background = UiUtils.getDrawable(mContext, /*0x601009c*/com.miui.internal.R.attr.v5_tab_indicator_bg_no_title);
            tabView.setBackground(background);
        }
    }
}
