package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import java.util.Calendar;

public class AddNewTask extends AppCompatActivity {
    EditText edittxtTitle , edittxtDescription;
    Button btnSave_Update_Task;
    TimePicker taskTimePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_task);
        btnSave_Update_Task = findViewById(R.id.btnSaveTask);
        edittxtTitle = findViewById(R.id.edittxtAddNewTask);
        edittxtDescription = findViewById(R.id.edittxtDescription);
        taskTimePicker = findViewById(R.id.timepicker);
        //getting data from adapter which user swipe on specific task
        Intent editIntent = getIntent();
        boolean isUpdate = editIntent.getBooleanExtra("status" , false);
        if(isUpdate){
            //this code only run if userswipe rightside
            int id = editIntent.getExtras().getInt("id");
            String task = editIntent.getExtras().getString("task");
            String description = editIntent.getExtras().getString("description");
            int taskhour = editIntent.getExtras().getInt("taskhour");
            int taskminute = editIntent.getExtras().getInt("taskminute");
            edittxtTitle.setText(task);
            edittxtDescription.setText(description);
            taskTimePicker.setHour(taskhour);
            taskTimePicker.setMinute(taskminute);
            btnSave_Update_Task.setText("Update");
            btnSave_Update_Task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ToDo edittask = new ToDo();
                    updateTaskIntoDB(edittask , id );
                    edittask.setId(id);
                    scheduleAlarm(edittask);
                }
            });

        }else {
            btnSave_Update_Task.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if(edittxtTitle.getText().toString().isEmpty()){
                        Intent i = new Intent(AddNewTask.this , Dashboard.class);
                        startActivity(i);
                        Toast.makeText(AddNewTask.this, "Enter task title then try again", Toast.LENGTH_SHORT).show();
                    }else{
                        //create new instance of class and set these values through setter for that class instance
                        ToDo toDo = new ToDo();
                        toDo.setTask(edittxtTitle.getText().toString());;
                        toDo.setDescription(edittxtDescription.getText().toString());
                        toDo.setHour(taskTimePicker.getHour());
                        toDo.setMinute(taskTimePicker.getMinute());

                        //calling database handler class for insert data into database
                        MyDBHandler handler = new MyDBHandler(AddNewTask.this);
                        //when data is added into database then because id is auto increment therefore
                        // we are retrieving that id and set to todo class instance for later user
                        int alarmId =(int) handler.insert(toDo);
                        toDo.setId(alarmId);
                        Toast.makeText(AddNewTask.this, "data insertion complete", Toast.LENGTH_SHORT).show();
                        //schedule alarm for ringing
                        scheduleAlarm(toDo);
                        //
                        Intent i = new Intent(AddNewTask.this , Dashboard.class);
                        startActivity(i);
                        finish();
                    }
                }
            });
        }
    }

    private void updateTaskIntoDB(ToDo toDo,int id) {
        MyDBHandler handler = new MyDBHandler(this );
        toDo.setId(id);
        toDo.setTask(edittxtTitle.getText().toString());
        toDo.setDescription(edittxtDescription.getText().toString());
        toDo.setHour(taskTimePicker.getHour());
        toDo.setMinute(taskTimePicker.getMinute());
        handler.updateTask(toDo);
        Intent intent = new Intent(this,Dashboard.class);
        startActivity(intent);
        finish();
        Toast.makeText(this, "Date Update in DB", Toast.LENGTH_SHORT).show();
    }
    public void scheduleAlarm(ToDo toDo) {

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this,TodoBroadcastReceiver.class);
        int id = toDo.getId();
        String task = toDo.getTask();
        intent.putExtra("id" ,id );
        intent.putExtra("task" , task);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, id, intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Calculate the alarm time based on the selected hour and minute
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, toDo.getHour());
        calendar.set(Calendar.MINUTE, toDo.getMinute());
        calendar.set(Calendar.SECOND, 0);

        // If the alarm time is in the past, add one day to it
        if (calendar.getTimeInMillis() <= System.currentTimeMillis()) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
        }

        // Set the alarm to trigger at the specified time
        alarmManager.set(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
        Toast.makeText(this, "Alarm set", Toast.LENGTH_LONG).show();
        Log.d("ScheduledAlarmTime", "Scheduled alarm for: " + calendar.getTime().toString());

    }
}