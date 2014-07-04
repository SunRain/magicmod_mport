
package com.magicmod.mport.v5.app;

import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.magicmod.mport.v5.widget.MotionDetectStrategy;
import com.magicmod.mport.v5.widget.PageScrollEffect;
import com.magicmod.mport.v5.widget.VerticalMotionFrameLayout;

public abstract interface MiuiViewPagerItem extends PageScrollEffect.Creator,
        MotionDetectStrategy.Creator, LayoutObserver.Creator,
        VerticalMotionFrameLayout.InertiaListener {
    public abstract int getBottomPlaceHolderHeight();

    public abstract boolean isBottomPlaceholderEnabled();

    public abstract boolean isMenuBarEnabled();

    public abstract boolean onCreateMenuBar(Menu paramMenu, MenuInflater paramMenuInflater);

    public abstract void onMenuBarClose(Menu paramMenu);

    public abstract boolean onMenuBarItemSelected(MenuItem paramMenuItem);

    public abstract void onMenuBarModeChange(Menu paramMenu, int paramInt);

    public abstract void onMenuBarOpen(Menu paramMenu);

    public abstract boolean onPrepareMenuBar(Menu paramMenu);

    public abstract void setBottomPlaceHolderEnabled(boolean paramBoolean);

    public abstract void setMenuBarEnabled(boolean paramBoolean);
}
