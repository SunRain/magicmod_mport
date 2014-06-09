
package com.magicmod.mport.v5.app;

import android.widget.AdapterView;

public class LayoutObservers {
    public static LayoutObserver makeLayoutObserverForListView(AdapterView<?> paramAdapterView) {
        return new ListViewLayoutObserver(paramAdapterView);
    }

    private static class ListViewLayoutObserver implements LayoutObserver {
        private final AdapterView<?> mListView;

        ListViewLayoutObserver(AdapterView<?> paramAdapterView) {
            this.mListView = paramAdapterView;
        }

        public boolean isContentFilled() {
            // int i = this.mListView.getChildCount();
            // int j = this.mListView.getCount();
            int childCount = this.mListView.getChildCount();
            int itemCount = this.mListView.getCount();
            // boolean filled = false;
            if (this.mListView.getChildAt(childCount - 1).getBottom() >= this.mListView.getHeight()) {
                return true;
            } else {
                return false;
            }
            // return filled
        }
    }
}
