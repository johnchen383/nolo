package com.example.nolo.util;

import android.view.MotionEvent;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ScrollView;

import java.util.function.Consumer;
import java.util.function.Function;

public class HorizontalSwipeListenerUtil {
    private float historicVal = 0;
    private View view;
    private Consumer<Void> majorAction;
    private Consumer<Void> minorAction;
    private Consumer<Void> idleAction;

    private boolean onTouch(MotionEvent motionEvent) {
        switch (motionEvent.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (view.getClass().equals(ScrollView.class)){
                    historicVal = motionEvent.getY();
                } else {
                    historicVal = motionEvent.getX();
                }

                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.AXIS_SIZE:
                float currentVal;

                if (view.getClass().equals(ScrollView.class)){
                    currentVal = motionEvent.getY();
                } else {
                    currentVal = motionEvent.getX();
                }

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

    public HorizontalSwipeListenerUtil(HorizontalScrollView scrollView, Consumer<Void> swipeRight, Consumer<Void> swipeLeft, Consumer<Void> idleAction){
        this.view = scrollView;
        this.majorAction = swipeRight;
        this.minorAction = swipeLeft;
        this.idleAction = idleAction;
    }

    public void setUpListener(){
        view.setOnTouchListener((view1, motionEvent) -> onTouch(motionEvent));
    }
}
