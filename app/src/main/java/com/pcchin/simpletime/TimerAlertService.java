package com.pcchin.simpletime;

import android.annotation.SuppressLint;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.media.MediaPlayer;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.IBinder;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;

import org.jetbrains.annotations.Nullable;

public class TimerAlertService extends Service {
    private WindowManager windowManager;
    private LinearLayout alertLayout;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("InflateParams")
    @Override
    public void onCreate() {
        super.onCreate();

        // Start alarm
        final MediaPlayer mp = MediaPlayer.create(this,
                RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM));
        mp.setLooping(true);
        mp.start();

        // Make background visible
        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        alertLayout = (LinearLayout) ((LayoutInflater) getBaseContext()
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE))
                .inflate(R.layout.view_timer_dialog, null);
        int displayType;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            displayType = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
        } else {
            displayType = WindowManager.LayoutParams.TYPE_PHONE;
        }
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.MATCH_PARENT,
                WindowManager.LayoutParams.MATCH_PARENT,
                displayType,
                WindowManager.LayoutParams.FLAG_DIM_BEHIND,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.CENTER;

        // Set listener to dismiss button
        Button okBtn = alertLayout.findViewById(R.id.timer_dialog_ok);
        okBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                windowManager.removeViewImmediate(alertLayout);
                mp.stop();
                mp.release();
            }
        });

        // Service had to be used as activity and dialogs will cause black background
        System.out.println(windowManager);
        windowManager.addView(alertLayout, params);
    }
}
