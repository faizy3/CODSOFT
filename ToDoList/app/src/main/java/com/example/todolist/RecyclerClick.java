package com.example.todolist;

import android.view.View;

public interface RecyclerClick {
    public void onClick(int positon , View v);
    public void onLongClick(int positon , View v);
}
