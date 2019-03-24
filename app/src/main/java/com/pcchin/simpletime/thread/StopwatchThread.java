package com.pcchin.simpletime.thread;

import android.app.Activity;
import android.view.View;
import android.widget.TextView;

import com.pcchin.simpletime.R;
import com.pcchin.simpletime.fragment.StopwatchFragment;

public class StopwatchThread extends Thread {
    private Activity activity;
    private long elapsedTime;
    private boolean isRunning;

    public StopwatchThread(long elapsedTime, Activity activity) {
        this.elapsedTime = elapsedTime;
        this.activity = activity;
    }

    @Override
    public void run() {
        final long startTime = System.currentTimeMillis();
        while (this.isRunning) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    ((TextView) activity.findViewById(R.id.stopwatch_title))
                            .setText(StopwatchFragment.milliToString
                                    (System.currentTimeMillis() - startTime + elapsedTime));
                }
            });
        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}
