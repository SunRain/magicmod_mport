package com.magicmod.mport.v5.view;

import android.graphics.drawable.Drawable;

import com.android.internal.R;

public abstract interface EditModeTitleBar
{
  public static final int ACTION_LEFT = R.id.button1;//16908313;
  public static final int ACTION_RIGHT = R.id.button2;//16908314;

  public abstract CharSequence getButtonText(int paramInt);

  public abstract int getButtonVisiblity(int paramInt);

  public abstract CharSequence getTitle();

  public abstract void setBackground(int paramInt);

  public abstract void setBackground(Drawable paramDrawable);

  public abstract void setButtonBackground(int paramInt1, int paramInt2);

  public abstract void setButtonBackground(int paramInt, Drawable paramDrawable);

  public abstract void setButtonText(int paramInt1, int paramInt2);

  public abstract void setButtonText(int paramInt, CharSequence paramCharSequence);

  public abstract void setButtonVisibility(int paramInt1, int paramInt2);

  public abstract void setTitle(int paramInt);

  public abstract void setTitle(CharSequence paramCharSequence);
}
