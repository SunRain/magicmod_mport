
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.HeaderViewListAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.internal.util.Predicate;
import com.google.android.collect.Lists;
import com.magicmod.mport.v5.android.support.view.ViewPager;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

public class Views {
    static final String TAG = Views.class.getName();
 
    public static final Comparator<View> TOP_COMPARATOR = new Comparator<View>() {
        @Override
        public int compare(View lhs, View rhs) {
            return lhs.getTop() - rhs.getTop();
        }
    };

    public static void collectChildren(ViewGroup view, Predicate<View> pred, List<View> list) {
        int count = view.getChildCount();
        for (int i = 0; i < count; i++) {
            View child = view.getChildAt(i);
            if (pred.apply(child))
                list.add(child);
        }
    }

    public static void detach(View v) {
        ViewParent parent = v.getParent();
        if ((parent instanceof ViewGroup))
            ((ViewGroup) parent).removeView(v);
    }

    /*public static void ensureBottomHeight(ListView paramListView, int paramInt) {
        Stub localStub = (Stub) paramListView.findViewWithTag(Stub.class);
        if (localStub != null)
            paramListView.removeFooterView(localStub);
        ListAdapter localListAdapter;
        if (paramInt > 0) {
            if (localStub == null) {
                localStub = new Stub(paramListView.getContext());
                localStub.setTag(Stub.class);
            }
            localStub.setHeight(paramInt);
            localListAdapter = paramListView.getAdapter();
            if ((localListAdapter instanceof HeaderViewListAdapter))
                break label94;
        }
        label94: for (int i = 1;; i = 0) {
            if (i != 0)
                paramListView.setAdapter(null);
            paramListView.addFooterView(localStub, null, false);
            if (i != 0)
                paramListView.setAdapter(localListAdapter);
            return;
        }
    }*/
    public static void ensureBottomHeight(ListView lv, int height) {
        Stub stub = (Stub) lv.findViewWithTag(Stub.class);
        if (stub != null)
            lv.removeFooterView(stub);
        if (height > 0) {
            if (stub == null) {
                stub = new Stub(lv.getContext());
                stub.setTag(Stub.class);
            }
            stub.setHeight(height);
            ListAdapter la = lv.getAdapter();
            boolean resetAdapter;
            if (!(la instanceof HeaderViewListAdapter))
                resetAdapter = true;
            else
                resetAdapter = false;
            if (resetAdapter)
                lv.setAdapter(null);
            lv.addFooterView(stub, null, false);
            if (resetAdapter)
                lv.setAdapter(la);
        }
    }

    public static int getBackgroundHeight(View view) {
        Drawable d = view.getBackground();
        int i;
        if (d != null)
            i = d.getIntrinsicHeight();
        else
            i = -1;
        return i;
    }

    public static int getBackgroundWidth(View view) {
        Drawable d = view.getBackground();
        int i;
        if (d != null)
            i = d.getIntrinsicWidth();
        else
            i = -1;
        return i;
    }

    public static void getContentRect(ViewGroup v, Rect out) {
        out.left = (v.getScrollX() + v.getPaddingLeft());
        out.top = (v.getScrollY() + v.getPaddingTop());
        out.right = (v.getWidth() - v.getPaddingRight() - out.left);
        out.bottom = (v.getHeight() - v.getPaddingBottom() - out.top);
    }

    public static View inflate(Context context, int layoutId, ViewGroup parent, boolean attachToRoot) {
        return LayoutInflater.from(context).inflate(layoutId, parent, attachToRoot);
    }

    public static boolean isBottomFullVisible(ListView lv) {
        View bottom = lv.findViewWithTag(Stub.class);
        if (bottom == null)
            throw new IllegalArgumentException("Can not find bottom view");
        boolean flag;
        if (bottom.getParent() != null && bottom.getBottom() < lv.getHeight())
            flag = true;
        else
            flag = false;
        return flag;
    }

    public static boolean isContainPoint(View view, int x, int y) {
        boolean flag;
        if (x > view.getLeft() && x < view.getRight() && y > view.getTop() && y < view.getBottom())
            flag = true;
        else
            flag = false;
        return flag;
    }

    /*public static boolean isIntersectWithRect(View paramView, Rect paramRect) {
        boolean bool = false;
        if (paramRect == null)
            ;
        while (true) {
            return bool;
            if ((paramView.getLeft() < paramRect.right) && (paramView.getTop() < paramRect.bottom)
                    && (paramView.getRight() > paramRect.left)
                    && (paramView.getBottom() > paramRect.top))
                bool = true;
        }
    }*/

    public static boolean isIntersectWithRect(View view, Rect rect) {
        // const/4 v0, 0x0
        if (rect == null) { // cond_1 in
            // :cond_0
            // :goto_0
            return false;// return v0
        } // cond_1 after
          // :cond_1
        if (view.getLeft() < rect.right && view.getTop() < rect.bottom
                && view.getRight() > rect.left && view.getBottom() > rect.top) {
            return true;
        }
        return false;
    }

    public static int measureHeightWidhBackground(View view) {
        int height = Math.max(getBackgroundHeight(view), 0);
        setHeight(view, height);
        return height;
    }

    /*public static void setHeight(View paramView, int paramInt) {
        ViewGroup.LayoutParams localLayoutParams = paramView.getLayoutParams();
        if (localLayoutParams == null)
            ;
        while (true) {
            return;
            if (localLayoutParams.height != paramInt) {
                localLayoutParams.height = paramInt;
                paramView.setLayoutParams(localLayoutParams);
            }
        }
    }*/

    public static void setHeight(View view, int height) {
        ViewGroup.LayoutParams lp = view.getLayoutParams(); // v0
        if (lp != null) {
            if (lp.height != height) { // cond_0 in
                lp.height = height;
                view.setLayoutParams(lp);
            }
        }
    }

    /*public static void setPadding(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        if (paramInt1 >= 0) {
            if (paramInt2 < 0)
                break label35;
            label8: if (paramInt3 < 0)
                break label43;
            label12: if (paramInt4 < 0)
                break label51;
        }
        while (true) {
            paramView.setPadding(paramInt1, paramInt2, paramInt3, paramInt4);
            return;
            paramInt1 = paramView.getPaddingLeft();
            break;
            label35: paramInt2 = paramView.getPaddingTop();
            break label8;
            label43: paramInt3 = paramView.getPaddingRight();
            break label12;
            label51: paramInt4 = paramView.getPaddingBottom();
        }
    }*/
    public static void setPadding(View v, int l, int t, int r, int b) {
        if (l < 0)
            l = v.getPaddingLeft();
        if (t < 0)
            t = v.getPaddingTop();
        if (r < 0)
            r = v.getPaddingRight();
        if (b < 0)
            b = v.getPaddingBottom();
        v.setPadding(l, t, r, b);
    }

    public static class ComposedPageChangeListener implements ViewPager.OnPageChangeListener {
        final List<ViewPager.OnPageChangeListener> mListeners = Lists.newArrayList();

        public void add(ViewPager.OnPageChangeListener l) {
            this.mListeners.add(l);
        }

        public void onPageScrollStateChanged(int state) {
            Iterator i = this.mListeners.iterator();
            while (i.hasNext())
                ((ViewPager.OnPageChangeListener) i.next())
                        .onPageScrollStateChanged(state);
        }

        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            Iterator i = this.mListeners.iterator();
            while (i.hasNext())
                ((ViewPager.OnPageChangeListener) i.next()).onPageScrolled(position,
                        positionOffset, positionOffsetPixels);
        }

        public void onPageSelected(int position) {
            Iterator i = this.mListeners.iterator();
            while (i.hasNext())
                ((ViewPager.OnPageChangeListener) i.next()).onPageSelected(position);
        }

        public void remove(ViewPager.OnPageChangeListener l) {
            this.mListeners.remove(l);
        }

        public void reset() {
            this.mListeners.clear();
        }
    }

    private static class Stub extends View {
        private int mHeight = 0;

        public Stub(Context context) {
            super(context);
            setVisibility(4);
        }

        protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(1, 0x40000000),
                    View.MeasureSpec.makeMeasureSpec(this.mHeight, 0x40000000));
        }

        public void setHeight(int height) {
            this.mHeight = Math.max(0, height);
        }
    }
}
