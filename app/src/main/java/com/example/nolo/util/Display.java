package com.example.nolo.util;

import android.app.Activity;
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
     * Get height of device screen
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
    //TODO: may not be needed (delete if not!!)
    public static int getScreenWidth(View v){
        return getDisplayMetrics(v).widthPixels;
    }
}
