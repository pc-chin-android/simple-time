package com.pcchin.simpletime.fragment;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.text.InputFilter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.pcchin.simpletime.AlarmReceiver;
import com.pcchin.simpletime.InputFilterMinMax;
import com.pcchin.simpletime.R;

import java.util.Calendar;
import java.util.Locale;

public class TimerFragment extends Fragment {
    private SharedPreferences sharedPref;
    private AlarmManager alarmManager;
    private PendingIntent timerAlarmIntent;
    private Handler timerHandler;
    private long targetTime;
    private static final int TIMER_CODE = 1;

    private TextView timerDisplay;
    private TextView timerSeparator;
    private EditText timerHrs;
    private EditText timerMins;
    private Button timerBtn;

    // Runnable instead of thread is used to ensure smoothness
    private Runnable timerRunnable = new Runnable() {
        @Override
        public void run() {
            long currentTime = (targetTime - System.currentTimeMillis()) / 1000;
            if (currentTime > 0) {
                timerDisplay.setText(secsToString(currentTime));
                timerHandler.postDelayed(this, 1000);
            } else if (currentTime < 0) {
                onTimerStop(false);
            }
        }
    };

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View returnView = inflater.inflate(R.layout.fragment_timer, container, false);
        ((EditText) returnView.findViewById(R.id.timer_input_mins))
                .setFilters(new InputFilter[]{new InputFilterMinMax(0, 59)});
        if (getContext() != null) {
            sharedPref = getContext()
                    .getSharedPreferences("com.pcchin.simpletime", Context.MODE_PRIVATE);
        }

        // Sets alarm
        timerHandler = new Handler();
        alarmManager = (AlarmManager) getContext().getSystemService(Context.ALARM_SERVICE);
        timerAlarmIntent = PendingIntent.getBroadcast(getContext(), TIMER_CODE,
                new Intent(getContext(), AlarmReceiver.class), 0);

        // Set button listener
        timerBtn = returnView.findViewById(R.id.timer_start_cancel);
        timerBtn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (((Button) v).getText().equals(getString(R.string.start))) {
                            onTimerStart();
                        } else {
                            onTimerStop(true);
                        }
                    }
                });

        timerDisplay = returnView.findViewById(R.id.timer_current);
        timerSeparator = returnView.findViewById(R.id.timer_separator);
        timerHrs = returnView.findViewById(R.id.timer_input_hrs);
        timerMins = returnView.findViewById(R.id.timer_input_mins);
        return returnView;
    }

    @Override
    public void onResume() {
        super.onResume();
        // Check if timer is running
        boolean timerRunning = sharedPref.getBoolean("timerRunning", false);
        setTimerDisplayed(timerRunning);
        if (timerRunning) {
            targetTime = sharedPref.getLong("timerTime", 0);
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    private void onTimerStart() {
        // Check if value is larger than 0
        if ((timerHrs.getText() != null && timerMins.getText() != null)
                && (timerHrs.getText().toString().length() > 0
                && timerMins.getText().toString().length() > 0)
                && (Integer.valueOf(timerHrs.getText().toString()) > 0
                || Integer.valueOf(timerMins.getText().toString()) > 0)) {
            setTimerDisplayed(true);
            // Show time
            Calendar calendar = Calendar.getInstance();
            calendar.add(Calendar.HOUR, Integer.valueOf(timerHrs.getText().toString()));
            calendar.add(Calendar.MINUTE, Integer.valueOf(timerMins.getText().toString()));

            targetTime = calendar.getTimeInMillis();
            alarmManager.set(AlarmManager.RTC_WAKEUP, targetTime, timerAlarmIntent);

            // Update timer data to SharedPref
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putBoolean("timerRunning", true);
            editor.putLong("timerTime", targetTime);
            editor.apply();
            timerHandler.postDelayed(timerRunnable, 0);
        }
    }

    private void onTimerStop(boolean removeAlert) {
        setTimerDisplayed(false);
        alarmManager.cancel(timerAlarmIntent);
        // Update timer data to SharedPref
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putBoolean("timerRunning", false);
        editor.apply();
        if (removeAlert) {
            timerHandler.removeCallbacks(timerRunnable);
        }
    }

    private void setTimerDisplayed(boolean isDisplayed) {
        // Choose whether to display the timer or the selection
        if (isDisplayed) {
            timerDisplay.setVisibility(View.VISIBLE);
            timerSeparator.setVisibility(View.INVISIBLE);
            timerHrs.setVisibility(View.INVISIBLE);
            timerMins.setVisibility(View.INVISIBLE);
            timerBtn.setText(R.string.stop);
        } else {
            timerDisplay.setVisibility(View.INVISIBLE);
            timerSeparator.setVisibility(View.VISIBLE);
            timerHrs.setVisibility(View.VISIBLE);
            timerMins.setVisibility(View.VISIBLE);
            timerBtn.setText(R.string.start);
        }
        timerDisplay.setEnabled(isDisplayed);
        timerSeparator.setEnabled(! isDisplayed);
        timerHrs.setEnabled(! isDisplayed);
        timerMins.setEnabled(! isDisplayed);
    }

    @NonNull
    private static String secsToString(long original) {
        double secs = original % 60;
        original = (int) Math.floor((original - secs) / 60);
        double mins = original % 60;
        int hrs = (int) Math.floor((original - mins) / 60);
        return String.format(Locale.ENGLISH, "%d:%02d:%02d",
                hrs, (int) mins, (int) secs);
    }
}
