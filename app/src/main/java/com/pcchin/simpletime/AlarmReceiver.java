package com.pcchin.simpletime;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class AlarmReceiver extends BroadcastReceiver {
    private static final int ID_NOTIF = 11037;
    private static final int ID_PENDING_NOTIF = 73011;

    @Override
    public void onReceive(final Context context, Intent intent) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                // Show notification
                PendingIntent notifMainIntent = PendingIntent.getActivity(context, ID_PENDING_NOTIF,
                        new Intent(context, MainActivity.class), 0);
                NotificationManager manager = (NotificationManager)
                        context.getSystemService(Context.NOTIFICATION_SERVICE);
                Notification notif = new Notification.Builder(context)
                        .setSmallIcon(R.drawable.ic_launcher_foreground)
                        .setContentText("Time's up!")
                        .setContentTitle("Timer")
                        .setContentIntent(notifMainIntent)
                        .build();
                manager.notify(ID_NOTIF, notif);

                // Show alert view
                Intent alertIntent = new Intent(context, TimerAlertService.class);
                context.startService(alertIntent);
            }
        }).start();
    }
}
