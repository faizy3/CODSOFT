package com.example.alarmmanager;

import static android.content.Context.NOTIFICATION_SERVICE;

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
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.os.Build;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.Settings;
import android.widget.Toast;

import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

public class MyBroadcastReceiver extends BroadcastReceiver {
    public static Ringtone ringtone;
    private static final String ALARM_CHANNEL_ID = "alarm_channel";
    public static NotificationManagerCompat notificationManager;
    private static final  long SnoozeCountDown = 1000L *  60;
    private Handler autoSnoozeHandler = new Handler() ;
    private static int counter = 2;
    private static String  label;
    public static Vibrator vibrator;
    public static CountDownTimer countDownTimer;

    @Override
    public void onReceive(Context context, Intent intent) {



        int alarmId = intent.getExtras().getInt("id");
        label = intent.getExtras().getString("label");
        //notitificaion building

        // Create a notification channel (required for Android Oreo and above)
        createAlarmChannel(context);

        // Create an intent for when the user taps the notification
        Intent infoIntent = new Intent(context, AddNewAlarm.class);
        PendingIntent infoPendingIntent = PendingIntent.getActivity(context,alarmId,infoIntent,PendingIntent.FLAG_UPDATE_CURRENT);

        // Create a notification with an action to stop the alarm or snooze the alarm
        Notification notification = new NotificationCompat.Builder(context, ALARM_CHANNEL_ID)
                .setSmallIcon(R.drawable.baseline_alarm_on_24)
                .setContentText(label)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setColor(Color.CYAN)
                .setContentIntent(infoPendingIntent)
                .setSound(Settings.System.DEFAULT_ALARM_ALERT_URI)
                .addAction(R.drawable.baseline_cancel_24, "STOP", getStopPendingIntent(context, alarmId))
                .addAction(R.drawable.baseline_cancel_24  , "SNOOZE" , snoozeAlarm(context , alarmId))
                .build();
        // Display the notification
         notificationManager = NotificationManagerCompat.from(context);
        //check if permission is granted or not
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


        //after given time ringtone will be stop and autosnooze alarm
        cancelOrSnoozeAlarm(context , alarmId);
    }
//    private void getAutoSnooze(Context context  , int alarmId){
//        autoSnoozeHandler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                ringtone.stop();
//                notificationManager.cancel(alarmId);
//                //auto snooze alarm if user dont click on Stop/snooze actionbutton
//                autoSnoozeAlarm( context, alarmId);
//
//            }
//        } , SnoozeCountDown );
//    }
    private void cancelOrSnoozeAlarm(Context context , int alarmId){
        countDownTimer = new CountDownTimer(SnoozeCountDown, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                ringtone.play();
                long [] pattern = {0 , 200 , 500 , 2000};
                vibrator.vibrate(pattern , 0);
            }

            @Override
            public void onFinish() {
                ringtone.stop();
                notificationManager.cancel(alarmId);
                vibrator.cancel();
                if(counter>0){
                    //this method call and get autoSnooze schedule if user dont click
                    autoSnoozeAlarm(context , alarmId);
                    Toast.makeText(context, "alarm Snooze for 1 minute counter: "+counter, Toast.LENGTH_SHORT).show();
                    counter -- ;
                }else {
                    Toast.makeText(context, "alarm cancel and no more longer ", Toast.LENGTH_SHORT).show();
                }


            }

        }.start();
    }
    //this method is called when user dont click
    private void autoSnoozeAlarm(Context context, int alarmId) {

        long snoozetime = System.currentTimeMillis() + ( 1 * 60 * 1000);
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent snoozeIntent = new Intent(context , MyBroadcastReceiver.class);
        snoozeIntent.putExtra("label" , label);
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context , alarmId , snoozeIntent , PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , snoozetime , snoozePendingIntent);
    }


    //this method is called when user click on SNOOZE actionbutton on the notification
    private PendingIntent snoozeAlarm(Context context, int alarmId) {
        //create intent for snooze alarm
        Intent snoozeIntent = new Intent(context , SnoozeReceiver.class);
        snoozeIntent.putExtra("id" , alarmId);
        snoozeIntent.putExtra("label" , label);
        //setaction method is used to identity the action of intent that what kind of action an intent have to perform
        snoozeIntent.setAction("SNOOZE_ACTION");
        return PendingIntent.getBroadcast(context, alarmId , snoozeIntent , PendingIntent.FLAG_UPDATE_CURRENT);
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
    //this method is called when user click STOP action button on the notification
    private PendingIntent getStopPendingIntent(Context context, int alarmId) {
        Intent stopIntent = new Intent(context, StopAlarmReceiver.class);
        stopIntent.putExtra("id", alarmId);
        stopIntent.setAction("STOP_ALARM");
        return PendingIntent.getBroadcast(context, alarmId, stopIntent, PendingIntent.FLAG_UPDATE_CURRENT);
    }
}

