
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.os.SystemClock;
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

    /*public boolean dispatchTouchEvent(MotionEvent paramMotionEvent) {
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
    }*/

    public boolean dispatchTouchEvent(MotionEvent ev) {
        boolean handled = false; // v4
        int x = (int) ev.getX(); // v10
        int y = (int) ev.getY(); // v11

        int action = 0xff & ev.getAction(); // v1
        switch (action) {
            case 0: // pswitch_0
                initVelocityTracker();
                VerticalMotionDetector.trackMovement(mVelocityTracker, ev);
                // goto :goto_0
                break;
            case 1: // pswitch_2
                if (mModifiedTime != 0L) { // cond_7 in
                    if (Math.abs(y - mModifiedY) < mTouchSlop
                            || Math.abs(x - mModifiedX) < mTouchSlop) { // cond_1
                                                                        // in
                        // if (Math.abs(x - mModifiedX) < mTouchSlop) { //cond_3
                        // in
                        // :cond_1
                        if (mVelocityTracker != null) { // cond_6 in
                            float dur = (float) (SystemClock.uptimeMillis() - mModifiedTime) / 1000F; // v3
                            mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                            float vX = mVelocityTracker.getXVelocity(); // v7
                            float vY = mVelocityTracker.getYVelocity(); // v8
                            int newX = bound(x + (int) (vX * dur), mLeft, mRight); // local
                                                                                   // v5
                            int newY = bound(y + (int) (vY * dur), mTop, mBottom); // local
                                                                                   // v6
                            if (Math.abs(newY - mModifiedY) < mTouchSlop
                                    || Math.abs(newX - mModifiedX) < mTouchSlop) { // cond_2
                                                                                   // in
                                // if (Math.abs(newX - mModifiedX) < mTouchSlop)
                                // { //cond_5 in
                                // :cond_2
                                // v12 0x3
                                ev.setAction(0x3);
                                // :cond_3
                                // :goto_2
                                // mModifiedTime = 0L;
                                // :cond_4
                                // :goto_3
                                // recycleVelocityTracker();
                                // goto/16 :goto_0
                            } else {// cond_5 after
                                // :cond_5
                                ev.setAction(0x2);
                                ev.setLocation(newX, newY);
                                super.dispatchTouchEvent(ev);
                                ev.setAction(0x1);
                                // goto :goto_2
                            }
                            // } //cond_2 after
                        } else { // cond_6 after
                            // :cond_6
                            ev.setAction(0x3);
                            // goto :goto_2
                        }
                        // } //cond_3 after
                    } // cond_1 after
                    mModifiedTime = 0L;
                } else { // cond_7 after
                    if (mInertiaListener != null && mMotionDetector.isBeingDragged()
                            && mVelocityTracker != null) { // cond_4 in
                        super.dispatchTouchEvent(ev);
                        handled = true;
                        mVelocityTracker.computeCurrentVelocity(1000, mMaximumVelocity);
                        int vy = (int) mVelocityTracker.getYVelocity(); // v9
                        int anchor = mMotionDetector.getMotionStrategy().getLastAnchorPosition(); // v2
                        mInertiaListener.onInertiaMotion(vy, anchor);
                        // goto :goto_3
                    } // cond_4 after
                }
                recycleVelocityTracker();
                // goto/16 :goto_0
                break;
            case 2: // pswitch_1
                if (mMotionDetector.isBeingDragged() && !mMotionDetector.isMovable(x, y)) { // cond_0
                                                                                            // in
                    ev.setAction(0x3);
                    super.dispatchTouchEvent(ev);
                    ev.setAction(0);
                    mModifiedX = x;
                    mModifiedY = y;
                    mModifiedTime = SystemClock.uptimeMillis();
                } // cond_0 after
                VerticalMotionDetector.trackMovement(mVelocityTracker, ev);
                // goto :goto_0
                break;
            case 3:// pswitch_3
                mModifiedTime = 0L;
                recycleVelocityTracker();
                // goto/16 :goto_0
                break;
        }
        // :goto_0
        if (handled) { // cond_8 in
            // v12 0x1
            // :goto_1
            return true;
        } else { // cond_8 after
            return super.dispatchTouchEvent(ev);
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
