package com.magicmod.mport.v5.app;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.SpinnerAdapter;

public abstract interface MiuiActionBar
{
  public abstract int addFragmentTab(String paramString, ActionBar.Tab paramTab, int paramInt, Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean);

  public abstract int addFragmentTab(String paramString, ActionBar.Tab paramTab, Class<? extends Fragment> paramClass, Bundle paramBundle, boolean paramBoolean);

  public abstract void addOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener);

  public abstract void addOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener paramOnMenuVisibilityListener);

  public abstract void addTab(ActionBar.Tab paramTab);

  public abstract void addTab(ActionBar.Tab paramTab, int paramInt);

  public abstract void addTab(ActionBar.Tab paramTab, int paramInt, boolean paramBoolean);

  public abstract void addTab(ActionBar.Tab paramTab, boolean paramBoolean);

  public abstract View getCustomView();

  public abstract int getDisplayOptions();

  public abstract Fragment getFragmentAt(int paramInt);

  public abstract int getFragmentTabCount();

  public abstract int getHeight();

  public abstract int getNavigationItemCount();

  public abstract int getNavigationMode();

  public abstract int getSelectedNavigationIndex();

  public abstract ActionBar.Tab getSelectedTab();

  public abstract CharSequence getSubtitle();

  public abstract ActionBar.Tab getTabAt(int paramInt);

  public abstract int getTabCount();

  public abstract CharSequence getTertiaryTitle();

  public abstract Context getThemedContext();

  public abstract CharSequence getTitle();

  public abstract int getViewPagerOffscreenPageLimit();

  public abstract void hide();

  public abstract boolean isFragmentViewPagerMode();

  public abstract boolean isShowing();

  public abstract ActionBar.Tab newTab();

  public abstract void removeAllFragmentTab();

  public abstract void removeAllTabs();

  public abstract void removeFragmentTab(ActionBar.Tab paramTab);

  public abstract void removeFragmentTab(Fragment paramFragment);

  public abstract void removeFragmentTab(String paramString);

  public abstract void removeFragmentTabAt(int paramInt);

  public abstract void removeOnFragmentViewPagerChangeListener(FragmentViewPagerChangeListener paramFragmentViewPagerChangeListener);

  public abstract void removeOnMenuVisibilityListener(ActionBar.OnMenuVisibilityListener paramOnMenuVisibilityListener);

  public abstract void removeTab(ActionBar.Tab paramTab);

  public abstract void removeTabAt(int paramInt);

  public abstract void selectTab(ActionBar.Tab paramTab);

  public abstract void setBackgroundDrawable(Drawable paramDrawable);

  public abstract void setCustomView(int paramInt);

  public abstract void setCustomView(View paramView);

  public abstract void setCustomView(View paramView, ActionBar.LayoutParams paramLayoutParams);

  public abstract void setDisplayHomeAsUpEnabled(boolean paramBoolean);

  public abstract void setDisplayOptions(int paramInt);

  public abstract void setDisplayOptions(int paramInt1, int paramInt2);

  public abstract void setDisplayShowCustomEnabled(boolean paramBoolean);

  public abstract void setDisplayShowHomeEnabled(boolean paramBoolean);

  public abstract void setDisplayShowTitleEnabled(boolean paramBoolean);

  public abstract void setDisplayUseLogoEnabled(boolean paramBoolean);

  public abstract void setFragmentActionMenuAt(int paramInt, boolean paramBoolean);

  public abstract void setFragmentViewPagerMode(Context paramContext, FragmentManager paramFragmentManager);

  public abstract void setFragmentViewPagerMode(Context paramContext, FragmentManager paramFragmentManager, boolean paramBoolean);

  public abstract void setHomeButtonEnabled(boolean paramBoolean);

  public abstract void setIcon(int paramInt);

  public abstract void setIcon(Drawable paramDrawable);

  public abstract void setListNavigationCallbacks(SpinnerAdapter paramSpinnerAdapter, ActionBar.OnNavigationListener paramOnNavigationListener);

  public abstract void setLogo(int paramInt);

  public abstract void setLogo(Drawable paramDrawable);

  public abstract void setNavigationMode(int paramInt);

  public abstract void setSelectedNavigationItem(int paramInt);

  public abstract void setSplitBackgroundDrawable(Drawable paramDrawable);

  public abstract void setStackedBackgroundDrawable(Drawable paramDrawable);

  public abstract void setSubtitle(int paramInt);

  public abstract void setSubtitle(CharSequence paramCharSequence);

  public abstract void setTertiaryTitle(int paramInt);

  public abstract void setTertiaryTitle(CharSequence paramCharSequence);

  public abstract void setTitle(int paramInt);

  public abstract void setTitle(CharSequence paramCharSequence);

  public abstract void setViewPagerOffscreenPageLimit(int paramInt);

  public abstract void show();

  public abstract void showActionBarShadow(boolean paramBoolean);

  public abstract void showSplitActionBar(boolean paramBoolean1, boolean paramBoolean2);

  public static abstract interface FragmentViewPagerChangeListener
  {
    public abstract void onPageScrollStateChanged(int paramInt);

    public abstract void onPageScrolled(int paramInt, float paramFloat, boolean paramBoolean1, boolean paramBoolean2);

    public abstract void onPageSelected(int paramInt);
  }
}
