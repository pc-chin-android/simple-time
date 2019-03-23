package com.pcchin.simpletime.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcchin.simpletime.R;
import com.pcchin.simpletime.thread.StopwatchThread;

public class StopwatchFragment extends Fragment {
    private StopwatchThread stopwatchThread;
    private SharedPreferences sharedPref;
    private boolean isRunning;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        if (getContext() != null) {
            sharedPref = getContext()
                    .getSharedPreferences("com.pcchin.simpletime", Context.MODE_PRIVATE);
        }
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }

    @Override
    public void onResume() {
        super.onResume();
        // Get values
        isRunning = sharedPref.getBoolean("stopwatchRunning", false);

        // Start if stopwatch is running
        if (isRunning) {
            int elapsedTime = sharedPref.getInt("stopwatchTime", 0);
            long pausedTime = sharedPref.getLong("stopwatchPausedTime", 0);
            stopwatchThread = new StopwatchThread(elapsedTime +
                    (System.nanoTime() - pausedTime));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    private void onStopwatchStart() {

    }

    private void onStopwatchPause() {

    }

    private void onStopwatchStop() {

    }

    private void onStopwatchLap() {

    }
}
