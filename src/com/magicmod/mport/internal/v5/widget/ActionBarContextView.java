
package com.magicmod.mport.internal.v5.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.android.internal.view.menu.ActionMenuItem;
import com.android.internal.view.menu.MenuBuilder;
import com.magicmod.mport.internal.v5.view.ActionModeView;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuPresenter;
import com.magicmod.mport.internal.v5.view.menu.ActionMenuView;
import com.magicmod.mport.util.UiUtils;
import com.magicmod.mport.v5.view.EditActionMode;
import com.miui.internal.R;

import java.lang.ref.WeakReference;

public class ActionBarContextView extends com.android.internal.widget.ActionBarContextView
        implements OnClickListener, ActionModeView {
    private static final int ANIMATE_IDLE = 0;
    private static final int ANIMATE_IN = 1;
    private static final int ANIMATE_OUT = 2;
    private static final int FADE_DURATION = 200;
    private WeakReference<ActionMode> mActionMode;
    private TextView mButton1;
    private ActionMenuItem mButton1MenuItem;
    private TextView mButton2;
    private ActionMenuItem mButton2MenuItem;
    private int mContentHeightWithoutPadding;
    private boolean mRequestAniamtion;

    public ActionBarContextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Drawable localDrawable = getBackground();
        if (localDrawable != null) {
            mContentHeight = localDrawable.getIntrinsicHeight();
            Rect localRect = new Rect();
            localDrawable.getPadding(localRect);
            mContentHeightWithoutPadding = (mContentHeight - localRect.top - localRect.bottom);
        }
        mButton1MenuItem = new ActionMenuItem(context, 0, com.android.internal.R.id.button1, 0, 0,
                context.getString(com.android.internal.R.string.cancel));
        mButton2MenuItem = new ActionMenuItem(context, 0, com.android.internal.R.id.button2, 0, 0,
                context.getString(R.string.v5_select_all));
    }

    public void animateToVisibility(int visibility) {
        if (mVisibilityAnim != null)
            mVisibilityAnim.cancel();
        if (visibility == 0) {
            setVisibility(0);
            mRequestAniamtion = true;
        } else {
            makeInOutAnimator(visibility).start();
        }
    }

    protected void cancelVisibilityAnimation() {
        if (mVisibilityAnim != null) {
            mVisibilityAnim.cancel();
            mVisibilityAnim = null;
        }
    }

    public void closeMode() {
        cancelVisibilityAnimation();
        setAnimationMode(2);
    }

    public CharSequence getSubtitle() {
        throw new UnsupportedOperationException("getSubTitle is not supported");
    }

    public void initForMode(ActionMode mode) {
        if (mActionMode != null) {
            cancelVisibilityAnimation();
            killMode();
        }
        mActionMode = new WeakReference(mode);
        MenuBuilder localMenuBuilder = (MenuBuilder) mode.getMenu();
        if (mActionMenuPresenter != null)
            mActionMenuPresenter.dismissPopupMenus();
        mActionMenuPresenter = new ActionMenuPresenter(
                // mContext, 100859944, 100859941, 100859943, true);
                mContext, R.layout.v5_edit_mode_action_menu_view,
                R.layout.v5_edit_mode_action_menu_primary_item,
                R.layout.v5_edit_mode_action_menu_secondary_item, true);
        localMenuBuilder.addMenuPresenter(this.mActionMenuPresenter);
        FrameLayout.LayoutParams localLayoutParams = new FrameLayout.LayoutParams(-2, -1);
        if (!mSplitActionBar) {
            mMenuView = ((ActionMenuView) mActionMenuPresenter.getMenuView(this));
            this.mMenuView.setBackgroundDrawable(null);
            addView(this.mMenuView, localLayoutParams);
        } else {
            mActionMenuPresenter.setWidthLimit(
                    getContext().getResources().getDisplayMetrics().widthPixels, true);
            mActionMenuPresenter.setItemLimit(0x7fffffff);
            localLayoutParams.width = -1;
            localLayoutParams.height = -2;
            localLayoutParams.gravity = 80;
            mMenuView = ((ActionMenuView) mActionMenuPresenter.getMenuView(mSplitView));
            mSplitView.addView(mMenuView, localLayoutParams);
        }
        /*while (true) {
            return;
            this.mActionMenuPresenter.setWidthLimit(
                    getContext().getResources().getDisplayMetrics().widthPixels, true);
            this.mActionMenuPresenter.setItemLimit(2147483647);
            localLayoutParams.width = -1;
            localLayoutParams.height = -2;
            localLayoutParams.gravity = 80;
            this.mMenuView = ((com.miui.internal.v5.view.menu.ActionMenuView) this.mActionMenuPresenter
                    .getMenuView(this.mSplitView));
            this.mSplitView.addView(this.mMenuView, localLayoutParams);
        }*/
    }

    public void killMode() {
        super.killMode();
        mActionMode = null;
    }

    protected Animator makeInOutAnimator(int visibilityi)
    {
        ActionMenuView actionmenuview = (ActionMenuView).mMenuView;
        int j = actionmenuview.getPrimaryContainerHeight();
        float f = actionmenuview.getTranslationY();
        float f1;
        float f2;
        float f3;
        float f4;
        Object obj;
        if(visibilityi == 0)
        {
            f1 = -super.mContentHeight;
            f2 = 0.0F;
            f3 = f + (float)j;
            f4 = f;
        } else
        {
            f1 = 0.0F;
            f2 = -super.mContentHeight;
            f3 = f;
            f4 = f + (float)j;
        }
        obj = ObjectAnimator.ofFloat(this, "TranslationY", new float[] {
            f1, f2
        });
        if(!super.mSplitActionBar)
        {
            ((ObjectAnimator) (obj)).addListener(super.mVisAnimListener.withFinalVisibility(visibilityi));
            ((ObjectAnimator) (obj)).addListener(this);
        } else
        {
            ObjectAnimator objectanimator = ObjectAnimator.ofFloat(super.mMenuView, "TranslationY", new float[] {
                f3, f4
            });
            AnimatorSet animatorset = new AnimatorSet();
            animatorset.play(((Animator) (obj))).with(objectanimator);
            animatorset.addListener(super.mVisAnimListener.withFinalVisibility(visibilityi));
            animatorset.addListener(this);
            animatorset.setDuration(200L);
            obj = animatorset;
        }
        return ((Animator) (obj));
    }

    protected int measureChildView(View child, int availableWidth, int childSpecHeight, int spacing) {
        //if (paramView != getTitleLayout())
        //    ;
        //for (int i = super.measureChildView(paramView, paramInt1, paramInt2, paramInt3);; i = Math
        //        .max(0, paramInt1 - paramView.getMeasuredWidth() - paramInt3)) {
        //    return i;
        //    paramView.measure(View.MeasureSpec.makeMeasureSpec(paramInt1, 1073741824), paramInt2);
        //}
        int l;
        if(child != getTitleLayout())
        {
            l = super.measureChildView(child, availableWidth, childSpecHeight, spacing);
        } else
        {
            child.measure(android.view.View.MeasureSpec.makeMeasureSpec(availableWidth, 0x40000000), childSpecHeight);
            l = Math.max(0, availableWidth - child.getMeasuredWidth() - spacing);
        }
        return l;
    }

    protected boolean miuiInitTitle() {
        TextView localTextView = getTitleView();
        LinearLayout localLinearLayout = getTitleLayout();
        int i = UiUtils.resolveAttribute(this.mContext, R.attr.v5_action_mode_title_layout);
        if (localLinearLayout == null) {
            LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
            if (i != 0)
                localLinearLayout = (LinearLayout) localLayoutInflater.inflate(i, this, false);
            if (localLinearLayout != null) {
                this.mButton1 = ((TextView) localLinearLayout.findViewById(com.android.internal.R.id.button1));
                this.mButton2 = ((TextView) localLinearLayout.findViewById(com.android.internal.R.id.button2));
                localTextView = (TextView) localLinearLayout.findViewById(com.android.internal.R.id.title);
                if (this.mButton1 != null) {
                    this.mButton1.setOnClickListener(this);
                    this.mButton1.setText(this.mButton1MenuItem.getTitle());
                }
                if (this.mButton2 != null) {
                    this.mButton2.setOnClickListener(this);
                    this.mButton2.setText(this.mButton2MenuItem.getTitle());
                }
                if (localTextView != null) {
                    int j = getTitleStyleRes();
                    if (j != 0)
                        localTextView.setTextAppearance(this.mContext, j);
                    setTitleView(localTextView);
                }
            }
        }
        if (localTextView != null)
            localTextView.setText(getTitle());
        if ((localLinearLayout != null) && (localLinearLayout.getParent() == null)) {
            addView(localLinearLayout);
            setTitleLayout(localLinearLayout);
        }
        return true;
    }

    @Override
    public void onClick(View v) {
        //if (paramView.getId() == 16908313)
        //    ;
        ////for (ActionMenuItem localActionMenuItem = this.mButton1MenuItem;; localActionMenuItem = this.mButton2MenuItem) {
         //   EditActionMode localEditActionMode = (EditActionMode) this.mActionMode.get();
         //   if (localEditActionMode != null)
        ////        localEditActionMode.onMenuItemSelected((MenuBuilder) localEditActionMode.getMenu(),
         //               localActionMenuItem);
          //  return;
        //}
        ActionMenuItem actionmenuitem;
        EditActionMode editactionmode;
        if(v.getId() == com.android.internal.R.id.button1)
            actionmenuitem = mButton1MenuItem;
        else
            actionmenuitem = mButton2MenuItem;
        editactionmode = (EditActionMode)mActionMode.get();
        if(editactionmode != null)
            editactionmode.onMenuItemSelected((MenuBuilder)(MenuBuilder)editactionmode.getMenu(), actionmenuitem);
    }

    protected void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        Drawable localDrawable = UiUtils.getDrawable(this.mContext, R.attr.v5_edit_mode_top_bar_bg);//100728938);
        if (localDrawable != null)
            this.mContentHeight = localDrawable.getIntrinsicHeight();
        setBackground(localDrawable);
    }

    protected void onLayout(boolean changed, int left, int top, int right,
            int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (this.mRequestAniamtion) {
            makeInOutAnimator(0).start();
            this.mRequestAniamtion = false;
        }
    }

    public boolean onTouchEvent(MotionEvent event) {
        //if (paramMotionEvent.getY() < this.mContentHeightWithoutPadding)
        //    ;
        //for (boolean bool = true;; bool = super.onTouchEvent(paramMotionEvent))
        //    return bool;
        boolean flag;
        if(event.getY() < (float)mContentHeightWithoutPadding)
            flag = true;
        else
            flag = super.onTouchEvent(event);
        return flag;
    }

    protected int positionChild(View child, int x, int y, int contentHeight,
            boolean reverse) {
        int i = 0;
        if ((child != null) && (child.getParent() == this))
            i = super.positionChild(child, x, y, contentHeight, reverse);
        return i;
    }

    public void setButton(int paramInt, CharSequence paramCharSequence) {
        if (paramInt == 16908313)
            if (this.mButton1 != null) {
                this.mButton1.setText(paramCharSequence);
                this.mButton1MenuItem.setTitle(paramCharSequence);
            }
        while (true) {
            return;
            if ((paramInt == 16908314) && (this.mButton2 != null)) {
                this.mButton2.setText(paramCharSequence);
                this.mButton2MenuItem.setTitle(paramCharSequence);
            }
        }
    }

    public void setContentHeight(int paramInt) {
    }

    public void setCustomView(View paramView) {
        throw new UnsupportedOperationException("setCustomView is not supported");
    }

    public void setSplitActionBar(boolean paramBoolean) {
        super.setSplitActionBar(paramBoolean);
        if ((paramBoolean) && (this.mActionMenuPresenter != null)) {
            this.mMenuView = ((com.android.internal.view.menu.ActionMenuView) this.mActionMenuPresenter
                    .getMenuView(this));
            this.mMenuView.getLayoutParams().height = -2;
        }
    }

    public void setSubtitle(CharSequence paramCharSequence) {
        throw new UnsupportedOperationException("setSubtitle is not supported");
    }
}
