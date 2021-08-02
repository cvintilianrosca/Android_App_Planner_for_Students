package com.example.studentplanner;

public class TaskOrExam {
    private String Title;
    private String Details;
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public TaskOrExam(String title, String details, int type) {
        Title = title;
        Details = details;
        this.type = type;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getDetails() {
        return Details;
    }

    public void setDetails(String details) {
        Details = details;
    }
}
