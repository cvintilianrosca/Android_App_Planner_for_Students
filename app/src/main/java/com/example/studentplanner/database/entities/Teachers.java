package com.example.studentplanner.database.entities;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "teachers_table")
public class Teachers {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private String name;
    private String surname;
    private String phoneNumber;
    private String email;
    private String address;

    public int getFlagFirst() {
        return flagFirst;
    }

    public void setFlagFirst(int flagFirst) {
        this.flagFirst = flagFirst;
    }

    private int flagFirst;

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Teachers(String name, String surname, String phoneNumber, String email, String address) {
        this.name = name;
        this.surname = surname;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.address = address;
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

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }
}
