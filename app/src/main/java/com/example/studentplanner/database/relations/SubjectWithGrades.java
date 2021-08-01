package com.example.studentplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;

import java.util.List;

public class SubjectWithGrades {
    @Embedded public Subject subject;
    @Relation(parentColumn = "id",
    entityColumn = "subjectId") public List<Grades> listGrades;
}
