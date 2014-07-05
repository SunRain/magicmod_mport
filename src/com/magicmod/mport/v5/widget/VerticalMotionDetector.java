
package com.magicmod.mport.v5.widget;

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
                //MotionDetectListener motiondetectlistener = mStrategy;
               // ViewGroup viewgroup = mView;
                //int k = mStartMotionX;
                //int l = mStartMotionY;
                //VelocityTracker velocitytracker;
                if (hasTracker)
                    //velocitytracker = mVelocityTracker;
                    mStrategy.onMoveFinish(mView, x, y, mStartMotionX, mStartMotionY, mVelocityTracker);
                else
                    //velocitytracker = null;
                    mStrategy.onMoveFinish(mView, x, y, mStartMotionX, mStartMotionY, null);
                //motiondetectlistener.onMoveFinish(viewgroup, x, y, k, l, velocitytracker);
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

    /*private void onSecondaryPointerUp(MotionEvent paramMotionEvent) {
        int i = (0xFF00 & paramMotionEvent.getAction()) >> 8;
        if (paramMotionEvent.getPointerId(i) == this.mActivePointerId)
            if (i != 0)
                break label79;
        label79: for (int j = 1;; j = 0) {
            int k = (int) paramMotionEvent.getY(j);
            int m = (int) paramMotionEvent.getX(j);
            this.mStartMotionY = k;
            this.mStartMotionX = m;
            this.mLastMotionY = k;
            this.mLastMotionX = m;
            this.mActivePointerId = paramMotionEvent.getPointerId(j);
            return;
        }
    }*/

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

    /*private void startDragging(int paramInt1, int paramInt2) {
        this.mStartMotionY = paramInt2;
        this.mStartMotionX = paramInt1;
        if (this.mIsBeingDragged)
            ;
        while (true) {
            return;
            this.mIsBeingDragged = true;
            if (this.mStrategy != null)
                this.mStrategy.onMoveStart(this.mView, paramInt1, paramInt2);
        }
    }*/
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

    public boolean onInterceptTouchEvent(MotionEvent paramMotionEvent) {
        //TODO: fix it
        
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
    }

    public boolean onTouchEvent(MotionEvent paramMotionEvent) {
      //TODO: fix it
        
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
    }

    public void setMotionStrategy(MotionDetectListener strategy) {
        this.mStrategy = strategy;
    }
}
