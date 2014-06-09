
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.view.ActionMode;

public class EditActionMode extends DefaultActionMode {
    // public static final int BUTTON1 = 16908313;
    // public static final int BUTTON2 = 16908314;

    public EditActionMode(Context paramContext, ActionMode.Callback paramCallback) {
        super(paramContext, paramCallback);
    }

    public void setButton(int paramInt1, int paramInt2) {
        setButton(paramInt1, this.mContext.getResources().getString(paramInt2));
    }

    public void setButton(int paramInt, CharSequence paramCharSequence) {
        ((ActionBarContextView) this.mActionModeView.get()).setButton(paramInt, paramCharSequence);
    }

    public void setCustomView(View paramView) {
    }

    public void setSubtitle(int paramInt) {
    }

    public void setSubtitle(CharSequence paramCharSequence) {
    }

    public void setTitle(int paramInt) {
        setTitle(this.mContext.getResources().getString(paramInt));
    }

    public void setTitle(CharSequence paramCharSequence) {
        ((ActionBarContextView) this.mActionModeView.get()).setTitle(paramCharSequence);
    }
}
