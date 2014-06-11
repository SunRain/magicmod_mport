
package com.magicmod.mport.v5.widget.menubar;

import android.graphics.drawable.Drawable;

public abstract interface MenuBarView {
    public abstract void initialize(MenuBar paramMenuBar);

    public abstract void onScroll(float paramFloat, boolean paramBoolean1, boolean paramBoolean2);

    public abstract void onScrollStateChanged(int paramInt);

    public static abstract interface ItemView {
        public abstract MenuBarItem getItemData();

        public abstract void initialize(MenuBarItem paramMenuBarItem, int paramInt);

        public abstract boolean prefersCondensedTitle();

        public abstract void setCheckable(boolean paramBoolean);

        public abstract void setChecked(boolean paramBoolean);

        public abstract void setEnabled(boolean paramBoolean);

        public abstract void setIcon(Drawable paramDrawable);

        public abstract void setShortcut(boolean paramBoolean, char paramChar);

        public abstract void setTitle(CharSequence paramCharSequence);

        public abstract boolean showsIcon();
    }
}
