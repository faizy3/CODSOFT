package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class ToDoAdapter extends RecyclerView.Adapter<ToDoAdapter.ViewHolder> {
    ArrayList<ToDo> todolist;
    private Dashboard activity;
    private Context context;
    RecyclerClick recyclerClick=null;

    public ToDoAdapter(Context context,ArrayList<ToDo> todolist ) {

        this.context = context;
        this.todolist = todolist;
    }
    public ViewHolder onCreateViewHolder(ViewGroup parent , int viewType){
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_layout, parent, false);
        return  new ViewHolder(view);
    }
    public void onBindViewHolder(ViewHolder holder , int postion){
        ToDo item = todolist.get(postion);
        holder.titletxt.setText(item.getTask());
        String taskTime = item.toString();
        holder.descriptiontxt.setText("Today "+taskTime+", "+item.getDescription());
        holder.taskcheckbox.setChecked(false);
    }
    public int getItemCount(){
        return todolist.size();
    }

    public void setListener(RecyclerClick recyclerClick) {
        this.recyclerClick = recyclerClick;
    }

    public  class ViewHolder extends RecyclerView.ViewHolder{

        CheckBox taskcheckbox;
        TextView titletxt , descriptiontxt;
        public ViewHolder(View v){
            super(v);
            taskcheckbox = v.findViewById(R.id.taskcheckbox);
            titletxt = v.findViewById(R.id.titletxt);
            descriptiontxt = v.findViewById(R.id.descriptiontxt);
            taskcheckbox = v.findViewById(R.id.taskcheckbox);
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    recyclerClick.onClick(getLayoutPosition() , v);
                }
            });

            v.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    recyclerClick.onLongClick(getLayoutPosition() , v);
                    return true;
                }
            });
            taskcheckbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    ToDo toDo = todolist.get(getAdapterPosition());
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("Task");
                    builder.setMessage("do you complete this task");
                    //check if the status is true in database its means that task already complete
                    if(toDo.getStatus()){
                        if(isChecked){
                            Toast.makeText(context, "You already Completed this task", Toast.LENGTH_SHORT).show();
                        }
                    }else if(isChecked){
                        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                MyDBHandler handler = new MyDBHandler(context);

                                int id  = toDo.getId();
                                boolean status = isChecked;
                                toDo.setTask("completed");
                                toDo.setStatus(status);
                                handler.updateStatus(id,status);
                                notifyDataSetChanged();

                            }
                        });
                        builder.setNegativeButton("NO", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                notifyItemChanged(getAdapterPosition());
                            }
                        });
                        AlertDialog dialog = builder.create();
                        dialog.show();

                    }
                }
            });

        }
    }
    public Context getContext(){
        return context;
    }

    public void deleteItem(int position){
        MyDBHandler handler  = new MyDBHandler(context);
        ToDo item = todolist.get(position);
        int id = item.getId();
        handler.deleteTask(id);
        todolist.remove(position);
        notifyItemRemoved(position);
    }
    public void EditTask(int position){
        ToDo editTask = todolist.get(position);
        int id = editTask.getId();
        Intent editIntent = new Intent(context , AddNewTask.class);
        editIntent.putExtra("id" , id);
        editIntent.putExtra("task" ,editTask.getTask() );
        editIntent.putExtra("description" , editTask.getDescription());
        editIntent.putExtra("status"  , true);
        int hour=editTask.getHour();
        int minute=editTask.getMinute();
        editIntent.putExtra("taskhour"  ,hour );
        editIntent.putExtra("taskminute"  , minute);
        context.startActivity(editIntent);
        ((Activity) getContext()).finish();
    }


}
