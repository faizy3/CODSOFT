package com.example.todolist;

import android.Manifest;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Vibrator;
import android.provider.Settings;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class TodoBroadcastReceiver extends BroadcastReceiver {
    public static Ringtone ringtone ;
    private static final String ALARM_CHANNEL_ID = "alarm_channel";
    public static NotificationManagerCompat notificationManager;
    private static final long SnoozeCountDown = 1000L * 60;
    public static Vibrator vibrator;
    public static CountDownTimer countDownTimer;

    @Override
    public void onReceive(Context context, Intent intent) {

        int alarmId = intent.getExtras().getInt("id");
        String task = intent.getExtras().getString("task");

        // Create a notification channel (required for Android Oreo and above)
        createAlarmChannel(context);

        // Create an intent for when the user taps the notification
        Intent infoIntent = new Intent(context, AlarmManager.class);
        PendingIntent infoPendingIntent = PendingIntent.getActivity(context, alarmId, infoIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        // Create a notification with an action to stop the alarm or snooze the alarm
        Notification notification = new NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_alarm_on_24)
                .setContentText(task)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.CYAN)
                .setContentIntent(infoPendingIntent)
                .setAutoCancel(true)
                .setSound(null)
                .setOnlyAlertOnce(true)
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .addAction(R.drawable.baseline_delete_forever_24, "STOP", getStopPendingIntent(context, alarmId))
                .build();
        // Display the notification
        notificationManager = NotificationManagerCompat.from(context);
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        notificationManager.notify(alarmId, notification);
        //when notification fires then ringtone starts
        ringtone = RingtoneManager.getRingtone(context, Settings.System.DEFAULT_ALARM_ALERT_URI);
        vibrator = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
        //this method called countDown timer
        cancelOrSnoozeAlarm(alarmId);

    }

    private void cancelOrSnoozeAlarm(int alarmId){
        countDownTimer = new CountDownTimer(SnoozeCountDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ringtone.play();
                long [] vibratePattern = {0 , 200 , 1000 , 5000};
                vibrator.vibrate(vibratePattern , -1);

            }

            @Override
            public void onFinish() {
                ringtone.stop();
                notificationManager.cancel(alarmId);
                vibrator.cancel();
            }
        }.start();
    }

    private PendingIntent getStopPendingIntent(Context context, int alarmId) {
        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        stopIntent.putExtra("id", alarmId);
        stopIntent.setAction("CANCEL_RINGTONE");
        return PendingIntent.getBroadcast(context, alarmId, stopIntent, 0);
    }

    private void createAlarmChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel alarmChannel = new NotificationChannel(
                    ALARM_CHANNEL_ID,
                    "Alarm",
                    NotificationManager.IMPORTANCE_HIGH
            );
            NotificationManager notificationManager =(NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.createNotificationChannel(alarmChannel);
        }
    }
}
