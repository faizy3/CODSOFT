package com.example.alarmmanager;

public class Alarm {
    private int id;
    private  String label;
    private int hour;
    private int minute;
    private boolean status;

    private String alarm_time;
    public String getAlarm_time() {
        return alarm_time;
    }



    public int getId() {
        return id;
    }

    public String getLabel() {
        return label;
    }

    public int getHour() {
        return hour;
    }

    public int getMinute() {
        return minute;
    }

    public boolean isStatus() {
        return status;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public void setMinute(int minute) {
        this.minute = minute;
    }

    public void setAlarm_time(String alarm_time) {
        this.alarm_time = alarm_time;
    }

    public void setStatus(boolean status) {
        this.status = status;
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
