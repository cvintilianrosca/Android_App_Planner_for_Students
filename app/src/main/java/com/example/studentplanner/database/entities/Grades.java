package com.example.studentplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import com.example.studentplanner.addentities.AddGradeActivity;

import java.sql.Date;

@Entity(tableName = "grades_table")
public class Grades {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int subjectId;
    private String nameSubject;
    private int value;
    private String date;
    private String term;
    private String type;
    private String note;

    public Grades(int subjectId, String nameSubject, int value, String date, String term, String type, String note) {
        this.subjectId = subjectId;
        this.nameSubject = nameSubject;
        this.value = value;
        this.date = date;
        this.term = term;
        this.type = type;
        this.note = note;
    }

    public String getNameSubject() {
        return nameSubject;
    }

    public void setNameSubject(String nameSubject) {
        this.nameSubject = nameSubject;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTerm() {
        return term;
    }

    public void setTerm(String term) {
        this.term = term;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }



    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}


