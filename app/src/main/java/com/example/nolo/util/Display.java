package com.example.nolo.util;

import android.app.Activity;
import android.util.DisplayMetrics;
import android.view.View;

public class Display {
    private static DisplayMetrics getDisplayMetrics(View v){
        DisplayMetrics displayMetrics = new DisplayMetrics();

        ((Activity) v.getContext()).getWindowManager()
                .getDefaultDisplay()
                .getMetrics(displayMetrics);

        return displayMetrics;
    }

    public static int getScreenHeight(View v){
        return getDisplayMetrics(v).heightPixels;
    }

    public static int getScreenWidth(View v){
        return getDisplayMetrics(v).widthPixels;
    }
}
