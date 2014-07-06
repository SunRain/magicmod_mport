
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;

public class TranslateIndicator extends ImageView implements TabIndicator {
    private TabContainerLayout mTabContainer;
    private int mTabWidth;

    public TranslateIndicator(Context context, int defStyle) {
        super(context, null, defStyle);
        setScaleType(ImageView.ScaleType.CENTER);
    }

    public float apply(int position, float percent) {
        if ((ViewGroup.MarginLayoutParams) getLayoutParams() != null)
            setTranslationX(Math.round((percent + (float) position) * (float) mTabWidth));
        return 0.0F;
    }

    public void attach(TabContainerLayout tabContainer) {
        this.mTabContainer = tabContainer;
        tabContainer.getIndicatorContainer().addView(this);
        setTabWidth(tabContainer.getWidth());
    }

    public void detach() {
        ViewParent parent = getParent();
        if ((parent instanceof ViewGroup))
            ((ViewGroup) parent).removeView(this);
    }

    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(
                View.MeasureSpec.makeMeasureSpec(this.mTabContainer.getTabWidth(), 0x40000000),
                heightMeasureSpec);
    }

    @Override
    public void setIndicator(Drawable indicator) {
        setImageDrawable(indicator);
    }

    public void setTabWidth(int width) {
        this.mTabWidth = width;
        FrameLayout.LayoutParams lp = (FrameLayout.LayoutParams) getLayoutParams();
        lp.height = -2;
        lp.width = width;
        lp.leftMargin = this.mTabContainer.getOffsetX();
        lp.gravity = 0x53;//83;
        setLayoutParams(lp);
    }
}
