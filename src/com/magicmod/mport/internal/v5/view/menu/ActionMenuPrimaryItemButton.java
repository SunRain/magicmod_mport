
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

    public ActionMenuPrimaryItemButton(Context context, AttributeSet paramAttributeSet) {
        this(context, paramAttributeSet, 0);
    }

    public ActionMenuPrimaryItemButton(Context context, AttributeSet paramAttributeSet,
            int paramInt) {
        super(context, paramAttributeSet, paramInt);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
        this.mItemData = paramMenuItemImpl;
        setSelected(false);
        setTitle(paramMenuItemImpl.getTitle());
        setIcon(paramMenuItemImpl.getIcon());
        setCheckable(paramMenuItemImpl.isCheckable());
        setChecked(paramMenuItemImpl.isChecked());
        setEnabled(paramMenuItemImpl.isEnabled());
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
        super.performClick();
        if ( (mItemInvoker!= null) && mItemInvoker.invokeItem(mItemData)) {
            playSoundEffect(0);
            return true;
        } else {
            return false;
        }
    }

    public boolean prefersCondensedTitle() {
        return false;
    }

    public void setCheckable(boolean paramBoolean) {
        this.mIsCheckable = paramBoolean;
    }

    public void setChecked(boolean paramBoolean) {
        if (this.mIsCheckable)
            setSelected(paramBoolean);
    }

    public void setIcon(Drawable paramDrawable) {
        if (getCompoundDrawables()[1] != paramDrawable)
            setCompoundDrawablesWithIntrinsicBounds(null, paramDrawable, null, null);
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker) {
        this.mItemInvoker = paramItemInvoker;
    }

    public void setShortcut(boolean paramBoolean, char paramChar) {
    }

    public void setTitle(CharSequence paramCharSequence) {
        setText(paramCharSequence);
    }

    public boolean showsIcon() {
        return true;
    }

}
