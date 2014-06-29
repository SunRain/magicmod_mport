
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.view.ActionMode;
import android.view.View;

import com.android.internal.R;
import com.magicmod.mport.internal.v5.widget.ActionBarContextView;

public class EditActionMode extends DefaultActionMode {
    public static final int BUTTON1 = R.id.button1;//0x1020019;
    public static final int BUTTON2 = R.id.button2;//0x102001a;

    public EditActionMode(Context context, ActionMode.Callback callback) {
        super(context, callback);
    }

    public void setButton(int whichButton, int resId) {
        setButton(whichButton, mContext.getResources().getString(resId));
    }

    public void setButton(int whichButton, CharSequence text) {
        ((ActionBarContextView) mActionModeView.get()).setButton(whichButton, text);
    }

    public void setCustomView(View view) {
    }

    public void setSubtitle(int resId) {
    }

    public void setSubtitle(CharSequence subtitle) {
    }

    public void setTitle(int resId) {
        setTitle(this.mContext.getResources().getString(resId));
    }

    public void setTitle(CharSequence title) {
        ((ActionBarContextView) this.mActionModeView.get()).setTitle(title);
    }
}
