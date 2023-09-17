package com.example.alarmmanager;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class StopAlarmReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        if("STOP_ALARM".equals(intent.getAction())){
            int alarmId = intent.getExtras().getInt("id");

            //this will stop ringtone if user press notification STOP action button
            if(MyBroadcastReceiver.ringtone!=null){
                MyBroadcastReceiver.ringtone.stop();
            }
            if(MyBroadcastReceiver.vibrator !=null){
                MyBroadcastReceiver.vibrator.cancel();
            }
            if(MyBroadcastReceiver.notificationManager !=null){
                MyBroadcastReceiver.notificationManager.cancel(alarmId);
            }
            if(MyBroadcastReceiver.countDownTimer != null){
                MyBroadcastReceiver.countDownTimer.cancel();
            }
        }

    }
}
