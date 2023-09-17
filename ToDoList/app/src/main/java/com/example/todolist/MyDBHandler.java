package com.example.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class MyDBHandler extends SQLiteOpenHelper {
    private static final int VERSION = 1;
   // private static final String Name = "ToDoListDatabase";
    private static final String TableName = "ToDo";
    public MyDBHandler(Context context) {
        super(context, "ToDoListDB",null, VERSION);
    }

    @Override
    public void onCreate( SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE ToDO (id integer primary key AUTOINCREMENT, task varchar,description varchar, status boolean , taskhour integer , taskminute integer )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + TableName);
        onCreate(sqLiteDatabase);

    }

    public long insert(ToDo toDo){
        SQLiteDatabase database = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task", toDo.getTask());
        contentValues.put("description" , toDo.getDescription());
        contentValues.put("status", false);
        contentValues.put("taskhour" , toDo.getHour());
        contentValues.put("taskminute" , toDo.getMinute());
        long generatedId = database.insert(TableName,null,contentValues);
        return generatedId;
    }
    public void  updateStatus(int id, boolean status){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task" , "completed");
        contentValues.put("description" , "");
        contentValues.put("status" , status);
        db.update("ToDo" , contentValues ,"id" + "=?", new String[] {String.valueOf(id)});
    }
    public void  updateTask(ToDo toDo){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("task" , toDo.getTask());
        contentValues.put("description" , toDo.getDescription());
        contentValues.put("status" , false);
        contentValues.put("taskhour" , toDo.getHour());
        contentValues.put("taskminute" , toDo.getMinute());
        db.update("ToDo" , contentValues ,"id" + "=?", new String[]{String.valueOf(toDo.getId())});
    }
    public void deleteTask(int id){
        SQLiteDatabase db = getWritableDatabase();
        db.delete("ToDo" , "id" + "=?", new String[]{String.valueOf(id)});
    }

}
