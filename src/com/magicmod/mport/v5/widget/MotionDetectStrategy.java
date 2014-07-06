package com.magicmod.mport.v5.widget;

import android.view.View;

public abstract interface MotionDetectStrategy {
    public abstract boolean isMovable(View view, int x, int y, int startX,
            int startY);

    public static abstract interface Creator {
        public abstract MotionDetectStrategy createMotionDetectStrategy();
    }
}