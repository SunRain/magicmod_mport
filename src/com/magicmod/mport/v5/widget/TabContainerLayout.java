
package com.magicmod.mport.v5.widget;

import android.app.ActionBar;
import android.app.FragmentTransaction;
import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.miui.internal.R;

public class TabContainerLayout extends HorizontalScrollView implements View.OnClickListener {
    static final String TAG = TabContainerLayout.class.getName();
    private boolean mInteractive = true;
    int mMaxVisibleTabCount;
    ScrollStrategy mScrollStrategy;
    private ActionBar.Tab mSelectedTab;
    int mTabBackgroundResId;
    final LinearLayout mTabLayout;
    Runnable mTabSelector;
    TabViewStyle mTabViewStyle = null;
    int mTabWidth;
    private TabWidthChangedListener mTabWidthChangedListener;
    private TransactionFractory mTransactions;
    final FrameLayout mWrapLayout;

    public TabContainerLayout(Context context) {
        this(context, null);
    }

    public TabContainerLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TabContainerLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        setHorizontalScrollBarEnabled(false);
        setScrollStrategy(null);
        setMaxVisibleTabCount(MiuiViewConfiguration.get(context).getMaxVisibleTabCount());
        FrameLayout wrap = new FrameLayout(context);
        LinearLayout tabLayout = new LinearLayout(context, null, /*0x601009f*/R.attr.v5_tab_indicator_style);
        tabLayout.setMeasureWithLargestChildEnabled(true);
        tabLayout.setGravity(0x11);
        wrap.addView(tabLayout, new FrameLayout.LayoutParams(-1, -1));
        this.mTabLayout = tabLayout;
        addView(wrap, new FrameLayout.LayoutParams(-1, -1));
        this.mWrapLayout = wrap;
    }

    private void animateToTab(final View tabView) {
        if (mTabSelector != null)
            removeCallbacks(mTabSelector);

        mTabSelector = new Runnable() {
            @Override
            public void run() {
                if (tabView != null) {
                    int scrollX = mScrollStrategy.getScrollX(tabView, TabContainerLayout.this);
                    if (getScrollX() != scrollX)
                        smoothScrollTo(scrollX, 0);
                }
                mTabSelector = null;
            }
        };
        post(mTabSelector);
    }

    private TabViewImpl createTabView(ActionBar.Tab tab) {
        TabViewImpl tabView = new TabViewImpl(getContext(), tab);
        tabView.setFocusable(true);
        tabView.setOnClickListener(this);
        return tabView;
    }

    private ActionBar.Tab doSelectTab(ActionBar.Tab tab) {
        ActionBar.Tab oldSelected = mSelectedTab;
        //Object obj;
        View newTabView;
        if (tab == oldSelected) {
            newTabView = mTabLayout.getChildAt(findTabPosition(tab));
        } else {
            mSelectedTab = tab;
            newTabView = selectedTabView(tab);
        }
        animateToTab(newTabView);
        return oldSelected;
    }

    private TabViewImpl selectedTabView(ActionBar.Tab tab) {
        TabViewImpl selected = null;
        int count = mTabLayout.getChildCount();
        int i = 0;
        while (i < count) {
            TabViewImpl tv = (TabViewImpl) mTabLayout.getChildAt(i);
            if (tab == tv.getTab()) {
                selected = tv;
                tv.setSelected(true);
            } else {
                tv.setSelected(false);
            }
            i++;
        }
        return selected;
    }

    public void addTab(ActionBar.Tab tab, int position, boolean selected) {
        TabViewImpl tabView = createTabView(tab);
        this.mTabLayout.addView(tabView, position, new LinearLayout.LayoutParams(0, -1, 1.0F));
        if (selected)
            selectTab(tab);
    }

    public void addTab(ActionBar.Tab tab, boolean selected) {
        addTab(tab, -1, selected);
    }

    public void attachIndicator(TabIndicator indicator) {
        if (indicator != null)
            indicator.attach(this);
    }

    public void detachIndicator(TabIndicator indicator) {
        if (indicator != null)
            indicator.detach();
    }

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean flag;
        if (!mInteractive)
            flag = false;
        else
            flag = super.dispatchTouchEvent(ev);
        return flag;
    }

    public int findCurrentTabPos() {
        return findTabPosition(mSelectedTab);
    }

    public int findTabPosition(ActionBar.Tab tab) {
        int index = -1;
        int count = this.mTabLayout.getChildCount();
        for (int i = 0; i < count; i++) {
            TabViewImpl tv = (TabViewImpl) mTabLayout.getChildAt(i);
            if (tv.getTab() == tab) {
                index = i;
                break;
            }
        }
        return index;
    }

    public int getBackgroundHeight() {
        int height = Views.getBackgroundHeight(this);
        if (height <= 0)
            height = Views.getBackgroundHeight(this.mTabLayout);
        return Math.max(0, height);
    }

    public ViewGroup getIndicatorContainer() {
        return this.mWrapLayout;
    }

    int getOffsetX() {
        return this.mTabLayout.getPaddingLeft();
    }

    public ActionBar.Tab getSelectedTab() {
        return this.mSelectedTab;
    }

    public android.app.ActionBar.Tab getTabAt(int position) {
        TabViewImpl tv = (TabViewImpl) mTabLayout.getChildAt(position);
        ActionBar.Tab tab;
        if (tv != null)
            tab = tv.getTab();
        else
            tab = null;
        return tab;
    }

    public int getTabCount() {
        return this.mTabLayout.getChildCount();
    }

    public int getTabWidth() {
        return this.mTabWidth;
    }

    public TabImpl newTab(ActionBar.TabListener l) {
        return new TabImpl(l);
    }

    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (this.mTabSelector != null)
            post(this.mTabSelector);
    }

    @Override
    public void onClick(View view) {
        ActionBar.Tab tab = ((TabViewImpl) view).getTab();
        tab.select();
        selectedTabView(tab);
    }

    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mTabSelector != null)
            removeCallbacks(this.mTabSelector);
    }

    /*public void onMeasure(int paramInt1, int paramInt2) {
        int i = View.MeasureSpec.makeMeasureSpec(getBackgroundHeight(), 1073741824);
        int j = View.MeasureSpec.getMode(paramInt1);
        boolean bool;
        int i1;
        int i2;
        if (j == 1073741824) {
            bool = true;
            setFillViewport(bool);
            int k = this.mTabLayout.getChildCount();
            if ((k <= 1) || ((j != 1073741824) && (j != -2147483648)))
                break label163;
            i1 = Math.min(k, this.mMaxVisibleTabCount);
            i2 = this.mTabLayout.getPaddingLeft() + this.mTabLayout.getPaddingRight();
        }
        label163: for (int m = (View.MeasureSpec.getSize(paramInt1) - i2) / i1;; m = -1) {
            if (this.mTabWidth != m) {
                this.mTabWidth = m;
                onTabWidthChanged();
            }
            int n = getMeasuredWidth();
            super.onMeasure(paramInt1, i);
            if (n != getMeasuredWidth())
                selectTab(this.mSelectedTab);
            return;
            bool = false;
            break;
        }
    }*/
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int k = android.view.View.MeasureSpec.makeMeasureSpec(getBackgroundHeight(), 0x40000000);
        int widthMode = android.view.View.MeasureSpec.getMode(widthMeasureSpec);
        boolean lockedExpanded;
        int childCount;
        int j1;
        int oldWidth;
        if (widthMode == 0x40000000)
            lockedExpanded = true;
        else
            lockedExpanded = false;
        setFillViewport(lockedExpanded);
        int tabWidth = mTabWidth;
        childCount = mTabLayout.getChildCount();
        if (childCount > 1 && (widthMode == 0x40000000 || widthMode == 0x80000000)) {
            int tabCount = Math.min(childCount, mMaxVisibleTabCount);
            int padding = mTabLayout.getPaddingLeft() + mTabLayout.getPaddingRight();
            j1 = (android.view.View.MeasureSpec.getSize(widthMeasureSpec) - padding) / tabCount;
        } else {
            j1 = -1;
        }
        if (mTabWidth != j1) {
            mTabWidth = j1;
            onTabWidthChanged();
        }
        oldWidth = getMeasuredWidth();
        super.onMeasure(widthMeasureSpec, k);
        if (oldWidth != getMeasuredWidth())
            selectTab(mSelectedTab);
    }

    public boolean onRequestSendAccessibilityEvent(View child, AccessibilityEvent event) {
        boolean flag;
        if (child == mTabLayout && event.getEventType() == 1)
            flag = false;
        else
            flag = super.onRequestSendAccessibilityEvent(child, event);
        return flag;
    }

    protected void onTabReselected(ActionBar.Tab tab) {
        if (tab != null) {
            ActionBar.TabListener l = ((TabImpl) tab).getCallback();
            if (l != null) {
                FragmentTransaction ft;
                if (mTransactions != null)
                    ft = mTransactions.beginTransaction();
                else
                    ft = null;
                l.onTabReselected(tab, ft);
                if (ft != null && !ft.isEmpty())
                    ft.commit();
            }
        }
    }
    
    /*protected void onTabSelected(ActionBar.Tab paramTab) {
        if (paramTab == null)
            return;
        ActionBar.TabListener localTabListener = ((TabImpl) paramTab).getCallback();
        if (localTabListener != null)
            if (this.mTransactions == null)
                break label87;
        label87: for (FragmentTransaction localFragmentTransaction = this.mTransactions
                .beginTransaction();; localFragmentTransaction = null) {
            localTabListener.onTabSelected(paramTab, localFragmentTransaction);
            if ((localFragmentTransaction != null) && (!localFragmentTransaction.isEmpty()))
                localFragmentTransaction.commit();
            View localView = this.mTabLayout.getChildAt(paramTab.getPosition());
            if (localView == null)
                break;
            localView.sendAccessibilityEvent(4);
            break;
        }
    }*/
    protected void onTabSelected(android.app.ActionBar.Tab tab) {
        if (tab != null) {
            View tabView;
            android.app.ActionBar.TabListener l = ((TabImpl) tab).getCallback();
            if (l != null) {
                FragmentTransaction tf;
                if (mTransactions != null)
                    tf = mTransactions.beginTransaction();
                else
                    tf = null;
                l.onTabSelected(tab, tf);
                if (tf != null && !tf.isEmpty())
                    tf.commit();
            }
            tabView = mTabLayout.getChildAt(tab.getPosition());
            if (tabView != null)
                tabView.sendAccessibilityEvent(4);
        }
    }

    /*protected void onTabUnselected(ActionBar.Tab paramTab) {
        if (paramTab == null)
            ;
        ActionBar.TabListener localTabListener;
        do {
            return;
            localTabListener = ((TabImpl) paramTab).getCallback();
        } while (localTabListener == null);
        if (this.mTransactions != null)
            ;
        for (FragmentTransaction localFragmentTransaction = this.mTransactions.beginTransaction();; localFragmentTransaction = null) {
            localTabListener.onTabUnselected(paramTab, localFragmentTransaction);
            if ((localFragmentTransaction == null) || (localFragmentTransaction.isEmpty()))
                break;
            localFragmentTransaction.commit();
            break;
        }
    }*/
    protected void onTabUnselected(android.app.ActionBar.Tab tab) {
        if (tab != null) {
            android.app.ActionBar.TabListener l = ((TabImpl) tab).getCallback();
            if (l != null) {
                FragmentTransaction ft;
                if (mTransactions != null)
                    ft = mTransactions.beginTransaction();
                else
                    ft = null;
                l.onTabUnselected(tab, ft);
                if (ft != null && !ft.isEmpty())
                    ft.commit();
            }
        }
    }

    protected void onTabWidthChanged() {
        if (this.mTabWidthChangedListener != null)
            this.mTabWidthChangedListener.onTabWidthChanged(this);
    }

    public boolean removeAllTabs() {
        this.mSelectedTab = null;
        this.mTabLayout.removeAllViews();
        return true;
    }

    public boolean removeTab(ActionBar.Tab tab) {
        boolean selectChanged = false;
        if ((this.mSelectedTab == tab) && (tab != null)) {
            this.mSelectedTab = null;
            selectChanged = true;
        }
        this.mTabLayout.removeViewAt(findTabPosition(tab));
        return selectChanged;
    }

    public boolean removeTabAt(int position) {
        return removeTab(getTabAt(position));
    }

    /*public boolean selectTab(ActionBar.Tab paramTab) {
        boolean bool = false;
        ActionBar.Tab localTab = doSelectTab(paramTab);
        if (paramTab == localTab)
            onTabReselected(paramTab);
        while (true) {
            return bool;
            bool = true;
            onTabUnselected(localTab);
            onTabSelected(paramTab);
        }
    }*/
    public boolean selectTab(android.app.ActionBar.Tab tab) {
        boolean changed = false;
        android.app.ActionBar.Tab oldSelected = doSelectTab(tab);
        if (tab == oldSelected) {
            onTabReselected(tab);
        } else {
            changed = true;
            onTabUnselected(oldSelected);
            onTabSelected(tab);
        }
        return changed;
    }

    public void selectTabAt(int position) {
        selectTab(getTabAt(position));
    }

    public void setInteractive(boolean interactive) {
        this.mInteractive = interactive;
    }

    public void setMaxVisibleTabCount(int count) {
        if (count <= 0)
            throw new IllegalArgumentException("count > 0 is need! count=" + count);
        if (this.mMaxVisibleTabCount != count) {
            this.mMaxVisibleTabCount = count;
            requestLayout();
        }
    }

    public void setScrollStrategy(ScrollStrategy strategy) {
        if (strategy != null)
            mScrollStrategy = strategy;
        else
            mScrollStrategy = new DefaultScrollStrategy();
    }

    public void setTabBackground(int resId) {
        this.mTabBackgroundResId = resId;
        for (int i = getTabCount(); i >= 0; i--)
            updateTabAt(i);
    }

    public void setTabLayoutBackground(Drawable d) {
        this.mTabLayout.setBackground(d);
        requestLayout();
    }

    public void setTabViewStyle(TabViewStyle style) {
        this.mTabViewStyle = style;
    }

    void setTabWidthChangedListener(TabWidthChangedListener l) {
        this.mTabWidthChangedListener = l;
    }

    public void setTransactionFractory(TransactionFractory factory) {
        this.mTransactions = factory;
    }

    public void updateTabAt(int position) {
        TabViewImpl tabView = (TabViewImpl) this.mTabLayout.getChildAt(position);
        if (tabView != null)
            tabView.update();
    }

    public void updateTabPosition() {
        int count = this.mTabLayout.getChildCount();
        for (int i = 0; i < count; i++)
            ((TabImpl) ((TabViewImpl) this.mTabLayout.getChildAt(i)).getTab()).setPosition(i);
    }

    private static class DefaultScrollStrategy implements TabContainerLayout.ScrollStrategy {
        private final Rect mBounds = new Rect();

        public int getScrollX(View tabView, ViewGroup parent) {
            int tabLeft = tabView.getLeft();
            int tabRight = tabView.getRight();
            Rect r = mBounds;
            parent.getLocalVisibleRect(r);
            int scrollX;
            if (tabLeft < r.left)
                scrollX = tabLeft;
            else if (tabRight > r.right)
                scrollX = (tabRight + r.left) - r.right;
            else
                scrollX = r.left;
            return scrollX;
        }
    }

    public static abstract interface ScrollStrategy {
        public abstract int getScrollX(View tabView, ViewGroup parent);
    }

    class TabImpl extends ActionBar.Tab {
        private ActionBar.TabListener mCallback;
        private CharSequence mContentDesc;
        private Drawable mIcon;
        private int mPosition = -1;
        private Object mTag;
        private CharSequence mText;

        public TabImpl(ActionBar.TabListener callback) {
            super();
            mCallback = callback;
        }

        private void update() {
            if (this.mPosition >= 0)
                TabContainerLayout.this.updateTabAt(this.mPosition);
        }

        public ActionBar.TabListener getCallback() {
            return this.mCallback;
        }

        public CharSequence getContentDescription() {
            return this.mContentDesc;
        }

        public View getCustomView() {
            return null;
        }

        public Drawable getIcon() {
            return this.mIcon;
        }

        public int getPosition() {
            return this.mPosition;
        }

        public Object getTag() {
            return this.mTag;
        }

        public CharSequence getText() {
            return this.mText;
        }

        public void select() {
            TabContainerLayout.this.selectTab(this);
        }

        public ActionBar.Tab setContentDescription(int resId) {
            return setContentDescription(TabContainerLayout.this.getResources().getText(resId));
        }

        public ActionBar.Tab setContentDescription(CharSequence contentDesc) {
            this.mContentDesc = contentDesc;
            return this;
        }

        public ActionBar.Tab setCustomView(int layoutResId) {
            Log.w(TabContainerLayout.TAG, "custom is unsupported");
            return this;
        }

        public ActionBar.Tab setCustomView(View view) {
            Log.w(TabContainerLayout.TAG, "custom is unsupported");
            return this;
        }

        public ActionBar.Tab setIcon(int resId) {
            Drawable d = TabContainerLayout.this.getResources().getDrawable(resId);
            d.setBounds(0, 0, d.getIntrinsicWidth(),
                    d.getIntrinsicHeight());
            return setIcon(d);
        }

        public ActionBar.Tab setIcon(Drawable icon) {
            this.mIcon = icon;
            update();
            return this;
        }

        public void setPosition(int i) {
            this.mPosition = i;
        }

        public ActionBar.Tab setTabListener(ActionBar.TabListener listener) {
            this.mCallback = listener;
            return this;
        }

        public ActionBar.Tab setTag(Object tag) {
            this.mTag = tag;
            return this;
        }

        public ActionBar.Tab setText(int resId) {
            return setText(TabContainerLayout.this.getResources().getText(resId));
        }

        public ActionBar.Tab setText(CharSequence text) {
            this.mText = text;
            update();
            return this;
        }
    }

    private class TabViewImpl extends LinearLayout {
        private ActionBar.Tab mTab;
        private TextView mTextView;

        public TabViewImpl(Context context, android.app.ActionBar.Tab tab) {
            super(context, null, /*0x60100a0*/R.attr.v5_tab_style);
            setGravity(0x11);
            mTab = tab;
            update();
        }

        public ActionBar.Tab getTab() {
            return this.mTab;
        }

        public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            if (mTabWidth > 0)
                widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(mTabWidth, 0x40000000);
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }

        /*public void update() {
            if (TabContainerLayout.this.mTabBackgroundResId != 0)
                setBackgroundResource(TabContainerLayout.this.mTabBackgroundResId);
            ActionBar.Tab localTab = this.mTab;
            Drawable localDrawable = localTab.getIcon();
            CharSequence localCharSequence = localTab.getText();
            LayoutInflater localLayoutInflater = LayoutInflater.from(this.mContext);
            if (this.mTextView == null) {
                this.mTextView = ((TextView) localLayoutInflater.inflate(100859984, this, false));
                addView(this.mTextView);
            }
            this.mTextView.setText(localCharSequence);
            this.mTextView.setCompoundDrawablesRelative(null, null, localDrawable, null);
            TextView localTextView = this.mTextView;
            if (localDrawable != null)
                ;
            for (int i = localDrawable.getIntrinsicWidth();; i = 0) {
                localTextView.setPadding(i, 0, 0, 0);
                return;
            }
        }*/
        public void update() {
            if (mTabBackgroundResId != 0)
                setBackgroundResource(mTabBackgroundResId);
            android.app.ActionBar.Tab tab = mTab;
            Drawable icon = tab.getIcon();
            CharSequence text = tab.getText();
            LayoutInflater inflater = LayoutInflater.from(mContext);
            if (mTextView == null) {
                mTextView = (TextView) inflater.inflate(0x6030050, this, false);
                addView(mTextView);
            }
            mTextView.setText(text);
            mTextView.setCompoundDrawablesRelative(null, null, icon, null);
            TextView textview = mTextView;
            int i;
            if (icon != null)
                i = icon.getIntrinsicWidth();
            else
                i = 0;
            textview.setPadding(i, 0, 0, 0);
        }
    }

    public static abstract interface TabViewStyle {
        public abstract void bindIconView(ActionBar.Tab paramTab, ImageView paramImageView);

        public abstract void bindTextView(ActionBar.Tab paramTab, TextView paramTextView);
    }

    static abstract interface TabWidthChangedListener {
        public abstract void onTabWidthChanged(TabContainerLayout paramTabContainerLayout);
    }

    public static abstract interface TransactionFractory {
        public abstract FragmentTransaction beginTransaction();
    }
}
