package com.magicmod.mport.v5.android.support.view;

import android.widget.EdgeEffect;

public class EdgeEffectWrapper {
    
    public static boolean onRelease(Object edgeEffect) {
        EdgeEffect eff = (EdgeEffect) edgeEffect;
        eff.onRelease();
        return eff.isFinished();
    }
    
    public static boolean onPull(Object edgeEffect, float deltaDistance) {
        ((EdgeEffect) edgeEffect).onPull(deltaDistance);
        return true;
    }
}
