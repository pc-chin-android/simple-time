package com.pcchin.simpletime;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.TextView;

import com.pcchin.simpletime.MainActivity;
import com.pcchin.simpletime.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class ClockThread extends Thread {

    private MainActivity context;
    private View currentView;
    private boolean isRunning;

    public ClockThread(MainActivity context, View currentView) {
        this.context = context;
        this.currentView = currentView;
    }

    @Override
    public void run() {
        while (this.isRunning) {
            long tempTime = System.nanoTime()/1000;
            // Updates clock
            final SimpleDateFormat dtFormat;
            if (DateFormat.is24HourFormat(context)) {
                dtFormat = new SimpleDateFormat("HH:mm:ss", Locale.ENGLISH);
            } else {
                dtFormat = new SimpleDateFormat("hh:mm:ss a", Locale.ENGLISH);
            }
            final Date currentDate = new Date();
            context.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            TextView sysTime = currentView.findViewById(R.id.clock_text);
                            sysTime.setText(dtFormat.format(currentDate));
                        }
                    });
            // Updates every second
            try {
                long sleepTime = 1000 + tempTime - (System.nanoTime() / 1000);
                if (sleepTime > 0) {
                    sleep(sleepTime);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void setRunning(boolean running) {
        this.isRunning = running;
    }
}
