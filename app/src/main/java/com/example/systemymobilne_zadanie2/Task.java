package com.example.systemymobilne_zadanie2;

import android.icu.text.DateFormat;

import java.util.Date;
import java.util.UUID;
import java.util.Calendar;

public class Task {
    private UUID id;
    private String name;
    private Date date;
    private boolean done;
    private Category category;

    public Task(){
        id = UUID.randomUUID();
        date = new Date();
        category = Category.HOME;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getDate() {
        return date;
    }

    public void setDone(boolean done) {
        this.done = done;
    }

    public String getName() {
        return name;
    }

    public UUID getId() {
        return id;
    }

    public boolean isDone() {
        return done;
    }

    public void setCategory(Category category) {
        this.category = category;
    }



    public void setDate(Date time) {date = time; }

    public Category getCategory() {
        return  category;
    }
}
