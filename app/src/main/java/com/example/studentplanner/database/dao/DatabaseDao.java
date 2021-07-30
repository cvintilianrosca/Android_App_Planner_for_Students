package com.example.studentplanner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;

import java.util.List;

@Dao
public interface DatabaseDao {
    @Insert
    void insert(Subject subject);

    @Update
    void update(Subject subject);

    @Delete
    void delete(Subject subject);

    @Query("DELETE FROM subject_table")
    void deleteAllSubjects();

    @Query("SELECT * FROM subject_table ORDER BY name DESC")
    LiveData<List<Subject>> getAllSubjects();

    @Insert
    void insert(Teachers teachers);

    @Update
    void update(Teachers teachers);

    @Delete
    void delete(Teachers teachers);

    @Query("DELETE FROM teachers_table")
    void deleteAllTeachers();




}
