
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.view.VelocityTracker;
import android.view.View;

public class VerticalTranslationController extends AbsTranslationController {
    private static final float LOG_D = (float) Math.log(1.5D);
    private final int mMaxY;
    private final int mMaxYBounce;
    private final int mMinAnchorVelocity;
    private final int mMinY;
    private final int mMinYBounce;
    private int mStartY = 0;
    private final int mTranslateSlop;

    public VerticalTranslationController(Context context, MotionDetectStrategy mml, int minY,
            int maxY, int minYBounce, int maxYBounce) {
        super(context, mml);
        mStartY = 0;
        if (minYBounce <= minY && minY < maxY && maxY <= maxYBounce) {
            mMinY = minY;
            mMinYBounce = minYBounce;
            mMaxY = maxY;
            mMaxYBounce = maxYBounce;
            MiuiViewConfiguration config = MiuiViewConfiguration.get(context);
            mTranslateSlop = config.getScaledTranslateSlop();
            mMinAnchorVelocity = config.getScaledMinAnchorVelocity();
            //return;
        } else {
            StringBuilder stringbuilder = (new StringBuilder())
                    .append("minYBounce <= minY < maxY <= maxYBounce is necessary!");
            Object aobj[] = new Object[4];
            aobj[0] = Integer.valueOf(minYBounce);
            aobj[1] = Integer.valueOf(minY);
            aobj[2] = Integer.valueOf(maxY);
            aobj[3] = Integer.valueOf(maxYBounce);
            throw new IllegalArgumentException(stringbuilder.append(
                    String.format("%d %d %d %d", aobj)).toString());
        }
    }

    private int computDistance(int velocity, int start, int end) {
        int l;
        if (velocity == 0 || start == end)
            l = 0;
        else
            l = 10 * (int) (Math.log(velocity) / (double) LOG_D);
        return l;
    }

    
    @Override
    protected int computVelocity(VelocityTracker tracker) {
        int velocity;
        if (tracker != null) {
            tracker.computeCurrentVelocity(1000, super.mMaximumVelocity);
            velocity = (int) tracker.getYVelocity();
        } else {
            velocity = 0;
        }
        return velocity;
    }

    /*protected int getAnchorPostion(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, int paramInt5) {
        int i = 2147483647;
        int j = (int) paramView.getY();
        if (Math.abs(paramInt5) < this.mMinAnchorVelocity) {
            if ((j < this.mStartY) && (j < this.mMaxY - this.mTranslateSlop))
                i = this.mMinY;
        } else if (i == 2147483647) {
            int k = j + computDistance(paramInt5, this.mStartY, j);
            int m = this.mMinY - k;
            int n = this.mMaxY - k;
            if (Math.abs(m) >= Math.abs(n))
                break label148;
        }
        label148: for (i = this.mMinY;; i = this.mMaxY) {
            return i;
            if ((j <= this.mStartY) || (j <= this.mMinY + this.mTranslateSlop))
                break;
            i = this.mMaxY;
            break;
        }
    }*/
    @Override
    protected int getAnchorPostion(View v, int x, int y, int startX, int startY, int velocity) {
        int anchor = Integer.MAX_VALUE;// 0x7fffffff;
        int currentY = (int) v.getY();
        if (Math.abs(velocity) < mMinAnchorVelocity)
            if (currentY < mStartY && currentY < mMaxY - mTranslateSlop)
                anchor = mMinY;
            else if (currentY > mStartY && currentY > mMinY + mTranslateSlop)
                anchor = mMaxY;
        if (anchor == /* 0x7fffffff */Integer.MAX_VALUE) {
            int l1 = currentY + computDistance(velocity, mStartY, currentY);
            int deltaMinY = mMinY - l1;
            int deltaMaxY = mMaxY - l1;
            if (Math.abs(deltaMinY) < Math.abs(deltaMaxY))
                anchor = mMinY;
            else
                anchor = mMaxY;
        }
        return anchor;
    }

    /*protected int getInertiaPosition(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4, int paramInt5) {
        int i = (int) paramView.getY();
        int j = i + computDistance(paramInt5, this.mStartY, i);
        if (j < this.mMinYBounce)
            j = this.mMinYBounce;
        while (true) {
            return j;
            if (j > this.mMaxYBounce)
                j = this.mMaxYBounce;
        }
    }*/
    @Override
    protected int getInertiaPosition(View v, int x, int y, int startX, int startY, int velocity)
    {
        int dst;
        int currentY = (int)v.getY();
        dst = currentY + computDistance(velocity, mStartY, currentY);
        if(dst >= mMinYBounce) {
            if(dst > mMaxYBounce)
                dst = mMaxYBounce;
        } else {
            dst = mMinYBounce;
        }
        return dst;    
    }

    protected int getValidMovePosition(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        int i = (int) (paramView.getTranslationY() + paramInt2 - paramInt4);
        int j;
        if (i < this.mMinYBounce)
            j = this.mMinYBounce;
        while (true) {
            return j;
            if (i > this.mMaxYBounce)
                j = this.mMaxYBounce;
            else
                j = i;
        }
    }

    public void onMoveStart(View paramView, int paramInt1, int paramInt2) {
        super.onMoveStart(paramView, paramInt1, paramInt2);
        this.mStartY = ((int) paramView.getY());
    }

    protected void translate(View paramView, float paramFloat) {
        paramView.setTranslationY(paramFloat);
    }

    @Override
    protected int getValidMovePosition(View paramView, int paramInt1, int paramInt2, int paramInt3,
            int paramInt4) {
        // TODO Auto-generated method stub
        return 0;
    }

    @Override
    protected void translate(View paramView, float paramFloat) {
        // TODO Auto-generated method stub
        
    }
}
