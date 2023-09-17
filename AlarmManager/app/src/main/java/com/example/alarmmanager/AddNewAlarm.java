package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;

public class AddNewAlarm extends AppCompatActivity {
    Button btnAdd_Delete;
    TimePicker timePicker;
    TextView result;
    EditText label;
    CardView cardViewUpdate;
    ImageButton btncancel,btnupdate;
    MyDBHelper helper = new MyDBHelper(this);
    int alarmId;
    private AlarmManagerActivity alarmManagerActivity;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_alarm);
        btnAdd_Delete = findViewById(R.id.btnaddNewAlarm);
        timePicker = findViewById(R.id.timepicker);
        label =findViewById(R.id.edittxtlabel);
        cardViewUpdate = findViewById(R.id.cardviewUpdate);
        btnupdate = findViewById(R.id.imgbtnUpdate);
        btncancel = findViewById(R.id.imgbtnCancel);


        //getting intent values from my adapter class
        Intent intent = getIntent();
        boolean isUpdate = intent.getBooleanExtra("isUpdate" , false);
        if(isUpdate){
            alarmId= intent.getExtras().getInt("id");
            label.setText(intent.getStringExtra("label"));
            timePicker.setHour(intent.getExtras().getInt("hour"));
            timePicker.setMinute(intent.getExtras().getInt("minute"));
            btnAdd_Delete.setText("Delete Alarm");
            cardViewUpdate.setVisibility(View.VISIBLE);

            //update data into db
            btnupdate.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Alarm alarm = new Alarm();
                    //update data in Database
                    upDateAlarm(alarm, alarmId);
                    //shedule alarm called when user add Update alarm into DB
                     scheduleAlarm(alarm , alarmId);
                     //refresh recyclerView after update data
                }
            });


            btncancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    exist();
                }
            });
            btnAdd_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    deleteAlarm(alarmId);
                }
            });
        }else{

            //add new alarm into database
            btnAdd_Delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Alarm alarm = new Alarm();

                    //setting these values to alarm class and then adding to database
                    alarm.setLabel(label.getText().toString());
                    alarm.setHour(timePicker.getHour());
                    alarm.setMinute(timePicker.getMinute());
                    alarm.setStatus(true);
                    //below line add data into database
                    long id = helper.addAlarm(alarm);
                    int alarmId = (int) id;
                    //shedule alarm called when user add new alarm into DB and return time that display in Toast msg
                    scheduleAlarm(alarm,alarmId);
                    Toast.makeText(AddNewAlarm.this, "New Alarm inseted into database", Toast.LENGTH_LONG).show();
                    //Toast.makeText(AddNewAlarm.this, "Ring in: "+displaytime, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AddNewAlarm.this , AlarmManagerActivity.class);
                    startActivity(i);
                }
            });

        }
    }

    private void convertMSToHourAndMin(long timeInMS) {
        long seconds =  timeInMS / 1000;
        long minute = seconds / 60 ;
        long hour = minute / 60 ;

        String hourInString = String.valueOf(hour);
        String minInString = String.valueOf(minute);
        Toast.makeText(this, "ring in "+hourInString+":"+minInString, Toast.LENGTH_SHORT).show();

    }

    public void scheduleAlarm(Alarm alarm , int alarmId) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, MyBroadcastReceiver.class);
        String label = alarm.getLabel();
        intent.putExtra("id" ,alarmId );
        intent.putExtra("label" , label);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, alarmId, intent, 0);

        // Calculate the alarm time based on the selected hour and minute
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, alarm.getHour());
        calendar.set(Calendar.MINUTE, alarm.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // If the alarm time is in the past, add one day to it
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }
        //minus set alarm time from current time to display remaining ringing time
        long time = calendar.getTimeInMillis() - System.currentTimeMillis();
        //convert miliseconds to seconds , hours and minutes
        int seconds = (int) (time / 1000) % 60 ;
        int minutes = (int) ((time / (1000*60)) % 60);
        int hours   = (int) ((time / (1000*60*60)) % 24);
        // Set the alarm to trigger at the specified time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
//        Toast.makeText(AddNewAlarm.this, "Alarm set", Toast.LENGTH_LONG).show();
        if(minutes ==0){
            Toast.makeText(AddNewAlarm.this, "Ring in less then minute", Toast.LENGTH_LONG).show();
        }else {
            Toast.makeText(AddNewAlarm.this, "Ring in "+hours+":"+minutes, Toast.LENGTH_LONG).show();
        }
    }
    //create second method with diff. parameter to calling from myadapter class in togglebutton
    public void scheduleAlarm(Context context,AlarmManager alarmManager, int alarmId , String label , int hour, int minute) {
       // AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyBroadcastReceiver.class);
        intent.putExtra("id" , alarmId);
        intent.putExtra("label" , label);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, alarmId, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calculate the alarm time based on the selected hour and minute
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, hour);
        calendar.set(Calendar.MINUTE, minute);
        calendar.set(Calendar.SECOND, 0);

        // If the alarm time is in the past, add one day to it
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Set the alarm to trigger at the specified time
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);



    }
    private void upDateAlarm(Alarm alarm, int id ){
        //setting values to alarm class for after get values from alarm class to put in database
        alarm.setId(id);
        alarm.setLabel(label.getText().toString());
        alarm.setHour(timePicker.getHour());
        alarm.setMinute(timePicker.getMinute());
        //making database class object for calling update method of database class
        helper.updateAlarm(alarm);
        //this will finish current activity when we move to next activity
        finish();
        Intent i = new Intent(this, AlarmManagerActivity.class);
        startActivity(i);
        Toast.makeText(this, "data updated", Toast.LENGTH_SHORT).show();
    }

    private void deleteAlarm(int id) {
        helper.deleteAlarm(id);
        //for adapter constructer also pass to refreshlist
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();
        MyAdapter adapter = new MyAdapter(this,alarmArrayList);
        adapter.refreshlist(alarmArrayList);
        Intent i = new Intent(AddNewAlarm.this , AlarmManagerActivity.class);
        startActivity(i);
        Toast.makeText(this, "Alarm Deleted from Database", Toast.LENGTH_LONG).show();
    }

    private void exist() {
        findViewById(R.id.imgbtnCancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(AddNewAlarm.this , AlarmManagerActivity.class);
                startActivity(i);
            }
        });

    }
}