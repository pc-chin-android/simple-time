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

import java.util.Locale;

public class StopwatchFragment extends Fragment {
    private StopwatchThread stopwatchThread;
    private SharedPreferences sharedPref;
    private boolean isRunning;
    private int currentLaps;

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
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    // TODO: Only use SharedPreferences on onPause() and onResume()
    private void onStopwatchStart() {
    }

    private void onStopwatchPause() {
    }

    private void onStopwatchResume() {
    }

    private void onStopwatchStop() {
    }

    private void onStopwatchLap() {

    }

    @NonNull
    private String milliToString(long original) {
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
