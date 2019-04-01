package com.pcchin.simpletime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.pcchin.simpletime.MainActivity;
import com.pcchin.simpletime.R;
import com.pcchin.simpletime.ClockThread;

import java.util.Locale;
import java.util.TimeZone;

public class ClockFragment extends Fragment {
    private ClockThread clockThread;
    private View currentView;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_clock, container, false);
        this.currentView = view;
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        this.clockThread = new ClockThread((MainActivity) getContext(), currentView);
        this.clockThread.setRunning(true);
        this.clockThread.start();

        // Set GMT
        if (getView() != null && getView().findViewById(R.id.clock_gmt) != null) {
            TextView gmtView = getView().findViewById(R.id.clock_gmt);
            TimeZone currentTz = TimeZone.getDefault();
            gmtView.setText(String.format(Locale.ENGLISH, "%s - %s",
                    currentTz.getDisplayName(false, TimeZone.SHORT), currentTz.getID()));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        this.clockThread.setRunning(false);
    }
}
