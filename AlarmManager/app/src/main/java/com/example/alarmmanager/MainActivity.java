package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {
    TextView currentTime;
    ImageButton imgbtnAdd , imgbtnsetting;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        currentTime = findViewById(R.id.currenttime);
        imgbtnAdd = findViewById(R.id.imgbtnAdd);
        imgbtnsetting = findViewById(R.id.imgbtnSetting);


        //show current time with running seconds
        Handler handler = new Handler();
        Runnable  UpdateTimeRunnable = new Runnable() {
            @Override
            public void run() {
                //current time
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd-MM-yyyy  hh:mm:ss "  );
                String currentDateAndTime  = simpleDateFormat.format(new Date());
                currentTime.setText("CurrentDateTime\n"+currentDateAndTime);
                handler.postDelayed(this , 1000);

            }
        };
        handler.post(UpdateTimeRunnable);
        imgbtnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this , AddNewAlarm.class);
                startActivity(i);
            }
        });
        imgbtnsetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(MainActivity.this , AlarmManagerActivity.class);
                startActivity(i);
            }
        });
    }
}