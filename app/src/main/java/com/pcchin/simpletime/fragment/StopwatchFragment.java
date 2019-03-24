package com.pcchin.simpletime.fragment;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.pcchin.simpletime.R;
import com.pcchin.simpletime.thread.StopwatchThread;

import java.util.ArrayList;
import java.util.Locale;

public class StopwatchFragment extends Fragment {
    private static final int STATE_STOPPED = 0;
    private static final int STATE_RUNNING = 4;
    private static final int STATE_PAUSED = 2;

    private StopwatchThread stopwatchThread;
    private SharedPreferences sharedPref;

    private int state = STATE_STOPPED;
    private ArrayList<Long> lapsList;
    private long elapsedTime;
    private long lastResumeTime;

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
            Button btnL = getView().findViewById(R.id.stopwatch_start_pause);
            btnL.setText(R.string.pause);
            Button btnR = getView().findViewById(R.id.stopwatch_lap_reset);
            btnR.setText(R.string.lap);
        }

        // Start thread
        state = STATE_RUNNING;
        stopwatchThread = new StopwatchThread(0, (Activity) getContext());
        stopwatchThread.setRunning(true);
        stopwatchThread.start();
    }

    private void onStopwatchPause() {
        // Set values
        state = STATE_PAUSED;
        elapsedTime = System.currentTimeMillis() - lastResumeTime;
        stopwatchThread.setRunning(false);

        // Set buttons
        if (getView() != null) {
            Button btnL = getView().findViewById(R.id.stopwatch_start_pause);
            btnL.setText(R.string.resume);
            Button btnR = getView().findViewById(R.id.stopwatch_lap_reset);
            btnR.setText(R.string.stop);
        }
    }

    private void onStopwatchResume() {
        // Start thread
        state = STATE_RUNNING;
        stopwatchThread = new StopwatchThread(elapsedTime, (Activity) getContext());
        stopwatchThread.setRunning(true);
        stopwatchThread.start();

        // Set buttons
        if (getView() != null) {
            Button btnL = getView().findViewById(R.id.stopwatch_start_pause);
            btnL.setText(R.string.pause);
            Button btnR = getView().findViewById(R.id.stopwatch_lap_reset);
            btnR.setText(R.string.lap);
        }
    }

    private void onStopwatchStop() {
        state = STATE_STOPPED;
        stopwatchThread.setRunning(false);

        // Set buttons
        if (getView() != null) {
            Button btnL = getView().findViewById(R.id.stopwatch_start_pause);
            btnL.setText(R.string.start);
            Button btnR = getView().findViewById(R.id.stopwatch_lap_reset);
            btnR.setText(R.string.stop);
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
        return String.format(Locale.ENGLISH, "%d:%02d:%02ds:%03d",
                hrs, (int) mins, (int) secs,(int) millis);
    }
}
