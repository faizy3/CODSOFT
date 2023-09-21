package com.example.alarmmanager;

import static android.content.Context.ALARM_SERVICE;

import static java.security.AccessController.getContext;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Calendar;

public class MyAdapter extends RecyclerView.Adapter {
    public static boolean isUpdate = false;
    ArrayList<Alarm> alarmlist;
    Context context;
    MyAdapter(Context context , ArrayList<Alarm> alarmlist){
        this.alarmlist = alarmlist;
        this.context = context;
    }
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_manager_layout,parent,false);
        alarmViewHolder obj= new alarmViewHolder(v);
        return obj;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        alarmViewHolder viewHolder = (alarmViewHolder) holder;
        Alarm alarm = alarmlist.get(position);
        String time = alarm.toString();//getting hour and minute from data base and set time format
        viewHolder.alarmTime.setText("ID "+alarm.getId()+" "+time);
        viewHolder.label.setText(alarm.getLabel());
        viewHolder.btnSwitch.setChecked(alarm.isStatus());

        //when user click on toggle button (on/off) update status in db
        viewHolder.btnSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Alarm alarmstatus =  alarmlist.get(holder.getAdapterPosition());
                alarmstatus.setId(alarmstatus.getId());
                alarmstatus.setStatus(isChecked);
                //for set or cancel alarm
                int id = alarmstatus.getId();
                String label = alarmstatus.getLabel();
                int hour = alarmstatus.getHour();
                int minute = alarmstatus.getMinute();
                MyDBHelper helper = new MyDBHelper(context);
                helper.updateStatus(alarmstatus);
                if(isChecked){
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);

                    //these line of code set alarm on if the switch button on
                    Intent intent = new Intent(context, MyBroadcastReceiver.class);
                    intent.putExtra("id" , id);
                    intent.putExtra("label" , label);
                    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);
                    // Calculate the alarm time based on the selected hour and minute
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.HOUR_OF_DAY, hour);
                    calendar.set(Calendar.MINUTE, minute);
                    calendar.set(Calendar.SECOND, 0);
                    // Set the alarm to trigger at the specified time
                    alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
                    Toast.makeText(context, "Alarm on", Toast.LENGTH_LONG).show();
                }
                else{
                    //these line of code set alarm off if the switch button on
                    AlarmManager alarmManager = (AlarmManager) getContext().getSystemService(ALARM_SERVICE);
                    Intent cancelAlarmIntent = new Intent(context , MyBroadcastReceiver.class);
                    cancelAlarmIntent.putExtra("id" , id);
                    PendingIntent cancelPendingIntent = PendingIntent.getBroadcast(context , id , cancelAlarmIntent , PendingIntent.FLAG_CANCEL_CURRENT);
                    alarmManager.cancel(cancelPendingIntent);
                    Toast.makeText(context, "Alarm off", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    @Override
    public int getItemCount() {
        return alarmlist.size();
    }

    public Context getContext(){
        return context;
    }
    public void refreshlist(ArrayList<Alarm> alarmlist){
        this.alarmlist = alarmlist;
        notifyDataSetChanged();
    }

    public class alarmViewHolder extends RecyclerView.ViewHolder{
        TextView alarmTime,label;
        Switch btnSwitch;

        public alarmViewHolder(@NonNull View v) {
            super(v);
            alarmTime = v.findViewById(R.id.txtviewtime);
            label = v.findViewById(R.id.txtviewlabel);
            btnSwitch = v.findViewById(R.id.btntoggle);
            //when user click on recyclerview item then this method is called
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    isUpdate = true;
                    Intent i = new Intent(context , AddNewAlarm.class);
                    Alarm alarm = alarmlist.get(getAdapterPosition());
                    i.putExtra("id" , alarm.getId());
                    i.putExtra("hour" ,alarm.getHour() );
                    i.putExtra("minute" ,alarm.getMinute() );
                    i.putExtra("label" ,alarm.getLabel() );
                    i.putExtra("status" ,alarm.isStatus() );
                    i.putExtra("isUpdate" , isUpdate);
                    context.startActivity(i);
                    ((Activity) getContext()).finish();
                }
            });

        }
    }
}
