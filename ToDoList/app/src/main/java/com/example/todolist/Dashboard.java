package com.example.todolist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Dashboard extends AppCompatActivity {
    RecyclerView recyclerView ;
    ToDoAdapter adapter;
    ArrayList<ToDo> todolist;
    Button btnAddNewTask;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_dashboard);
        btnAddNewTask = findViewById(R.id.btnAddNewTask);

        btnAddNewTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Dashboard.this , AddNewTask.class);
                startActivity(i);
                finish();
            }
        });
        //creating new array list of for todo class instance to store all data and after retrieve data from database
        todolist = new ArrayList<>();
        //provide that new list arraylist to load method
        loadDateFromDB(todolist);



        //getting access of recycler view
        recyclerView = findViewById(R.id.taskrecyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ToDoAdapter(Dashboard.this, todolist);
        recyclerView.setAdapter(adapter);
        //attach Item touch helper class with recycler view to swipe function
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new RecyclerItemTouchHelper(adapter));
        itemTouchHelper.attachToRecyclerView(recyclerView);
        //setting click listenere to recylcerview
        adapter.setListener(new RecyclerClick() {
            @Override
            public void onClick(int position, View v) {
                adapter.notifyDataSetChanged();
                ToDo toDo = todolist.get(position);
                boolean status = toDo.getStatus();
                if(status){
                    Toast.makeText(Dashboard.this, "You completed task!", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(Dashboard.this, "You don't complete task yet!", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onLongClick(int position, View v) {
                adapter.notifyDataSetChanged();
                ToDo toDo = todolist.get(position);
                boolean status = toDo.getStatus();
                if(status){
                    AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
                    builder.setTitle("Delete");
                    builder.setMessage("do you delete completed task");
                    builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            MyDBHandler handler = new MyDBHandler(Dashboard.this);
                            handler.deleteTask(toDo.getId());
                            todolist.remove(position);
                            adapter.notifyItemRemoved(position);
                        }
                    });
                    builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            adapter.notifyItemChanged(position);
                        }
                    });
                    AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    Toast.makeText(Dashboard.this, "first complete task by tick on checkBox", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    private void loadDateFromDB(ArrayList<ToDo> todolist) {
        MyDBHandler Helper= new MyDBHandler(this);
        String qry= "Select * From ToDo";
        SQLiteDatabase database =Helper.getReadableDatabase();


        Cursor cursor = database.rawQuery(qry,null);
        ToDo todo;
        //list.clear();

        while(cursor.moveToNext()) {
            todo = new ToDo();
            todo.setId(Integer.parseInt(cursor.getString(0)));
            todo.setTask(cursor.getString(1));
            todo.setDescription(cursor.getString(2));
            todo.setStatus((cursor.getInt(3)) !=0);
            todo.setHour(cursor.getInt(4));
            todo.setMinute(cursor.getInt(5));
            todolist.add(todo);
        }
    }

}