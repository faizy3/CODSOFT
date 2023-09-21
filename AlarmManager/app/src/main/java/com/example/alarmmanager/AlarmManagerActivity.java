package com.example.alarmmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.util.ArrayList;
import java.util.Calendar;

public class AlarmManagerActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    public MyAdapter myadapter;
    ArrayList<Alarm> list;

    @Override
   public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_manager);
        recyclerView = findViewById(R.id.amrecylcerview);
        //creating new instance of MyDbHelper class
        MyDBHelper helper = new MyDBHelper(this);
        //getAlarmDetail method fetch data from DB and store in arrayList and return that arraylist
        list = helper.getalarmdetail();
        myadapter  =  new MyAdapter(this,list);
        if(list.isEmpty()){
            Toast.makeText(this, "List is empty please add Alarm first", Toast.LENGTH_SHORT).show();
        }else {
            myadapter.notifyDataSetChanged();
        }
        //set list to adapter and bind view
        LinearLayoutManager manager = new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        recyclerView.setLayoutManager(manager);

        recyclerView.setAdapter(myadapter);
    }
}
