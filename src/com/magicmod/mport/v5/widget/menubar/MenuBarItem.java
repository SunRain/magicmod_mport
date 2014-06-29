
package com.magicmod.mport.v5.widget.menubar;

import android.R.menu;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.view.ActionProvider;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;

public class MenuBarItem implements MenuItem {
    private static final int CHECKABLE = 0x1;//1;
    private static final int CHECKED = 0x2;//2;
    private static final int ENABLED = 0x10;//16;
    private static final int EXCLUSIVE = 0x4;//4;
    private static final int HIDDEN = 0x8;//8;
    private static final int IS_ACTION = 0x20;//32;
    private static final int IS_SECONDARY = -0x80000000;;//-2147483648;
    //private static final int NO_ICON;
    private MenuItem.OnMenuItemClickListener mClickListener;
    private int mFlags;
    private Drawable mIconDrawable;
    protected final int mId;
    private Intent mIntent;
    private Runnable mItemCallback;
    protected MenuBar mMenu;
    protected final int mOrder;
    private Object mTag;
    protected CharSequence mTitle;

    public MenuBarItem(MenuBar menu, int id, int order,
            CharSequence title) {
        this.mMenu = menu;
        this.mId = id;
        this.mOrder = order;
        this.mTitle = title;
        this.mFlags = (0x10 | this.mFlags);
    }

    public boolean collapseActionView() {
        return false;
    }

    public boolean expandActionView() {
        return false;
    }

    public ActionProvider getActionProvider() {
        return null;
    }

    public View getActionView() {
        return null;
    }

    public char getAlphabeticShortcut() {
        return '\000';
    }

    public int getGroupId() {
        return 0;
    }

    public Drawable getIcon() {
        return this.mIconDrawable;
    }

    public Intent getIntent() {
        return this.mIntent;
    }

    public int getItemId() {
        return this.mId;
    }

    public ContextMenu.ContextMenuInfo getMenuInfo() {
        return null;
    }

    public char getNumericShortcut() {
        return '\000';
    }

    public int getOrder() {
        return this.mOrder;
    }

    public SubMenu getSubMenu() {
        return null;
    }

    Object getTag() {
        return this.mTag;
    }

    public CharSequence getTitle() {
        return this.mTitle;
    }

    public CharSequence getTitleCondensed() {
        return null;
    }

    public boolean hasSubMenu() {
        return false;
    }

    public boolean invoke() {
        /*boolean bool = true;
        if ((this.mClickListener != null) && (this.mClickListener.onMenuItemClick(this)))
            ;
        while (true) {
            return bool;
            if (!this.mMenu.dispatchMenuItemSelected(this.mMenu, this))
                if (this.mItemCallback != null)
                    this.mItemCallback.run();
                else if (this.mIntent != null)
                    try {
                        this.mMenu.getContext().startActivity(this.mIntent);
                    } catch (ActivityNotFoundException localActivityNotFoundException) {
                    }
                else
                    bool = false;
        }*/
        //const/4 v0, 0x1
        boolean bool = true;
        if ((mClickListener != null) && mClickListener.onMenuItemClick(this)) {
            bool = true;
        } else { //cond_1
            if (mMenu.dispatchMenuItemSelected(mMenu, this) != false) {
                bool = true;
            } else {
                if (mItemCallback != null) {
                    mItemCallback.run();
                    bool = true;
                } else { //cond_2
                    if (mIntent != null) {
                        try {
                            mMenu.getContext().startActivity(mIntent);
                        } catch (ActivityNotFoundException e) {
                            e.printStackTrace();
                        }
                        bool = true;
                    } else { //cond_3
                        bool = false;
                    }
                }
            }
        }
        return bool;        
    }

    public boolean isActionViewExpanded() {
        return false;
    }

    public boolean isCheckable() {
        //if ((0x1 & this.mFlags) != 0)
        //    ;
        //for (boolean bool = true;; bool = false)
        //    return bool;
        if((1 & mFlags) != 0)
            return true;
        else
            return false;
    }

    public boolean isChecked() {
        //if ((0x2 & this.mFlags) != 0)
        //    ;
        //for (boolean bool = true;; bool = false)
        //    return bool;
        if((2 & mFlags) != 0)
            return true;
        else
            return false;
    }

    public boolean isEnabled() {
        //if ((0x10 & this.mFlags) != 0)
        //    ;
        //for (boolean bool = true;; bool = false)
        //    return bool;
        if((0x10 & mFlags) != 0)
            return true;
        else
            return false;
    }

    boolean isSecondary() {
        //if ((0x80000000 & this.mFlags) != 0)
         //   ;
        //for (boolean bool = true;; bool = false)
         //   return bool;
        if((0x80000000 & mFlags) != 0)
            return true;
        else
            return false;
    }

    public boolean isVisible() {
        //if ((0x8 & this.mFlags) == 0)
        //    ;
        ///for (boolean bool = true;; bool = false)
        //    return bool;
        if((8 & mFlags) == 0)
            return true;
        else
            return false;
    }

    @Override
    public MenuItem setActionProvider(ActionProvider actionProvider) {
        return this;
    }

    public MenuItem setActionView(int resId) {
        return this;
    }

    @Override
    public MenuItem setActionView(View view) {
        return this;
    }

    public MenuItem setAlphabeticShortcut(char alphaChar) {
        return this;
    }

    public MenuItem setCheckable(boolean checkable) {
        /*if (paramBoolean) {
            this.mFlags = (0x1 | this.mFlags);
            Object localObject = getTag();
            if (!(localObject instanceof MenuBarView.ItemView))
                break label52;
            ((MenuBarView.ItemView) localObject).setCheckable(paramBoolean);
        }
        while (true) {
            return this;
            this.mFlags = (0xFFFFFFFE & this.mFlags);
            break;
            label52: this.mMenu.onItemsChanged(false);
        }*/
        Object tag;
        if(checkable)
            mFlags = 1 | mFlags;
        else
            mFlags = -2 & mFlags;
        tag = getTag();
        if(tag instanceof MenuBarView.ItemView)
            ((MenuBarView.ItemView)tag).setCheckable(checkable);
        else
            mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setChecked(boolean checked) {
        /*if (paramBoolean) {
            this.mFlags = (0x2 | this.mFlags);
            Object localObject = getTag();
            if (!(localObject instanceof MenuBarView.ItemView))
                break label52;
            ((MenuBarView.ItemView) localObject).setChecked(paramBoolean);
        }
        while (true) {
            return this;
            this.mFlags = (0xFFFFFFFD & this.mFlags);
            break;
            label52: this.mMenu.onItemsChanged(false);
        }*/
        Object tag;
        if(checked)
            mFlags = 2 | mFlags;
        else
            mFlags = -3 & mFlags;
        tag = getTag();
        if(tag instanceof MenuBarView.ItemView)
            ((MenuBarView.ItemView)tag).setChecked(checked);
        else
            mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setEnabled(boolean enabled) {
        /*if (paramBoolean) {
            this.mFlags = (0x10 | this.mFlags);
            Object localObject = getTag();
            if (!(localObject instanceof MenuBarView.ItemView))
                break label53;
            ((MenuBarView.ItemView) localObject).setEnabled(paramBoolean);
        }
        while (true) {
            return this;
            this.mFlags = (0xFFFFFFEF & this.mFlags);
            break;
            label53: this.mMenu.onItemsChanged(false);
        }*/
        Object tag;
        if(enabled)
            mFlags = 0x10 | mFlags;
        else
            mFlags = 0xffffffef & mFlags;
        tag = getTag();
        if(tag instanceof MenuBarView.ItemView)
            ((MenuBarView.ItemView)tag).setEnabled(enabled);
        else
            mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setIcon(int iconResId) {
        /*if paramInt == 0)
            ;
        while (true) {
            return this;
            this = setIcon(this.mMenu.getContext().getResources().getDrawable(paramInt));
        }*/
        if(iconResId != 0)
            return setIcon(mMenu.getContext().getResources().getDrawable(iconResId));
        return this;
    }

    @Override
    public MenuItem setIcon(Drawable icon) {
        /*this.mIconDrawable = paramDrawable;
        Object localObject = getTag();
        if ((localObject instanceof MenuBarView.ItemView))
            ((MenuBarView.ItemView) localObject).setIcon(paramDrawable);
        while (true) {
            return this;
            this.mMenu.onItemsChanged(false);
        }*/
        mIconDrawable = icon;
        Object tag = getTag();
        if(tag instanceof MenuBarView.ItemView)
            ((MenuBarView.ItemView)tag).setIcon(icon);
        else
            mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setIntent(Intent intent) {
        this.mIntent = null;
        this.mIntent = intent;
        return this;
    }

    void setIsSecondary(boolean isSecondary) {
        //if (paramBoolean)
        //    ;
        //for (this.mFlags = (0x80000000 | this.mFlags);; this.mFlags = (0x7FFFFFFF & this.mFlags))
        //    return;
        if(isSecondary)
            mFlags = 0x80000000 | mFlags;
        else
            mFlags = 0x7fffffff & mFlags;
    }

    public MenuItem setNumericShortcut(char numericChar) {
        return this;
    }

    public MenuItem setOnActionExpandListener(
            MenuItem.OnActionExpandListener listener) {
        return this;
    }

    public MenuItem setOnMenuItemClickListener(
            MenuItem.OnMenuItemClickListener clickListener) {
        this.mClickListener = clickListener;
        return this;
    }

    public MenuItem setShortcut(char numericChar, char alphaChar) {
        return this;
    }

    public void setShowAsAction(int actionEnum) {
    }

    public MenuItem setShowAsActionFlags(int actionEnum) {
        return this;
    }

    MenuItem setTag(Object tag) {
        this.mTag = tag;
        return this;
    }

    public MenuItem setTitle(int title) {
        return setTitle(mMenu.getContext().getString(title));
    }

    public MenuItem setTitle(CharSequence title) {
        /*this.mTitle = paramCharSequence;
        Object localObject = getTag();
        if ((localObject instanceof MenuBarView.ItemView))
            ((MenuBarView.ItemView) localObject).setTitle(paramCharSequence);
        while (true) {
            return this;
            this.mMenu.onItemsChanged(false);
        }*/
        mTitle = title;
        Object tag = getTag();
        if(tag instanceof MenuBarView.ItemView)
            ((MenuBarView.ItemView)tag).setTitle(title);
        else
            mMenu.onItemsChanged(false);
        return this;
    }

    public MenuItem setTitleCondensed(CharSequence title) {
        return this;
    }

    public MenuItem setVisible(boolean visible) {
        if (setVisibleInt(visible))
            this.mMenu.onItemVisibleChanged(this);
        return this;
    }

    boolean setVisibleInt(boolean shown) {
        /*boolean bool = false;
        int i = this.mFlags;
        int j = 0xFFFFFFF7 & this.mFlags;
        if (paramBoolean)
            ;
        for (int k = 0;; k = 8) {
            this.mFlags = (k | j);
            if (i != this.mFlags)
                bool = true;
            return bool;
        }*/
        boolean flag1 = false;
        int oldFlags = mFlags;
        int j = -9 & mFlags;
        byte byte0;
        if(shown)
            byte0 = 0;
        else
            byte0 = 8;
        mFlags = byte0 | j;
        if(oldFlags != mFlags)
            flag1 = true;
        return flag1;
    }
}
