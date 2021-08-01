package com.example.studentplanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentplanner.database.Repository;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Subject>> allSubjects;
    private LiveData<List<Teachers>> allTeachers;
    private LiveData<List<Teachers>> teacherWithName;
    private LiveData<List<Grades>> allGrades;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allSubjects = repository.getSubjectsList();
        allTeachers = repository.getTeacherList();
        allGrades = repository.getAllGrades();
    }

    public void insert(Subject subject){
        repository.insert(subject);
    }

    public void insert(Teachers teachers){
        repository.insert(teachers);
    }

    public void insert(Grades grades){repository.insert(grades);}

    public void  update(Subject subject){
        repository.update(subject);
    }

    public void  update(Teachers teachers){
        repository.update(teachers);
    }

    public void  update(Grades grades){
        repository.update(grades);
    }

    public void  delete(Subject subject){
        repository.delete(subject);
    }

    public void  delete(Teachers teachers){
        repository.delete(teachers);
    }

    public void  delete(Grades grades){
        repository.delete(grades);
    }

    public void deleteAllSubjects(){
        repository.deleteAllSubjects();
    }

    public void deleteAllTeachers(){
        repository.deleteAllTeachers();
    }

    public void deleteAllGrades(){repository.deleteAllGrades();}

    public LiveData<List<Subject>> getAllSubjects(){
        return allSubjects;
    }

    public LiveData<List<Teachers>> getAllTeachers(){
        return allTeachers;
    }

    public LiveData<List<Grades>> getAllGrades(){return allGrades; }

    public LiveData<List<Teachers>> getTeacherDataWithName(String name){
        return repository.getTeacherWithName(name);
    }

    public LiveData<List<Subject>> getSubjectWithName(String name){
        return repository.getSubjectWithName(name);
    }
}
