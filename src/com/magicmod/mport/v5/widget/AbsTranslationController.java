
package com.magicmod.mport.v5.widget;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.view.VelocityTracker;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.animation.DecelerateInterpolator;

import java.lang.ref.WeakReference;

public abstract class AbsTranslationController implements MotionDetectListener, AnimatorListener {
    static final String TAG = AbsTranslationController.class.getName();
    public static final int TRANSLATE_STATE_FLING = 2;
    public static final int TRANSLATE_STATE_IDLE = 0;
    public static final int TRANSLATE_STATE_TOUCH = 1;
    private TranslateAnimationListener mAnimListener;
    private ValueAnimator mAnimator;
    private int mLastAnchorPostion;
    private final MotionDetectStrategy mListener;
    private final int mMaxAnchorDuration;
    protected final int mMaximumVelocity;
    protected final int mMinimumVelocity;
    private OnTranslateListener mTranslateListener;

    public AbsTranslationController(Context context,
            MotionDetectStrategy mml) {
        this.mListener = mml;
        ViewConfiguration configuration = ViewConfiguration.get(context);
        this.mMinimumVelocity = configuration.getScaledMinimumFlingVelocity();
        this.mMaximumVelocity = configuration.getScaledMaximumFlingVelocity();
        this.mMaxAnchorDuration = MiuiViewConfiguration.get(context).getMaxAnchorDuration();
    }

    /*private void fling(View paramView, int paramInt1, int paramInt2, int paramInt3,
            boolean paramBoolean1, boolean paramBoolean2) {
        if (this.mAnimator != null) {
            this.mAnimListener = null;
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        if (paramInt2 != 0) {
            if (paramBoolean1) {
                this.mAnimator = ObjectAnimator.ofFloat(new float[] {
                        0.0F, 1.0F
                });
                this.mAnimator.setInterpolator(new DecelerateInterpolator());
                this.mAnimator.setDuration(getDuration(paramInt2, paramInt3));
                this.mAnimator.addListener(this);
                this.mAnimListener = new TranslateAnimationListener(paramView, paramInt1,
                        paramInt2, paramBoolean2);
                this.mAnimator.addUpdateListener(this.mAnimListener);
                this.mAnimator.start();
            }
        } else {
            if ((this.mAnimator != null) || (!paramBoolean2))
                break label155;
            springBack(paramView);
        }
        while (true) {
            return;
            onTranslate(paramView, paramInt1 + paramInt2);
            break;
            label155: if (this.mAnimator != null)
                onTranslateStateChanged(2);
            else
                onTranslateStateChanged(0);
        }
    }*/
    private void fling(View view, int from, int delta, int velocity, boolean anim, boolean springBack) {
        if (mAnimator != null) {
            mAnimListener = null;
            mAnimator.cancel();
            mAnimator = null;
        }
        if (delta != 0) {
            if (anim) {
                mAnimator = ObjectAnimator.ofFloat(new float[] {
                        0.0F, 1.0F
                });
                mAnimator.setInterpolator(new DecelerateInterpolator());
                mAnimator.setDuration(getDuration(delta, velocity));
                mAnimator.addListener(this);
                mAnimListener = new TranslateAnimationListener(view, from, delta, springBack);
                mAnimator.addUpdateListener(mAnimListener);
                mAnimator.start();
            } else {
                onTranslate(view, from + delta);
            }
        }
        if (mAnimator == null && springBack) {
            springBack(view);
        } else if (mAnimator != null) {
            onTranslateStateChanged(2);
        } else {
            onTranslateStateChanged(0);
        }
    }
    
    /*private boolean springBack(View paramView) {
        int i = (int) paramView.getX();
        int j = (int) paramView.getY();
        int k = getAnchorPostion(paramView, i, j, i, j, 0);
        if (k != j)
            fling(paramView, j, k - j, 0, true, false);
        for (boolean bool = true;; bool = false)
            return bool;
    }*/
    private boolean springBack(View v) {
        int x = (int) v.getX();
        int y = (int) v.getY();
        int anchor = getAnchorPostion(v, x, y, x, y, 0);
        boolean flag;
        if (anchor != y) {
            fling(v, y, anchor - y, 0, true, false);
            flag = true;
        } else {
            flag = false;
        }
        return flag;
    }
    

    protected abstract int computVelocity(VelocityTracker paramVelocityTracker);

    protected void fling(View view, int x, int y, int startX,
            int startY, int velocity) {
        int from = (int) view.getY();
        fling(view, from,
                getAnchorPostion(view, x, y, startX, startY, velocity)
                        - from, velocity, true, false);
    }

    protected abstract int getAnchorPostion(View paramView, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4, int paramInt5);

    /*protected int getDuration(int paramInt1, int paramInt2) {
        int i = Math.abs(paramInt1);
        int j = (-1 + (1000 + Math.abs(paramInt2))) / 1000;
        if (j > 0)
            ;
        for (int k = Math.min(this.mMaxAnchorDuration, i * 2 / j);; k = this.mMaxAnchorDuration)
            return k;
    }*/
    protected int getDuration(int delta, int velocity) {
        int s = Math.abs(delta);
        int v = (-1 + (1000 + Math.abs(velocity))) / 1000;
        int i1;
        if (v > 0)
            i1 = Math.min(mMaxAnchorDuration, (s * 2) / v);
        else
            i1 = mMaxAnchorDuration;
        return i1;
    }
    

    protected abstract int getInertiaPosition(View paramView, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4, int paramInt5);

    public int getLastAnchorPosition() {
        return this.mLastAnchorPostion;
    }

    protected abstract int getValidMovePosition(View paramView, int paramInt1, int paramInt2,
            int paramInt3, int paramInt4);

    /*public boolean isAnimationPlaying() {
        if (this.mAnimator != null)
            ;
        for (boolean bool = true;; bool = false)
            return bool;
    }*/
    public boolean isAnimationPlaying() {
        boolean flag;
        if (mAnimator != null)
            flag = true;
        else
            flag = false;
        return flag;
    }

    /*public boolean isMovable(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        if (this.mListener != null)
            ;
        for (boolean bool = this.mListener.isMovable(paramView, paramInt1, paramInt2, paramInt3,
                paramInt4);; bool = true)
            return bool;
    }*/
    public boolean isMovable(View view, int x, int y, int startX, int startY)
    {
        boolean flag;
        if(mListener != null)
            flag = mListener.isMovable(view, x, y, startX, startY);
        else
            flag = true;
        return flag;
    }

    public boolean moveImmediately(View view, int x, int y) {
        return false;
    }

    public void onAnimationCancel(Animator animation) {
    }

    public void onAnimationEnd(Animator animation) {
        boolean anim = false;
        if ((this.mAnimListener != null) && (this.mAnimListener.needSpringBack())) {
            View v = this.mAnimListener.getView();
            if (v != null)
                anim = springBack(v);
        }
        if (!anim) {
            onTranslateStateChanged(0);
            this.mAnimator = null;
            this.mAnimListener = null;
        }
    }

    public void onAnimationRepeat(Animator animation) {
    }

    public void onAnimationStart(Animator animation) {
    }

    public boolean onMove(View view, int x, int y, int startX, int startY) {
        boolean movale = isMovable(view, x, y, startX, startY);
        if (movale)
            onTranslate(view,
                    getValidMovePosition(view, x, y, startX, startY));
        return movale;
    }

    public void onMoveCancel(View view, int x, int y) {
        fling(view, x, y, x, y, 0);
    }

    /*public void onMoveFinish(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, VelocityTracker paramVelocityTracker) {
        int i = computVelocity(paramVelocityTracker);
        this.mLastAnchorPostion = getAnchorPostion(paramView, paramInt1, paramInt2, paramInt3,
                paramInt4, i);
        if (Math.abs(i) > this.mMinimumVelocity) {
            int j = (int) paramView.getY();
            fling(paramView, j,
                    getInertiaPosition(paramView, paramInt1, paramInt2, paramInt3, paramInt4, i)
                            - j, i, true, true);
        }
        while (true) {
            return;
            fling(paramView, paramInt1, paramInt2, paramInt3, paramInt4, 0);
        }
    }*/
    public void onMoveFinish(View view, int x, int y, int startX, int startY, VelocityTracker tracker)
    {
        int velocity = computVelocity(tracker);
        mLastAnchorPostion = getAnchorPostion(view, x, y, startX, startY, velocity);
        if(Math.abs(velocity) > mMinimumVelocity)
        {
            int vy = (int)view.getY();
            fling(view, vy, getInertiaPosition(view, x, y, startX, startY, velocity) - vy, velocity, true, true);
        } else
        {
            fling(view, x, y, startX, startY, 0);
        }
    }

    public void onMoveStart(View view, int x, int y) {
        this.mLastAnchorPostion = 0x7fffffff;//2147483647;
        if (this.mAnimator != null) {
            this.mAnimListener = null;
            this.mAnimator.cancel();
            this.mAnimator = null;
        }
        onTranslateStateChanged(1);
    }

    protected void onTranslate(View v, float t) {
        translate(v, t);
        if (this.mTranslateListener != null)
            this.mTranslateListener.onTranslate(v, t);
    }

    protected void onTranslateStateChanged(int state) {
        if (this.mTranslateListener != null)
            this.mTranslateListener.onTranslateStateChanged(state);
    }

    public void setTranslateListener(OnTranslateListener l) {
        this.mTranslateListener = l;
    }

    protected abstract void translate(View paramView, float paramFloat);

    public static abstract interface OnTranslateListener {
        public abstract void onTranslate(View paramView, float paramFloat);

        public abstract void onTranslateStateChanged(int paramInt);
    }

    protected class TranslateAnimationListener implements ValueAnimator.AnimatorUpdateListener {
        private final int mDelta;
        private final int mFrom;
        private final boolean mSpringBack;
        private final WeakReference<View> mViewRef;

        /*public TranslateAnimationListener(View paramInt1, int paramInt2, int paramBoolean,
                boolean arg5) {
            this.mViewRef = new WeakReference(paramInt1);
            this.mFrom = paramInt2;
            this.mDelta = paramBoolean;
            boolean bool;
            this.mSpringBack = bool;
        }*/
        public TranslateAnimationListener(View view, int fromi, int delta, boolean springBack)
        {
            super();
            mViewRef = new WeakReference<View>(view);
            mFrom = fromi;
            mDelta = delta;
            mSpringBack = springBack;
        }

        public View getView() {
            return (View) this.mViewRef.get();
        }

        public boolean needSpringBack() {
            return this.mSpringBack;
        }

        public void onAnimationUpdate(ValueAnimator animation) {
            View v = (View) this.mViewRef.get();
            if (v != null) {
                float percent = ((Float) animation.getAnimatedValue()).floatValue();
                AbsTranslationController.this.onTranslate(v, this.mFrom + percent * this.mDelta);
            }
        }
    }
}
