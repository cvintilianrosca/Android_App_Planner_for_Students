package com.example.studentplanner.database;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;
import androidx.room.Delete;
import androidx.room.Update;

import com.example.studentplanner.database.dao.DatabaseDao;
import com.example.studentplanner.database.entities.Subject;

import java.util.List;

public class Repository {
    private DatabaseDao databaseDao;
    private LiveData<List<Subject>> subjectsList;

    public Repository(Application application){
        Database database = Database.getINSTANCE(application);
        databaseDao = database.databaseDao();
        subjectsList = databaseDao.getAllSubjects();
    }

    public void insert(Subject subject){
        new InsertAsyncSubject(databaseDao).execute(subject);
    }

    public void update(Subject subject){
        new UpdateAsyncSubject(databaseDao).execute(subject);

    }

    public void delete(Subject subject){
        new DeleteAsyncSubject(databaseDao).execute(subject);
    }
    public void deleteAllSubjects(){
        new DeleteAllAsyncSubject(databaseDao).execute();
    }

    public LiveData<List<Subject>> getSubjectsList(){
        return subjectsList;
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


}
