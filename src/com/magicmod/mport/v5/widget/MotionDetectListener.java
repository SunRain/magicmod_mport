
package com.magicmod.mport.v5.widget;

import android.view.VelocityTracker;
import android.view.View;

public abstract interface MotionDetectListener extends MotionDetectStrategy {
    public static final int INVALID_ANCHOR = 0x7fffffff;//2147483647;

    public abstract int getLastAnchorPosition();

    public abstract boolean moveImmediately(View paramView, int paramInt1, int paramInt2);

    public abstract boolean onMove(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4);

    public abstract void onMoveCancel(View paramView, int paramInt1, int paramInt2);

    public abstract void onMoveFinish(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, VelocityTracker paramVelocityTracker);

    public abstract void onMoveStart(View paramView, int paramInt1, int paramInt2);
}
