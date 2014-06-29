
package com.magicmod.mport.v5.widget;

import android.content.Context;
import android.content.res.Resources;
import android.util.SparseArray;

import com.miui.internal.R;

public class MiuiViewConfiguration {
    static final SparseArray<MiuiViewConfiguration> sConfigurations = new SparseArray<MiuiViewConfiguration>(2);
    private final int mFloatingViewOverDistance;
    private final int mFloatingViewTopHiddenSize;
    private final int mMaxAnchorDuration;
    private final int mMaxVisibleTabCount;
    private final int mMinAnchorVelocity;
    private final int mTranslateSlop;

    private MiuiViewConfiguration(Context context) {
        Resources res = context.getResources();
        this.mMinAnchorVelocity = res.getDimensionPixelSize(R.dimen.v5_min_anchor_velocity); //(101318698); 0x60a002a
        this.mTranslateSlop = res.getDimensionPixelSize(R.dimen.v5_translate_slop);//0x60a004c(101318732);
        this.mFloatingViewOverDistance = res.getDimensionPixelSize(R.dimen.v5_floating_view_over_distance);//0x60a0021(101318689);
        this.mFloatingViewTopHiddenSize = res.getDimensionPixelSize(R.dimen.v5_floating_view_top_hidden_size);//0x60a0022(101318690);
        this.mMaxAnchorDuration = res.getInteger(R.integer.v5_max_anchor_duration);//0x608000b(101187595);
        this.mMaxVisibleTabCount = res.getInteger(R.integer.v5_max_visible_tab_count);//0x608000c(101187596);
    }

    public static MiuiViewConfiguration get(Context context) {
        int i = (int) (100.0F * context.getResources().getDisplayMetrics().density);
        MiuiViewConfiguration configuration = (MiuiViewConfiguration) sConfigurations
                .get(i);
        if (configuration == null) {
            configuration = new MiuiViewConfiguration(context);
            sConfigurations.put(i, configuration);
        }
        return configuration;
    }

    public int getMaxAnchorDuration() {
        return this.mMaxAnchorDuration;
    }

    public int getMaxVisibleTabCount() {
        return this.mMaxVisibleTabCount;
    }

    public int getScaledFloatingViewHiddenSize() {
        return this.mFloatingViewTopHiddenSize;
    }

    public int getScaledFloatingViewOverDistance() {
        return this.mFloatingViewOverDistance;
    }

    public int getScaledMinAnchorVelocity() {
        return this.mMinAnchorVelocity;
    }

    public int getScaledTranslateSlop() {
        return this.mTranslateSlop;
    }
}
