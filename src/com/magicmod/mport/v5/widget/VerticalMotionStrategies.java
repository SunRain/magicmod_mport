
package com.magicmod.mport.v5.widget;

import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ScrollView;

import com.magicmod.mport.v5.util.Factory;

public class VerticalMotionStrategies {
    
    public static boolean canListScroll(AdapterView<?> alv, int x, int y, int startX, int startY)
    {
        boolean flag;
        if(y == startY)
            flag = false;
        else
        if(y > startY)
            flag = canListScrollDown(alv);
        else
            flag = canListScrollUp(alv);
        return flag;
    }
    
    private static boolean canListScrollDown(AdapterView<?> paramAdapterView) {
        boolean bool = true;
        if (paramAdapterView.getFirstVisiblePosition() > 0)
            ;
        while (true) {
            return bool;
            int i = paramAdapterView.getPaddingTop();
            int j = paramAdapterView.getChildCount();
            for (int k = 0;; k++) {
                if (k >= j)
                    break label49;
                if (paramAdapterView.getChildAt(k).getTop() < i)
                    break;
            }
            label49: bool = false;
        }
    }

    private static boolean canListScrollUp(AdapterView<?> paramAdapterView) {
        boolean bool = true;
        if (paramAdapterView.getLastVisiblePosition() < -1 + paramAdapterView.getCount())
            ;
        while (true) {
            return bool;
            int i = paramAdapterView.getHeight() - paramAdapterView.getPaddingBottom()
                    - paramAdapterView.getPaddingTop();
            int j = paramAdapterView.getChildCount();
            for (int k = 0;; k++) {
                if (k >= j)
                    break label65;
                if (paramAdapterView.getChildAt(k).getBottom() >= i)
                    break;
            }
            label65: bool = false;
        }
    }

    public static boolean canScrollViewScroll(ScrollView sv, int x, int y, int startX, int startY)
    {
        boolean flag;
        if(y > startY)
            flag = canScrollViewScrollDown(sv);
        else
            flag = canScrollViewScrollUp(sv);
        return flag;
    }

    private static boolean canScrollViewScrollDown(ScrollView paramScrollView) {
        boolean bool = false;
        if (paramScrollView.getChildAt(0) == null)
            ;
        while (true) {
            return bool;
            if (paramScrollView.getScrollY() > 0)
                bool = true;
        }
    }

    /*private static boolean canScrollViewScrollUp(ScrollView paramScrollView) {
        boolean bool = false;
        View localView = paramScrollView.getChildAt(0);
        if (localView == null)
            ;
        while (true) {
            return bool;
            int i = paramScrollView.getHeight() - paramScrollView.getPaddingBottom()
                    - paramScrollView.getPaddingTop();
            if (localView.getBottom() > i)
                bool = true;
        }
    }*/
    private static boolean canScrollViewScrollUp(ScrollView sv) {
        boolean flag;
        View child;
        flag = false;
        child = sv.getChildAt(0);
        if (child != null) {
            int bottom = sv.getHeight() - sv.getPaddingBottom() - sv.getPaddingTop();
            if (child.getBottom() > bottom)
                flag = true;
        }
        return flag;
    }

    public static MotionDetectStrategy makeMotionStrategyForList(AdapterView<?> list) {
        ListMotionListener listmotionlistener;
        if (list != null)
            listmotionlistener = new ListMotionListener(list);
        else
            listmotionlistener = null;
        return listmotionlistener;
    }

    public static MotionDetectStrategy makeMotionStrategyForScrollView(ScrollView sv) {
        ScrollViewMotionListener scrollviewmotionlistener;
        if (sv != null)
            scrollviewmotionlistener = new ScrollViewMotionListener(sv);
        else
            scrollviewmotionlistener = null;
        return scrollviewmotionlistener;
    }

    /*public static <F> MotionDetectStrategy makeMotionStrategyTabController(
            TabController paramTabController, int paramInt,
            Factory<MotionDetectStrategy, Integer> paramFactory) {
        if (paramTabController != null)
            ;
        for (TabControllerMotionListener localTabControllerMotionListener = new TabControllerMotionListener(
                paramTabController, paramInt, paramFactory);; localTabControllerMotionListener = null)
            return localTabControllerMotionListener;
    }*/
    public static <F> MotionDetectStrategy makeMotionStrategyTabController(
            TabController tabController, int minY,
            Factory<MotionDetectStrategy, Integer> pagerStrategyFactory) {
        
        TabControllerMotionListener tabcontrollermotionlistener;
        if (tabController != null)
            tabcontrollermotionlistener = new TabControllerMotionListener(tabController, minY,
                    pagerStrategyFactory);
        else
            tabcontrollermotionlistener = null;
        return tabcontrollermotionlistener;
    }

    public static void scrollByInertia(AbsListView lv, int velocity) {
        lv.smoothScrollBy(-(int) (0.2D * velocity), 500);
    }

    
    private static class ListMotionListener implements MotionDetectStrategy {
        private final AdapterView<?> mList;

        public ListMotionListener(AdapterView<?> list) {
            this.mList = list;
        }

        @Override
        public boolean isMovable(View view, int x, int y, int startX, int startY) {
            boolean flag;
            if (!VerticalMotionStrategies.canListScroll(mList, x, y, startX, startY))
                flag = true;
            else
                flag = false;
            return flag;
        }
    }

    private static class ScrollViewMotionListener implements MotionDetectStrategy {
        private final ScrollView mScroll;

        public ScrollViewMotionListener(ScrollView scrollView) {
            this.mScroll = scrollView;
        }

        @Override
        public boolean isMovable(View view, int x, int y, int startX, int startY) {
            boolean flag;
            if (!VerticalMotionStrategies.canScrollViewScroll(mScroll, x, y, startX, startY))
                flag = true;
            else
                flag = false;
            return flag;
        }
    }

    private static class TabControllerMotionListener implements MotionDetectStrategy {
        private final int mMinY;
        private final Factory<MotionDetectStrategy, Integer> mPagerStrategyFactory;
        private final TabController mTabController;

        public TabControllerMotionListener(TabController controller, int minY,
                Factory<MotionDetectStrategy, Integer> pagerStrategyFactory) {
            this.mTabController = controller;
            this.mMinY = minY;
            this.mPagerStrategyFactory = pagerStrategyFactory;
        }

        /*public boolean isMovable(View paramView, int paramInt1, int paramInt2, int paramInt3,
                int paramInt4) {
            boolean bool = true;
            if (this.mTabController.getViewPagerState() != 0)
                bool = false;
            while (true) {
                return bool;
                if (!Views.isContainPoint(this.mTabController.getTabContainer(), paramInt3,
                        paramInt4))
                    if (paramInt2 < paramInt4) {
                        if (paramView.getY() <= this.mMinY)
                            bool = false;
                    } else if (this.mPagerStrategyFactory != null) {
                        int i = this.mTabController.getViewPager().getCurrentItem();
                        MotionDetectStrategy localMotionDetectStrategy = (MotionDetectStrategy) this.mPagerStrategyFactory
                                .create(Integer.valueOf(i));
                        if (localMotionDetectStrategy != null)
                            bool = localMotionDetectStrategy.isMovable(paramView, paramInt1,
                                    paramInt2, paramInt3, paramInt4);
                    }
            }
        }*/
        @Override
        public boolean isMovable(View view, int x, int y, int startX, int startY) {
            boolean flag = true;
            if (mTabController.getViewPagerState() == 0) {
                if (!Views.isContainPoint(mTabController.getTabContainer(), startX, startY)) {
                    if (y < startY) {
                        if (view.getY() <= (float) mMinY) {
                            flag = false;
                        }
                    } else if (mPagerStrategyFactory != null) {
                        int index = mTabController.getViewPager().getCurrentItem();
                        MotionDetectStrategy strategy = (MotionDetectStrategy) mPagerStrategyFactory
                                .create(Integer.valueOf(index));
                        if (strategy != null)
                            flag = strategy.isMovable(view, x, y, startX, startY);
                    }
                }
            } else {
                flag = false;
            }
            return flag;
        }
    }
}
