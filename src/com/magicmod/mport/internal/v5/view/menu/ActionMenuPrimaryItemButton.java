
package com.magicmod.mport.internal.v5.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.Button;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;

public class ActionMenuPrimaryItemButton extends Button implements ActionMenuPrimaryItemView {
    private boolean mIsCheckable;
    private MenuItemImpl mItemData;
    private MenuBuilder.ItemInvoker mItemInvoker;

    public ActionMenuPrimaryItemButton(Context context) {
        this(context, null, 0);
    }

    public ActionMenuPrimaryItemButton(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ActionMenuPrimaryItemButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setSelected(false);
        setTitle(itemData.getTitle());
        setIcon(itemData.getIcon());
        setCheckable(itemData.isCheckable());
        setChecked(itemData.isChecked());
        setEnabled(itemData.isEnabled());
    }

    public boolean performClick() {
        //boolean bool = true;
        //if (super.performClick())
        //    ;
        //while (true) {
        //    return bool;
        //    if ((this.mItemInvoker != null) && (this.mItemInvoker.invokeItem(this.mItemData)))
        //        playSoundEffect(0);
        //    else
        //        bool = false;
        //}
        if (!super.performClick()) {
            if ((mItemInvoker != null) && mItemInvoker.invokeItem(mItemData)) {
                playSoundEffect(0);
                return true;
            } else {
                return false;
            }
        }
        return true;
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

    public void setIcon(Drawable icon) {
        if (getCompoundDrawables()[1] != icon)
            setCompoundDrawablesWithIntrinsicBounds(null, icon, null, null);
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTitle(CharSequence title) {
        setText(title);
    }

    public boolean showsIcon() {
        return true;
    }

}
