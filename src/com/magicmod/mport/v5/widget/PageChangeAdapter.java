
package com.magicmod.mport.v5.widget;

import com.magicmod.mport.v5.android.support.view.ViewPager;

public class PageChangeAdapter implements ViewPager.OnPageChangeListener {
    private int mAppearingPosition = -1;
    private int mChangeStartPosition = -1;
    private int mLastPosition = -1;
    private OnPageScrollListener mScrolledListener;
    private int mSrollState = 0;
    private final ViewPager mViewPager;

    public PageChangeAdapter(ViewPager pager) {
        this(pager, null);
    }

    public PageChangeAdapter(ViewPager pager, OnPageScrollListener scrolledListener) {
        this.mViewPager = pager;
        this.mScrolledListener = scrolledListener;
    }

    /*public void onPageScrollStateChanged(int paramInt) {
        this.mSrollState = paramInt;
        if (paramInt == 1) {
            int i = this.mViewPager.getCurrentItem();
            if (this.mChangeStartPosition != i) {
                if (this.mAppearingPosition != -1) {
                    if (this.mScrolledListener != null)
                        this.mScrolledListener.onReset(this.mViewPager, this.mChangeStartPosition,
                                this.mAppearingPosition);
                    this.mAppearingPosition = -1;
                }
                this.mChangeStartPosition = i;
            }
        }
        while (true) {
            return;
            if (paramInt == 0) {
                if (this.mScrolledListener != null)
                    this.mScrolledListener.onReset(this.mViewPager, this.mChangeStartPosition,
                            this.mViewPager.getCurrentItem());
                this.mChangeStartPosition = -1;
                this.mAppearingPosition = -1;
            }
        }
    }*/
    public void onPageScrollStateChanged(int state) {
        mSrollState = state;
        if (state != 1) {
            if (state == 0) {
                if (mScrolledListener != null)
                    mScrolledListener.onReset(mViewPager, mChangeStartPosition,
                            mViewPager.getCurrentItem());
                mChangeStartPosition = -1;
                mAppearingPosition = -1;
            }
        } else {
            int position = mViewPager.getCurrentItem();
            if (mChangeStartPosition != position) {
                if (mAppearingPosition != -1) {
                    if (mScrolledListener != null)
                        mScrolledListener.onReset(mViewPager, mChangeStartPosition,
                                mAppearingPosition);
                    mAppearingPosition = -1;
                }
                mChangeStartPosition = position;
            }
        }
    }

    /*public void onPageScrolled(int paramInt1, float paramFloat, int paramInt2) {
        if (this.mScrolledListener == null)
            ;
        label126: while (true) {
            return;
            int i = this.mChangeStartPosition;
            if (i >= 0) {
                int j = i;
                float f = 1.0F;
                if (i == paramInt1) {
                    j = i + 1;
                    f = paramFloat;
                }
                while (true) {
                    if (i == j)
                        break label126;
                    this.mAppearingPosition = j;
                    this.mScrolledListener.onScroll(this.mViewPager, i, this.mAppearingPosition, f);
                    break;
                    if ((i + 1 == paramInt1) && (paramFloat == 0.0F)) {
                        j = paramInt1;
                        f = 1.0F;
                    } else if ((i - 1 == paramInt1) && (i > 0)) {
                        j = i - 1;
                        f = 1.0F - paramFloat;
                    }
                }
            }
        }
    }*/
    /*public void onPageScrolled(int i, float f, int j)
    {
        if(mScrolledListener != null) goto _L2; else goto _L1
_L1:
        return;
_L2:
        int k;
        int l;
        float f1;
        k = mChangeStartPosition;
        if(k < 0)
            continue; //* Loop/switch isn't completed 
        l = k;
        f1 = 1.0F;
        if(k != i)
            break; //* Loop/switch isn't completed 
        l = k + 1;
        f1 = f;
_L4:
        if(k != l)
        {
            mAppearingPosition = l;
            mScrolledListener.onScroll(mViewPager, k, mAppearingPosition, f1);
        }
        if(true) goto _L1; else goto _L3
_L3:
        if(k + 1 == i && f == 0.0F)
        {
            l = i;
            f1 = 1.0F;
        } else
        if(k - 1 == i && k > 0)
        {
            l = k - 1;
            f1 = 1.0F - f;
        }
          goto _L4
        if(true) goto _L1; else goto _L5
_L5:
    }*/
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
        if (mScrolledListener == null) { // cond_1 in
            // :cond_0
            // :goto_0
            return;
        } else { // cond_1 after
            int from = mChangeStartPosition; // v0
            if (from >= 0) { // cond_0 in
                int to = from; // v2
                Float percent = 1.0F; // local v1

                if (from == position) { // cond_3 in
                    to = from + 1;
                    percent = positionOffset;
                } else if ((from + 1 == position) && (positionOffset == 0.0F)) { // cond_4
                    // in
                    to = position;
                    percent = 1.0F;
                    // goto :goto_1
                } else if ((from - 1 == position) && (from > 0)) {
                    to = from - 1;
                    percent = 1.0F - positionOffset;
                    // goto :goto_1
                }
                // :cond_2
                // :goto_1
                if (from != to) { // cond_0 in
                    mAppearingPosition = to;
                    mScrolledListener.onScroll(mViewPager, from, mAppearingPosition, percent);
                    return; // goto :goto_0
                } // cond_0 after
                return;

                //} // cond_3 after
                  // v3 = from + 1;
                /*if ((from + 1 == position) && (positionOffset == 0.0F)) { // cond_4
                                                                          // in
                    to = position;
                    percent = 1.0F;
                    // goto :goto_1
                }*/ // cond_4 after
                  // v3 = from -1 ;
                /*if ((from - 1 == position) && (from > 0)) {
                    to = from - 1;
                    percent = 1.0F - positionOffset;
                    // goto :goto_1
                }*/
            } else {// cond_0 after
                return;
            }
        }
    }

    public void onPageSelected(int position) {
        if ((this.mScrolledListener != null) && (this.mSrollState == 0))
            this.mScrolledListener.onReset(this.mViewPager, this.mLastPosition, position);
        this.mLastPosition = position;
    }

    public void setScrolledListener(OnPageScrollListener scrolledListener) {
        this.mScrolledListener = scrolledListener;
    }

    public static abstract interface OnPageScrollListener {
        public abstract void onReset(ViewPager paramViewPager, int paramInt1, int paramInt2);

        public abstract void onScroll(ViewPager paramViewPager, int paramInt1, int paramInt2,
                float paramFloat);
    }
}
