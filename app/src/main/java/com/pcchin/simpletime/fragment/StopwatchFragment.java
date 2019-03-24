package com.pcchin.simpletime.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.pcchin.simpletime.R;

import java.util.ArrayList;
import java.util.Locale;

public class StopwatchFragment extends Fragment {
    private static final int STATE_STOPPED = 0;
    private static final int STATE_RUNNING = 4;
    private static final int STATE_PAUSED = 2;

    private SharedPreferences sharedPref;
    private int state = STATE_STOPPED;
    private ArrayList<Long> lapsList;
    private long elapsedTime;
    private long lastResumeTime;
    private Handler stopwatchHandler;

    // Runnable instead of thread is used to ensure smoothness
    private Runnable stopwatchRunnable = new Runnable() {
        @Override
        public void run() {
            if (getView() != null) {
                ((TextView) getView().findViewById(R.id.stopwatch_title))
                        .setText(milliToString(System.currentTimeMillis()
                                - lastResumeTime + elapsedTime));
            }
            stopwatchHandler.postDelayed(this, 0);
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getContext() != null) {
            sharedPref = getContext()
                    .getSharedPreferences("com.pcchin.simpletime", Context.MODE_PRIVATE);
        }

        // Set up listeners
        View returnView = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        View.OnClickListener btnListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (((Button) v).getText().equals(getString(R.string.start))) {
                    onStopwatchStart();
                } else if (((Button) v).getText().equals(getString(R.string.pause))) {
                    onStopwatchPause();
                } else if (((Button) v).getText().equals(getString(R.string.resume))) {
                    onStopwatchResume();
                } else if (((Button) v).getText().equals(getString(R.string.stop))) {
                    onStopwatchStop();
                } else if (((Button) v).getText().equals(getString(R.string.lap))) {
                    onStopwatchLap();
                }
            }
        };
        stopwatchHandler = new Handler();
        ((TextView) returnView.findViewById(R.id.stopwatch_title)).setText(R.string.milli_zero);
        returnView.findViewById(R.id.stopwatch_start_pause).setOnClickListener(btnListener);
        returnView.findViewById(R.id.stopwatch_lap_reset).setOnClickListener(btnListener);
        return returnView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // Stopwatch functions
    private void onStopwatchStart() {
        // Initialize values
        elapsedTime = 0;
        lapsList = new ArrayList<>();
        lastResumeTime = System.currentTimeMillis();

        // Set buttons
        if (getView() != null) {
            ((Button) getView().findViewById(R.id.stopwatch_start_pause)).setText(R.string.pause);
            ((Button) getView().findViewById(R.id.stopwatch_lap_reset)).setText(R.string.lap);
        }

        // Start thread
        state = STATE_RUNNING;
        stopwatchHandler.postDelayed(stopwatchRunnable, 0);
    }

    private void onStopwatchPause() {
        // Set values
        state = STATE_PAUSED;
        elapsedTime += System.currentTimeMillis() - lastResumeTime;
        stopwatchHandler.removeCallbacks(stopwatchRunnable);

        // Set buttons
        if (getView() != null) {
            ((Button) getView().findViewById(R.id.stopwatch_start_pause)).setText(R.string.resume);
            ((Button) getView().findViewById(R.id.stopwatch_lap_reset)).setText(R.string.stop);
        }
    }

    private void onStopwatchResume() {
        // Start thread
        state = STATE_RUNNING;
        lastResumeTime = System.currentTimeMillis();
        stopwatchHandler.postDelayed(stopwatchRunnable, 0);

        // Set buttons
        if (getView() != null) {
            ((Button) getView().findViewById(R.id.stopwatch_start_pause)).setText(R.string.pause);
            ((Button) getView().findViewById(R.id.stopwatch_lap_reset)).setText(R.string.lap);
        }
    }

    private void onStopwatchStop() {
        state = STATE_STOPPED;
        stopwatchHandler.removeCallbacks(stopwatchRunnable);

        // Set buttons
        if (getView() != null) {
            ((Button) getView().findViewById(R.id.stopwatch_start_pause)).setText(R.string.start);
            ((Button) getView().findViewById(R.id.stopwatch_lap_reset)).setText(R.string.stop);
            ((TextView) getView().findViewById(R.id.stopwatch_title)).setText(R.string.milli_zero);
        }
    }

    private void onStopwatchLap() {
        lapsList.add(System.currentTimeMillis() - lastResumeTime + elapsedTime);
    }

    @NonNull
    public static String milliToString(long original) {
        double millis = original % 1000;
        original = (int) Math.floor((original - millis) / 1000);
        double secs = original % 60;
        original = (int) Math.floor((original - secs) / 60);
        double mins = (int) original % 60;
        int hrs = (int) Math.floor((original - mins) / 60);
        return String.format(Locale.ENGLISH, "%d:%02d:%02d:%03d",
                hrs, (int) mins, (int) secs,(int) millis);
    }
}
