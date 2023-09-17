package com.example.alarmmanager;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public
class SnoozeReceiver extends BroadcastReceiver {
    private int alarmId = -1;
    private String label;
    @Override
    public void onReceive(Context context, Intent intent) {
        if("SNOOZE_ACTION".equals(intent.getAction())){

            alarmId = intent.getExtras().getInt("id");
            label = intent.getExtras().getString("label");
//            // snooze time 10 minute
            long snoozetime = System.currentTimeMillis() + (1 * 60 * 1000);
            //this ringtone will be stop if user press snooze button in notification
            if(MyBroadcastReceiver.ringtone!=null){
                MyBroadcastReceiver.ringtone.stop();

            }
            if(MyBroadcastReceiver.vibrator !=null){
                MyBroadcastReceiver.vibrator.cancel();
            }
            if(MyBroadcastReceiver.notificationManager != null){
                MyBroadcastReceiver.notificationManager.cancel(alarmId);
            }
            if(MyBroadcastReceiver.countDownTimer !=null){
                MyBroadcastReceiver.countDownTimer.cancel();
            }

            //set snoozeAlarm
            if(alarmId != -1){
                //Alarm snoozeAlarm = new Alarm();
                setSnoozeAlarm(context, alarmId , snoozetime);


            }

        }
    }

    private void setSnoozeAlarm(Context context, int alarmId , long snoozetime) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent snoozeIntent = new Intent(context , MyBroadcastReceiver.class);
        snoozeIntent.putExtra("label" , label );
        PendingIntent snoozePendingIntent = PendingIntent.getBroadcast(context , alarmId , snoozeIntent , PendingIntent.FLAG_UPDATE_CURRENT);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP , snoozetime , snoozePendingIntent);
    }
}
