
package com.magicmod.mport.internal.v5.view.menu;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.magicmod.mport.internal.v5.app.ActionBarImpl;
import com.magicmod.mport.internal.v5.widget.ActionBarView;
import com.magicmod.mport.util.UiUtils;
import com.magicmod.mport.v5.widget.Views;
import com.miui.internal.R;

public class ActionMenuView extends com.android.internal.view.menu.ActionMenuView implements
        View.OnClickListener, OnDismissListener {
    private static final int VIEW_STATE_COLLAPSED = 0x0;
    private static final int VIEW_STATE_COLLAPSING = 0x1;
    private static final int VIEW_STATE_EXPANDED = 0x3;
    private static final int VIEW_STATE_EXPANDING = 0x2;
    private ExpandCollapseAnimator mExpandCollapseAnimator;
    private MenuBuilder mMenu;
    private PopupWindow mMenuPopupWindow;
    private Drawable mMoreIcon;
    private View mMoreIconView;
    private OpenCloseAnimator mOpenCloseAnimator;
    private LinearLayout mPrimaryContainer;
    private Drawable mPrimaryContainerCollapsedBackground;
    private Drawable mPrimaryContainerExpanedBackground;
    private int mPrimaryContainerHeight;
    private LinearLayout mSecondaryContainer;
    private int mSecondaryContainerLayoutResId;
    private int mViewState;// = 0;

    public ActionMenuView(Context context) {
        this(context, null);
    }

    public ActionMenuView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mViewState = 0;
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ActionMenuView);
        this.mMoreIcon = a.getDrawable(1);
        this.mSecondaryContainerLayoutResId = a.getResourceId(2, 0);
        this.mPrimaryContainerExpanedBackground = a.getDrawable(0);
        a.recycle();
    }

    /*private float computeAlpha(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
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
    private float computeAlpha(float percent, boolean fromHasActionMenu, boolean toHasActionMenu) {
        float alpha = 1.0F;
        if (!fromHasActionMenu || !toHasActionMenu) {
            if (fromHasActionMenu)
                alpha = (float) (int) (10F * (1.0F - percent)) / 10F;
            else if (toHasActionMenu)
                alpha = (float) (int) (percent * 10F) / 10F;
            return alpha;
        } else {
            return alpha;
        }
    }


    /*private float computeTranslationY(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
        float f = this.mPrimaryContainer.getHeight();
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
    private float computeTranslationY(float percent, boolean fromHasActionMenu, boolean toHasActionMenu) {
        float height = mPrimaryContainer.getHeight();
        if (!fromHasActionMenu || !toHasActionMenu) {
            if (toHasActionMenu)
                percent = 1.0F - percent;
            //return percent * height;
        } else {
            if ((double) percent < 0.5D)
                percent *= 2.0F;
            else
                percent = 2.0F * (1.0F - percent);
        }
        return percent * height;
    }

    ActionMenuPrimaryItemView createMoreItemView(ActionMenuPrimaryItemView convertView, int primaryItemResId) {
        ActionMenuPrimaryItemView itemView;
        if (convertView != null)
            itemView = convertView;
        else
            itemView = (ActionMenuPrimaryItemView) View.inflate(mContext, primaryItemResId, null);
        itemView.setTitle(mContext.getResources().getText(/*0x60c01fc*/R.string.v5_icon_menu_bar_more_item_label));
        itemView.setIcon(mMoreIcon);
        itemView.setEnabled(true);
        itemView.setCheckable(true);
        ((View) itemView).setOnClickListener(this);
        itemView.setItemInvoker(this);
        mMoreIconView = (View) itemView;
        return itemView;
    }

    public int getMenuItems() {
        int retval = mPrimaryContainer.getChildCount();
        if (mSecondaryContainer != null)
            retval += mSecondaryContainer.getChildCount();
        return retval;
    }

    LinearLayout getPrimaryContainer() {
        return this.mPrimaryContainer;
    }

    public int getPrimaryContainerHeight() {
        return this.mPrimaryContainerHeight;
    }

    LinearLayout getSecondaryContainer(boolean create) {
        if ((mSecondaryContainer == null) && (create) && (mSecondaryContainerLayoutResId > 0))
            mSecondaryContainer = ((LinearLayout) Views.inflate(mContext,
                    mSecondaryContainerLayoutResId, this, false));
        if ((mSecondaryContainer != null) && (isSplitActionBar())) {
            ViewParent oldParent = mSecondaryContainer.getParent();
            if (oldParent != this) {
                if ((oldParent instanceof ViewGroup))
                    ((ViewGroup) oldParent).removeView(mSecondaryContainer);
                addView(this.mSecondaryContainer);
            }
        }
        return mSecondaryContainer;
    }

    public int getSecondryContainerHeight() {
        int retval = 0;
        if (mSecondaryContainer != null)
            retval = mSecondaryContainer.getMeasuredHeight();
        return retval;
    }

    public int getWindowAnimations() {
        return 0;
    }

    public void initialize(MenuBuilder menu) {
        this.mMenu = menu;
    }

    public boolean invokeItem(MenuItemImpl item) {
        boolean invoked = this.mMenu.performItemAction(item, 0);
        if (invoked)
            requestExpand(false);
        return invoked;
    }

    boolean isExpanded() {
        boolean flag;
        if (mViewState == 3)
            flag = true;
        else
            flag = false;
        return flag;
    }

    protected boolean isSplitActionBar() {
        boolean retval = false;
        ActionBarView actionBarView = ActionBarView.findActionBarViewByView(this);
        if (actionBarView != null)
            retval = actionBarView.isSplitActionBar();
        return retval;
    }

    /*public void onClick(View paramView) {
        switch (paramView.getId()) {
            default:
                return;
            case 101384354:
            case 101384355:
        }
        if (this.mMoreIconView != null)
            if (this.mMoreIconView.isSelected())
                break label57;
        label57: for (boolean bool = true;; bool = false) {
            requestExpand(bool);
            break;
            break;
        }
    }*/
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.v5_icon_menu_bar_dim_container: //0x60b00a2:
            case R.id.v5_icon_menu_bar_primary_item: //0x60b00a3:
                if (mMoreIconView != null) { //cond_0 in
                    if (!mMoreIconView.isSelected()) {
                        requestExpand(true);                        
                    } else {
                        requestExpand(false);
                    }
                } //cond_0 after
                break;
            default:
                break;
        }
    }
    

    public void onConfigurationChanged(Configuration newConfig) {
        if (this.mSecondaryContainer != null) {
            this.mSecondaryContainer.removeAllViews();
            ViewParent parent = this.mSecondaryContainer.getParent();
            if ((parent instanceof ViewGroup))
                ((ViewGroup) parent).removeView(this.mSecondaryContainer);
            this.mSecondaryContainer = null;
        }
        super.onConfigurationChanged(newConfig);
    }

    public void onDismiss() {
        this.mViewState = 0;
        this.mMoreIconView.setSelected(false);
        if (this.mSecondaryContainer != null) {
            ViewParent parent = this.mSecondaryContainer.getParent();
            if ((parent instanceof ViewGroup))
                ((ViewGroup) parent).removeView(this.mSecondaryContainer);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPrimaryContainerHeight = UiUtils.getSplitActionMenuHeight(getContext());
        this.mPrimaryContainer = ((LinearLayout) findViewById(/*0x60b00a4*/R.id.v5_icon_menu_bar_real_primary_container));
        this.mPrimaryContainerCollapsedBackground = this.mPrimaryContainer.getBackground();
        this.mOpenCloseAnimator = new OpenCloseAnimator();
        this.mExpandCollapseAnimator = new ExpandCollapseAnimator();
    }

    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        if (isSplitActionBar()) {
            int secondaryContainerHeight = getSecondryContainerHeight();
            mPrimaryContainer.layout(0, 0, r - l, b - t - secondaryContainerHeight);
            if (mSecondaryContainer != null)
                mSecondaryContainer.layout(0, b - t - secondaryContainerHeight, r - l, b - t);
        } else {
            mPrimaryContainer.layout(0, 0, r - l, b - t);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (getMenuItems() == 0) {
            setMeasuredDimension(0, 0);
        } else {
            byte gravity = 0x51;//81;
            if (!isSplitActionBar())
                gravity = 0x5;
            mPrimaryContainer.setGravity(gravity);
            mPrimaryContainer.measure(widthMeasureSpec, heightMeasureSpec);
            int measuredWidth = mPrimaryContainer.getMeasuredWidth();
            int measuredHeight = mPrimaryContainer.getMeasuredHeight();
            if (isSplitActionBar() && mSecondaryContainer != null) {
                mSecondaryContainer.measure(widthMeasureSpec, android.view.View.MeasureSpec.makeMeasureSpec(0, 0));
                measuredHeight += mSecondaryContainer.getMeasuredHeight();
                if (isExpanded())
                    setTranslationY(0.0F);
                else
                    setTranslationY(mSecondaryContainer.getMeasuredHeight());
            } else {
                setTranslationY(0.0F);
            }
            setMeasuredDimension(measuredWidth, measuredHeight);
        }
    }

    public void onPageScrolled(int position, float ratio, boolean fromHasActionMenu,
            boolean toHasActionMenu) {
        setAlpha(computeAlpha(ratio, fromHasActionMenu, toHasActionMenu));
        float translationY = computeTranslationY(ratio, fromHasActionMenu, toHasActionMenu);
        if ((!fromHasActionMenu) || (!toHasActionMenu)
                || (this.mPrimaryContainer.getTranslationY() != 0.0F))
            this.mPrimaryContainer.setTranslationY(translationY);
        this.mOpenCloseAnimator.setTranslationY(translationY);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        boolean flag;
        if (ev.getY() > (float) mPrimaryContainer.getPaddingTop() + getTranslationY())
            flag = true;
        else
            flag = super.onTouchEvent(ev);
        return flag;
    }

    protected void onVisibilityChanged(View changedView, int visibility) {
        super.onVisibilityChanged(changedView, visibility);
        if (changedView == this)
            playOpenAnimator();
    }

    public void playCloseAnimator() {
        if (isExpanded())
            requestExpand(false);
        this.mOpenCloseAnimator.close();
    }

    public void playOpenAnimator() {
        this.mOpenCloseAnimator.open();
    }

    /*boolean requestExpand(boolean paramBoolean) {
        boolean bool = false;
        switch (this.mViewState) {
            case 1:
            case 2:
            default:
                if (bool) {
                    if (!paramBoolean)
                        break label96;
                    if (!isSplitActionBar())
                        break label88;
                    this.mExpandCollapseAnimator.expand();
                }
                break;
            case 0:
            case 3:
        }
        while (true) {
            return bool;
            if (!paramBoolean)
                break;
            this.mViewState = 2;
            bool = true;
            break;
            if (paramBoolean)
                break;
            this.mViewState = 1;
            bool = true;
            break;
            label88: showOrHideMenuPopupWindow(true);
            continue;
            label96: if (this.mMenuPopupWindow == null)
                this.mExpandCollapseAnimator.collapse();
            else
                showOrHideMenuPopupWindow(false);
        }
    }*/
    boolean requestExpand(boolean expand) {
        boolean handled = false; // v0
        // v2 0x1
        switch (mViewState) {
            case 0: // pswitch_1
                if (expand) { // cond_0 in
                    mViewState = 0x2;
                    handled = true;
                    // goto goto_0
                }
                break;
            case 1:
            case 2:
                break;
            case 3: // pswitch_2
                if (!expand) { // cond_0 in
                    mViewState = 0x1;
                    handled = true;
                    // goto goto_0
                } // cond_0 after
                break;
            default:
                break;
        }
        if (handled) { // cond_1 in
            if (expand) { // cond_3 in
                if (isSplitActionBar())
                    mExpandCollapseAnimator.expand();
                else
                    showOrHideMenuPopupWindow(true);
            } else { // cond_3 after
                if (mMenuPopupWindow == null)
                    mExpandCollapseAnimator.collapse();
                else
                    showOrHideMenuPopupWindow(false);
            }
        } // cond_1 after
        return handled;
    }

    public void setPrimaryContainerCollapsedBackground(Drawable d) {
        if (this.mPrimaryContainer.getBackground() == this.mPrimaryContainerCollapsedBackground) {
            this.mPrimaryContainer.setPadding(0, 0, 0, 0);
            this.mPrimaryContainer.setBackground(d);
        }
        this.mPrimaryContainerCollapsedBackground = d;
    }

    public void setPrimaryContainerExpanedBackground(Drawable d) {
        if (this.mPrimaryContainer.getBackground() == this.mPrimaryContainerExpanedBackground) {
            this.mPrimaryContainer.setPadding(0, 0, 0, 0);
            this.mPrimaryContainer.setBackground(d);
        }
        this.mPrimaryContainerExpanedBackground = d;
    }

    protected void showOrHideMenuPopupWindow(boolean show) {
        if (show) {
            if (mMenuPopupWindow != null) {
                if (mMenuPopupWindow.isShowing())
                    mMenuPopupWindow.dismiss();
                mMenuPopupWindow = null;
            }
            mMenuPopupWindow = new PopupWindow(mContext);
            mMenuPopupWindow.setContentView(mSecondaryContainer);
            mMenuPopupWindow.setWindowLayoutMode(-2, -1);
            mMenuPopupWindow.setOnDismissListener(this);
            mMenuPopupWindow.setBackgroundDrawable(null);
            mMenuPopupWindow.setAnimationStyle(/*0x60d0007*/R.style.V5_Animation_Menu);
            mMenuPopupWindow.showAsDropDown(this, 0, 0);
            mViewState = 3;
            mMoreIconView.setSelected(true);
            ActionBarImpl.getActionBarByView(this).getDimAnimator(false, this)
                    .getDimShowingAnimator().start();
        } else {
            if (mMenuPopupWindow != null) {
                if (mMenuPopupWindow.isShowing())
                    mMenuPopupWindow.dismiss();
                mMenuPopupWindow = null;
            }
            ActionBarImpl.getActionBarByView(this).getDimAnimator(false, this)
                    .getDimHidingAnimator().start();
        }
    }
    
    public void startPrimaryContainerLayoutAnimation() {
        this.mPrimaryContainer.startLayoutAnimation();
    }

    class ExpandCollapseAnimator implements Animator.AnimatorListener {
        Animator mCollapseAnimator;
        Animator mExpandAnimator;

        public ExpandCollapseAnimator() {
        }

        void collapse() {
            initialize();
            this.mExpandAnimator.cancel();
            this.mCollapseAnimator.cancel();
            this.mCollapseAnimator.start();
        }

        void expand() {
            initialize();
            this.mExpandAnimator.cancel();
            this.mCollapseAnimator.cancel();
            this.mExpandAnimator.start();
        }

        void initialize() {
            if ((this.mExpandAnimator == null) || (this.mCollapseAnimator == null)) {
                ActionBarImpl.DimAnimator dimAnimator = ActionBarImpl.getActionBarByView(
                        ActionMenuView.this).getDimAnimator(false, ActionMenuView.this);
                AnimatorSet set = new AnimatorSet();
                set.play(ObjectAnimator.ofFloat(this, "Value", new float[] {
                        0.0F, 1.0F
                })).with(dimAnimator.getDimShowingAnimator());
                set.setDuration(ActionMenuView.this.getResources().getInteger(
                        com.android.internal.R.integer.config_shortAnimTime//0x10e0000
                        ));
                set.addListener(this);
                this.mExpandAnimator = set;
                AnimatorSet set2 = new AnimatorSet();
                set2.play(ObjectAnimator.ofFloat(this, "Value", new float[] {
                        0.0F, 1.0F
                })).with(dimAnimator.getDimHidingAnimator());
                set2.setDuration(ActionMenuView.this.getResources().getInteger(
                        0x10e0000));
                set2.addListener(this);
                this.mCollapseAnimator = set2;
            }
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animator) {
            if (animator == mExpandAnimator) {
                //ActionMenuView.access$502(ActionMenuView.this, 3);
                mViewState = 3;
            } else {
                //if (ActionMenuView.access$300(ActionMenuView.this) != null)
                //    ActionMenuView.access$000(ActionMenuView.this).setBackground(
                //            ActionMenuView.access$600(ActionMenuView.this));
                //ActionMenuView.access$502(ActionMenuView.this, 0);
                if (mPrimaryContainerExpanedBackground != null) {
                    mPrimaryContainer.setBackground(mPrimaryContainerCollapsedBackground);
                }
                mViewState = 0;
            }
        }

        public void onAnimationRepeat(Animator animation) {
        }

        public void onAnimationStart(Animator animation) {
            if (animation == this.mExpandAnimator) {
                if (mPrimaryContainerExpanedBackground != null)
                    mPrimaryContainer.setBackground(mPrimaryContainerExpanedBackground);
                mMoreIconView.setSelected(true);
            } else {
                mMoreIconView.setSelected(false);
            }
        }

        public void setValue(float value) {
            if (mExpandAnimator.isRunning()) {
                setTranslationY(mSecondaryContainer.getMeasuredHeight() * (1.0F - value));
            } else {
                setTranslationY(value * mSecondaryContainer.getMeasuredHeight());
            }
        }
    }

    class OpenCloseAnimator implements Animator.AnimatorListener {
        Animator mCloseAnimator;
        Animator mCurrentAnimator;
        boolean mIsOpen;

        public OpenCloseAnimator() {
        }

        void cancel() {
            initialize();
            if (this.mCurrentAnimator != null) {
                this.mCurrentAnimator.cancel();
                this.mCurrentAnimator = null;
            }
        }

        void close() {
            cancel();
            this.mIsOpen = false;
            this.mCloseAnimator.start();
        }

        void initialize() {
            if (this.mCloseAnimator == null) {
                float[] arrayOfFloat = new float[1];
                arrayOfFloat[0] = ActionMenuView.this.mPrimaryContainerHeight;
                this.mCloseAnimator = ObjectAnimator.ofFloat(this, "TranslationY", arrayOfFloat);
                this.mCloseAnimator.setDuration(ActionMenuView.this.getResources().getInteger(
                        com.android.internal.R.integer.config_shortAnimTime// 0x10e0000
                        ));
                this.mCloseAnimator.addListener(this);
            }
        }

        public void onAnimationCancel(Animator animation) {
        }

        public void onAnimationEnd(Animator animation) {
            this.mCurrentAnimator = null;
        }

        public void onAnimationRepeat(Animator paramAnimator) {
        }

        public void onAnimationStart(Animator animation) {
            this.mCurrentAnimator = animation;
        }

        void open() {
            cancel();
            this.mIsOpen = true;
            ActionMenuView.this.setAlpha(1.0F);
            setTranslationY(0.0F);
            ActionMenuView.this.mPrimaryContainer.setTranslationY(0.0F);
            ActionMenuView.this.mPrimaryContainer.startLayoutAnimation();
        }

        public void setTranslationY(float y) {
            for (int i = 0; i < mPrimaryContainer.getChildCount(); i++)
                mPrimaryContainer.getChildAt(i).setTranslationY(y);
        }
    }
}
