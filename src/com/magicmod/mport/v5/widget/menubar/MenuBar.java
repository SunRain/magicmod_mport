
package com.magicmod.mport.v5.widget.menubar;

import android.content.Context;
import android.view.Menu;

import java.util.ArrayList;

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
        public void onScroll(float paramAnonymousFloat, boolean paramAnonymousBoolean1,
                boolean paramAnonymousBoolean2) {
            MenuBar.this.dispatchMenuBarScroll(paramAnonymousFloat, paramAnonymousBoolean1,
                    paramAnonymousBoolean2);
        }

        public void onScrollStateChanged(int paramAnonymousInt) {
        }
    };
    private int mMenuBarState = 0;
    MenuBarPresenter.Callback mMenuPresenterCallback = new MenuBarPresenter.Callback() {
        public void onCloseMenu(MenuBar paramAnonymousMenuBar, boolean paramAnonymousBoolean) {
            MenuBar.access$310(MenuBar.this);
            if (MenuBar.this.mCloseMenuBarViewCount == 0) {
                MenuBar.access$202(MenuBar.this, 0);
                MenuBar.this.dispatchMenuClose();
            }
        }

        public void onOpenMenu(MenuBar paramAnonymousMenuBar, boolean paramAnonymousBoolean) {
            MenuBar.access$110(MenuBar.this);
            if (MenuBar.this.mOpenMenuBarViewCount == 0) {
                MenuBar.access$202(MenuBar.this, 2);
                MenuBar.this.dispatchMenuOpen();
            }
        }

        public boolean onOpenSubMenu(MenuBar paramAnonymousMenuBar) {
            return false;
        }
    };
    private int mOpenMenuBarViewCount;
    private CopyOnWriteArrayList<WeakReference<MenuBarPresenter>> mPresenters = new CopyOnWriteArrayList();
    private boolean mPreventDispatchingItemsChanged;
    protected ArrayList<MenuBarItem> mVisibleItems = new ArrayList();

    public MenuBar(Context paramContext) {
        this.mContext = paramContext;
    }

    private void dispatchMenuBarScroll(float paramFloat, boolean paramBoolean1,
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
    }

    private void dispatchPresenterUpdate(boolean paramBoolean) {
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
    }

    private static int findInsertIndex(ArrayList<MenuBarItem> paramArrayList, int paramInt) {
        int i = -1 + paramArrayList.size();
        if (i >= 0)
            if (((MenuBarItem) paramArrayList.get(i)).getOrder() > paramInt)
                ;
        for (int j = i + 1;; j = 0) {
            return j;
            i--;
            break;
        }
    }

    private int findItemIndex(int paramInt) {
        int i = -1 + this.mItems.size();
        if (i >= 0)
            if (((MenuBarItem) this.mItems.get(i)).getItemId() != paramInt)
                ;
        while (true) {
            return i;
            i--;
            break;
            i = -1;
        }
    }

    private void removeItemAtInt(int paramInt, boolean paramBoolean) {
        if ((paramInt < 0) || (paramInt >= this.mItems.size()))
            ;
        while (true) {
            return;
            this.mItems.remove(paramInt);
            if (paramBoolean)
                onItemsChanged(true);
        }
    }

    public MenuItem add(int paramInt) {
        return addInternal(0, 0, this.mContext.getResources().getString(paramInt));
    }

    public MenuItem add(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return addInternal(paramInt2, paramInt3, this.mContext.getResources().getString(paramInt4));
    }

    public MenuItem add(int paramInt1, int paramInt2, int paramInt3, CharSequence paramCharSequence) {
        return addInternal(paramInt2, paramInt3, paramCharSequence);
    }

    public MenuItem add(CharSequence paramCharSequence) {
        return addInternal(0, 0, paramCharSequence);
    }

    public int addIntentOptions(int paramInt1, int paramInt2, int paramInt3,
            ComponentName paramComponentName, Intent[] paramArrayOfIntent, Intent paramIntent,
            int paramInt4, MenuItem[] paramArrayOfMenuItem) {
        return 0;
    }

    MenuItem addInternal(int paramInt1, int paramInt2, CharSequence paramCharSequence) {
        MenuBarItem localMenuBarItem = new MenuBarItem(this, paramInt1, paramInt2,
                paramCharSequence);
        this.mItems.add(findInsertIndex(this.mItems, paramInt2), localMenuBarItem);
        onItemsChanged(true);
        return localMenuBarItem;
    }

    public void addMenuPresenter(MenuBarPresenter paramMenuBarPresenter) {
        this.mPresenters.add(new WeakReference(paramMenuBarPresenter));
        paramMenuBarPresenter.initForMenu(this.mContext, this);
        paramMenuBarPresenter.setCallback(this.mMenuPresenterCallback);
    }

    public SubMenu addSubMenu(int paramInt) {
        return null;
    }

    public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3, int paramInt4) {
        return null;
    }

    public SubMenu addSubMenu(int paramInt1, int paramInt2, int paramInt3,
            CharSequence paramCharSequence) {
        return null;
    }

    public SubMenu addSubMenu(CharSequence paramCharSequence) {
        return null;
    }

    public void clear() {
        this.mItems.clear();
        onItemsChanged(true);
    }

    public void close() {
        close(false);
    }

    public void close(boolean paramBoolean) {
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
    }

    void dispatchMenuClose() {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelClose(this);
    }

    boolean dispatchMenuItemSelected(MenuBar paramMenuBar, MenuItem paramMenuItem) {
        if ((this.mCallback != null)
                && (this.mCallback.onMenuBarPanelItemSelected(paramMenuBar, paramMenuItem)))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    void dispatchMenuModeChange(int paramInt) {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelModeChange(this, paramInt);
    }

    void dispatchMenuOpen() {
        if (this.mCallback != null)
            this.mCallback.onMenuBarPanelOpen(this);
    }

    public boolean expand(boolean paramBoolean) {
        boolean bool;
        if (this.mMenuBarState != 2)
            bool = false;
        while (true) {
            return bool;
            bool = false;
            Iterator localIterator = this.mPresenters.iterator();
            while (localIterator.hasNext()) {
                WeakReference localWeakReference = (WeakReference) localIterator.next();
                MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference
                        .get();
                if (localMenuBarPresenter == null)
                    this.mPresenters.remove(localWeakReference);
                else
                    bool |= localMenuBarPresenter.onExpandMenu(this, paramBoolean);
            }
        }
    }

    public MenuItem findItem(int paramInt) {
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
    }

    public Context getContext() {
        return this.mContext;
    }

    public MenuItem getItem(int paramInt) {
        if ((paramInt < 0) || (paramInt >= this.mItems.size()))
            ;
        for (MenuItem localMenuItem = null;; localMenuItem = (MenuItem) this.mItems.get(paramInt))
            return localMenuItem;
    }

    public MenuBarScrollHandler getMenuBarSrollHandler() {
        return this.mMenuBarScrollHandler;
    }

    ArrayList<MenuBarItem> getVisibleItems() {
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
    }

    public boolean hasVisibleItems() {
        int i = size();
        int j = 0;
        if (j < i)
            if (!((MenuBarItem) this.mItems.get(j)).isVisible())
                ;
        for (boolean bool = true;; bool = false) {
            return bool;
            j++;
            break;
        }
    }

    public void invalidate() {
        if (this.mCallback != null)
            this.mCallback.onPrepareMenuBarPanel(this);
    }

    public boolean isOpen() {
        if (this.mMenuBarState == 2)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public boolean isShortcutKey(int paramInt, KeyEvent paramKeyEvent) {
        return false;
    }

    void onItemVisibleChanged(MenuBarItem paramMenuBarItem) {
        onItemsChanged(true);
    }

    void onItemsChanged(boolean paramBoolean) {
        if (!this.mPreventDispatchingItemsChanged)
            dispatchPresenterUpdate(paramBoolean);
        while (true) {
            return;
            this.mItemsChangedWhileDispatchPrevented = true;
        }
    }

    public void open() {
        open(false);
    }

    public void open(boolean paramBoolean) {
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
    }

    public boolean performIdentifierAction(int paramInt1, int paramInt2) {
        return false;
    }

    public boolean performItemAction(MenuItem paramMenuItem, int paramInt) {
        MenuBarItem localMenuBarItem = (MenuBarItem) paramMenuItem;
        if ((localMenuBarItem == null) || (!localMenuBarItem.isEnabled()))
            ;
        for (boolean bool = false;; bool = localMenuBarItem.invoke())
            return bool;
    }

    public boolean performShortcut(int paramInt1, KeyEvent paramKeyEvent, int paramInt2) {
        return false;
    }

    public void removeGroup(int paramInt) {
    }

    public void removeItem(int paramInt) {
        removeItemAtInt(findItemIndex(paramInt), true);
    }

    public void removeMenuPresenter(MenuBarPresenter paramMenuBarPresenter) {
        Iterator localIterator = this.mPresenters.iterator();
        while (localIterator.hasNext()) {
            WeakReference localWeakReference = (WeakReference) localIterator.next();
            MenuBarPresenter localMenuBarPresenter = (MenuBarPresenter) localWeakReference.get();
            if ((localMenuBarPresenter == null) || (localMenuBarPresenter == paramMenuBarPresenter))
                this.mPresenters.remove(localWeakReference);
        }
    }

    public void reopen() {
        reopen(false);
    }

    public void reopen(boolean paramBoolean) {
        this.mIsCreated = false;
        this.mItems.clear();
        this.mVisibleItems.clear();
        this.mMenuBarState = 0;
        open(paramBoolean);
    }

    public void setCallback(Callback paramCallback) {
        this.mCallback = null;
        this.mCallback = paramCallback;
    }

    public void setGroupCheckable(int paramInt, boolean paramBoolean1, boolean paramBoolean2) {
    }

    public void setGroupEnabled(int paramInt, boolean paramBoolean) {
    }

    public void setGroupVisible(int paramInt, boolean paramBoolean) {
    }

    public void setQwertyMode(boolean paramBoolean) {
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
        public abstract void onScroll(float paramFloat, boolean paramBoolean1, boolean paramBoolean2);

        public abstract void onScrollStateChanged(int paramInt);
    }
}
