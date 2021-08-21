package com.example.studentplanner.database;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.example.studentplanner.database.dao.DatabaseDao;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.database.entities.Teachers;
import com.example.studentplanner.database.entities.Timetable;

@androidx.room.Database(entities = {Subject.class, Teachers.class, Grades.class, Tasks.class,
        Exams.class, Timetable.class}, version = 11)
abstract public class Database extends RoomDatabase {
    private static Database INSTANCE;

    public abstract DatabaseDao databaseDao();

    public static synchronized Database getINSTANCE(Context context) {
        if (INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                    Database.class, "student_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallBack)
                    .build();
        }
        return INSTANCE;
    }

    private static RoomDatabase.Callback roomCallBack = new RoomDatabase.Callback() {

        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(INSTANCE).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {

        private DatabaseDao databaseDao;

        private PopulateDbAsyncTask(Database database) {
            databaseDao = database.databaseDao();

        }

        @Override
        protected Void doInBackground(Void... voids) {
            databaseDao.insert(new Subject("Lalal", null, null, null));
            databaseDao.insert(new Subject("Lalal1", null, null, null));
            databaseDao.insert(new Subject("Lalal2", null, null, null));

            return null;
        }
    }
}
