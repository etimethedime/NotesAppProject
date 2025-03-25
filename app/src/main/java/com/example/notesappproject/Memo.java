package com.example.notesappproject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class Memo {
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("MM-dd-yyyy", Locale.US);
    private int id;
    private String title;
    private Calendar date;
    private String body;
    private int priority;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return dateFormat.format(date.getTime());
    }
    public void setDate(String dateStr) {
        try {
            Calendar cal = Calendar.getInstance();
            cal.setTime(dateFormat.parse(dateStr));
            this.date = cal;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }
}
