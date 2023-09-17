package com.example.todolist;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        //this will check if this action is match with the action we set in Stop pendingIntent then run these code
        if("CANCEL_RINGTONE".equals(intent.getAction())){
            int alarmId = intent.getExtras().getInt("id");

            //this will check if ringtone is running then stop ringtone
            if(TodoBroadcastReceiver.ringtone !=null){
                TodoBroadcastReceiver.ringtone.stop();
            }
            //this will check if vibrator is running then stop vibrator
            if(TodoBroadcastReceiver.vibrator !=null){
                TodoBroadcastReceiver.vibrator.cancel();
            }
            //this will check if notification is displaying then stop notification
            if(TodoBroadcastReceiver.notificationManager !=null){
                TodoBroadcastReceiver.notificationManager.cancel(alarmId);
            }
            //this will check if countDown timer is running then stop countDown Time
            if(TodoBroadcastReceiver.countDownTimer != null){
                TodoBroadcastReceiver.countDownTimer.cancel();
            }

        }
    }
}
