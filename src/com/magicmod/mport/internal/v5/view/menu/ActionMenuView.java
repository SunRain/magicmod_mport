
package com.magicmod.mport.internal.v5.view.menu;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.PopupWindow.OnDismissListener;

import com.android.internal.view.menu.MenuBuilder;

public class ActionMenuView extends com.android.internal.view.menu.ActionMenuView implements
        OnClickListener, OnDismissListener {
    private static final int VIEW_STATE_COLLAPSED = 0;
    private static final int VIEW_STATE_COLLAPSING = 1;
    private static final int VIEW_STATE_EXPANDED = 3;
    private static final int VIEW_STATE_EXPANDING = 2;
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
    private int mViewState = 0;

    public ActionMenuView(Context paramContext) {
        this(paramContext, null);
    }

    public ActionMenuView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
        TypedArray localTypedArray = paramContext.obtainStyledAttributes(paramAttributeSet,
                R.styleable.ActionMenuView);
        this.mMoreIcon = localTypedArray.getDrawable(1);
        this.mSecondaryContainerLayoutResId = localTypedArray.getResourceId(2, 0);
        this.mPrimaryContainerExpanedBackground = localTypedArray.getDrawable(0);
        localTypedArray.recycle();
    }

    private float computeAlpha(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
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
    }

    private float computeTranslationY(float paramFloat, boolean paramBoolean1, boolean paramBoolean2) {
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
    }

    ActionMenuPrimaryItemView createMoreItemView(
            ActionMenuPrimaryItemView paramActionMenuPrimaryItemView, int paramInt) {
        if (paramActionMenuPrimaryItemView != null)
            ;
        for (ActionMenuPrimaryItemView localActionMenuPrimaryItemView = paramActionMenuPrimaryItemView;; localActionMenuPrimaryItemView = (ActionMenuPrimaryItemView) View
                .inflate(this.mContext, paramInt, null)) {
            localActionMenuPrimaryItemView
                    .setTitle(this.mContext.getResources().getText(101450236));
            localActionMenuPrimaryItemView.setIcon(this.mMoreIcon);
            localActionMenuPrimaryItemView.setEnabled(true);
            localActionMenuPrimaryItemView.setCheckable(true);
            ((View) localActionMenuPrimaryItemView).setOnClickListener(this);
            localActionMenuPrimaryItemView.setItemInvoker(this);
            this.mMoreIconView = ((View) localActionMenuPrimaryItemView);
            return localActionMenuPrimaryItemView;
        }
    }

    public int getMenuItems() {
        int i = this.mPrimaryContainer.getChildCount();
        if (this.mSecondaryContainer != null)
            i += this.mSecondaryContainer.getChildCount();
        return i;
    }

    LinearLayout getPrimaryContainer() {
        return this.mPrimaryContainer;
    }

    public int getPrimaryContainerHeight() {
        return this.mPrimaryContainerHeight;
    }

    LinearLayout getSecondaryContainer(boolean paramBoolean) {
        if ((this.mSecondaryContainer == null) && (paramBoolean)
                && (this.mSecondaryContainerLayoutResId > 0))
            this.mSecondaryContainer = ((LinearLayout) Views.inflate(this.mContext,
                    this.mSecondaryContainerLayoutResId, this, false));
        if ((this.mSecondaryContainer != null) && (isSplitActionBar())) {
            ViewParent localViewParent = this.mSecondaryContainer.getParent();
            if (localViewParent != this) {
                if ((localViewParent instanceof ViewGroup))
                    ((ViewGroup) localViewParent).removeView(this.mSecondaryContainer);
                addView(this.mSecondaryContainer);
            }
        }
        return this.mSecondaryContainer;
    }

    public int getSecondryContainerHeight() {
        int i = 0;
        if (this.mSecondaryContainer != null)
            i = this.mSecondaryContainer.getMeasuredHeight();
        return i;
    }

    public int getWindowAnimations() {
        return 0;
    }

    public void initialize(MenuBuilder paramMenuBuilder) {
        this.mMenu = paramMenuBuilder;
    }

    public boolean invokeItem(MenuItemImpl paramMenuItemImpl) {
        boolean bool = this.mMenu.performItemAction(paramMenuItemImpl, 0);
        if (bool)
            requestExpand(false);
        return bool;
    }

    boolean isExpanded() {
        if (this.mViewState == 3)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    protected boolean isSplitActionBar() {
        boolean bool = false;
        ActionBarView localActionBarView = ActionBarView.findActionBarViewByView(this);
        if (localActionBarView != null)
            bool = localActionBarView.isSplitActionBar();
        return bool;
    }

    public void onClick(View paramView) {
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
    }

    public void onConfigurationChanged(Configuration paramConfiguration) {
        if (this.mSecondaryContainer != null) {
            this.mSecondaryContainer.removeAllViews();
            ViewParent localViewParent = this.mSecondaryContainer.getParent();
            if ((localViewParent instanceof ViewGroup))
                ((ViewGroup) localViewParent).removeView(this.mSecondaryContainer);
            this.mSecondaryContainer = null;
        }
        super.onConfigurationChanged(paramConfiguration);
    }

    public void onDismiss() {
        this.mViewState = 0;
        this.mMoreIconView.setSelected(false);
        if (this.mSecondaryContainer != null) {
            ViewParent localViewParent = this.mSecondaryContainer.getParent();
            if ((localViewParent instanceof ViewGroup))
                ((ViewGroup) localViewParent).removeView(this.mSecondaryContainer);
        }
    }

    protected void onFinishInflate() {
        super.onFinishInflate();
        this.mPrimaryContainerHeight = UiUtils.getSplitActionMenuHeight(getContext());
        this.mPrimaryContainer = ((LinearLayout) findViewById(101384356));
        this.mPrimaryContainerCollapsedBackground = this.mPrimaryContainer.getBackground();
        this.mOpenCloseAnimator = new OpenCloseAnimator();
        this.mExpandCollapseAnimator = new ExpandCollapseAnimator();
    }

    protected void onLayout(boolean paramBoolean, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        if (isSplitActionBar()) {
            int i = getSecondryContainerHeight();
            this.mPrimaryContainer.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2 - i);
            if (this.mSecondaryContainer != null)
                this.mSecondaryContainer.layout(0, paramInt4 - paramInt2 - i,
                        paramInt3 - paramInt1, paramInt4 - paramInt2);
        }
        while (true) {
            return;
            this.mPrimaryContainer.layout(0, 0, paramInt3 - paramInt1, paramInt4 - paramInt2);
        }
    }

    protected void onMeasure(int paramInt1, int paramInt2) {
        if (getMenuItems() == 0) {
            setMeasuredDimension(0, 0);
            return;
        }
        int i = 81;
        if (!isSplitActionBar())
            i = 5;
        this.mPrimaryContainer.setGravity(i);
        this.mPrimaryContainer.measure(paramInt1, paramInt2);
        int j = this.mPrimaryContainer.getMeasuredWidth();
        int k = this.mPrimaryContainer.getMeasuredHeight();
        if ((isSplitActionBar()) && (this.mSecondaryContainer != null)) {
            this.mSecondaryContainer.measure(paramInt1, View.MeasureSpec.makeMeasureSpec(0, 0));
            k += this.mSecondaryContainer.getMeasuredHeight();
            if (isExpanded())
                setTranslationY(0.0F);
        }
        while (true) {
            setMeasuredDimension(j, k);
            break;
            setTranslationY(this.mSecondaryContainer.getMeasuredHeight());
            continue;
            setTranslationY(0.0F);
        }
    }

    public void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1,
            boolean paramBoolean2) {
        setAlpha(computeAlpha(paramFloat, paramBoolean1, paramBoolean2));
        float f = computeTranslationY(paramFloat, paramBoolean1, paramBoolean2);
        if ((!paramBoolean1) || (!paramBoolean2)
                || (this.mPrimaryContainer.getTranslationY() != 0.0F))
            this.mPrimaryContainer.setTranslationY(f);
        this.mOpenCloseAnimator.setTranslationY(f);
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        if (paramMotionEvent.getY() > this.mPrimaryContainer.getPaddingTop() + getTranslationY())
            ;
        for (boolean bool = true;; bool = super.onTouchEvent(paramMotionEvent))
            return bool;
    }

    protected void onVisibilityChanged(View paramView, int paramInt) {
        super.onVisibilityChanged(paramView, paramInt);
        if (paramView == this)
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

    boolean requestExpand(boolean paramBoolean) {
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
    }

    public void setPrimaryContainerCollapsedBackground(Drawable paramDrawable) {
        if (this.mPrimaryContainer.getBackground() == this.mPrimaryContainerCollapsedBackground) {
            this.mPrimaryContainer.setPadding(0, 0, 0, 0);
            this.mPrimaryContainer.setBackground(paramDrawable);
        }
        this.mPrimaryContainerCollapsedBackground = paramDrawable;
    }

    public void setPrimaryContainerExpanedBackground(Drawable paramDrawable) {
        if (this.mPrimaryContainer.getBackground() == this.mPrimaryContainerExpanedBackground) {
            this.mPrimaryContainer.setPadding(0, 0, 0, 0);
            this.mPrimaryContainer.setBackground(paramDrawable);
        }
        this.mPrimaryContainerExpanedBackground = paramDrawable;
    }

    protected void showOrHideMenuPopupWindow(boolean paramBoolean) {
        if (paramBoolean) {
            if (this.mMenuPopupWindow != null) {
                if (this.mMenuPopupWindow.isShowing())
                    this.mMenuPopupWindow.dismiss();
                this.mMenuPopupWindow = null;
            }
            this.mMenuPopupWindow = new PopupWindow(this.mContext);
            this.mMenuPopupWindow.setContentView(this.mSecondaryContainer);
            this.mMenuPopupWindow.setWindowLayoutMode(-2, -1);
            this.mMenuPopupWindow.setOnDismissListener(this);
            this.mMenuPopupWindow.setBackgroundDrawable(null);
            this.mMenuPopupWindow.setAnimationStyle(101515271);
            this.mMenuPopupWindow.showAsDropDown(this, 0, 0);
            this.mViewState = 3;
            this.mMoreIconView.setSelected(true);
            ActionBarImpl.getActionBarByView(this).getDimAnimator(false, this)
                    .getDimShowingAnimator().start();
        }
        while (true) {
            return;
            if (this.mMenuPopupWindow != null) {
                if (this.mMenuPopupWindow.isShowing())
                    this.mMenuPopupWindow.dismiss();
                this.mMenuPopupWindow = null;
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
                ActionBarImpl.DimAnimator localDimAnimator = ActionBarImpl.getActionBarByView(
                        ActionMenuView.this).getDimAnimator(false, ActionMenuView.this);
                AnimatorSet localAnimatorSet1 = new AnimatorSet();
                localAnimatorSet1.play(ObjectAnimator.ofFloat(this, "Value", new float[] {
                        0.0F, 1.0F
                })).with(localDimAnimator.getDimShowingAnimator());
                localAnimatorSet1.setDuration(ActionMenuView.this.getResources().getInteger(
                        17694720));
                localAnimatorSet1.addListener(this);
                this.mExpandAnimator = localAnimatorSet1;
                AnimatorSet localAnimatorSet2 = new AnimatorSet();
                localAnimatorSet2.play(ObjectAnimator.ofFloat(this, "Value", new float[] {
                        0.0F, 1.0F
                })).with(localDimAnimator.getDimHidingAnimator());
                localAnimatorSet2.setDuration(ActionMenuView.this.getResources().getInteger(
                        17694720));
                localAnimatorSet2.addListener(this);
                this.mCollapseAnimator = localAnimatorSet2;
            }
        }

        public void onAnimationCancel(Animator paramAnimator) {
        }

        public void onAnimationEnd(Animator paramAnimator) {
            if (paramAnimator == this.mExpandAnimator)
                ActionMenuView.access$502(ActionMenuView.this, 3);
            while (true) {
                return;
                if (ActionMenuView.this.mPrimaryContainerExpanedBackground != null)
                    ActionMenuView.this.mPrimaryContainer
                            .setBackground(ActionMenuView.this.mPrimaryContainerCollapsedBackground);
                ActionMenuView.access$502(ActionMenuView.this, 0);
            }
        }

        public void onAnimationRepeat(Animator paramAnimator) {
        }

        public void onAnimationStart(Animator paramAnimator) {
            if (paramAnimator == this.mExpandAnimator) {
                if (ActionMenuView.this.mPrimaryContainerExpanedBackground != null)
                    ActionMenuView.this.mPrimaryContainer
                            .setBackground(ActionMenuView.this.mPrimaryContainerExpanedBackground);
                ActionMenuView.this.mMoreIconView.setSelected(true);
            }
            while (true) {
                return;
                ActionMenuView.this.mMoreIconView.setSelected(false);
            }
        }

        public void setValue(float paramFloat) {
            if (this.mExpandAnimator.isRunning())
                ActionMenuView.this.setTranslationY(ActionMenuView.this.mSecondaryContainer
                        .getMeasuredHeight() * (1.0F - paramFloat));
            while (true) {
                return;
                ActionMenuView.this.setTranslationY(paramFloat
                        * ActionMenuView.this.mSecondaryContainer.getMeasuredHeight());
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
                        17694720));
                this.mCloseAnimator.addListener(this);
            }
        }

        public void onAnimationCancel(Animator paramAnimator) {
        }

        public void onAnimationEnd(Animator paramAnimator) {
            this.mCurrentAnimator = null;
        }

        public void onAnimationRepeat(Animator paramAnimator) {
        }

        public void onAnimationStart(Animator paramAnimator) {
            this.mCurrentAnimator = paramAnimator;
        }

        void open() {
            cancel();
            this.mIsOpen = true;
            ActionMenuView.this.setAlpha(1.0F);
            setTranslationY(0.0F);
            ActionMenuView.this.mPrimaryContainer.setTranslationY(0.0F);
            ActionMenuView.this.mPrimaryContainer.startLayoutAnimation();
        }

        public void setTranslationY(float paramFloat) {
            for (int i = 0; i < ActionMenuView.this.mPrimaryContainer.getChildCount(); i++)
                ActionMenuView.this.mPrimaryContainer.getChildAt(i).setTranslationY(paramFloat);
        }
    }
}
