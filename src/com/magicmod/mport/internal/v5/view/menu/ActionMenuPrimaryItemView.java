package com.magicmod.mport.internal.v5.view.menu;

import com.android.internal.view.menu.MenuBuilder;
import com.android.internal.view.menu.MenuView.ItemView;

public abstract interface ActionMenuPrimaryItemView extends ItemView{
    public abstract void setItemInvoker(MenuBuilder.ItemInvoker itemInvoker);
}
