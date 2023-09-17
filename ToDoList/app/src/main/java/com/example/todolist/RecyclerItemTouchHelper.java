package com.example.todolist;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerItemTouchHelper extends ItemTouchHelper.SimpleCallback {
    private ToDoAdapter adapter;

    public RecyclerItemTouchHelper(ToDoAdapter adapter) {
            //this is constructer
        super(0 , ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.adapter = adapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        //on swipe method called when user swipe left or right
        final int position = viewHolder.getAdapterPosition();
        if (direction == ItemTouchHelper.LEFT){
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Delete Task");
            builder.setMessage("Are you Sure You want to delete the task");
            //called when user swipe left and click on confirm button
            builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.deleteItem(position);
                }
            });
            //called when user swipe left and click on cancel button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();
        }else if(direction == ItemTouchHelper.RIGHT){
            //this code build new dialouge
            AlertDialog.Builder builder = new AlertDialog.Builder(adapter.getContext());
            builder.setTitle("Edit Task");
            builder.setMessage("Do you want to edit task");
            //called when user swipe left and click on yes button
            builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.EditTask(position);
                    //this line remove current dashboard activity from android activity stack
                    ((Activity) adapter.getContext()).finish();
                    adapter.notifyDataSetChanged();
                }
            });
            //called when user swipe left and click on cancel button
            builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    adapter.notifyItemChanged(position);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

    }
    public void onChildDraw(Canvas c , RecyclerView recyclerView , RecyclerView.ViewHolder viewHolder, float dx, float dy, int actionstate , boolean isCurrentlyActive){
        super.onChildDraw(c, recyclerView , viewHolder ,dx , dy , actionstate , isCurrentlyActive);

        Drawable icon;
        ColorDrawable background;
        View v = viewHolder.itemView;
        int backgroundCornerOffSet = 20;

        //dx>0 meaning swipe left side
        if(dx>0){
            icon = ContextCompat.getDrawable(adapter.getContext() , R.drawable.baseline_edit_24 );
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext() , R.color.teal_700));
        }else {
            icon = ContextCompat.getDrawable(adapter.getContext() , R.drawable.baseline_delete_forever_24 );
            background = new ColorDrawable(ContextCompat.getColor(adapter.getContext() , R.color.red));
        }
        int iconmargin = (v.getHeight() - icon.getIntrinsicHeight()) / 2;
        int icontop = (v.getTop() + (v.getHeight() - icon.getIntrinsicHeight())) / 2;
        int iconbuttom = icontop + icon.getIntrinsicHeight();
        if(dx>0){//swipe right
            int iconLeft = v.getLeft() + iconmargin;
            int iconRight = v.getLeft() + iconmargin + icon.getIntrinsicWidth();
            icon.setBounds(iconLeft ,icontop, iconRight , iconbuttom );
            background.setBounds(v.getLeft() , v.getTop() , v.getLeft() + ((int)dx) + backgroundCornerOffSet , v.getBottom());
        }else if(dx<0){
            int iconLeft = v.getRight() - iconmargin - icon.getIntrinsicWidth();
            int iconRight = v.getRight() - iconmargin ;
            icon.setBounds(iconLeft ,icontop, iconRight , iconbuttom );
            background.setBounds(v.getRight() + ((int)dx)- backgroundCornerOffSet , v.getTop() , v.getRight() , v.getBottom());
        }else{
            background.setBounds(0 , 0,0,0);
        }
        background.draw(c);
        icon.draw(c);
    }
}
