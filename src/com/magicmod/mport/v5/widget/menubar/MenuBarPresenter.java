
package com.magicmod.mport.v5.widget.menubar;

import android.content.Context;
import android.os.Parcelable;
import android.view.ViewGroup;

public abstract interface MenuBarPresenter {
    public abstract boolean collapseItemActionView(MenuBar paramMenuBar,
            MenuBarItem paramMenuBarItem);

    public abstract boolean expandItemActionView(MenuBar paramMenuBar, MenuBarItem paramMenuBarItem);

    public abstract boolean flagActionItems();

    public abstract int getId();

    public abstract MenuBarView getMenuView(ViewGroup paramViewGroup);

    public abstract void initForMenu(Context paramContext, MenuBar paramMenuBar);

    public abstract void onCloseMenu(MenuBar paramMenuBar, boolean paramBoolean);

    public abstract boolean onExpandMenu(MenuBar paramMenuBar, boolean paramBoolean);

    public abstract void onOpenMenu(MenuBar paramMenuBar, boolean paramBoolean);

    public abstract void onRestoreInstanceState(Parcelable paramParcelable);

    public abstract Parcelable onSaveInstanceState();

    public abstract void onScroll(float paramFloat, boolean paramBoolean1, boolean paramBoolean2);

    public abstract void onScrollStateChanged(int paramInt);

    public abstract void setCallback(Callback paramCallback);

    public abstract void updateMenuView(boolean paramBoolean);

    public static abstract interface Callback {
        public abstract void onCloseMenu(MenuBar menu, boolean allMenusAreClosing);

        public abstract void onOpenMenu(MenuBar menu, boolean allMenusAreClosing);

        public abstract boolean onOpenSubMenu(MenuBar subMenu);
    }
}
