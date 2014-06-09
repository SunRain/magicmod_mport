
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.android.internal.view.menu.MenuBuilder;
import com.magicmod.mport.internal.v5.view.ActionModeView;

import java.lang.ref.WeakReference;

public class DefaultActionMode extends ActionMode implements MenuBuilder.Callback {
    private ActionModeCallback mActionModeCallback;
    protected WeakReference<ActionModeView> mActionModeView;
    private ActionMode.Callback mCallback;
    protected Context mContext;
    private boolean mFinished;
    private MenuBuilder mMenu;

    public DefaultActionMode(Context paramContext, ActionMode.Callback paramCallback) {
        this.mContext = paramContext;
        this.mCallback = paramCallback;
        this.mMenu = new MenuBuilder(paramContext).setDefaultShowAsAction(1);
        this.mMenu.setCallback(this);
    }

    public boolean dispatchOnCreate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            boolean bool = this.mCallback.onCreateActionMode(this, this.mMenu);
            return bool;
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    public void finish() {
        if (!mFinished) {
            ((ActionModeView)this.mActionModeView.get()).closeMode();
            if (mCallback != null) {
                mCallback.onDestroyActionMode(this);
                mCallback = null;
            }
            if (this.mActionModeCallback != null) {
                mActionModeCallback.onActionModeFinish(this);
            }
            mFinished = true;
        }
    }

    public View getCustomView() {
        throw new UnsupportedOperationException("getCustomView not supported");
    }

    public Menu getMenu() {
        return this.mMenu;
    }

    public MenuInflater getMenuInflater() {
        return new MenuInflater(this.mContext);
    }

    public CharSequence getSubtitle() {
        throw new UnsupportedOperationException("getSubtitle not supported");
    }

    public CharSequence getTitle() {
        throw new UnsupportedOperationException("getTitle not supported");
    }

    public void invalidate() {
        this.mMenu.stopDispatchingItemsChanged();
        try {
            this.mCallback.onPrepareActionMode(this, this.mMenu);
            return;
        } finally {
            this.mMenu.startDispatchingItemsChanged();
        }
    }

    public boolean onMenuItemSelected(MenuBuilder paramMenuBuilder, MenuItem paramMenuItem) {
        //if (this.mCallback != null)
        //    ;
        //for (boolean bool = this.mCallback.onActionItemClicked(this, paramMenuItem);; bool = false)
        //    return bool;
        if (this.mCallback != null) {
            return this.mCallback.onActionItemClicked(this, paramMenuItem);
        }
        return false;
    }

    public void onMenuModeChange(MenuBuilder paramMenuBuilder) {
        //if (this.mCallback == null)
        //    ;
        //while (true) {
        //    return;
        //    invalidate();
        //}
        if (this.mCallback != null) {
            invalidate();
        }
    }

    public void setActionModeCallback(ActionModeCallback paramActionModeCallback) {
        this.mActionModeCallback = paramActionModeCallback;
    }

    public void setActionModeView(ActionModeView paramActionModeView) {
        this.mActionModeView = new WeakReference(paramActionModeView);
        paramActionModeView.initForMode(this);
    }

    public void setCustomView(View paramView) {
        throw new UnsupportedOperationException("setCustomView not supported");
    }

    public void setSubtitle(int paramInt) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    public void setSubtitle(CharSequence paramCharSequence) {
        throw new UnsupportedOperationException("setSubTitle not supported");
    }

    public void setTitle(int paramInt) {
        throw new UnsupportedOperationException("setTitle not supported");
    }

    public void setTitle(CharSequence paramCharSequence) {
        throw new UnsupportedOperationException("setTitle not supported");
    }

    public static abstract interface ActionModeCallback {
        public abstract void onActionModeFinish(ActionMode paramActionMode);
    }
}
