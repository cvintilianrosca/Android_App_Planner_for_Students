package com.example.studentplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "exam_table" )
public class Exams {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private int subjectId;
    private String date;
    private int formExam;
    private String details;

    public Exams( String name, int subjectId, String date, int formExam, String details) {
        this.name = name;
        this.subjectId = subjectId;
        this.date = date;
        this.formExam = formExam;
        this.details = details;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(int subjectId) {
        this.subjectId = subjectId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getFormExam() {
        return formExam;
    }

    public void setFormExam(int formExam) {
        this.formExam = formExam;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}
