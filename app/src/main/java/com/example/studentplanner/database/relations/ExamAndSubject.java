package com.example.studentplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;

public class ExamAndSubject {
   @Embedded
   public Exams exams;
   @Relation(parentColumn = "subjectId",
    entityColumn = "id") public Subject subject;
}
