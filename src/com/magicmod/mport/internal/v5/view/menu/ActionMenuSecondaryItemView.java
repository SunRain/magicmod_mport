
package com.magicmod.mport.internal.v5.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.widget.TextView;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuView;

public class ActionMenuSecondaryItemView extends TextView implements MenuView.ItemView {
    private MenuItemImpl mItemData;
    private MenuBuilder.ItemInvoker mItemInvoker;

    public ActionMenuSecondaryItemView(Context context) {
        super(context);
    }

    public ActionMenuSecondaryItemView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public ActionMenuSecondaryItemView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl itemData, int menuType) {
        this.mItemData = itemData;
        setTitle(itemData.getTitle());
        setIcon(itemData.getIcon());
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
    }

    public void setChecked(boolean checked) {
    }

    public void setIcon(Drawable icon) {
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTitle(CharSequence title) {
        if (!TextUtils.equals(getText(), title))
            setText(title);
    }

    public boolean showsIcon() {
        return false;
    }
}
