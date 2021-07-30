package com.example.studentplanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentplanner.database.Repository;
import com.example.studentplanner.database.entities.Subject;

import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Subject>> allSubjects;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allSubjects = repository.getSubjectsList();
    }

    public void insert(Subject subject){
        repository.insert(subject);
    }

    public void  update(Subject subject){
        repository.update(subject);
    }
    public void  delete(Subject subject){
        repository.delete(subject);
    }
    public void deleteAllSubjects(){
        repository.deleteAllSubjects();
    }
    public LiveData<List<Subject>> getAllSubjects(){
        return allSubjects;
    }
}
