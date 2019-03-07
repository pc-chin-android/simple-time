package com.pcchin.simpletime.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.pcchin.simpletime.R;

/**
 * A placeholder fragment containing a simple view.
 */
public class StopwatchFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public StopwatchFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static StopwatchFragment newInstance(int sectionNumber) {
        StopwatchFragment fragment = new StopwatchFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_stopwatch, container, false);
    }
}
