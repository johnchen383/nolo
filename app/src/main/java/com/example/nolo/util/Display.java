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
     * Get height of device screen in dp
     * @param v any view associated with an activity (i.e., displayed)
     * @return
     */
    public static int getScreenHeightInDp(View v){
        return pxToDp(getScreenHeight(v), v.getContext());
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
     * Convert pixels into dp
     *
     * @param px
     * @param context
     * @return pixels
     */
    public static int pxToDp(int px, Context context) {
        float density = context.getResources().getDisplayMetrics().density;
        return Math.round((float) px / density);
    }
}
