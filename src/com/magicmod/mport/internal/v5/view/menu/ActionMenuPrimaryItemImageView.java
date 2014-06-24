
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

    public ActionMenuPrimaryItemImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
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

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Drawable d = getBackground();
        if (d != null) {
            int backgroundWidth = d.getIntrinsicWidth();
            int backgroundHeight = d.getIntrinsicHeight();
            setMeasuredDimension(Math.min(backgroundWidth, getMeasuredWidth()), Math.min(backgroundHeight, getMeasuredHeight()));
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
        setImageDrawable(icon);
    }

    public void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker) {
        this.mItemInvoker = itemInvoker;
    }

    public void setShortcut(boolean showShortcut, char shortcutKey) {
    }

    public void setTitle(CharSequence title) {
    }

    public boolean showsIcon() {
        return true;
    }
}
