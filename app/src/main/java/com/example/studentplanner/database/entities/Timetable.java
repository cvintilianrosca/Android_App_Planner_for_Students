package com.example.studentplanner.database.entities;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.List;

@Entity(tableName = "timetable_table")
public class Timetable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String subject;
    private String day;
    private String start;
    private String end;
    private String location;
    private String teacher;
    private String note;

    public Timetable(String subject, String day, String start, String end, String location, String teacher, String note) {
        this.subject = subject;
        this.day = day;
        this.start = start;
        this.end = end;
        this.location = location;
        this.teacher = teacher;
        this.note = note;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public String getSubject() {
        return subject;
    }

    public String getDay() {
        return day;
    }

    public String getStart() {
        return start;
    }

    public String getEnd() {
        return end;
    }

    public String getLocation() {
        return location;
    }

    public String getTeacher() {
        return teacher;
    }

    public String getNote() {
        return note;
    }
}
