
package com.magicmod.mport.v5.widget;

import android.R.integer;
import android.util.Log;
import android.view.MotionEvent;
import android.view.VelocityTracker;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewParent;

public class VerticalMotionDetector {
    static final int INVALID_POINTER = -1;
    static final String TAG = VerticalMotionDetector.class.getName();
    private int mActivePointerId = -1;
    private boolean mIsBeingDragged = false;
    private int mLastMotionX = 0;
    private int mLastMotionY = 0;
    private int mStartMotionX = 0;
    private int mStartMotionY = 0;
    private MotionDetectListener mStrategy = null;
    private final int mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private final ViewGroup mView;

    public VerticalMotionDetector(ViewGroup view) {
        this.mView = view;
        this.mView.setFocusable(true);
        this.mView.setDescendantFocusability(0x40000);
        this.mView.setWillNotDraw(false);
        this.mTouchSlop = ViewConfiguration.get(this.mView.getContext()).getScaledTouchSlop();
    }

    private void cancelDragging(int x, int y) {
        if (mIsBeingDragged) {
            mIsBeingDragged = false;
            if (mStrategy != null)
                mStrategy.onMoveCancel(mView, x, y);
        }
    }

    private void clearVelocityTracker() {
        if (this.mVelocityTracker != null)
            this.mVelocityTracker.clear();
    }

    private void finishDragging(int x, int y, boolean hasTracker) {
        if (mIsBeingDragged) {
            mIsBeingDragged = false;
            if (mStrategy != null) {
                // MotionDetectListener motiondetectlistener = mStrategy;
                // ViewGroup viewgroup = mView;
                // int k = mStartMotionX;
                // int l = mStartMotionY;
                // VelocityTracker velocitytracker;
                if (hasTracker)
                    // velocitytracker = mVelocityTracker;
                    mStrategy.onMoveFinish(mView, x, y, mStartMotionX, mStartMotionY,
                            mVelocityTracker);
                else
                    // velocitytracker = null;
                    mStrategy.onMoveFinish(mView, x, y, mStartMotionX, mStartMotionY, null);
                // motiondetectlistener.onMoveFinish(viewgroup, x, y, k, l,
                // velocitytracker);
            }
        }
    }

    private void initOrResetVelocityTracker() {
        if (mVelocityTracker == null)
            mVelocityTracker = VelocityTracker.obtain();
        else
            mVelocityTracker.clear();
    }

    private void initVelocityTrackerIfNotExists() {
        if (this.mVelocityTracker == null)
            this.mVelocityTracker = VelocityTracker.obtain();
    }

    private void onSecondaryPointerDown(MotionEvent ev) {
        int index = ev.getActionIndex();
        int id = ev.getPointerId(index);
        int y = (int) ev.getY(index);
        int x = (int) ev.getX(index);
        this.mActivePointerId = id;
        this.mStartMotionY = y;
        this.mStartMotionX = x;
        this.mLastMotionY = y;
        this.mLastMotionX = x;
        clearVelocityTracker();
    }

    /*
     * private void onSecondaryPointerUp(MotionEvent paramMotionEvent) { int i =
     * (0xFF00 & paramMotionEvent.getAction()) >> 8; if
     * (paramMotionEvent.getPointerId(i) == this.mActivePointerId) if (i != 0)
     * break label79; label79: for (int j = 1;; j = 0) { int k = (int)
     * paramMotionEvent.getY(j); int m = (int) paramMotionEvent.getX(j);
     * this.mStartMotionY = k; this.mStartMotionX = m; this.mLastMotionY = k;
     * this.mLastMotionX = m; this.mActivePointerId =
     * paramMotionEvent.getPointerId(j); return; } }
     */

    private void onSecondaryPointerUp(MotionEvent ev) {
        int pointerIndex = (0xff00 & ev.getAction()) >> 8;
        if (ev.getPointerId(pointerIndex) == mActivePointerId) {
            int newPointerIndex;
            int y;
            int x;
            if (pointerIndex == 0)
                newPointerIndex = 1;
            else
                newPointerIndex = 0;
            y = (int) ev.getY(newPointerIndex);
            x = (int) ev.getX(newPointerIndex);
            mStartMotionY = y;
            mStartMotionX = x;
            mLastMotionY = y;
            mLastMotionX = x;
            mActivePointerId = ev.getPointerId(newPointerIndex);
        }
    }

    private void recycleVelocityTracker() {
        if (this.mVelocityTracker != null) {
            this.mVelocityTracker.recycle();
            this.mVelocityTracker = null;
        }
    }

    /*
     * private void startDragging(int paramInt1, int paramInt2) {
     * this.mStartMotionY = paramInt2; this.mStartMotionX = paramInt1; if
     * (this.mIsBeingDragged) ; while (true) { return; this.mIsBeingDragged =
     * true; if (this.mStrategy != null) this.mStrategy.onMoveStart(this.mView,
     * paramInt1, paramInt2); } }
     */
    private void startDragging(int x, int y) {
        mStartMotionY = y;
        mStartMotionX = x;
        if (!mIsBeingDragged) {
            mIsBeingDragged = true;
            if (mStrategy != null)
                mStrategy.onMoveStart(mView, x, y);
        }
    }

    public static void trackMovement(VelocityTracker tracker, MotionEvent event) {
        float deltaX = event.getRawX() - event.getX();
        float deltaY = event.getRawY() - event.getY();
        event.offsetLocation(deltaX, deltaY);
        tracker.addMovement(event);
        event.offsetLocation(-deltaX, -deltaY);
    }

    public MotionDetectListener getMotionStrategy() {
        return this.mStrategy;
    }

    public boolean isBeingDragged() {
        return this.mIsBeingDragged;
    }

    public boolean isMovable(int x, int y) {
        boolean flag;
        if (mStrategy != null)
            flag = mStrategy.isMovable(mView, x, y, mStartMotionX, mStartMotionY);
        else
            flag = true;
        return flag;
    }

    /*public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        // TODO: fix it

        boolean bool = false;
        MotionDetectListener localMotionDetectListener = this.mStrategy;
        if (localMotionDetectListener == null)
            ;
        int i;
        while (true) {
            return bool;
            i = paramMotionEvent.getAction();
            if ((i != 2) || (!this.mIsBeingDragged))
                break;
            bool = true;
        }
        switch (i & 0xFF) {
            case 4:
            case 5:
            default:
            case 2:
            case 0:
            case 1:
            case 3:
            case 6:
        }
        while (true) {
            bool = this.mIsBeingDragged;
            break;
            int i3 = this.mActivePointerId;
            if (i3 != -1) {
                int i4 = paramMotionEvent.findPointerIndex(i3);
                if (i4 < 0) {
                    Log.e(TAG, "Invalid pointerId=" + i3 + " in onInterceptTouchEvent");
                } else {
                    int i5 = (int) paramMotionEvent.getY(i4);
                    int i6 = (int) paramMotionEvent.getX(i4);
                    if (!localMotionDetectListener.isMovable(this.mView, i6, i5, this.mLastMotionX,
                            this.mLastMotionY)) {
                        clearVelocityTracker();
                    } else {
                        int i7 = Math.abs(i5 - this.mLastMotionY);
                        int i8 = Math.abs(i6 - this.mLastMotionX);
                        if ((i7 > this.mTouchSlop) && (i8 < i7)) {
                            initVelocityTrackerIfNotExists();
                            trackMovement(this.mVelocityTracker, paramMotionEvent);
                            startDragging(i6, i5);
                            this.mLastMotionY = i5;
                            this.mLastMotionX = i6;
                            ViewParent localViewParent = this.mView.getParent();
                            if (localViewParent != null) {
                                localViewParent.requestDisallowInterceptTouchEvent(true);
                                continue;
                                int i1 = (int) paramMotionEvent.getY();
                                int i2 = (int) paramMotionEvent.getX();
                                this.mLastMotionY = i1;
                                this.mLastMotionX = i2;
                                this.mActivePointerId = paramMotionEvent.getPointerId(0);
                                initOrResetVelocityTracker();
                                trackMovement(this.mVelocityTracker, paramMotionEvent);
                                if (localMotionDetectListener.moveImmediately(this.mView, i2, i1)) {
                                    startDragging(i2, i1);
                                } else {
                                    cancelDragging(i2, i1);
                                    continue;
                                    int k = paramMotionEvent
                                            .findPointerIndex(this.mActivePointerId);
                                    int m;
                                    int n;
                                    if (k < 0) {
                                        m = this.mLastMotionX;
                                        n = this.mLastMotionY;
                                        label412: if (i != 1)
                                            break label460;
                                        finishDragging(m, n, false);
                                    }
                                    while (true) {
                                        this.mActivePointerId = -1;
                                        recycleVelocityTracker();
                                        break;
                                        m = (int) paramMotionEvent.getX(k);
                                        n = (int) paramMotionEvent.getY(k);
                                        break label412;
                                        label460: cancelDragging(m, n);
                                    }
                                    onSecondaryPointerUp(paramMotionEvent);
                                    int j = paramMotionEvent
                                            .findPointerIndex(this.mActivePointerId);
                                    this.mLastMotionY = ((int) paramMotionEvent.getY(j));
                                    this.mLastMotionX = ((int) paramMotionEvent.getX(j));
                                }
                            }
                        }
                    }
                }
            }
        }
    }*/

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        // const/4 v5, -0x1
        // const/4 v12, 0x1
        // const/4 v1, 0x0
        MotionDetectListener strategy = mStrategy; // v0
        if (strategy == null) { // cond_0 in
            // :goto_0
            return false; // return v1
        } // cond_0 after

        // :cond_0
        int action = ev.getAction(); // v6
        // const/4 v4, 0x2
        if (action == 2 && mIsBeingDragged != false) { // cond_1 in
            return true;
        } // cond_1 after

        int x, y, pointerIndex;
        switch (action & 0xFF) {
            case 0: // pswitch_2
                y = (int) ev.getY(); // v3
                x = (int) ev.getX(); // v2
                mLastMotionY = y;
                mLastMotionX = x;
                mActivePointerId = ev.getPointerId(0);
                initOrResetVelocityTracker();
                trackMovement(mVelocityTracker, ev);
                if (strategy.moveImmediately(mView, x, y)) {
                    startDragging(x, y);
                } else {
                    cancelDragging(x, y);
                }
                // goto/16 :goto_1
                break;
            case 1: // pswitch_3
            case 3: // pswitch_3
                pointerIndex = ev.findPointerIndex(mActivePointerId); // v9

                if (pointerIndex < 0) { // :cond_6 in
                    x = mLastMotionX; // v2
                    y = mLastMotionY; // v3
                } else {
                    x = (int) ev.getX(pointerIndex); // v2
                    y = (int) ev.getY(pointerIndex); // v3
                }
                // :goto_2
                if (action == 1) { // cond_7 in
                    finishDragging(x, y, false);
                    // :goto_3
                    // mActivePointerId = -1;
                    // recycleVelocityTracker();
                    // goto/16 :goto_1
                } else {// cond_7 after
                    cancelDragging(x, y);
                }
                // goto :goto_3
                // :goto_3
                mActivePointerId = -1;
                recycleVelocityTracker();
                // goto/16 :goto_1
                // } //:cond_6 after
                // int x = (int) ev.getX(pointerIndex); //v2
                // int y = (int) ev.getY(pointerIndex); //v3
                // goto :goto_2
                break;
            case 2: // pswitch_1
                int activePointerId = mActivePointerId; // v7
                if (activePointerId != -1) { // cond_2 in
                    pointerIndex = ev.findPointerIndex(activePointerId); // v9
                    if (pointerIndex < 0) { // cond_3 in
                        Log.e(TAG,
                                (new StringBuilder()).append("Invalid pointerId=")
                                        .append(activePointerId)
                                        .append(" in onInterceptTouchEvent").toString());
                        // goto :goto_1
                    } else { // cond_3 after
                        y = (int) ev.getY(pointerIndex); // v3
                        x = (int) ev.getX(pointerIndex); // v2
                        if (!strategy.isMovable(mView, x, y, mLastMotionX, mLastMotionY)) { // cond_4
                                                                                            // in
                            clearVelocityTracker();
                            // goto :goto_1
                        } else {// cond_4 after
                            // :cond_4
                            int yDiff = Math.abs(y - mLastMotionY); // v11
                            int xDiff = Math.abs(x - mLastMotionX); // v10
                            if (yDiff > mTouchSlop && xDiff < yDiff) { // cond_2
                                                                       // in
                                initVelocityTrackerIfNotExists();
                                trackMovement(mVelocityTracker, ev);
                                startDragging(x, y);
                                mLastMotionY = y;
                                mLastMotionX = x;
                                ViewParent parent = mView.getParent(); // v8
                                if (parent != null) {
                                    parent.requestDisallowInterceptTouchEvent(true);
                                }
                            } // cond_2 after
                        }
                    }
                } // cond_2 after
                break;
            case 4: // pswitch_0
            case 5: // pswitch_0
                // v1 mIsBeingDragged
                // goto :goto_0
                //return mIsBeingDragged;
                break;
            case 6: // pswitch_4
                onSecondaryPointerUp(ev);
                pointerIndex = ev.findPointerIndex(0); // v9
                mLastMotionY = (int) ev.getY(pointerIndex);
                mLastMotionX = (int) ev.getX(pointerIndex);
                // goto/16 :goto_1
                break;
        // default:
        // break;
        }
        // :cond_2
        // :goto_1
        // :pswitch_0
        return mIsBeingDragged;
    }

    /*public boolean onTouchEvent(MotionEvent paramMotionEvent) {
        // TODO: fix it

        MotionDetectListener localMotionDetectListener = this.mStrategy;
        boolean bool;
        if (localMotionDetectListener == null) {
            bool = false;
            return bool;
        }
        initVelocityTrackerIfNotExists();
        trackMovement(this.mVelocityTracker, paramMotionEvent);
        switch (0xFF & paramMotionEvent.getAction()) {
            case 4:
            default:
            case 0:
            case 2:
            case 1:
            case 3:
            case 6:
            case 5:
        }
        while (true) {
            bool = true;
            break;
            int i8 = (int) paramMotionEvent.getY();
            int i9 = (int) paramMotionEvent.getX();
            this.mLastMotionY = i8;
            this.mLastMotionX = i9;
            this.mActivePointerId = paramMotionEvent.getPointerId(0);
            if (localMotionDetectListener.moveImmediately(this.mView, i9, i8)) {
                startDragging(i9, i8);
                ViewParent localViewParent2 = this.mView.getParent();
                if (localViewParent2 != null)
                    localViewParent2.requestDisallowInterceptTouchEvent(true);
            } else {
                cancelDragging(i9, i8);
                continue;
                int i2 = this.mActivePointerId;
                if (i2 != -1) {
                    int i3 = paramMotionEvent.findPointerIndex(i2);
                    if (i3 < 0) {
                        Log.e(TAG, "Invalid pointerId=" + i2 + " in onInterceptTouchEvent");
                    } else {
                        int i4 = (int) paramMotionEvent.getY(i3);
                        int i5 = (int) paramMotionEvent.getX(i3);
                        int i6 = this.mLastMotionY - i4;
                        int i7 = 0;
                        if ((!this.mIsBeingDragged) && (Math.abs(i6) > this.mTouchSlop)) {
                            startDragging(i5, i4);
                            ViewParent localViewParent1 = this.mView.getParent();
                            if (localViewParent1 != null)
                                localViewParent1.requestDisallowInterceptTouchEvent(true);
                            if (i6 <= 0)
                                break label382;
                        }
                        label382: for (i7 = -this.mTouchSlop; this.mIsBeingDragged; i7 = this.mTouchSlop) {
                            if (!localMotionDetectListener.onMove(this.mView, i5, i4 + i7,
                                    this.mStartMotionX, this.mStartMotionY))
                                clearVelocityTracker();
                            this.mLastMotionY = i4;
                            this.mLastMotionX = i5;
                            break;
                        }
                        if (this.mIsBeingDragged) {
                            int m = paramMotionEvent.findPointerIndex(this.mActivePointerId);
                            int n;
                            if (m < 0)
                                n = this.mLastMotionX;
                            for (int i1 = this.mLastMotionY;; i1 = (int) paramMotionEvent.getY(m)) {
                                finishDragging(n, i1, true);
                                this.mActivePointerId = -1;
                                recycleVelocityTracker();
                                break;
                                n = (int) paramMotionEvent.getX(m);
                            }
                            if (this.mIsBeingDragged) {
                                int i = paramMotionEvent.findPointerIndex(this.mActivePointerId);
                                int j;
                                if (i < 0)
                                    j = this.mLastMotionX;
                                for (int k = this.mLastMotionY;; k = (int) paramMotionEvent.getY(i)) {
                                    cancelDragging(j, k);
                                    this.mActivePointerId = -1;
                                    recycleVelocityTracker();
                                    break;
                                    j = (int) paramMotionEvent.getX(i);
                                }
                                onSecondaryPointerUp(paramMotionEvent);
                                continue;
                                onSecondaryPointerDown(paramMotionEvent);
                            }
                        }
                    }
                }
            }
        }
    }*/
    
    public boolean onTouchEvent(MotionEvent ev) {
        MotionDetectListener strategy = mStrategy; // v0
        if (strategy == null) { // cond_0 in
            // const/4 v1, 0x0
            // :goto_0
            return false; // return v1
        } // cond_0 after
        // :cond_0
        initVelocityTrackerIfNotExists();
        trackMovement(mVelocityTracker, ev);

        int action = ev.getAction(); // v6
        int x, y, pointerIndex;
        switch (0xFF & action) {
            case 0: // pswitch_1
                y = (int) ev.getY(); // v13
                x = (int) ev.getX(); // v2
                mLastMotionY = y;
                mLastMotionX = x;
                // const/4 v1, 0x0
                mActivePointerId = ev.getPointerId(0);
                if (strategy.moveImmediately(mView, x, y)) { // cond_2 in
                    startDragging(x, y);
                    ViewParent parent = mView.getParent(); // v11
                    if (parent != null) { // cond_1 in
                        // const/4 v1, 0x1
                        parent.requestDisallowInterceptTouchEvent(true);
                        // goto :goto_1
                    } // cond_1 after
                } else {// cond_2 after
                    // :cond_2
                    cancelDragging(x, y);
                    // goto :goto_1
                }
                // goto :goto_1
                break;
            case 1: // pswitch_3
                if (mIsBeingDragged) { // cond_1 in
                    pointerIndex = ev.findPointerIndex(mActivePointerId); // v12
                    if (pointerIndex < 0) { // cond_8 in
                        x = mLastMotionX; // v2
                        y = mLastMotionY; // v13
                    } else {
                        x = (int) ev.getX(pointerIndex); // v2
                        y = (int) ev.getY(pointerIndex); // v13
                    }
                    // :goto_3
                    // const/4 v1, 0x1
                    finishDragging(x, y, true);
                    // const/4 v1, -0x1
                    mActivePointerId = -1;
                    recycleVelocityTracker();
                    // goto/16 :goto_1
                    // } //cond_8 after
                    // x = (int) ev.getX(pointerIndex); //v2
                    // y = (int) ev.getY(pointerIndex); //v13
                    // goto :goto_3
                } // cond_1 after
                break;
            case 2: // pswitch_2
                int activePointerId = mActivePointerId; // v7
                if (activePointerId != -1) { // cond_1 in
                    int activePointerIndex = ev.findPointerIndex(activePointerId); // v8
                    if (activePointerIndex < 0) { // cond_3 in
                        Log.e(TAG,
                                (new StringBuilder()).append("Invalid pointerId=")
                                        .append(activePointerId)
                                        .append(" in onInterceptTouchEvent").toString());
                        // goto :goto_1
                    } else {// cond_3 after

                        // :cond_3
                        y = (int) ev.getY(activePointerIndex); // v13
                        x = (int) ev.getX(activePointerIndex); // v2

                        int deltaY = mLastMotionY - y; // v10
                        int adjust = 0x0; // v9
                        if (!mIsBeingDragged && Math.abs(deltaY) > mTouchSlop) { // cond_5
                                                                                 // in
                            startDragging(x, y);
                            ViewParent parent = mView.getParent(); // v11
                            if (parent != null) { // cond_4 in
                                parent.requestDisallowInterceptTouchEvent(true);
                            } // cond_4 after

                            if (deltaY > 0) { // cond_7 in
                                adjust = -mTouchSlop;
                            } else {
                                adjust = mTouchSlop;
                            }
                        }
                        // :cond_5
                        // :goto_2
                        if (mIsBeingDragged) { // cond_1 in
                            if (!strategy
                                    .onMove(mView, x, y + adjust, mStartMotionX, mStartMotionY)) { // cond_6
                                                                                                   // in
                                clearVelocityTracker();
                            } // cond_6 after
                              // :cond_6
                            mLastMotionY = y;
                            mLastMotionX = x;
                            // goto/16 :goto_1
                        } // cond_1 after
                        // } //cond_7 after
                        // :cond_7
                        // adjust = mTouchSlop;
                        // goto :goto_2
                    } // cond_5 after
                } // cond_1 after
                  // goto/16 :goto_1
                break;
            case 3: // pswitch_4
                if (mIsBeingDragged) { // cond_1 in
                    pointerIndex = ev.findPointerIndex(mActivePointerId); // v12
                    if (pointerIndex < 0) { // cond_9 in
                        x = mLastMotionX; // v2
                        y = mLastMotionY; // v13
                    } else {
                        x = (int) ev.getX(pointerIndex);
                        y = (int) ev.getY(pointerIndex);
                    }
                    // :goto_4
                    cancelDragging(x, y);
                    // const/4 v1, -0x1
                    mActivePointerId = -1;
                    recycleVelocityTracker();
                    // goto/16 :goto_1
                    // } //cond_9 after
                    // x = (int) ev.getX(pointerIndex);
                    // y = (int) ev.getY(pointerIndex);
                    // goto :goto_4

                } // cond_1 after
                break;
            case 4: // pswitch_0
                // const/4 v1, 0x1
                // goto :goto_0
                //return true;
                break;
            case 5: // pswitch_6
                onSecondaryPointerDown(ev);
                // goto/16 :goto_1
                break;
            case 6: // pswitch_5
                onSecondaryPointerUp(ev);
                // goto/16 :goto_1
                break;
            /*default:
                break;*/
        }
        // :cond_1
        // :goto_1
        // const/4 v1, 0x1
        // goto :goto_0 ==>: return v1
        return true;
    }

    public void setMotionStrategy(MotionDetectListener strategy) {
        this.mStrategy = strategy;
    }
}
