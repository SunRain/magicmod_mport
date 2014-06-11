
package com.magicmod.mport.v5.view;

import android.content.Context;
import android.view.View;
import android.widget.EditText;

import com.magicmod.mport.internal.v5.widget.SearchActionModeView;

public class SearchActionMode extends DefaultActionMode {
    public SearchActionMode(Context context, Callback callback) {
        super(context, callback);
    }

    public void addAnimationListener(SearchModeAnimationListener listener) {
        ((SearchActionModeView) this.mActionModeView.get())
                .addAnimationListener(listener);
    }

    public EditText getSearchView() {
        return ((SearchActionModeView) this.mActionModeView.get()).getSearchView();
    }

    public void removeAnimationListener(SearchModeAnimationListener listener) {
        ((SearchActionModeView) this.mActionModeView.get())
                .removeAnimationListener(listener);
    }

    public void setAnchorView(View view) {
        ((SearchActionModeView) this.mActionModeView.get()).setAnchorView(view);
    }

    public void setAnchorViewHint(CharSequence hint) {
        ((SearchActionModeView) this.mActionModeView.get()).setAnchorViewHint(hint);
    }

    public void setAnimateView(View view) {
        ((SearchActionModeView) this.mActionModeView.get()).setAnimateView(view);
    }

    public void setResultView(View view) {
        ((SearchActionModeView) this.mActionModeView.get()).setResultView(view);
    }

    public static abstract interface Callback extends  android.view.ActionMode.Callback {
    }
}
