package com.example.nolo.util;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

public class Keyboard {
    /**
     * Show the keyboard for the current activity
     *
     * @param activity current activity
     */
    public static void show(Activity activity) {
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);
    }

    /**
     * Hide the keyboard for the current activity
     *
     * @param activity current activity
     * @param view     current view
     */
    public static void hide(Activity activity, View view) {
        ((InputMethodManager) activity.getSystemService(Activity.INPUT_METHOD_SERVICE))
                .hideSoftInputFromWindow(view.getWindowToken(), 0);
    }
}
