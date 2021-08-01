package com.example.studentplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.sql.Date;

@Entity(tableName = "grades_table")
public class Grades {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private int subjectId;
    private int value;

    public Grades(int subjectId, int value) {
        this.subjectId = subjectId;
        this.value = value;
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


