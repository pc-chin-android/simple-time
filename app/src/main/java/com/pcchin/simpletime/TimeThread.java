package com.pcchin.simpletime;

public class TimeThread extends Thread {
    public static final int TYPE_CLOCK = 300;
    public static final int TYPE_STOPWATCH = 301;
    public static final int TYPE_TIMER = 302;

    private int type;

    public TimeThread(int type) {
        this.type = type;
    }

    @Override
    public void run() {

    }
}
