package com.magicmod.mport.internal.v5.widget;

import android.R;
import android.R.integer;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;

import com.android.internal.view.menu.ActionMenuView;
import com.android.internal.widget.ActionBarView;
import com.android.internal.widget.ScrollingTabContainerView;
import com.magicmod.mport.v5.app.MiuiActionBar.FragmentViewPagerChangeListener;

public class ActionBarContainer extends com.android.internal.widget.ActionBarContainer
implements FragmentViewPagerChangeListener{
    //private static final int[] ATTRS = { 16842964 };
    //private static final int[] ATTRS = R.attr.background;
    private Animator mCurrentShowAnim;
    private AnimatorListenerAdapter mHideListener = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        ActionBarContainer.this.setVisibility(8);
        ActionBarContainer.access$002(ActionBarContainer.this, null);
      }
    };
    private ActionMenuView mMenuView;
    private boolean mRequestAnimation = true;
    private Runnable mShowActionMenuViewRunnable = new Runnable()
    {
      public void run()
      {
        ActionBarContainer.this.ensureActionMenuView();
        if ((ActionBarContainer.this.mMenuView != null) && (ActionBarContainer.this.mMenuView.getMenuItems() > 0))
          ActionBarContainer.this.mMenuView.playOpenAnimator();
      }
    };
    private AnimatorListenerAdapter mShowListener = new AnimatorListenerAdapter()
    {
      public void onAnimationEnd(Animator paramAnonymousAnimator)
      {
        ActionBarContainer.access$002(ActionBarContainer.this, null);
      }
    };

    public ActionBarContainer(Context paramContext)
    {
      this(paramContext, null);
    }

    public ActionBarContainer(Context paramContext, AttributeSet paramAttributeSet)
    {
      super(paramContext, paramAttributeSet);
    }

    public void draw(Canvas paramCanvas)
    {
      super.draw(paramCanvas);
      if (this.mRequestAnimation)
      {
        post(new Runnable()
        {
          public void run()
          {
            ActionBarContainer.this.show(true);
          }
        });
        this.mRequestAnimation = false;
      }
    }

    protected void ensureActionMenuView()
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
    }

    public void hide(boolean paramBoolean)
    {
      if (this.mCurrentShowAnim != null)
        this.mCurrentShowAnim.cancel();
      if ((paramBoolean) && (isSplit()))
      {
        float[] arrayOfFloat = new float[2];
        arrayOfFloat[0] = 0.0F;
        arrayOfFloat[1] = getHeight();
        this.mCurrentShowAnim = ObjectAnimator.ofFloat(this, "TranslationY", arrayOfFloat);
        this.mCurrentShowAnim.setDuration(getContext().getResources().getInteger(17694720));
        this.mCurrentShowAnim.addListener(this.mHideListener);
        this.mCurrentShowAnim.start();
      }
      while (true)
      {
        return;
        setVisibility(8);
      }
    }

    protected void onConfigurationChanged(Configuration paramConfiguration)
    {
      super.onConfigurationChanged(paramConfiguration);
      if (!isSplit())
      {
          //ATTRS =>R.attr.background;
        TypedArray localTypedArray = getContext().obtainStyledAttributes(null, ATTRS, 16843470, 0);
        setPrimaryBackground(localTypedArray.getDrawable(0));
        localTypedArray.recycle();
      }
    }

    public void onPageScrollStateChanged(int paramInt)
    {
    }

    public void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2)
    {
      ensureActionMenuView();
      if ((this.mMenuView != null) && ((paramBoolean1) || (paramBoolean2)))
        this.mMenuView.onPageScrolled(paramInt, paramFloat, paramBoolean1, paramBoolean2);
    }

    public void onPageSelected(int paramInt)
    {
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent)
    {
      boolean bool = super.onTouchEvent(paramMotionEvent);
      if (isSplit())
        bool = false;
      return bool;
    }

    public void onWindowHide()
    {
      removeCallbacks(this.mShowActionMenuViewRunnable);
      ensureActionMenuView();
      if ((this.mMenuView != null) && (this.mMenuView.getMenuItems() > 0))
        this.mMenuView.playCloseAnimator();
    }

    public void onWindowShow()
    {
      post(this.mShowActionMenuViewRunnable);
    }

    public void setTabContainer(ScrollingTabContainerView paramScrollingTabContainerView)
    {
      if (paramScrollingTabContainerView == getTabContainer());
      while (true)
      {
        return;
        super.setTabContainer(paramScrollingTabContainerView);
        updateTabContainerBackground();
      }
    }

    public void show(boolean paramBoolean)
    {
      if (this.mCurrentShowAnim != null)
        this.mCurrentShowAnim.cancel();
      setVisibility(0);
      if (paramBoolean)
      {
        if (isSplit())
        {
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
      while (true)
      {
        return;
        setTranslationY(0.0F);
      }
    }

    public void updateTabContainerBackground()
    {
      View localView = getTabContainer();
      if ((localView != null) && (!isSplit()))
        if (!((ActionBarView)getActionBarView()).hasTitle())
          break label45;
      label45: for (Drawable localDrawable = UiUtils.getDrawable(this.mContext, 100728987); ; localDrawable = UiUtils.getDrawable(this.mContext, 100728988))
      {
        localView.setBackground(localDrawable);
        return;
      }
    }
}
