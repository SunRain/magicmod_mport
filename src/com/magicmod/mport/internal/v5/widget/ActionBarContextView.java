
package com.magicmod.mport.internal.v5.widget;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.ActionMode;
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
        ActionMenuView menuView = (ActionMenuView).mMenuView;
        int primaryHeight = menuView.getPrimaryContainerHeight();
        float menuViewTranslationY = menuView.getTranslationY();
        float topAnimTransFrom;
        float f2;
        float f3;
        float f4;
        Object topAnim;
        if(visibilityi == 0)
        {
            topAnimTransFrom = -mContentHeight;
            f2 = 0.0F;
            f3 = menuViewTranslationY + (float)primaryHeight;
            f4 = menuViewTranslationY;
        } else
        {
            topAnimTransFrom = 0.0F;
            f2 = -mContentHeight;
            f3 = menuViewTranslationY;
            f4 = menuViewTranslationY + (float)primaryHeight;
        }
        topAnim = ObjectAnimator.ofFloat(this, "TranslationY", new float[] {
            topAnimTransFrom, f2
        });
        if(!mSplitActionBar)
        {
            ((ObjectAnimator) (topAnim)).addListener(mVisAnimListener.withFinalVisibility(visibilityi));
            ((ObjectAnimator) (topAnim)).addListener(this);
        } else
        {
            ObjectAnimator bottomAnim = ObjectAnimator.ofFloat(mMenuView, "TranslationY", new float[] {
                f3, f4
            });
            AnimatorSet set = new AnimatorSet();
            set.play(((Animator) (topAnim))).with(bottomAnim);
            set.addListener(super.mVisAnimListener.withFinalVisibility(visibilityi));
            set.addListener(this);
            set.setDuration(200L);
            topAnim = set;
        }
        return ((Animator) (topAnim));
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
        TextView titleView = getTitleView();
        LinearLayout titleLayout = getTitleLayout();
        int titleLayoutResId = UiUtils.resolveAttribute(mContext, R.attr.v5_action_mode_title_layout);
        if (titleLayout == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (titleLayoutResId != 0)
                titleLayout = (LinearLayout) inflater.inflate(titleLayoutResId, this, false);
            if (titleLayout != null) {
                mButton1 = ((TextView) titleLayout.findViewById(com.android.internal.R.id.button1));
                mButton2 = ((TextView) titleLayout.findViewById(com.android.internal.R.id.button2));
                titleView = (TextView) titleLayout.findViewById(com.android.internal.R.id.title);
                if (mButton1 != null) {
                    mButton1.setOnClickListener(this);
                    mButton1.setText(mButton1MenuItem.getTitle());
                }
                if (mButton2 != null) {
                    mButton2.setOnClickListener(this);
                    mButton2.setText(mButton2MenuItem.getTitle());
                }
                if (titleView != null) {
                    int titleViewStyleRes = getTitleStyleRes();
                    if (titleViewStyleRes != 0)
                        titleView.setTextAppearance(this.mContext, titleViewStyleRes);
                    setTitleView(titleView);
                }
            }
        }
        if (titleView != null)
            titleView.setText(getTitle());
        if ((titleLayout != null) && (titleLayout.getParent() == null)) {
            addView(titleLayout);
            setTitleLayout(titleLayout);
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
        ActionMenuItem item;
        EditActionMode actionMode;
        if(v.getId() == com.android.internal.R.id.button1)
            item = mButton1MenuItem;
        else
            item = mButton2MenuItem;
        actionMode = (EditActionMode)mActionMode.get();
        if(actionMode != null)
            actionMode.onMenuItemSelected((MenuBuilder)(MenuBuilder)actionMode.getMenu(), item);
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
        if (mRequestAniamtion) {
            makeInOutAnimator(0).start();
            mRequestAniamtion = false;
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

    /*public void setButton(int paramInt, CharSequence paramCharSequence) {
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
    }*/
    public void setButton(int whichButton, CharSequence text) {
        if (whichButton != /* 0x1020019 */com.android.internal.R.id.button1) { // goto
                                                                               // _L2;
            if (whichButton == /* 0x102001a */com.android.internal.R.id.button2 
                    && mButton2 != null) {
                mButton2.setText(text);
                mButton2MenuItem.setTitle(text);
            }
            return;
        } else { // goto _L1
            if (mButton1 != null) {
                mButton1.setText(text);
                mButton1MenuItem.setTitle(text);
            }
        }
    }

    public void setContentHeight(int height) {
    }

    public void setCustomView(View view) {
        throw new UnsupportedOperationException("setCustomView is not supported");
    }

    public void setSplitActionBar(boolean split) {
        super.setSplitActionBar(split);
        if ((split) && (mActionMenuPresenter != null)) {
            mMenuView = ((com.android.internal.view.menu.ActionMenuView) mActionMenuPresenter
                    .getMenuView(this));
            mMenuView.getLayoutParams().height = -2;
        }
    }

    public void setSubtitle(CharSequence subtitle) {
        throw new UnsupportedOperationException("setSubtitle is not supported");
    }
}
