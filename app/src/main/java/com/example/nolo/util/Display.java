package com.example.nolo.util;

import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;

public class Display {
    /**
     * Retrieve display metrics
     * @param v any view associated with an activity (i.e., displayed)
     * @return
     */
    private static DisplayMetrics getDisplayMetrics(View v){
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) v.getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics;
    }

    /**
     * Get height of device screen in pixels
     * @param v any view associated with an activity (i.e., displayed)
     * @return
     */
    public static int getScreenHeight(View v){
        return getDisplayMetrics(v).heightPixels;
    }

    /**
     * Get width of device screen
     * @param v any view associated with an activity (i.e., displayed)
     * @return
     */
    public static int getScreenWidth(View v){
        return getDisplayMetrics(v).widthPixels;
    }

    /**
     * Convert dp into pixels
     *
     * @param dp
     * @param context
     * @return pixels
     */
    public static int dpToPx(int dp, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) dp * density);
    }

    /**
     * Calculate dynamic height
     *
     * @param y1 Height 1
     * @param y2 Height 2
     * @return Dynamic height
     */
    public static double getDynamicHeight(View v, double y1, double y2) {
        double currentHeight = getScreenHeight(v);
        double x1 = 1794.0;
        double x2 = 2274.0;
        double m = (y2 - y1)/(x2 - x1);
        double c = y1 - (m * x1);
        return m * currentHeight + c;
    }
}
