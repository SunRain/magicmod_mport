package com.magicmod.mport.internal.v5.view;

import android.view.ActionMode;

public abstract interface ActionModeView
{
  public abstract void animateToVisibility(int paramInt);

  public abstract void closeMode();

  public abstract void initForMode(ActionMode paramActionMode);

  public abstract void killMode();
}
