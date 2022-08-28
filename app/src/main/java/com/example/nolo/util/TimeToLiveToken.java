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

    /**
     * Reset the time to live token
     */
    public void reset() {
        startedTime = currentTime();
    }

    /**
     * Check whether the time of live token is expired
     *
     * @return True if it is expired;
     *         Otherwise False
     */
    public boolean hasExpired() {
        return currentTime() - startedTime > timeLimit;
    }

    /**
     * Get the current time in milliseconds
     *
     * @return Current time in milliseconds
     */
    private long currentTime() {
        return System.currentTimeMillis();
    }
}
