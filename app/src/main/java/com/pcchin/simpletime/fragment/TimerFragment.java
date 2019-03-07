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
public class TimerFragment extends Fragment {
    /**
     * The fragment argument representing the section number for this
     * fragment.
     */
    private static final String ARG_SECTION_NUMBER = "section_number";

    public TimerFragment() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static TimerFragment newInstance(int sectionNumber) {
        TimerFragment fragment = new TimerFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_timer, container, false);
    }
}
