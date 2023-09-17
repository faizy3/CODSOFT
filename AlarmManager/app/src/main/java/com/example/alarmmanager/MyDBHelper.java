package com.example.alarmmanager;

import static java.sql.Types.INTEGER;
import static java.text.Collator.PRIMARY;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class MyDBHelper extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "AlarManager";
    private static final String TABLE_NAME = "Alarm";

    private static final String COLUMN_ALARM_ID = "alarm_id";
    private static final String COLUMN_ALARM_LABEL = "alarm_label";
    private static final String COLUMN_ALARM_HOUR = "alarm_hour";
    private static final String COLUMN_ALARM_MINUTE = "alarm_minute";
    private static final String COLUMN_ALARM_STATUS = "alarm_status";
    public MyDBHelper(Context context){
        super(context , DATABASE_NAME , null, DATABASE_VERSION);

    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
            String qry = "CREATE TABLE " + TABLE_NAME + " ( "
                    + COLUMN_ALARM_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + COLUMN_ALARM_LABEL + " VARCHAR, "
                    + COLUMN_ALARM_HOUR + " INTEGER, "
                    + COLUMN_ALARM_MINUTE + " INTEGER, "
                    + COLUMN_ALARM_STATUS + " BOOLEAN"
                    + " ) ";
            sqLiteDatabase.execSQL(qry);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(sqLiteDatabase);

    }
    public long addAlarm(Alarm alarm){
        SQLiteDatabase  db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALARM_LABEL , alarm.getLabel());
        contentValues.put(COLUMN_ALARM_HOUR , alarm.getHour());
        contentValues.put(COLUMN_ALARM_MINUTE , alarm.getMinute());
        contentValues.put(COLUMN_ALARM_STATUS , alarm.isStatus());
        //this line insert data into database and return new generated id from database
        long generatedId =  db.insert(TABLE_NAME , null ,contentValues);
        db.close();
        return generatedId;
    }
    public List<Alarm> getalarmdetail(){
        ArrayList<Alarm> alarmArrayList = new ArrayList<>();
        String qry = "SELECT * FROM " + TABLE_NAME ;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor  =db.rawQuery(qry , null);
        while(cursor.moveToNext()){
            Alarm alarm = new Alarm();
            alarm.setId(cursor.getInt(0));
            alarm.setLabel(cursor.getString(1));
            alarm.setAlarm_time(cursor.getString(2));
            alarm.setStatus(cursor.getInt(3) !=0  );
            alarmArrayList.add(alarm);
        }

        return alarmArrayList;
    }
    public void updateStatus(Alarm alarm ){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALARM_STATUS , alarm.isStatus());
        db.update(TABLE_NAME , contentValues ,COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(alarm.getId())});
        db.close();
    }
    public void updateAlarm(Alarm alarm){
        SQLiteDatabase db = getWritableDatabase();

        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_ALARM_LABEL , alarm.getLabel());
        contentValues.put(COLUMN_ALARM_HOUR , alarm.getHour());
        contentValues.put(COLUMN_ALARM_MINUTE , alarm.getMinute());
        contentValues.put(COLUMN_ALARM_STATUS , true);
        db.update(TABLE_NAME , contentValues ,COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(alarm.getId())});
        db.close();
    }

    public void deleteAlarm(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TABLE_NAME , COLUMN_ALARM_ID + "=?", new String[]{String.valueOf(id)});
    }
}
