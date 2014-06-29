package com.magicmod.mport.util;

import android.content.Context;
import android.util.TypedValue;

public class ResourceMapper {

    public ResourceMapper() {
    }

    public static int resolveReference(Context context, int id) {
        TypedValue outValue = new TypedValue();
        context.getResources().getValue(id, outValue, true);
        if (outValue.resourceId != 0)
            id = outValue.resourceId;
        return id;
    }
}
