
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.miui.internal.R;

public class NumberProgressView extends LinearLayout {
    private static final int MAX_PROGRESS = 100;
    private int mCurProgress = -1;
    private ImageView mNumberProgress1;
    private ImageView mNumberProgress2;
    private ImageView mNumberProgress3;
    private ImageView mPercent;
    private Drawable[] mResNumber = new Drawable[10];
    private Drawable mResPercent = null;

    public NumberProgressView(Context context) {
        super(context);
        init(context, null);
    }

    public NumberProgressView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public NumberProgressView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    /*private void init(Context context, AttributeSet attrs) {
        for (int i = 0; i < this.mResNumber.length; i++)
            this.mResNumber[i] = null;
        if (attrs != null) {
            TypedArray localTypedArray1 = context.obtainStyledAttributes(attrs,
                    R.styleable.NumberProgressView);
            int k = localTypedArray1.getResourceId(1, -1);
            if (-1 != k) {
                TypedArray localTypedArray2 = localTypedArray1.getResources().obtainTypedArray(k);
                if (localTypedArray2.length() == this.mResNumber.length)
                    for (int m = 0; m < this.mResNumber.length; m++)
                        this.mResNumber[m] = localTypedArray2.getDrawable(m);
            }
            this.mResPercent = localTypedArray1.getDrawable(0);
            localTypedArray1.recycle();
        }
        if (this.mResPercent == null)
            this.mResPercent = context.getResources().getDrawable(100794852);
        if (this.mResNumber[0] == null) {
            Resources localResources = context.getResources();
            for (int j = 0; j < this.mResNumber.length; j++)
                this.mResNumber[j] = localResources.getDrawable(100794842 + j);
        }
        ((LayoutInflater) context.getSystemService("layout_inflater"))
                .inflate(100859956, this);
        this.mNumberProgress1 = ((ImageView) findViewById(101384358));
        this.mNumberProgress2 = ((ImageView) findViewById(101384359));
        this.mNumberProgress3 = ((ImageView) findViewById(101384360));
        this.mPercent = ((ImageView) findViewById(101384361));
        if (this.mResPercent != null)
            this.mPercent.setBackground(this.mResPercent);
    }*/
    private void init(Context context, AttributeSet attrs) {
        for (int i = 0; i < mResNumber.length; i++)
            mResNumber[i] = null;

        if (attrs != null) {
            TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.NumberProgressView);
            int arrId = a.getResourceId(1, -1);
            if (-1 != arrId) {
                TypedArray t = a.getResources().obtainTypedArray(arrId);
                if (t.length() == mResNumber.length) {
                    for (int l = 0; l < mResNumber.length; l++) {
                        mResNumber[l] = t.getDrawable(l);
                    }
                }
            }
            mResPercent = a.getDrawable(0);
            a.recycle();
        }
        if (mResPercent == null)
            mResPercent = context.getResources().getDrawable(
                    /* 0x60201e4 */R.drawable.v5_number_progress_view_percent);
        if (mResNumber[0] == null) {
            Resources resources = context.getResources();
            for (int j = 0; j < mResNumber.length; j++)
                mResNumber[j] = resources
                        .getDrawable(/* 0x60201da */R.drawable.v5_number_progress_view_0 + j);

        }
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(
                /* 0x6030034 */R.layout.v5_number_progress, this);
        mNumberProgress1 = (ImageView) findViewById(/* 0x60b00a6 */R.id.v5_number_progress_1);
        mNumberProgress2 = (ImageView) findViewById(/* 0x60b00a7 */R.id.v5_number_progress_2);
        mNumberProgress3 = (ImageView) findViewById(/* 0x60b00a8 */R.id.v5_number_progress_3);
        mPercent = (ImageView) findViewById(/* 0x60b00a9 */R.id.v5_number_progress_percent);
        if (mResPercent != null)
            mPercent.setBackground(mResPercent);
    }

    private void setNumber(int number, ImageView tv) {
        if (mResNumber[number] != null)
            tv.setImageDrawable(mResNumber[number]);
    }

    /*public boolean setProgress(int paramInt) {
        boolean bool = false;
        if ((paramInt >= 0) && (paramInt <= 100) && (paramInt != this.mCurProgress)) {
            this.mCurProgress = paramInt;
            if (paramInt <= 99)
                break label109;
            this.mNumberProgress3.setVisibility(0);
            setNumber(paramInt / 100, this.mNumberProgress3);
            paramInt %= 100;
            if ((paramInt <= 9) && (this.mCurProgress <= 99))
                break label121;
            this.mNumberProgress2.setVisibility(0);
            setNumber(paramInt / 10, this.mNumberProgress2);
            paramInt %= 10;
        }
        while (true) {
            setNumber(paramInt, this.mNumberProgress1);
            bool = true;
            return bool;
            label109: this.mNumberProgress3.setVisibility(8);
            break;
            label121: this.mNumberProgress2.setVisibility(8);
        }
    }*/
    public boolean setProgress(int progress) {
        boolean flag = false;
        if (progress >= 0 && progress <= 100 && progress != mCurProgress) {
            mCurProgress = progress;
            if (progress > 99) {
                mNumberProgress3.setVisibility(0);
                setNumber(progress / 100, mNumberProgress3);
                progress %= 100;
            } else {
                mNumberProgress3.setVisibility(8);
            }
            if (progress > 9 || mCurProgress > 99) {
                mNumberProgress2.setVisibility(0);
                setNumber(progress / 10, mNumberProgress2);
                progress %= 10;
            } else {
                mNumberProgress2.setVisibility(8);
            }
            setNumber(progress, mNumberProgress1);
            flag = true;
        }
        return flag;
    }
}
