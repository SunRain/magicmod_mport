
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

    public ActionMenuSecondaryItemView(Context paramContext) {
        super(paramContext);
    }

    public ActionMenuSecondaryItemView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    public ActionMenuSecondaryItemView(Context paramContext, AttributeSet paramAttributeSet,
            int paramInt) {
        super(paramContext, paramAttributeSet, paramInt);
    }

    public MenuItemImpl getItemData() {
        return this.mItemData;
    }

    public void initialize(MenuItemImpl paramMenuItemImpl, int paramInt) {
        this.mItemData = paramMenuItemImpl;
        setTitle(paramMenuItemImpl.getTitle());
        setIcon(paramMenuItemImpl.getIcon());
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
        if ((mItemInvoker != null) && mItemInvoker.invokeItem(mItemData)) {
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
    }

    public void setChecked(boolean paramBoolean) {
    }

    public void setIcon(Drawable paramDrawable) {
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker) {
        this.mItemInvoker = paramItemInvoker;
    }

    public void setShortcut(boolean paramBoolean, char paramChar) {
    }

    public void setTitle(CharSequence paramCharSequence) {
        if (!TextUtils.equals(getText(), paramCharSequence))
            setText(paramCharSequence);
    }

    public boolean showsIcon() {
        return false;
    }
}
