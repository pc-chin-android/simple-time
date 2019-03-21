package com.pcchin.simpletime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcchin.simpletime.MainActivity;
import com.pcchin.simpletime.R;
import com.pcchin.simpletime.thread.ClockThread;

public class ClockFragment extends Fragment {
    private ClockThread clockThread;
    private View currentView;

    public ClockFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stopwatch, container, false);
        this.currentView = view;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.clockThread = new ClockThread((MainActivity) getContext(), currentView);
        this.clockThread.setRunning(true);
        this.clockThread.start();
    }

    @Override
    public void onPause() {
        super.onPause();
        this.clockThread.setRunning(false);
    }
}
