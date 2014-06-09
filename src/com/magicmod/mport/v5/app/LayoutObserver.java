package com.magicmod.mport.v5.app;

public abstract interface LayoutObserver
{
  public abstract boolean isContentFilled();

  public static abstract interface Creator
  {
    public abstract LayoutObserver createLayoutObserver();
  }
}
