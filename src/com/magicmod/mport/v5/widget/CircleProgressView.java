
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.miui.internal.R;

public class CircleProgressView extends View {
    private static int MAX_PROGRESS = 100;
    private static String TAG = "CircleProgressView";
    private int mAngle;
    private RectF mArcRect;
    private int mCurProgress;
    private Bitmap mFgBitmap;
    private BitmapDrawable mForeground;
    private int mHeight;
    private int mMaxProgress;// = MAX_PROGRESS;
    private Bitmap mMemBitmap;
    private Canvas mMemCanvas;
    private Paint mPaint;
    private int mWidth;

    public CircleProgressView(Context context) {
        super(context);
        mMaxProgress = MAX_PROGRESS;
        init(context, null);
    }

    public CircleProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mMaxProgress = MAX_PROGRESS;
        init(context, attrs);
    }

    public CircleProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mMaxProgress = MAX_PROGRESS;
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs,
                    R.styleable.CircleProgressView);
            Resources res = context.getResources();
            this.mForeground = ((BitmapDrawable) a.getDrawable(3));
            if (this.mForeground == null)
                this.mForeground = ((BitmapDrawable) res.getDrawable(100794684));
            this.mFgBitmap = this.mForeground.getBitmap();
            this.mWidth = this.mFgBitmap.getWidth();
            this.mHeight = this.mFgBitmap.getHeight();
            this.mPaint = new Paint();
            this.mPaint.setStyle(Paint.Style.FILL);
            this.mPaint.setAntiAlias(true);
            this.mPaint.setColor(0);
            this.mPaint.setXfermode(new PorterDuffXfermode(/*PorterDuff.Mode.CLEAR*/PorterDuff.Mode.CLEAR));
            this.mMemBitmap = Bitmap.createBitmap(this.mWidth, this.mHeight,
                    Bitmap.Config.ARGB_8888);
            this.mMemCanvas = new Canvas(this.mMemBitmap);
            this.mArcRect = new RectF(0.0F, 0.0F, this.mWidth, this.mHeight);
            a.recycle();
        }
    }

    public int getMaxProgress() {
        return this.mMaxProgress;
    }

    public int getProgress() {
        return this.mCurProgress;
    }

    protected void onDraw(Canvas canvas) {
        if (this.mForeground != null) {
            this.mMemBitmap.eraseColor(0);
            this.mMemCanvas.drawBitmap(this.mFgBitmap, 0.0F, 0.0F, null);
            this.mMemCanvas.drawArc(this.mArcRect, 270 - this.mAngle, this.mAngle, true,
                    this.mPaint);
            canvas.drawBitmap(this.mMemBitmap, 0.0F, 0.0F, null);
        }
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        setMeasuredDimension(this.mWidth, this.mHeight);
    }

    public void setMaxProgress(int maxProgress) {
        if ((maxProgress > 0) && (this.mMaxProgress != maxProgress)) {
            this.mMaxProgress = maxProgress;
            setProgress(this.mCurProgress);
        }
    }

    /*public void setProgress(int paramInt) {
        this.mCurProgress = paramInt;
        if (this.mCurProgress > this.mMaxProgress)
            this.mCurProgress = this.mMaxProgress;
        while (true) {
            this.mCurProgress = (this.mMaxProgress - this.mCurProgress);
            int i = 360 * this.mCurProgress / this.mMaxProgress;
            if (i != this.mAngle) {
                Log.i(TAG, "progress:" + this.mCurProgress);
                this.mAngle = i;
                invalidate();
            }
            return;
            if (this.mCurProgress < 0)
                this.mCurProgress = 0;
        }
    }*/
    public void setProgress(int progress) {
        mCurProgress = progress;
        if (mCurProgress <= mMaxProgress) { // goto _L2
            if (mCurProgress < 0) {
                mCurProgress = 0;
            }
            // if(true) goto _L4; else goto _L3
            mCurProgress = mMaxProgress - mCurProgress;
            int angle = (360 * mCurProgress) / mMaxProgress;
            if (angle != mAngle) {
                Log.i(TAG, (new StringBuilder()).append("progress:").append(mCurProgress)
                        .toString());
                mAngle = angle;
                invalidate();
            }
            return;
        } else { // goto _L1
            mCurProgress = mMaxProgress;
        }
/*            goto _L2; else goto _L1
_L1:
        mCurProgress = mMaxProgress;
_L4:
        mCurProgress = mMaxProgress - mCurProgress;
        int j = (360 * mCurProgress) / mMaxProgress;
        if(j != mAngle)
        {
            Log.i(TAG, (new StringBuilder()).append("progress:").append(mCurProgress).toString());
            mAngle = j;
            invalidate();
        }
        return;
_L2:
        if(mCurProgress < 0)
            mCurProgress = 0;
        if(true) goto _L4; else goto _L3
_L3:*/
    }
}
