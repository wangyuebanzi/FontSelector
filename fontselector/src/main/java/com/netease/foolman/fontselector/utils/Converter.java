package com.netease.foolman.fontselector.utils;

import android.content.Context;
import android.util.TypedValue;

/**
 * Created by wangyuebanzi on 2017/11/8.
 */

public class Converter {
    public static float dp2px(Context context,float dp){
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,dp,context.getResources().getDisplayMetrics());
    }
}
