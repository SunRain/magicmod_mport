package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.magicmod.mport.v5.view.ViewPager;

public class CooperativeViewPager extends ViewPager {
    private boolean mDragEnabled = true;

    public CooperativeViewPager(Context context) {
        super(context);
    }

    public CooperativeViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public boolean onInterceptTouchEvent(MotionEvent ev) {
        boolean flag;
        if (!mDragEnabled)
            flag = false;
        else
            flag = super.onInterceptTouchEvent(ev);
        return flag;
    }

    public void setDraggable(boolean enabled) {
        this.mDragEnabled = enabled;
    }
}
