
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.widget.FrameLayout;

public class VerticalMotionFrameLayout extends FrameLayout {
    private InertiaListener mInertiaListener;
    private final int mMaximumVelocity;
    private long mModifiedTime;
    private int mModifiedX;
    private int mModifiedY;
    private final VerticalMotionDetector mMotionDetector = new VerticalMotionDetector(this);
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;

    public VerticalMotionFrameLayout(Context context) {
        this(context, null);
    }

    public VerticalMotionFrameLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public VerticalMotionFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mTouchSlop = configuration.getScaledTouchSlop();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
    }

    private static int bound(int src, int min, int max) {
        int ret;
        if (src < min)
            ret = min;
        else if (src > max)
            ret = max;
        else
            ret = src;
        return ret;
    }

    private void initVelocityTracker() {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        else
            mVelocityTracker.clear();
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
        int i = 0;
        int j = (int) paramMotionEvent.getX();
        int k = (int) paramMotionEvent.getY();
        switch (0xFF & paramMotionEvent.getAction()) {
            default:
                if (i == 0)
                    break;
            case 0:
            case 2:
            case 1:
            case 3:
        }
        for (boolean bool = true;; bool = super.dispatchTouchEvent(paramMotionEvent)) {
            return bool;
            initVelocityTracker();
            VerticalMotionDetector.trackMovement(this.mVelocityTracker, paramMotionEvent);
            break;
            if ((this.mMotionDetector.isBeingDragged()) && (!this.mMotionDetector.isMovable(j, k))) {
                paramMotionEvent.setAction(3);
                super.dispatchTouchEvent(paramMotionEvent);
                paramMotionEvent.setAction(0);
                this.mModifiedX = j;
                this.mModifiedY = k;
                this.mModifiedTime = SystemClock.uptimeMillis();
            }
            VerticalMotionDetector.trackMovement(this.mVelocityTracker, paramMotionEvent);
            break;
            int i1;
            int i2;
            if (this.mModifiedTime != 0L)
                if ((Math.abs(k - this.mModifiedY) < this.mTouchSlop)
                        || (Math.abs(j - this.mModifiedX) < this.mTouchSlop)) {
                    if (this.mVelocityTracker == null)
                        break label364;
                    float f1 = (float) (SystemClock.uptimeMillis() - this.mModifiedTime) / 1000.0F;
                    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                    float f2 = this.mVelocityTracker.getXVelocity();
                    float f3 = this.mVelocityTracker.getYVelocity();
                    i1 = bound(j + (int) (f2 * f1), this.mLeft, this.mRight);
                    i2 = bound(k + (int) (f3 * f1), this.mTop, this.mBottom);
                    if ((Math.abs(i2 - this.mModifiedY) < this.mTouchSlop)
                            || (Math.abs(i1 - this.mModifiedX) < this.mTouchSlop))
                        paramMotionEvent.setAction(3);
                } else {
                    label323: this.mModifiedTime = 0L;
                }
            while (true) {
                recycleVelocityTracker();
                break;
                paramMotionEvent.setAction(2);
                paramMotionEvent.setLocation(i1, i2);
                super.dispatchTouchEvent(paramMotionEvent);
                paramMotionEvent.setAction(1);
                break label323;
                label364: paramMotionEvent.setAction(3);
                break label323;
                if ((this.mInertiaListener != null) && (this.mMotionDetector.isBeingDragged())
                        && (this.mVelocityTracker != null)) {
                    super.dispatchTouchEvent(paramMotionEvent);
                    i = 1;
                    this.mVelocityTracker.computeCurrentVelocity(1000, this.mMaximumVelocity);
                    int m = (int) this.mVelocityTracker.getYVelocity();
                    int n = this.mMotionDetector.getMotionStrategy().getLastAnchorPosition();
                    this.mInertiaListener.onInertiaMotion(m, n);
                }
            }
            this.mModifiedTime = 0L;
            recycleVelocityTracker();
            break;
        }
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        return this.mMotionDetector.onInterceptTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent ev) {
        return this.mMotionDetector.onTouchEvent(ev);
    }

    public void setInertiaListener(InertiaListener ev) {
        this.mInertiaListener = ev;
    }

    public void setMotionStrategy(MotionDetectListener l) {
        this.mMotionDetector.setMotionStrategy(l);
    }

    public static abstract interface InertiaListener {
        public abstract void onInertiaMotion(int paramInt1, int paramInt2);
    }
}
