package com.example.nolo.util;

import androidx.core.app.ActivityOptionsCompat;

public class Animation {
    /**
     * Get fade animation
     *
     * @param context
     * @return Fade animation
     */
    public static ActivityOptionsCompat Fade(android.content.Context context) {
        return ActivityOptionsCompat.makeCustomAnimation(context, android.R.anim.fade_in, android.R.anim.fade_out);
    }
}
