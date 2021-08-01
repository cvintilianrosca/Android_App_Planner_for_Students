package com.example.studentplanner.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.studentplanner.database.dao.DatabaseDao;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;
import com.example.studentplanner.database.relations.SubjectWithGrades;
import com.example.studentplanner.database.relations.TeacherWithSubjects;

import java.util.List;

public class Repository {
    private DatabaseDao databaseDao;
    private LiveData<List<Subject>> subjectsList;
    private LiveData<List<Teachers>> teacherList;


    public Repository(Application application){
        Database database = Database.getINSTANCE(application);
        databaseDao = database.databaseDao();
        subjectsList = databaseDao.getAllSubjects();
        teacherList  = databaseDao.getAllTeachers();
    }

    public List<TeacherWithSubjects> getTeacherWithSubjectsList(int id){
        return databaseDao.getTeacherWithSubjects(id);
    }

    public List<SubjectWithGrades> getSubjectWithGrades(int subjectId){
        return databaseDao.getSubjectWithGrades(subjectId);
    }

    public void insert(Subject subject){
        new InsertAsyncSubject(databaseDao).execute(subject);
    }

    public void insert(Teachers teachers){
        new InsertAsyncTeachers(databaseDao).execute(teachers);
    }
    public void insert(Grades grades){
        new InsertAsyncGrade(databaseDao).execute(grades);
    }

    public void update(Subject subject){
        new UpdateAsyncSubject(databaseDao).execute(subject);
    }

    public void update(Teachers teachers){
        new UpdateAsyncTeacher(databaseDao).execute(teachers);
    }

    public void update(Grades grades){
        new UpdateAsyncGrade(databaseDao).execute(grades);
    }

    public void delete(Subject subject){
        new DeleteAsyncSubject(databaseDao).execute(subject);
    }

    public void delete(Grades grades){
        new DeleteAsyncGrade(databaseDao).execute(grades);
    }

    public void delete(Teachers teachers){
        new DeleteAsyncTeacher(databaseDao).execute(teachers);
    }

    public void deleteAllSubjects(){
        new DeleteAllAsyncSubject(databaseDao).execute();
    }

    public void deleteAllTeachers(){
        new DeleteAllAsyncTeachers(databaseDao).execute();
    }

    public void deleteAllGrades(){new DeleteAllAsyncGrades(databaseDao).execute();}

    public LiveData<List<Subject>> getSubjectsList(){
        return subjectsList;
    }

    public LiveData<List<Teachers>> getTeacherList(){
        return teacherList;
    }


    private static class InsertAsyncSubject extends AsyncTask<Subject, Void, Void> {
        private DatabaseDao databaseDao;
        private InsertAsyncSubject(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Subject... subjects) {
            databaseDao.insert(subjects[0]);
            return null;
        }
    }
    private static class InsertAsyncGrade  extends AsyncTask<Grades, Void, Void> {
        private DatabaseDao databaseDao;
        private InsertAsyncGrade(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Grades... grades) {
            databaseDao.insert(grades[0]);
            return null;
        }
    }

    private static class InsertAsyncTeachers extends AsyncTask<Teachers, Void, Void> {
        private DatabaseDao databaseDao;
        private InsertAsyncTeachers(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Teachers... teachers) {
            databaseDao.insert(teachers[0]);
            return null;
        }
    }

    private static class UpdateAsyncSubject extends AsyncTask<Subject, Void, Void> {
        private DatabaseDao databaseDao;
        private UpdateAsyncSubject(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Subject... subjects) {
            databaseDao.update(subjects[0]);
            return null;
        }
    }
    private static class UpdateAsyncGrade  extends AsyncTask<Grades, Void, Void> {
        private DatabaseDao databaseDao;
        private UpdateAsyncGrade(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Grades... grades) {
            databaseDao.update(grades[0]);
            return null;
        }
    }

    private static class UpdateAsyncTeacher extends AsyncTask<Teachers, Void, Void> {
        private DatabaseDao databaseDao;
        private UpdateAsyncTeacher(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Teachers... teachers) {
            databaseDao.update(teachers[0]);
            return null;
        }
    }


    private static class DeleteAsyncSubject extends AsyncTask<Subject, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAsyncSubject(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Subject... subjects) {
            databaseDao.delete(subjects[0]);
            return null;
        }
    }
    private static class DeleteAsyncGrade  extends AsyncTask<Grades, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAsyncGrade(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Grades... grades) {
            databaseDao.delete(grades[0]);
            return null;
        }
    }

    private static class DeleteAsyncTeacher extends AsyncTask<Teachers, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAsyncTeacher(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Teachers... teachers) {
            databaseDao.delete(teachers[0]);
            return null;
        }
    }

    private static class DeleteAllAsyncSubject extends AsyncTask<Void, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAllAsyncSubject(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            databaseDao.deleteAllSubjects();
            return null;
        }
    }

    private static class DeleteAllAsyncTeachers extends AsyncTask<Void, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAllAsyncTeachers(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            databaseDao.deleteAllTeachers();
            return null;
        }
    }
    private static class DeleteAllAsyncGrades extends AsyncTask<Void, Void, Void> {
        private DatabaseDao databaseDao;
        private DeleteAllAsyncGrades(DatabaseDao databaseDao){
            this.databaseDao = databaseDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            databaseDao.deleteAllGrades();
            return null;
        }
    }

    public LiveData<List<Teachers>> getTeacherWithName(String name){
        return databaseDao.getTeacherWithName(name);
    }

    public LiveData<List<Grades>> getAllGrades(){
        return databaseDao.getAllGrades();
    }

    public LiveData<List<Subject>> getSubjectWithName(String name){
        return databaseDao.getSubjectWithName(name);
    }
}
