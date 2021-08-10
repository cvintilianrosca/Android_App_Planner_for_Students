package com.example.studentplanner.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.database.entities.Teachers;
import com.example.studentplanner.database.relations.SubjectWithGrades;
import com.example.studentplanner.database.relations.TeacherWithSubjects;

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

    @Insert
    void insert(Grades grades);

    @Update
    void update(Grades grades);

    @Delete
    void delete(Grades grades);

    @Insert
    void insert(Tasks tasks);

    @Update
    void update(Tasks tasks);

    @Delete
    void delete(Tasks tasks);

    @Insert
    void insert(Exams exams);

    @Update
    void update(Exams exams);

    @Delete
    void delete(Exams exams);

    @Query("DELETE FROM exam_table")
    void deleteAllExams();

    @Query("DELETE FROM task_table")
    void deleteAllTasks();

    @Query("SELECT * FROM task_table")
    LiveData<List<Tasks>> getAllTasks();

    @Query("SELECT * FROM exam_table")
    LiveData<List<Exams>> getAllExams();

    @Query("DELETE FROM grades_table")
    void deleteAllGrades();

    @Query("SELECT * FROM grades_table")
    LiveData<List<Grades>> getAllGrades();

    @Query("DELETE FROM teachers_table")
    void deleteAllTeachers();

    @Query("SELECT * FROM teachers_table ORDER BY name")
    LiveData<List<Teachers>> getAllTeachers();

    @Query("SELECT * FROM teachers_table WHERE id = :teacher_id")
    List<TeacherWithSubjects> getTeacherWithSubjects(Integer teacher_id);

    @Query("SELECT * FROM teachers_table WHERE name = :teacherName")
    LiveData<List<Teachers>> getTeacherWithName(String teacherName);

    @Query("SELECT * FROM subject_table WHERE id = :idOfSubject")
    LiveData<List<SubjectWithGrades>> getSubjectWithGrades(int idOfSubject);

    @Query("SELECT * FROM subject_table WHERE name = :subjectName")
    LiveData<List<Subject>> getSubjectWithName(String subjectName);

    @Query("SELECT * FROM subject_table WHERE id = :sId")
    LiveData<List<Subject>> getSubjectWithId(int sId);
}
