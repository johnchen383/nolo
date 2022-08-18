package com.example.nolo.util;

/**
 * The time in TimeKeeper class is calculated in milliseconds
 */
public class TimeToLiveToken {
    private final long timeLimit;
    private long startedTime;

    public TimeToLiveToken(long timeLimit) {
        this.timeLimit = timeLimit;
        this.startedTime = currentTime();
    }

    public void reset() {
        startedTime = currentTime();
    }

    public boolean hasExpired() {
        return currentTime() - startedTime > timeLimit;
    }

    private long currentTime() {
        return System.currentTimeMillis();
    }
}
