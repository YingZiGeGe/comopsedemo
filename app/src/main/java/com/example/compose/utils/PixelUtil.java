package com.example.compose.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;

/**
 * Created by bigw on 23/08/2017.
 */

public class PixelUtil {

    public static int sp2px(Context context, float sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, context.getResources().getDisplayMetrics());
    }

    public static int dp2px(Context context, float dp) {
        return (int) dp2FloatPx(context, dp);
    }

    public static float dp2FloatPx(Context context, float dp) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, context.getResources().getDisplayMetrics());
    }

    public static float px2dp(Context context, float px) {
        Resources resources = context.getResources();
        if (resources == null) {
            return px;
        }
        return px / resources.getDisplayMetrics().density;
    }
}
