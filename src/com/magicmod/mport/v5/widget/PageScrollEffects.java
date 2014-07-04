
package com.magicmod.mport.v5.widget;

import android.graphics.Rect;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.collect.Lists;
import com.magicmod.mport.util.SimplePool;
import com.magicmod.mport.v5.android.support.view.ViewPager;
import com.magicmod.mport.v5.util.Factory;

import java.util.List;

public class PageScrollEffects {
    static final long MAX_REFRESH_INTERVAL = 0x5L;
    static final float MIN_REFRESH_DIFF = 0.001F;
    static final long SCROLL_ANIMATION_DURATION = 0x64L;

    public static void cancelPageScrollEffect(View paramView) {
        paramView.setTranslationX(0.0F);
    }

    public static ViewPager.OnPageChangeListener makePageChangeAdapter(ViewPager pager,
            Factory<PageScrollEffect, Integer> factory) {
        PageChangeAdapter adapter = new PageChangeAdapter(pager);
        adapter.setScrolledListener(new PageScrolledDispatcher(factory));
        return adapter;
    }

    public static PageScrollEffect makePageScrollEffect(ViewGroup group) {
        return makePageScrollEffect(group, null);
    }

    public static PageScrollEffect makePageScrollEffect(ViewGroup group, int[] resIds) {
        DefaultPageScrollEffect localDefaultPageScrollEffect = null;
        if (group != null) {
            localDefaultPageScrollEffect = new DefaultPageScrollEffect(group);
            localDefaultPageScrollEffect.attach(group, resIds);
        }
        return localDefaultPageScrollEffect;
    }

    private static class DefaultPageScrollEffect extends PageScrollEffect.AbsPageScrollEffect {
        static final PageScrollEffect.AbsPageScrollEffect.AllPred ALL_PRED = new PageScrollEffect.AbsPageScrollEffect.AllPred();
        static final PageScrollEffect.AbsPageScrollEffect.AnyPred ANY_PRED = new PageScrollEffect.AbsPageScrollEffect.AnyPred();
        static SimplePool.PoolInstance<List<View>> sListPool = SimplePool.newInsance(
                new SimplePool.Manager() {
                    public List<View> createInstance() {
                        return Lists.newArrayList();
                    }

                    public void onRelease(List<View> paramAnonymousList) {
                        paramAnonymousList.clear();
                    }
                }, 4);
        static SimplePool.PoolInstance<Rect> sRectPool = SimplePool.newInsance(
                new SimplePool.Manager() {
                    public Rect createInstance() {
                        return new Rect();
                    }

                    public void onAcquire(Rect paramAnonymousRect) {
                        paramAnonymousRect.setEmpty();
                    }
                }, 4);
        private final PageScrollEffects.ScrollAnimation mAnimation;

        public DefaultPageScrollEffect(ViewGroup v) {
            super(v);
            this.mAnimation = new PageScrollEffects.ScrollAnimation(v);
        }

        /*private static int computOffset(int paramInt1, int paramInt2, int paramInt3,
                float paramFloat) {
            int i;
            float f2;
            if (paramInt1 < paramInt3) {
                i = paramInt1 * paramInt2 / paramInt3;
                float f1 = paramFloat * paramFloat;
                f2 = i + (0.1F - f1 / 0.9F) * paramInt2;
                if (f2 <= 0.0F)
                    break label55;
            }
            label55: for (int j = (int) f2;; j = 0) {
                return j;
                i = paramInt2;
                break;
            }
        }*/
        private static int computOffset(int top, int width, int height, float percent) {
            int indent;
            float coeff;
            float offset;
            int i1;
            if (top < height)
                indent = (top * width) / height;
            else
                indent = width;
            coeff = percent * percent;
            offset = (float) indent + (0.1F - coeff / 0.9F) * (float) width;
            if (offset > 0.0F)
                i1 = (int) offset;
            else
                i1 = 0;
            return i1;
        }

        static void translateView(ViewGroup paramViewGroup, int paramInt1, int paramInt2,
                float paramFloat, boolean paramBoolean) {
            Rect localRect = null;
            List localList = null;
            while (true) {
                SimplePool.PoolInstance localPoolInstance;
                try {
                    localRect = (Rect) sRectPool.acquire();
                    Views.getContentRect(paramViewGroup, localRect);
                    boolean bool1 = localRect.isEmpty();
                    if (bool1) {
                        if (0 != 0)
                            sListPool.release(null);
                        if (localRect != null) {
                            localPoolInstance = sRectPool;
                            localPoolInstance.release(localRect);
                        }
                        return;
                    }
                    localList = (List) sListPool.acquire();
                    int i = getEffectType(paramViewGroup);
                    if (i == 3) {
                        Views.collectChildren(paramViewGroup, ALL_PRED, localList);
                        boolean bool2 = localList.isEmpty();
                        if (!bool2)
                            break label185;
                        if (localList != null)
                            sListPool.release(localList);
                        if (localRect == null)
                            continue;
                        localPoolInstance = sRectPool;
                        continue;
                    }
                    if (i != 2)
                        continue;
                    Views.collectChildren(paramViewGroup, ANY_PRED, localList);
                    continue;
                } finally {
                    if (localList != null)
                        sListPool.release(localList);
                    if (localRect != null)
                        sRectPool.release(localRect);
                }
                label185: Collections.sort(localList, Views.TOP_COMPARATOR);
                int j = ((View) localList.get(0)).getTop();
                int k = 2147483647;
                int m = 0;
                Iterator localIterator = localList.iterator();
                if (localIterator.hasNext()) {
                    View localView = (View) localIterator.next();
                    int n;
                    if (k != localView.getTop()) {
                        k = localView.getTop();
                        n = computOffset(k - j, paramInt1, paramInt2, paramFloat);
                        if (!paramBoolean)
                            break label318;
                    }
                    label318: for (m = n;; m = -n) {
                        localView.setTranslationX(m);
                        if (!(localView instanceof ViewGroup))
                            break;
                        translateView((ViewGroup) localView, paramInt1, paramInt2, paramFloat,
                                paramBoolean);
                        break;
                    }
                }
                if (localList != null)
                    sListPool.release(localList);
                if (localRect != null)
                    localPoolInstance = sRectPool;
            }
        }

        public void reset() {
            this.mAnimation.cancel();
        }

        public void scroll(float percent, boolean isLeft) {
            if (mRoot.getVisibility() == 0) {
                mAnimation.setNavigator(isLeft);
                mAnimation.scrollTo(percent);
            }
        }

        public void setStateListener(
                PageScrollEffect.OnStateChangeListener paramOnStateChangeListener) {
            this.mAnimation.setOnStateChangeListener(paramOnStateChangeListener);
        }
    }

    private static class PageScrolledDispatcher implements PageChangeAdapter.OnPageScrollListener {
        private final Factory<PageScrollEffect, Integer> mFactory;

        public PageScrolledDispatcher(Factory<PageScrollEffect, Integer> paramFactory) {
            this.mFactory = paramFactory;
        }

        private void onScrolled(ViewPager paramViewPager, int paramInt, float paramFloat,
                boolean paramBoolean) {
            PageScrollEffect localPageScrollEffect = (PageScrollEffect) this.mFactory
                    .create(Integer.valueOf(paramInt));
            if (localPageScrollEffect != null)
                localPageScrollEffect.scroll(paramFloat, paramBoolean);
        }

        public void onReset(ViewPager paramViewPager, int paramInt1, int paramInt2) {
            PageScrollEffect localPageScrollEffect = (PageScrollEffect) this.mFactory
                    .create(Integer.valueOf(paramInt2));
            if (localPageScrollEffect != null)
                localPageScrollEffect.reset();
        }

        public void onScroll(ViewPager paramViewPager, int paramInt1, int paramInt2,
                float paramFloat) {
            if (paramInt1 < paramInt2)
                ;
            for (boolean bool = true;; bool = false) {
                onScrolled(paramViewPager, paramInt2, paramFloat, bool);
                return;
            }
        }
    }

    private static class ScrollAnimation implements PageScrollEffects.TickerListener {
        private boolean mLeft = false;
        private PageScrollEffect.OnStateChangeListener mListener;
        private final ViewGroup mRoot;
        private final PageScrollEffects.SerialTicker mSerialTicker;
        private boolean mStarted = false;

        public ScrollAnimation(ViewGroup paramViewGroup) {
            this.mRoot = paramViewGroup;
            this.mSerialTicker = new PageScrollEffects.SerialTicker(100L, this);
        }

        public void cancel() {
            this.mSerialTicker.cancel();
            PageScrollEffects.DefaultPageScrollEffect.translateView(this.mRoot,
                    this.mRoot.getWidth(), this.mRoot.getHeight(), 1.0F, true);
            onComplete();
        }

        public void onAbort(float paramFloat) {
        }

        public void onComplete() {
            if (!this.mStarted)
                ;
            while (true) {
                return;
                this.mStarted = false;
                if (this.mListener != null)
                    this.mListener.onEffectFinished();
            }
        }

        public void onStart() {
            if (this.mStarted)
                ;
            while (true) {
                return;
                this.mStarted = true;
                if (this.mListener != null)
                    this.mListener.onEffectStarted();
            }
        }

        public void onUpdate(float paramFloat) {
            PageScrollEffects.DefaultPageScrollEffect.translateView(this.mRoot,
                    this.mRoot.getWidth(), this.mRoot.getHeight(), paramFloat, this.mLeft);
        }

        public void scrollTo(float paramFloat) {
            this.mSerialTicker.setPercent(paramFloat);
        }

        public void setNavigator(boolean paramBoolean) {
            if (this.mLeft != paramBoolean) {
                this.mLeft = paramBoolean;
                cancel();
            }
        }

        public void setOnStateChangeListener(
                PageScrollEffect.OnStateChangeListener paramOnStateChangeListener) {
            this.mListener = paramOnStateChangeListener;
        }
    }

    private static class SerialTicker implements PageScrollEffects.TickerListener {
        private final PageScrollEffects.TickerListener mListener;
        private float mPercentFrom;
        private float mPercentTo;
        private boolean mStarted = false;
        private final PageScrollEffects.Ticker mTicker;
        private float mUpdateLast = 3.4028235E+38F;

        public SerialTicker(long paramLong, PageScrollEffects.TickerListener paramTickerListener) {
            this.mListener = paramTickerListener;
            this.mTicker = new PageScrollEffects.Ticker(paramLong, this);
        }

        private void reset() {
            this.mPercentFrom = 0.0F;
            this.mPercentTo = 0.0F;
            this.mUpdateLast = 3.4028235E+38F;
            this.mStarted = false;
        }

        public void cancel() {
            reset();
            this.mTicker.abort();
        }

        public void onAbort(float paramFloat) {
            this.mPercentFrom = (paramFloat * (this.mPercentTo - this.mPercentFrom) + this.mPercentFrom);
        }

        public void onComplete() {
            if ((this.mPercentTo == 0.0F) || (this.mPercentTo == 1.0F)) {
                this.mListener.onComplete();
                reset();
            }
            while (true) {
                return;
                this.mPercentFrom = this.mPercentTo;
            }
        }

        public void onStart() {
            if ((!this.mStarted) && (this.mPercentFrom == 0.0F)) {
                this.mStarted = true;
                this.mListener.onStart();
            }
        }

        public void onUpdate(float paramFloat) {
            int i;
            if ((paramFloat == 1.0F) && ((this.mPercentTo == 1.0F) || (this.mPercentTo == 0.0F)))
                i = 1;
            for (float f = this.mPercentTo;; f = paramFloat * (this.mPercentTo - this.mPercentFrom)
                    + this.mPercentFrom) {
                if ((i != 0) || (Math.abs(f - this.mUpdateLast) > 0.001F)) {
                    this.mListener.onUpdate(f);
                    this.mUpdateLast = f;
                }
                return;
                i = 0;
            }
        }

        public void setPercent(float paramFloat) {
            if (this.mTicker.isStarted())
                this.mTicker.abort();
            this.mPercentTo = paramFloat;
            this.mTicker.start();
        }
    }

    private static class Ticker implements Runnable {
        private final long mDuration;
        private final Handler mHandler = new Handler();
        private final PageScrollEffects.TickerListener mListener;
        private long mTimeStart = -1L;

        Ticker(long paramLong, PageScrollEffects.TickerListener paramTickerListener) {
            this.mDuration = paramLong;
            this.mListener = paramTickerListener;
        }

        private float getPercent(long paramLong) {
            float f;
            if (this.mTimeStart < 0L)
                f = 0.0F;
            while (true) {
                return f;
                long l = paramLong - this.mTimeStart;
                if (l < this.mDuration)
                    f = (float) l / (float) this.mDuration;
                else
                    f = 1.0F;
            }
        }

        private void stop(boolean paramBoolean) {
            if (this.mTimeStart < 0L)
                ;
            while (true) {
                return;
                float f = getPercent(SystemClock.uptimeMillis());
                this.mListener.onUpdate(f);
                if (paramBoolean)
                    this.mListener.onAbort(f);
                this.mHandler.removeCallbacks(this);
                this.mTimeStart = -1L;
            }
        }

        private long tick() {
            long l1 = 0L;
            long l2 = SystemClock.uptimeMillis();
            float f = getPercent(l2);
            this.mListener.onUpdate(f);
            long l3 = this.mDuration - (l2 - this.mTimeStart);
            if (l3 > l1)
                l1 = Math.min(l3, 5L);
            return l1;
        }

        public void abort() {
            stop(true);
        }

        public boolean isStarted() {
            if (this.mTimeStart >= 0L)
                ;
            for (boolean bool = true;; bool = false)
                return bool;
        }

        public void run() {
            long l = tick();
            if (l > 0L)
                this.mHandler.postDelayed(this, l);
            while (true) {
                return;
                stop(false);
                this.mListener.onComplete();
            }
        }

        public void start() {
            if (isStarted())
                ;
            while (true) {
                return;
                this.mTimeStart = SystemClock.uptimeMillis();
                this.mListener.onStart();
                this.mListener.onUpdate(0.0F);
                this.mHandler.post(this);
            }
        }
    }

    private static abstract interface TickerListener {
        public abstract void onAbort(float paramFloat);

        public abstract void onComplete();

        public abstract void onStart();

        public abstract void onUpdate(float paramFloat);
    }
}
