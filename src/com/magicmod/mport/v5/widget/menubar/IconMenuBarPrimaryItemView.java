
package com.magicmod.mport.v5.widget.menubar;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

public class IconMenuBarPrimaryItemView extends Button implements MenuBarView.ItemView {
    private boolean mIsCheckable;
    private boolean mIsMoreView;
    private MenuBarItem mItemData;
    private MenuBar.ItemInvoker mItemInvoker;

    public IconMenuBarPrimaryItemView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);
    }

    public MenuBarItem getItemData() {
        return mItemData;
    }

    public void initialize(MenuBarItem itemData, int menuType) {
        this.mItemData = itemData;
        setSelected(false);
        setTitle(itemData.getTitle());
        setIcon(itemData.getIcon());
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
    }

    boolean isMoreView() {
        return mIsMoreView;
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
        this.mIsCheckable = checkable;
    }

    public void setChecked(boolean checked) {
        if (this.mIsCheckable)
            setSelected(checked);
    }

    @Override
    public void setIcon(Drawable icon) {
        Drawable currentIcon = getCompoundDrawables()[1];
        if ((icon != null) && (currentIcon != icon))
            setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
    }

    void setIsMoreView(boolean isMoreView) {
        this.mIsMoreView = isMoreView;
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
        return true;
    }
}
