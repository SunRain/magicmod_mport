
package com.magicmod.mport.v5.view;

import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;

public class MiuiActionMode extends ActionMode {
    private ActionModeListener mActionModeListener;
    private boolean mActive = false;
    protected ActionMode.Callback mCallback;

    public void finish() {
        this.mActive = false;
        this.mCallback = null;
        if (this.mActionModeListener != null)
            this.mActionModeListener.onActionModeFinished(this);
    }

    public View getCustomView() {
        return null;
    }

    public Menu getMenu() {
        return null;
    }

    public MenuInflater getMenuInflater() {
        return null;
    }

    public CharSequence getSubtitle() {
        return null;
    }

    public CharSequence getTitle() {
        return null;
    }

    public void invalidate() {
    }

    public boolean isActive() {
        return this.mActive;
    }

    public void setActionModeListener(ActionModeListener paramActionModeListener) {
        this.mActionModeListener = paramActionModeListener;
    }

    @Override
    public void setCustomView(View view) {
    }

    public void setSubtitle(int resId) {
    }

    public void setSubtitle(CharSequence subtitle) {
    }

    public void setTitle(int resId) {
    }

    public void setTitle(CharSequence title) {
    }

    public void start(ActionMode.Callback callback) {
        if (this.mCallback != null)
            finish();
        this.mActive = true;
        this.mCallback = paramCallback;
        if (this.mActionModeListener != null)
            this.mActionModeListener.onActionModeStarted(this);
    }

    public boolean tryToFinish() {
        finish();
        return true;
    }

    public static abstract interface ActionModeListener {
        public abstract void onActionModeFinished(MiuiActionMode paramMiuiActionMode);

        public abstract void onActionModeStarted(MiuiActionMode paramMiuiActionMode);
    }
}
