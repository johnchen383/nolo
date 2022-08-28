package com.example.nolo.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;

import java.util.function.Consumer;

/**
 * Generic Util class to handle horizontal swipe events
 */
public class HorizontalSwipeListenerUtil {
    private float historicVal = 0;
    private View view;
    private Consumer<Void> majorAction;
    private Consumer<Void> minorAction;
    private Consumer<Void> idleAction;

    public HorizontalSwipeListenerUtil(HorizontalScrollView scrollView, Consumer<Void> swipeRight, Consumer<Void> swipeLeft, Consumer<Void> idleAction){
        this.view = scrollView;
        this.majorAction = swipeRight;
        this.minorAction = swipeLeft;
        this.idleAction = idleAction;
    }

    /**
     * Motion event on touch
     *
     * @param motionEvent
     * @return
     */
    private boolean onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                historicVal = motionEvent.getX();
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.AXIS_SIZE:
                float currentVal = motionEvent.getX();

                if (currentVal > historicVal) {
                    minorAction.accept(null);
                } else if (currentVal < historicVal) {
                    majorAction.accept(null);
                } else {
                    idleAction.accept(null);
                    return false;
                }

                return true;
        }
        return false;
    }

    /**
     * Listener for motion/touch
     */
    public void setUpListener(){
        view.setOnTouchListener((view1, motionEvent) -> onTouch(motionEvent));
    }
}
