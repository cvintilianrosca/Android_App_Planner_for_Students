package com.example.studentplanner;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.studentplanner.database.Repository;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.database.entities.Teachers;
import com.example.studentplanner.database.entities.Timetable;
import com.example.studentplanner.database.relations.SubjectWithGrades;

import java.sql.Time;
import java.util.List;

public class DatabaseViewModel extends AndroidViewModel {
    private Repository repository;
    private LiveData<List<Subject>> allSubjects;
    private LiveData<List<Teachers>> allTeachers;
    private LiveData<List<Teachers>> teacherWithName;
    private LiveData<List<Grades>> allGrades;
    private LiveData<List<Exams>> allExams;
    private LiveData<List<Tasks>> allTasks;
    private LiveData<List<Timetable>> allTimetables;

    public DatabaseViewModel(@NonNull Application application) {
        super(application);
        repository = new Repository(application);
        allSubjects = repository.getSubjectsList();
        allTeachers = repository.getTeacherList();
        allGrades = repository.getAllGrades();
        allExams = repository.getAllExams();
        allTasks = repository.getAllTasks();
        allTimetables = repository.getAllTimetables();
    }

    public void insert(Subject subject){
        repository.insert(subject);
    }

    public void insert(Teachers teachers){
        repository.insert(teachers);
    }

    public void insert(Grades grades){repository.insert(grades);}

    public void insert(Tasks tasks){repository.insert(tasks);}

    public void insert(Exams exams){repository.insert(exams);}

    public void insert(Timetable timetable){repository.insert(timetable);}


    public void  update(Subject subject){
        repository.update(subject);
    }

    public void  update(Teachers teachers){
        repository.update(teachers);
    }

    public void  update(Grades grades){
        repository.update(grades);
    }

    public void  update(Tasks tasks){repository.update(tasks);}

    public void  update(Exams exams){repository.update(exams);}

    public void  update(Timetable timetable){repository.update(timetable);}


    public void  delete(Subject subject){
        repository.delete(subject);
    }

    public void  delete(Teachers teachers){
        repository.delete(teachers);
    }

    public void  delete(Grades grades){
        repository.delete(grades);
    }

    public void  delete(Tasks tasks){repository.delete(tasks);}

    public void  delete(Exams exams){repository.delete(exams);}

    public void  delete(Timetable timetable){repository.delete(timetable);}


    public void deleteAllSubjects(){
        repository.deleteAllSubjects();
    }

    public void deleteAllTeachers(){
        repository.deleteAllTeachers();
    }
    public void deleteAllTimetables(){
        repository.deleteAllTimetables();
    }

    public void deleteAllGrades(){repository.deleteAllGrades();}

    public void deleteAllTasks(){repository.deleteAllTasks();}

    public void deleteAllExams(){repository.deleteAllExams();}


    public LiveData<List<Subject>> getAllSubjects(){
        return allSubjects;
    }

    public LiveData<List<Teachers>> getAllTeachers(){
        return allTeachers;
    }

    public LiveData<List<Grades>> getAllGrades(){return allGrades; }

    public LiveData<List<Tasks>> getAllTasks(){return allTasks;}

    public LiveData<List<Exams>> getAllExams(){return allExams;}



    public LiveData<List<Teachers>> getTeacherDataWithName(String name){
        return repository.getTeacherWithName(name);
    }

    public LiveData<List<Subject>> getSubjectWithName(String name){
        return repository.getSubjectWithName(name);
    }

    public LiveData<List<Subject>> getSubjectWithId(int id){
        return repository.getSubjectWithId(id);
    }

    public LiveData<List<SubjectWithGrades>> getSubjectsWithGrades(int id){
        return repository.getSubjectWithGrades(id);
    }

    public LiveData<List<Timetable>> getAllTimetables(){
        return repository.getAllTimetables();
    }

    public LiveData<List<SubjectWithGrades>> getAllCombinationsOfSubjectsWithGrades(){
        return repository.getAllCombinationsOfSubjectsWithGrades();
    }
}
