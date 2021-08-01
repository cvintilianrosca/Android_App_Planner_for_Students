package com.example.studentplanner.fragment_slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.AddGradeActivity;
import com.example.studentplanner.AddTeacherActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.GradeAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.TeacherAdapter;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Grades extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    public static final int ADD_GRADE_REQUEST = 3;

    public Fragment_Grades(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       toolbar.setTitle("Grades");
      View v = inflater.inflate(R.layout.fragment_grades, container, false);
      databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
      recyclerView = v.findViewById(R.id.recycleViewFragmentGrades);
      recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
      recyclerView.setHasFixedSize(true);
        final GradeAdapter gradeAdapter = new GradeAdapter();
        databaseViewModel.getAllGrades().observe(getViewLifecycleOwner(), new Observer<List<Grades>>() {
            @Override
            public void onChanged(List<Grades> grades) {
                gradeAdapter.setGrades(grades);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGradeActivity.class);
                startActivityForResult(intent, ADD_GRADE_REQUEST);
            }
        });
        recyclerView.setAdapter(gradeAdapter);
      return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GRADE_REQUEST && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            } else {
                final int grade = data.getIntExtra(AddGradeActivity.EXTRA_GRADE, -1);
                String subjectName = data.getStringExtra(AddGradeActivity.EXTRA_SUBJECT_NAME);
                LiveData<List<Subject>> subjectInfo =  databaseViewModel.getSubjectWithName(subjectName);
                subjectInfo.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        databaseViewModel.insert(new Grades(subjects.get(0).getId(), grade));
                    }
                });

//                final Teachers teachers = new Teachers(teacherName, teacherPhoneNumber);
//                databaseViewModel.insert(teachers);
            }
        } else {
            Toast.makeText(getContext(), "Teacher not added", Toast.LENGTH_SHORT).show();
        }
    }
}
