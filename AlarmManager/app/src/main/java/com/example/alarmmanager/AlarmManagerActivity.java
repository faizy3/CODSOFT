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

          list = new ArrayList<>();

            Alarm alarm ;
            list.clear();
            MyDBHelper helper = new MyDBHelper(this);
            String qry = "SELECT * FROM " + "Alarm" ;
            SQLiteDatabase db = helper.getWritableDatabase();
            Cursor cursor  =db.rawQuery(qry , null);
            while(cursor.moveToNext()){
                alarm = new Alarm();
                alarm.setId(cursor.getInt(0));
                alarm.setLabel(cursor.getString(1));
                alarm.setHour(cursor.getInt(2));
                alarm.setMinute(cursor.getInt(3));
                alarm.setStatus(cursor.getInt(4) !=0  );
                list.add(alarm);
            }
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
