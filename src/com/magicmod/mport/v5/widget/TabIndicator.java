
package com.magicmod.mport.v5.widget;

import android.graphics.drawable.Drawable;

public abstract interface TabIndicator {
    public abstract float apply(int paramInt, float paramFloat);

    public abstract void attach(TabContainerLayout paramTabContainerLayout);

    public abstract void detach();

    public abstract void setIndicator(Drawable paramDrawable);

    public abstract void setTabWidth(int paramInt);
}
