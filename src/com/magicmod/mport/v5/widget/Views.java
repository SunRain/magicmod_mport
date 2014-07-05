
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.android.internal.util.Predicate;

import java.util.Comparator;
import java.util.List;

public class Views {
    static final String TAG = Views.class.getName();
    public static final Comparator<View> TOP_COMPARATOR = new Comparator() {
        public int compare(View paramAnonymousView1, View paramAnonymousView2) {
            return paramAnonymousView1.getTop() - paramAnonymousView2.getTop();
        }

        @Override
        public int compare(Object lhs, Object rhs) {
            // TODO Auto-generated method stub
            return 0;
        }
    };

    public static void collectChildren(ViewGroup paramViewGroup, Predicate<View> paramPredicate,
            List<View> paramList) {
        int i = paramViewGroup.getChildCount();
        for (int j = 0; j < i; j++) {
            View localView = paramViewGroup.getChildAt(j);
            if (paramPredicate.apply(localView))
                paramList.add(localView);
        }
    }

    public static void detach(View paramView) {
        ViewParent localViewParent = paramView.getParent();
        if ((localViewParent instanceof ViewGroup))
            ((ViewGroup) localViewParent).removeView(paramView);
    }

    public static void ensureBottomHeight(ListView paramListView, int paramInt) {
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
    }

    public static int getBackgroundHeight(View paramView) {
        Drawable localDrawable = paramView.getBackground();
        if (localDrawable != null)
            ;
        for (int i = localDrawable.getIntrinsicHeight();; i = -1)
            return i;
    }

    public static int getBackgroundWidth(View paramView) {
        Drawable localDrawable = paramView.getBackground();
        if (localDrawable != null)
            ;
        for (int i = localDrawable.getIntrinsicWidth();; i = -1)
            return i;
    }

    public static void getContentRect(ViewGroup paramViewGroup, Rect paramRect) {
        paramRect.left = (paramViewGroup.getScrollX() + paramViewGroup.getPaddingLeft());
        paramRect.top = (paramViewGroup.getScrollY() + paramViewGroup.getPaddingTop());
        paramRect.right = (paramViewGroup.getWidth() - paramViewGroup.getPaddingRight() - paramRect.left);
        paramRect.bottom = (paramViewGroup.getHeight() - paramViewGroup.getPaddingBottom() - paramRect.top);
    }

    public static View inflate(Context paramContext, int paramInt, ViewGroup paramViewGroup,
            boolean paramBoolean) {
        return LayoutInflater.from(paramContext).inflate(paramInt, paramViewGroup, paramBoolean);
    }

    public static boolean isBottomFullVisible(ListView paramListView) {
        View localView = paramListView.findViewWithTag(Stub.class);
        if (localView == null)
            throw new IllegalArgumentException("Can not find bottom view");
        if ((localView.getParent() != null) && (localView.getBottom() < paramListView.getHeight()))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public static boolean isContainPoint(View paramView, int paramInt1, int paramInt2) {
        if ((paramInt1 > paramView.getLeft()) && (paramInt1 < paramView.getRight())
                && (paramInt2 > paramView.getTop()) && (paramInt2 < paramView.getBottom()))
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }

    public static boolean isIntersectWithRect(View paramView, Rect paramRect) {
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
    }

    public static int measureHeightWidhBackground(View paramView) {
        int i = Math.max(getBackgroundHeight(paramView), 0);
        setHeight(paramView, i);
        return i;
    }

    public static void setHeight(View paramView, int paramInt) {
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
    }

    public static void setPadding(View paramView, int paramInt1, int paramInt2, int paramInt3,
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
    }

    public static class ComposedPageChangeListener implements ViewPager.OnPageChangeListener {
        final List<ViewPager.OnPageChangeListener> mListeners = Lists.newArrayList();

        public void add(ViewPager.OnPageChangeListener paramOnPageChangeListener) {
            this.mListeners.add(paramOnPageChangeListener);
        }

        public void onPageScrollStateChanged(int paramInt) {
            Iterator localIterator = this.mListeners.iterator();
            while (localIterator.hasNext())
                ((ViewPager.OnPageChangeListener) localIterator.next())
                        .onPageScrollStateChanged(paramInt);
        }

        public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
            Iterator localIterator = this.mListeners.iterator();
            while (localIterator.hasNext())
                ((ViewPager.OnPageChangeListener) localIterator.next()).onPageScrolled(paramInt1,
                        paramFloat, paramInt2);
        }

        public void onPageSelected(int paramInt) {
            Iterator localIterator = this.mListeners.iterator();
            while (localIterator.hasNext())
                ((ViewPager.OnPageChangeListener) localIterator.next()).onPageSelected(paramInt);
        }

        public void remove(ViewPager.OnPageChangeListener paramOnPageChangeListener) {
            this.mListeners.remove(paramOnPageChangeListener);
        }

        public void reset() {
            this.mListeners.clear();
        }
    }

    private static class Stub extends View {
        private int mHeight = 0;

        public Stub(Context paramContext) {
            super();
            setVisibility(4);
        }

        protected void onMeasure(int paramInt1, int paramInt2) {
            super.onMeasure(View.MeasureSpec.makeMeasureSpec(1, 1073741824),
                    View.MeasureSpec.makeMeasureSpec(this.mHeight, 1073741824));
        }

        public void setHeight(int paramInt) {
            this.mHeight = Math.max(0, paramInt);
        }
    }
}
