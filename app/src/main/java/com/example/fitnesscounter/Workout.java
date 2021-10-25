package com.example.fitnesscounter;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Workout implements Serializable {
    private int time;
    private String name;
    private String date;

    public Workout(int time, String name){
        this.time = time;
        this.name = name;
    }

    public int getTime() {
        return time;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public void setDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        this.date = sdf.format(new Date());
    }
}
