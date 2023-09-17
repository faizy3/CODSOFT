package com.example.todolist;

public class ToDo {
    private int id;
    private boolean status;
    private String task;
    private  String description ;
    private String taskTime;
    private int hour;
    private int minute;

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    public int getId() {
        return id;
    }

    public boolean getStatus() {
        return status;
    }

    public String getDescription() {
        return description;
    }

    public String getTask() {
        return task;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public String toString(){
        String hourstring, minutestring, format;
        //for hour
        if(hour > 12){
            hourstring = (hour -12) + "";
            format = "PM";
        }else if(hour == 0){
            hourstring = "12";
            format = "Am";
        }else if(hour == 12){
            hourstring = "12";
            format = "PM";
        }else{
            hourstring = hour + "";
            format = "AM";
        }
        //for minute

        if(minute <10){
            minutestring = "0" + minute;
        }else{
            minutestring = "" + minute;
        }
        return hourstring + ":" +  minutestring + format;

    }
    public String toString(long hour , long minute){
        String hourstring, minutestring, format;
        //for hour
        if(hour > 12){
            hourstring = (hour -12) + "";
            format = "PM";
        }else if(hour == 0){
            hourstring = "12";
            format = "Am";
        }else if(hour == 12){
            hourstring = "12";
            format = "PM";
        }else{
            hourstring = hour + "";
            format = "AM";
        }
        //for minute

        if(minute <10){
            minutestring = "0" + minute;
        }else{
            minutestring = "" + minute;
        }
        return hourstring + ":" +  minutestring + format;

    }
}
