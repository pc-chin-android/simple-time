package com.pcchin.simpletime.thread;

public class StopwatchThread extends Thread {
    private long elapsedTime;
    private long startTime;

    public StopwatchThread(long elapsedTime) {
        this.elapsedTime = elapsedTime;
    }

    @Override
    public void run() {

    }

    public long getElapsedTime(int elapsedTime) {
        return this.elapsedTime;
    }
}
