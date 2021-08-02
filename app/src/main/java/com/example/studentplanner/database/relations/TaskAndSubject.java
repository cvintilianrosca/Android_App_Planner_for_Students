package com.example.studentplanner.database.relations;

import androidx.room.Embedded;
import androidx.room.Relation;

import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;

public class TaskAndSubject {
   @Embedded
   public Tasks tasks;
   @Relation(parentColumn = "subjectId",
    entityColumn = "id") public Subject subject;
}
