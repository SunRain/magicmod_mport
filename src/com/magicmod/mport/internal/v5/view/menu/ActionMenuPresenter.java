
package com.magicmod.mport.internal.v5.view.menu;

import android.R;
import android.content.Context;
import android.content.res.Configuration;
import android.graphics.drawable.Drawable;
import android.os.Parcelable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuItemImpl;
import com.android.internal.view.menu.MenuPresenter;
import com.android.internal.view.menu.MenuView;
import com.android.internal.view.menu.MenuView.ItemView;
import com.magicmod.mport.util.UiUtils;

import java.util.ArrayList;

public class ActionMenuPresenter extends com.android.internal.view.menu.ActionMenuPresenter {

    static final int MAX_PRIMARY_ITEMS = 4;
    private int mId;
    private boolean mIsEditMode;
    private int mLayoutResId;
    private int mMaxPrimaryItems = 4;
    private int mMenuItems;
    private ActionMenuPrimaryItemView mMoreView;
    private int mPrimaryItemResId;
    private int mSecondaryItemResId;
    private MenuUpdateListener mUpdateListener;

    public ActionMenuPresenter(Context context, int layoutResId, int primaryItemResId,
            int secondaryItemResId) {
        this(context, layoutResId, primaryItemResId, secondaryItemResId, false);
    }

    public ActionMenuPresenter(Context context, int layoutResId, int primaryItemResId,
            int secondaryItemResId, boolean isEditMode) {
        super(context);
        this.mLayoutResId = layoutResId;
        this.mPrimaryItemResId = primaryItemResId;
        this.mSecondaryItemResId = secondaryItemResId;
        this.mIsEditMode = isEditMode;
    }

    void addItemView(ViewGroup parent, View itemView, int index) {
        ViewGroup localViewGroup = (ViewGroup) itemView.getParent();
        if (localViewGroup != null)
            localViewGroup.removeView(itemView);
        parent.addView(itemView, index);
    }

    public boolean dismissPopupMenus() {
        ActionMenuView v = (ActionMenuView)mMenuView;
        if (v != null && v.requestExpand(false)) {
            return true;
        } else {
            return false;
        }
    }

    public boolean flagActionItems() {
        ArrayList localArrayList = mMenu.miuiGetVisibleItems();
        int i = localArrayList.size();
        for (int j = 0; j < i; j++) {
            MenuItemImpl localMenuItemImpl = (MenuItemImpl) localArrayList.get(j);
            if ((localMenuItemImpl.requestsActionButton())
                    || (localMenuItemImpl.requiresActionButton()))
                localMenuItemImpl.setIsActionButton(true);
        }
        return true;
    }

    public int getMenuItems() {
        return this.mMenuItems;
    }

    public MenuView getMenuView(ViewGroup paramViewGroup) {
        if (mMenuView == null) {
            mMenuView = ((ActionMenuView) mSystemInflater.inflate(mLayoutResId,
                    paramViewGroup, false));
            mMenuView.initialize(mMenu);
            ((ActionMenuView) mMenuView).setPresenter(this);
        }
        return mMenuView;
    }

    ActionMenuPrimaryItemView getPrimaryItemView(MenuItemImpl item, View convertView) {
        View v = null;
        if (convertView instanceof ActionMenuPrimaryItemView) {
            v = convertView;
        } else {
            v = View.inflate(mContext, mPrimaryItemResId, null);
        }
        ActionMenuPrimaryItemView itemView = (ActionMenuPrimaryItemView) v;
        itemView.initialize(item, 0);
        return itemView;
    }

    ActionMenuSecondaryItemView getSecondaryItemView(MenuItemImpl item, View convertView) {
        //if ((convertView instanceof ActionMenuSecondaryItemView))
         //   ;
        //for (View localView = convertView;; localView = View.inflate(this.mContext,
        //        this.mSecondaryItemResId, null)) {
        //    ActionMenuSecondaryItemView localActionMenuSecondaryItemView = (ActionMenuSecondaryItemView) localView;
        //    localActionMenuSecondaryItemView.initialize(item, 0);
        //    return localActionMenuSecondaryItemView;
        //}
        View view = null;
        if (convertView instanceof ActionMenuSecondaryItemView) {
            view = convertView;
        } else {
            view = View.inflate(mContext, mSecondaryItemResId, null);
        }
        ActionMenuSecondaryItemView itemView = (ActionMenuSecondaryItemView) view;
        itemView.initialize(item, 0);
        return itemView;
    }

    public boolean hideOverflowMenu() {
        //if ((this.mMenuView != null) && (((ActionMenuView) this.mMenuView).requestExpand(false)))
        //    ;
        //for (boolean bool = true;; bool = false)
       //     return bool;
        if ((mMenuView != null) && ((ActionMenuView)mMenuView).requestExpand(false)) {
            return true;
        } else {
            return false;
        }
    }

    public void initForMenu(Context paramContext, MenuBuilder paramMenuBuilder) {
        if (this.mMenu != null)
            this.mMenu.removeMenuPresenter(this);
        this.mContext = paramContext;
        this.mMenu = paramMenuBuilder;
    }

    public void onCloseMenu(MenuBuilder paramMenuBuilder, boolean paramBoolean) {
        //if (this.mMenuView == null)
        //    ;
        //while (true) {
         //   return;
         //   ((ActionMenuView) this.mMenuView).requestExpand(false);
        //}
        if (mMenuView != null) {
            ((ActionMenuView)mMenuView).requestExpand(false);
        }
    }

    public void onConfigurationChanged(Configuration newConfig) {
        //ActionMenuView localActionMenuView = (ActionMenuView) this.mMenuView;
        //if (localActionMenuView != null) {
        //    localActionMenuView.getPrimaryContainer().removeAllViews();
        ///    if (!this.mIsEditMode)
         //       break label47;
        //}
        //label47: for (Drawable localDrawable = UiUtils.getDrawable(this.mContext, 100728926);; localDrawable = UiUtils
        ////        .getDrawable(this.mContext, 100728900)) {
        //    localActionMenuView.setPrimaryContainerCollapsedBackground(localDrawable);
        //    super.onConfigurationChanged(paramConfiguration);
         //   return;
        //}
        ActionMenuView menuView = (ActionMenuView)mMenuView;
        if (menuView != null) {
            menuView.getPrimaryContainer().removeAllViews();
            //TODO: is this right?
            Drawable d = null;
            if (mIsEditMode) {
                d = UiUtils.getDrawable(mContext, miui.R.attr.v5_edit_mode_bottom_bar_bg);
                //menuView.setPrimaryContainerCollapsedBackground(d);
            } else {
                //cond_1
                d = UiUtils.getDrawable(mContext, miui.R.attr.v5_bottom_bar_bg);
                //menuView.setPrimaryContainerCollapsedBackground(d);
            }
            menuView.setPrimaryContainerCollapsedBackground(d);
        } 
        super.onConfigurationChanged(newConfig);
    }

    public void onRestoreInstanceState(Parcelable paramParcelable) {
    }

    public Parcelable onSaveInstanceState() {
        return null;
    }

    public void setUpdateListener(MenuUpdateListener paramMenuUpdateListener) {
        this.mUpdateListener = paramMenuUpdateListener;
    }

    public void updateMenuView(boolean cleared)
    {
        ActionMenuView actionMenuView = (ActionMenuView) mMenuView;
        mMenuView.initialize(mMenu);

        if (mMenu != null) {
            ArrayList arraylist = mMenu.miuiGetActionItems();
            mMenuItems = arraylist.size();
            int i = mMenuItems;
            boolean flag1;
            int j;
            LinearLayout linearlayout;
            int k;
            if (i > mMaxPrimaryItems)
                flag1 = true;
            else
                flag1 = false;
            if (flag1)
                j = -1 + mMaxPrimaryItems;
            else
                j = i;
            linearlayout = actionMenuView.getPrimaryContainer();
            k = 0;
            while (k < j) {
                MenuItemImpl menuitemimpl2 = (MenuItemImpl) arraylist.get(k);
                View view2 = linearlayout.getChildAt(k);
                MenuItemImpl menuitemimpl3;
                ActionMenuPrimaryItemView actionmenuprimaryitemview;
                if (view2 instanceof ItemView)
                    menuitemimpl3 = ((ItemView) view2)
                            .getItemData();
                else
                    menuitemimpl3 = null;
                actionmenuprimaryitemview = getPrimaryItemView(menuitemimpl2, view2);
                ((View) actionmenuprimaryitemview).setOnClickListener(null);
                if (menuitemimpl2 != menuitemimpl3) {
                    ((View) actionmenuprimaryitemview).setPressed(false);
                    ((View) actionmenuprimaryitemview).jumpDrawablesToCurrentState();
                }
                if (actionmenuprimaryitemview != view2) {
                    addItemView(linearlayout, (View) actionmenuprimaryitemview, k);
                    actionmenuprimaryitemview.setItemInvoker(actionMenuView);
                }
                k++;
            }
            if (flag1) {
                View view = linearlayout.getChildAt(k);
                if (view != mMoreView || mMoreView == null) {
                    mMoreView = actionMenuView.createMoreItemView((ActionMenuPrimaryItemView) view,
                            mPrimaryItemResId);
                    if (mMoreView != view)
                        addItemView(linearlayout, (View) mMoreView, k);
                }
                LinearLayout linearlayout2 = actionMenuView.getSecondaryContainer(true);
                int l = 0;
                while (k < i) {
                    MenuItemImpl menuitemimpl = (MenuItemImpl) arraylist.get(k);
                    View view1 = linearlayout2.getChildAt(l);
                    MenuItemImpl menuitemimpl1;
                    ActionMenuSecondaryItemView actionmenusecondaryitemview;
                    if (view1 instanceof ItemView)
                        menuitemimpl1 = ((ItemView) view1)
                                .getItemData();
                    else
                        menuitemimpl1 = null;
                    actionmenusecondaryitemview = getSecondaryItemView(menuitemimpl, view1);
                    if (menuitemimpl != menuitemimpl1) {
                        actionmenusecondaryitemview.setPressed(false);
                        actionmenusecondaryitemview.jumpDrawablesToCurrentState();
                    }
                    if (actionmenusecondaryitemview != view1) {
                        addItemView(linearlayout2, actionmenusecondaryitemview, l);
                        actionmenusecondaryitemview.setItemInvoker(actionMenuView);
                    }
                    k++;
                    l++;
                }
                linearlayout2.removeViews(l, linearlayout2.getChildCount() - l);
            } else {
                mMoreView = null;
                linearlayout.removeViews(k, linearlayout.getChildCount() - k);
                LinearLayout linearlayout1 = actionMenuView.getSecondaryContainer(false);
                if (linearlayout1 != null)
                    linearlayout1.removeAllViews();
            }
            if (mUpdateListener != null)
                mUpdateListener.onMenuUpdated(this);
            return;
        } else {
            actionMenuView.getPrimaryContainer().removeAllViews();
            LinearLayout linearlayout3 = actionMenuView.getSecondaryContainer(false);
            if (linearlayout3 != null)
                linearlayout3.removeAllViews();
            mMenuItems = 0;
        }
      /*  ActionMenuView localActionMenuView = (ActionMenuView)this.mMenuView;
      this.mMenuView.initialize(this.mMenu);
      if (this.mMenu == null)
      {
        localActionMenuView.getPrimaryContainer().removeAllViews();
        LinearLayout localLinearLayout4 = localActionMenuView.getSecondaryContainer(false);
        if (localLinearLayout4 != null)
          localLinearLayout4.removeAllViews();
        this.mMenuItems = 0;
      }
      while (true)
      {
        if (this.mUpdateListener != null)
          this.mUpdateListener.onMenuUpdated(this);
        return;
        ArrayList localArrayList = this.mMenu.miuiGetActionItems();
        this.mMenuItems = localArrayList.size();
        int i = this.mMenuItems;
        int j;
        int k;
        label122: LinearLayout localLinearLayout1;
        int m;
        label131: MenuItemImpl localMenuItemImpl3;
        View localView3;
        if (i > this.mMaxPrimaryItems)
        {
          j = 1;
          if (j == 0)
            break label261;
          k = -1 + this.mMaxPrimaryItems;
          localLinearLayout1 = localActionMenuView.getPrimaryContainer();
          m = 0;
          if (m >= k)
            break label274;
          localMenuItemImpl3 = (MenuItemImpl)localArrayList.get(m);
          localView3 = localLinearLayout1.getChildAt(m);
          if (!(localView3 instanceof MenuView.ItemView))
            break label268;
        }
        label261: label268: for (MenuItemImpl localMenuItemImpl4 = ((MenuView.ItemView)localView3).getItemData(); ; localMenuItemImpl4 = null)
        {
          ActionMenuPrimaryItemView localActionMenuPrimaryItemView = getPrimaryItemView(localMenuItemImpl3, localView3);
          ((View)localActionMenuPrimaryItemView).setOnClickListener(null);
          if (localMenuItemImpl3 != localMenuItemImpl4)
          {
            ((View)localActionMenuPrimaryItemView).setPressed(false);
            ((View)localActionMenuPrimaryItemView).jumpDrawablesToCurrentState();
          }
          if (localActionMenuPrimaryItemView != localView3)
          {
            addItemView(localLinearLayout1, (View)localActionMenuPrimaryItemView, m);
            localActionMenuPrimaryItemView.setItemInvoker(localActionMenuView);
          }
          m++;
          break label131;
          j = 0;
          break;
          k = i;
          break label122;
        }
        label274: if (j != 0)
        {
          View localView1 = localLinearLayout1.getChildAt(m);
          if ((localView1 != this.mMoreView) || (this.mMoreView == null))
          {
            this.mMoreView = localActionMenuView.createMoreItemView((ActionMenuPrimaryItemView)localView1, this.mPrimaryItemResId);
            if (this.mMoreView != localView1)
              addItemView(localLinearLayout1, (View)this.mMoreView, m);
          }
          LinearLayout localLinearLayout3 = localActionMenuView.getSecondaryContainer(true);
          int n = 0;
          if (m < i)
          {
            MenuItemImpl localMenuItemImpl1 = (MenuItemImpl)localArrayList.get(m);
            View localView2 = localLinearLayout3.getChildAt(n);
            if ((localView2 instanceof MenuView.ItemView));
            for (MenuItemImpl localMenuItemImpl2 = ((MenuView.ItemView)localView2).getItemData(); ; localMenuItemImpl2 = null)
            {
              ActionMenuSecondaryItemView localActionMenuSecondaryItemView = getSecondaryItemView(localMenuItemImpl1, localView2);
              if (localMenuItemImpl1 != localMenuItemImpl2)
              {
                localActionMenuSecondaryItemView.setPressed(false);
                localActionMenuSecondaryItemView.jumpDrawablesToCurrentState();
              }
              if (localActionMenuSecondaryItemView != localView2)
              {
                addItemView(localLinearLayout3, localActionMenuSecondaryItemView, n);
                localActionMenuSecondaryItemView.setItemInvoker(localActionMenuView);
              }
              m++;
              n++;
              break;
            }
          }
          localLinearLayout3.removeViews(n, localLinearLayout3.getChildCount() - n);
        }
        else
        {
          this.mMoreView = null;
          localLinearLayout1.removeViews(m, localLinearLayout1.getChildCount() - m);
          LinearLayout localLinearLayout2 = localActionMenuView.getSecondaryContainer(false);
          if (localLinearLayout2 != null)
            localLinearLayout2.removeAllViews();
        }
      }*/
    }

    public static abstract interface MenuUpdateListener {
        public abstract void onMenuUpdated(MenuPresenter paramMenuPresenter);
    }
}
