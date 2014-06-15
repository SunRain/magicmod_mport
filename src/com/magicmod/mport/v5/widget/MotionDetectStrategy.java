package com.magicmod.mport.v5.widget;

import android.view.View;

public abstract interface MotionDetectStrategy {
    public abstract boolean isMovable(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4);

    public static abstract interface Creator {
        public abstract MotionDetectStrategy createMotionDetectStrategy();
    }
}