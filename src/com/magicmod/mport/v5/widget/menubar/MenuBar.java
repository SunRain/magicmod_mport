
package com.magicmod.mport.v5.widget.menubar;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.android.internal.R.integer;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class MenuBar implements Menu {
    public static final int MENU_BAR_MODE_COLLAPSE = 1;
    public static final int MENU_BAR_MODE_EXPAND = 0;
    private static final int MENU_BAR_STATE_CLOSED = 0;
    private static final int MENU_BAR_STATE_CLOSING = 3;
    private static final int MENU_BAR_STATE_OPENED = 2;
    private static final int MENU_BAR_STATE_OPENING = 1;
    private Callback mCallback;
    private int mCloseMenuBarViewCount;
    protected Context mContext;
    private boolean mIsCreated;
    private boolean mIsPrepared;
    protected ArrayList<MenuBarItem> mItems = new ArrayList();
    private boolean mItemsChangedWhileDispatchPrevented;
    
    MenuBarScrollHandler mMenuBarScrollHandler = new MenuBarScrollHandler() {
        
        @Override
        public void onScroll(float percent, boolean fromHasMenuBar,
                boolean toHasMenuBar) {
            dispatchMenuBarScroll(percent, fromHasMenuBar,
                    toHasMenuBar);
        }

        @Override
        public void onScrollStateChanged(int state) {
        }
    };
    
    private int mMenuBarState = 0;
    
    MenuBarPresenter.Callback mMenuPresenterCallback = new MenuBarPresenter.Callback() {
        @Override
        public void onCloseMenu(MenuBar menu, boolean allMenusAreClosing) {
            //MenuBar.access$310(MenuBar.this);
            mCloseMenuBarViewCount = mCloseMenuBarViewCount - 1;
            if (mCloseMenuBarViewCount == 0) {
                //MenuBar.access$202(MenuBar.this, 0);
                mMenuBarState = 0;
                dispatchMenuClose();
            }
        }

        @Override
        public void onOpenMenu(MenuBar menu, boolean allMenusAreClosing) {
           // MenuBar.access$110(MenuBar.this);
            mOpenMenuBarViewCount = mOpenMenuBarViewCount - 1;
            if (mOpenMenuBarViewCount == 0) {
                //MenuBar.access$202(MenuBar.this, 2);
                mMenuBarState = 2;
                dispatchMenuOpen();
            }
        }

        public boolean onOpenSubMenu(MenuBar subMenu) {
            return false;
        }
    };
    
    private int mOpenMenuBarViewCount;
    private CopyOnWriteArrayList<WeakReference<MenuBarPresenter>> mPresenters = new CopyOnWriteArrayList();
    private boolean mPreventDispatchingItemsChanged;
    protected ArrayList<MenuBarItem> mVisibleItems = new ArrayList();

    public MenuBar(Context context) {
        this.mContext = context;
    }

    /*private void dispatchMenuBarScroll(float paramFloat, boolean paramBoolean1,
            boolean paramBoolean2) {
        if (this.mPresenters.isEmpty())
            ;
        while (true) {
            return;
            stopDispatchingItemsChanged();
            Iterator localIterator = this.mPresenters.iterator();
            while (localIterator.hasNext()) {
                WeakReference localWeakReference = (WeakReference) localIterator.next();
                MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference
                        .get();
                if (localMenuBarPresenter == null)
                    this.mPresenters.remove(localWeakReference);
                else
                    localMenuBarPresenter.onScroll(paramFloat, paramBoolean1, paramBoolean2);
            }
            startDispatchingItemsChanged();
        }
    }*/
    private void dispatchMenuBarScroll(float percent, boolean fromHasMenuBar, boolean toHasMenuBar) {
        if (!mPresenters.isEmpty()) {
            stopDispatchingItemsChanged();
            for (Iterator i = mPresenters.iterator(); i.hasNext();) {
                WeakReference<MenuBarPresenter> ref = (WeakReference) i.next();
                MenuBarPresenter presenter = (MenuBarPresenter) ref.get();
                if (presenter == null)
                    mPresenters.remove(ref);
                else
                    presenter.onScroll(percent, fromHasMenuBar, toHasMenuBar);
            }

            startDispatchingItemsChanged();
        }
    }

    /*private void dispatchPresenterUpdate(boolean paramBoolean) {
        if (this.mPresenters.isEmpty())
            ;
        while (true) {
            return;
            stopDispatchingItemsChanged();
            Iterator localIterator = this.mPresenters.iterator();
            while (localIterator.hasNext()) {
                WeakReference localWeakReference = (WeakReference) localIterator.next();
                MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference
                        .get();
                if (localMenuBarPresenter == null)
                    this.mPresenters.remove(localWeakReference);
                else
                    localMenuBarPresenter.updateMenuView(paramBoolean);
            }
            startDispatchingItemsChanged();
        }
    }*/
    private void dispatchPresenterUpdate(boolean cleared) {
        if (!mPresenters.isEmpty()) {
            stopDispatchingItemsChanged();
            for (Iterator i = mPresenters.iterator(); i.hasNext();) {
                WeakReference<MenuBarPresenter> ref = (WeakReference) i.next();
                MenuBarPresenter presenter = (MenuBarPresenter) ref.get();
                if (presenter == null)
                    mPresenters.remove(ref);
                else
                    presenter.updateMenuView(cleared);
            }

            startDispatchingItemsChanged();
        }
    }

    /*private static int findInsertIndex(ArrayList<MenuBarItem> paramArrayList, int paramInt) {
        int i = -1 + paramArrayList.size();
        if (i >= 0)
            if (((MenuBarItem) paramArrayList.get(i)).getOrder() > paramInt)
                ;
        for (int j = i + 1;; j = 0) {
            return j;
            i--;
            break;
        }
    }*/
    private static int findInsertIndex(ArrayList<MenuBarItem> items, int ordering) {
        int i = items.size() - 1; // v0
        for (; i > 0; i--) {
            MenuBarItem item = (MenuBarItem) items.get(i);
            if (item.getOrder() < ordering) {
                return i++;
            }
        }
        return 0;
    }

    private int findItemIndex(int id) {
        /*int i = -1 + this.mItems.size();
        if (i >= 0)
            if (((MenuBarItem) this.mItems.get(i)).getItemId() != paramInt)
                ;
        while (true) {
            return i;
            i--;
            break;
            i = -1;
        }*/
        int size = mItems.size();
        for (int i=size-1; i>0; i--) {
            if (((MenuBarItem) mItems.get(i)).getItemId() == id) {
                return i;
            }
        }
        return -1;
    }

    /*private void removeItemAtInt(int paramInt, boolean paramBoolean) {
        if ((paramInt < 0) || (paramInt >= this.mItems.size()))
            ;
        while (true) {
            return;
            this.mItems.remove(paramInt);
            if (paramBoolean)
                onItemsChanged(true);
        }
    }*/
    private void removeItemAtInt(int index, boolean updateChildrenOnMenuViews) {
        if (index >= 0 && index < mItems.size()) {
            mItems.remove(index);
            if (updateChildrenOnMenuViews)
                onItemsChanged(true);
        }
    }

    public MenuItem add(int titleRes) {
        return addInternal(0, 0, this.mContext.getResources().getString(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, int titleRes) {
        return addInternal(itemId, order, this.mContext.getResources().getString(titleRes));
    }

    public MenuItem add(int groupId, int itemId, int order, CharSequence title) {
        return addInternal(itemId, order, title);
    }

    public MenuItem add(CharSequence title) {
        return addInternal(0, 0, title);
    }

    public int addIntentOptions(int groupId, int itemId, int order, ComponentName caller,
            Intent[] specifics, Intent intent, int flags, MenuItem[] outSpecificItems) {
        return 0;
    }

    MenuItem addInternal(int itemId, int order, CharSequence title) {
        MenuBarItem item = new MenuBarItem(this, itemId, order, title);
        this.mItems.add(findInsertIndex(this.mItems, order), item);
        onItemsChanged(true);
        return item;
    }

    public void addMenuPresenter(MenuBarPresenter presenter) {
        mPresenters.add(new WeakReference(presenter));
        presenter.initForMenu(mContext, this);
        presenter.setCallback(mMenuPresenterCallback);
    }

    public SubMenu addSubMenu(int titleRes) {
        return null;
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, int titleRes) {
        return null;
    }

    public SubMenu addSubMenu(int groupId, int itemId, int order, CharSequence title) {
        return null;
    }

    public SubMenu addSubMenu(CharSequence title) {
        return null;
    }

    public void clear() {
        this.mItems.clear();
        onItemsChanged(true);
    }

    public void close() {
        close(false);
    }

    /*public void close(boolean paramBoolean) {
        if ((this.mMenuBarState == 3) || (this.mMenuBarState == 0))
            ;
        while (true) {
            return;
            this.mMenuBarState = 3;
            int i = 0;
            this.mCloseMenuBarViewCount = 0;
            Iterator localIterator = this.mPresenters.iterator();
            while (localIterator.hasNext()) {
                WeakReference localWeakReference = (WeakReference) localIterator.next();
                MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference
                        .get();
                if (localMenuBarPresenter == null) {
                    this.mPresenters.remove(localWeakReference);
                } else {
                    i = 1;
                    this.mCloseMenuBarViewCount = (1 + this.mCloseMenuBarViewCount);
                    localMenuBarPresenter.onCloseMenu(this, paramBoolean);
                }
            }
            if (i == 0)
                this.mMenuBarState = 0;
        }
    }*/
    public void close(boolean animate) {
        if (mMenuBarState != 3 && mMenuBarState != 0) {
            mMenuBarState = 3;
            boolean handled = false;
            mCloseMenuBarViewCount = 0;
            for (Iterator i = mPresenters.iterator(); i.hasNext();) {
                WeakReference<MenuBarPresenter> ref = (WeakReference<MenuBarPresenter>) i.next();
                MenuBarPresenter presenter = (MenuBarPresenter) ref.get();
                if (presenter == null) {
                    mPresenters.remove(ref);
                } else {
                    handled = true;
                    mCloseMenuBarViewCount = 1 + mCloseMenuBarViewCount;
                    presenter.onCloseMenu(this, animate);
                }
            }

            if (!handled)
                mMenuBarState = 0;
        }
    }

    void dispatchMenuClose() {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelClose(this);
    }

    /*boolean dispatchMenuItemSelected(MenuBar paramMenuBar, MenuItem paramMenuItem) {
        if ((this.mCallback != null)
                && (this.mCallback.onMenuBarPanelItemSelected(paramMenuBar, paramMenuItem)))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }*/
    boolean dispatchMenuItemSelected(MenuBar menu, MenuItem item) {
        boolean flag;
        if (mCallback != null && mCallback.onMenuBarPanelItemSelected(menu, item))
            flag = true;
        else
            flag = false;
        return flag;
    }

    void dispatchMenuModeChange(int mode) {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelModeChange(this, mode);
    }

    void dispatchMenuOpen() {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelOpen(this);
    }

    public boolean expand(boolean expand) {
        boolean handled;
        if (mMenuBarState != 2) {
            handled = false;
        } else {
            handled = false;
            Iterator i = mPresenters.iterator();
            while (i.hasNext()) {
                WeakReference<MenuBarPresenter> ref = (WeakReference<MenuBarPresenter>) i.next();
                MenuBarPresenter presenter = (MenuBarPresenter) ref.get();
                if (presenter == null)
                    mPresenters.remove(ref);
                else
                    handled |= presenter.onExpandMenu(this, expand);
            }
        }
        return handled;
    }

    /*public MenuItem findItem(int paramInt) {
        int i = size();
        int j = 0;
        MenuBarItem localMenuBarItem;
        if (j < i) {
            localMenuBarItem = (MenuBarItem) this.mItems.get(j);
            if (localMenuBarItem.getItemId() != paramInt)
                ;
        }
        while (true) {
            return localMenuBarItem;
            j++;
            break;
            localMenuBarItem = null;
        }
    }*/
    public MenuItem findItem(int id) {
        int size = size(); // v2
        // i v0
        for (int i = 0; i < size; i++) {
            MenuBarItem item = (MenuBarItem) mItems.get(i); // v1
            if (item.getItemId() == id) {
                return item;
            }
        }
        return null;
    }

    public Context getContext() {
        return this.mContext;
    }

    public MenuItem getItem(int index) {
        MenuItem menuitem;
        if(index < 0 || index >= mItems.size())
            menuitem = null;
        else
            menuitem = (MenuItem)mItems.get(index);
        return menuitem;
    }

    public MenuBarScrollHandler getMenuBarSrollHandler() {
        return this.mMenuBarScrollHandler;
    }

    /*ArrayList<MenuBarItem> getVisibleItems() {
        this.mVisibleItems.clear();
        int i = this.mItems.size();
        int j = 0;
        if (j < i) {
            MenuBarItem localMenuBarItem = (MenuBarItem) this.mItems.get(j);
            if (localMenuBarItem.isVisible())
                this.mVisibleItems.add(localMenuBarItem);
            while (true) {
                j++;
                break;
                localMenuBarItem.setTag(null);
            }
        }
        return this.mVisibleItems;
    }*/
    ArrayList<MenuBarItem> getVisibleItems()
    {
        mVisibleItems.clear();
        int itemsSize = mItems.size();
        int i = 0;
        while(i < itemsSize) 
        {
            MenuBarItem item = (MenuBarItem)mItems.get(i);
            if(item.isVisible())
                mVisibleItems.add(item);
            else
                item.setTag(null);
            i++;
        }
        return mVisibleItems;
    }

    public boolean hasVisibleItems() {
        int size = size(); //v2
        for (int i=0; i<size; i++) {
            MenuBarItem item = (MenuBarItem) mItems.get(i); //v1
            if (item.isVisible()) {
                return true;
            }
        }
        return false;        
    }

    public void invalidate() {
        if (this.mCallback != null)
            this.mCallback.onPrepareMenuBarPanel(this);
    }

    public boolean isOpen() {
        boolean flag;
        if (mMenuBarState == 2)
            flag = true;
        else
            flag = false;
        return flag;
    }

    void onItemVisibleChanged(MenuBarItem item) {
        onItemsChanged(true);
    }

    void onItemsChanged(boolean structureChanged) {
        if (!mPreventDispatchingItemsChanged)
            dispatchPresenterUpdate(structureChanged);
        else
            mItemsChangedWhileDispatchPrevented = true;
    }

    public void open() {
        open(false);
    }

    /*public void open(boolean paramBoolean) {
        if (this.mMenuBarState == 1)
            ;
        while (true) {
            return;
            this.mMenuBarState = 1;
            this.mPreventDispatchingItemsChanged = true;
            int i;
            Iterator localIterator;
            if (this.mCallback != null)
                if (this.mIsCreated) {
                    this.mIsPrepared = this.mCallback.onPrepareMenuBarPanel(this);
                    this.mPreventDispatchingItemsChanged = false;
                    this.mItemsChangedWhileDispatchPrevented = false;
                    i = 0;
                    if ((!this.mIsCreated) || (!this.mIsPrepared))
                        break label217;
                    this.mOpenMenuBarViewCount = 0;
                    localIterator = this.mPresenters.iterator();
                }
            while (true) {
                if (!localIterator.hasNext())
                    break label217;
                WeakReference localWeakReference = (WeakReference) localIterator.next();
                MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference
                        .get();
                if (localMenuBarPresenter == null) {
                    this.mPresenters.remove(localWeakReference);
                    continue;
                    this.mIsCreated = this.mCallback.onCreateMenuBarPanel(this);
                    if (this.mIsCreated) {
                        this.mIsPrepared = this.mCallback.onPrepareMenuBarPanel(this);
                        break;
                    }
                    this.mIsPrepared = false;
                    break;
                    this.mIsCreated = false;
                    this.mIsPrepared = false;
                    break;
                }
                i = 1;
                this.mOpenMenuBarViewCount = (1 + this.mOpenMenuBarViewCount);
                localMenuBarPresenter.onOpenMenu(this, paramBoolean);
            }
            label217: if (i == 0)
                close(false);
        }
    }*/
    public void open(boolean animate) {
        if (mMenuBarState != 1) {
            mMenuBarState = 1;
            mPreventDispatchingItemsChanged = true;
            boolean handled;
            if (mCallback != null) {
                if (mIsCreated) {
                    mIsPrepared = mCallback.onPrepareMenuBarPanel(this);
                } else {
                    mIsCreated = mCallback.onCreateMenuBarPanel(this);
                    if (mIsCreated)
                        mIsPrepared = mCallback.onPrepareMenuBarPanel(this);
                    else
                        mIsPrepared = false;
                }
            } else {
                mIsCreated = false;
                mIsPrepared = false;
            }
            mPreventDispatchingItemsChanged = false;
            mItemsChangedWhileDispatchPrevented = false;
            handled = false;
            if (mIsCreated && mIsPrepared) {
                mOpenMenuBarViewCount = 0;
                for (Iterator i = mPresenters.iterator(); i.hasNext();) {
                    WeakReference<MenuBarPresenter> ref = (WeakReference<MenuBarPresenter>) i.next();
                    MenuBarPresenter presenter = (MenuBarPresenter) ref.get();
                    if (presenter == null) {
                        mPresenters.remove(ref);
                    } else {
                        handled = true;
                        mOpenMenuBarViewCount = 1 + mOpenMenuBarViewCount;
                        presenter.onOpenMenu(this, animate);
                    }
                }

            }
            if (!handled)
                close(false);
        }
    }

    public boolean performIdentifierAction(int id, int flags) {
        return false;
    }

    public boolean performItemAction(MenuItem item, int flags) {
        MenuBarItem itemImpl = (MenuBarItem) item;
        boolean flag;
        if (itemImpl == null || !itemImpl.isEnabled())
            flag = false;
        else
            flag = itemImpl.invoke();
        return flag;
    }

    public void removeGroup(int groupId) {
    }

    public void removeItem(int id) {
        removeItemAtInt(findItemIndex(id), true);
    }

    public void removeMenuPresenter(MenuBarPresenter presenter) {
        Iterator i = mPresenters.iterator();
        while (i.hasNext()) {
            WeakReference<MenuBarPresenter> ref = (WeakReference<MenuBarPresenter>) i.next();
            MenuBarPresenter item = (MenuBarPresenter) ref.get();
            if ((item == null) || (item == presenter))
                mPresenters.remove(ref);
        }
    }

    public void reopen() {
        reopen(false);
    }

    public void reopen(boolean animate) {
        this.mIsCreated = false;
        this.mItems.clear();
        this.mVisibleItems.clear();
        this.mMenuBarState = 0;
        open(animate);
    }

    public void setCallback(Callback callback) {
        this.mCallback = null;
        this.mCallback = callback;
    }

    public void setGroupCheckable(int group, boolean checkable, boolean exclusive) {
    }

    public void setGroupEnabled(int group, boolean enabled) {
    }

    public void setGroupVisible(int group, boolean visible) {
    }

    public void setQwertyMode(boolean isQwerty) {
    }

    public int size() {
        return this.mItems.size();
    }

    public void startDispatchingItemsChanged() {
        this.mPreventDispatchingItemsChanged = false;
        if (this.mItemsChangedWhileDispatchPrevented) {
            this.mItemsChangedWhileDispatchPrevented = false;
            onItemsChanged(true);
        }
    }

    public void stopDispatchingItemsChanged() {
        if (!this.mPreventDispatchingItemsChanged) {
            this.mPreventDispatchingItemsChanged = true;
            this.mItemsChangedWhileDispatchPrevented = false;
        }
    }

    public static abstract interface Callback {
        public abstract boolean onCreateMenuBarPanel(Menu paramMenu);

        public abstract void onMenuBarPanelClose(Menu paramMenu);

        public abstract boolean onMenuBarPanelItemSelected(Menu paramMenu, MenuItem paramMenuItem);

        public abstract void onMenuBarPanelModeChange(Menu paramMenu, int paramInt);

        public abstract void onMenuBarPanelOpen(Menu paramMenu);

        public abstract boolean onPrepareMenuBarPanel(Menu paramMenu);
    }

    public static abstract interface ItemInvoker {
        public abstract boolean invokeItem(MenuBarItem paramMenuBarItem);
    }

    public static abstract interface MenuBarScrollHandler {
        public abstract void onScroll(float percent, boolean fromHasMenuBar, boolean toHasMenuBar);

        public abstract void onScrollStateChanged(int state);
    }

    @Override
    public boolean isShortcutKey(int arg0, KeyEvent arg1) {
        // TODO Auto-generated method stub
        return false;
    }

    @Override
    public boolean performShortcut(int arg0, KeyEvent arg1, int arg2) {
        // TODO Auto-generated method stub
        return false;
    }
}
