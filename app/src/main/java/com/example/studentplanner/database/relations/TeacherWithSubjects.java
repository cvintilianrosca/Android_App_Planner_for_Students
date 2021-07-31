package com.example.studentplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;

import java.util.List;

public class TeacherWithSubjects {

    @Embedded public Teachers teachers;
    @Relation(parentColumn = "id",
    entityColumn="teacherId" )
    public List<Subject> teacherSubjects;
}
