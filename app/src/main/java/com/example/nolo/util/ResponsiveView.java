package com.example.nolo.util;

import android.view.View;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;

public class ResponsiveView {
    /**
     * Set height of all the views
     *
     * @param height New height
     * @param views List of views
     */
    public static void setHeight(int height, @NonNull View... views) {
        for (View v : views) {
            v.getLayoutParams().height = height;
            v.requestLayout();
        }
    }

    /**
     * Set width of all the views
     *
     * @param width New height
     * @param views List of views
     */
    public static void setWidth(int width, @NonNull View... views) {
        for (View v : views) {
            v.getLayoutParams().width = width;
            v.requestLayout();
        }
    }

    /**
     * Get weight of view
     *
     * @param view View
     */
    public static float getWeight(@NonNull View view) {
        return ((LinearLayout.LayoutParams) view.getLayoutParams()).weight;
    }

    /**
     * Set weight of all the views
     *
     * @param weight New height
     * @param views List of views
     */
    public static void setWeight(float weight, @NonNull View... views) {
        for (View v : views) {
            ((LinearLayout.LayoutParams) v.getLayoutParams()).weight = weight;
            v.requestLayout();
        }
    }

    /**
     * Set bottomMargin of all the views
     *
     * @param bottomMargin New height
     * @param views List of views
     */
    public static void setBottomMargin(int bottomMargin, @NonNull View... views) {
        for (View v : views) {
            ((LinearLayout.LayoutParams) v.getLayoutParams()).bottomMargin = bottomMargin;
            v.requestLayout();
        }
    }
}
