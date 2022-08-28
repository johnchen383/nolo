package com.example.nolo.util;

import android.app.Activity;

import androidx.core.content.ContextCompat;

public class ColourUtil {
    public static  String getColourInHexFromResourceId(int rId, Activity activity) {
        return "#" + Integer.toHexString(ContextCompat.getColor(activity, rId) & 0x00ffffff);
    }
}
