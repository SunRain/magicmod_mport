
package com.magicmod.mport.internal.v5.view.menu;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;

public class ActionMenuPrimaryItemImageView extends ImageView implements ActionMenuPrimaryItemView {
    private boolean mIsCheckable;
    private MenuItemImpl mItemData;
    private MenuBuilder.ItemInvoker mItemInvoker;

    public ActionMenuPrimaryItemImageView(Context context, AttributeSet paramAttributeSet) {
        super(context, paramAttributeSet);
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

    protected void onMeasure(int paramInt1, int paramInt2) {
        super.onMeasure(paramInt1, paramInt2);
        Drawable localDrawable = getBackground();
        if (localDrawable != null) {
            int i = localDrawable.getIntrinsicWidth();
            int j = localDrawable.getIntrinsicHeight();
            setMeasuredDimension(Math.min(i, getMeasuredWidth()), Math.min(j, getMeasuredHeight()));
        }
    }

    public boolean performClick() {
        //boolean bool = true;
       // if (super.performClick())
        //    ;
        //while (true) {
        //    return bool;
        //    if ((this.mItemInvoker != null) && (this.mItemInvoker.invokeItem(this.mItemData)))
        //        playSoundEffect(0);
         //   else
         //       bool = false;
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
        this.mIsCheckable = paramBoolean;
    }

    public void setChecked(boolean paramBoolean) {
        if (this.mIsCheckable)
            setSelected(paramBoolean);
    }

    public void setIcon(Drawable paramDrawable) {
        setImageDrawable(paramDrawable);
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker paramItemInvoker) {
        this.mItemInvoker = paramItemInvoker;
    }

    public void setShortcut(boolean paramBoolean, char paramChar) {
    }

    public void setTitle(CharSequence paramCharSequence) {
    }

    public boolean showsIcon() {
        return true;
    }
}
