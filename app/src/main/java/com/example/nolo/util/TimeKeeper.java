package com.example.nolo.util;

/**
 * The time in TimeKeeper class is calculated in milliseconds
 */
public class TimeKeeper {
    private final long timeLimit;
    private long startedTime;

    public TimeKeeper(long timeLimit) {
        this.timeLimit = timeLimit;
        this.startedTime = 0;
    }

    public void startTimer() {
        startedTime = System.currentTimeMillis();
    }

    public boolean isTimeLimitReached() {
        return System.currentTimeMillis() - startedTime > timeLimit;
    }
}
