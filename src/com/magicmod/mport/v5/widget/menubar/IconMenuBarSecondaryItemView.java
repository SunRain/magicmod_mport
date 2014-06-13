package com.magicmod.mport.v5.widget.menubar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.TextView;

public class IconMenuBarSecondaryItemView extends TextView implements MenuBarView.ItemView {
    private MenuBarItem mItemData;
    private MenuBar.ItemInvoker mItemInvoker;

    public IconMenuBarSecondaryItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MenuBarItem getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuBarItem itemData, int menuType) {
        this.mItemData = itemData;
        setTitle(itemData.getTitle());
        setIcon(itemData.getIcon());
        setEnabled(itemData.isEnabled());
    }

    public boolean performClick() {
        /*boolean bool = true;
        if (super.performClick())
            ;
        while (true) {
            return bool;
            if ((this.mItemInvoker != null) && (this.mItemInvoker.invokeItem(this.mItemData)))
                playSoundEffect(0);
            else
                bool = false;
        }*/  
        boolean flag = true;
        if (!super.performClick())
            if (mItemInvoker != null && mItemInvoker.invokeItem(mItemData))
                playSoundEffect(0);
            else
                flag = false;
        return flag;
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setCheckable(boolean checkable) {
    }

    public void setChecked(boolean checked) {
    }

    @Override
    public void setIcon(Drawable icon) {
    }

    public void setItemInvoker(MenuBar.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTitle(CharSequence title) {
        CharSequence localCharSequence = getText();
        if ((localCharSequence == null) || (!localCharSequence.equals(title)))
            setText(title);
    }

    public boolean showsIcon() {
        return false;
    }
}