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

import com.example.studentplanner.AddSubjectActivity;
import com.example.studentplanner.AddTeacherActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.SubjectAdapter;
import com.example.studentplanner.TeacherAdapter;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Teachers extends Fragment {

    public static final int ADD_TEACHER_REQUEST=2;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;

    public Fragment_Teachers(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Teachers");
        View v = inflater.inflate(R.layout.fragment_teachers, container, false);
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        recyclerView = v.findViewById(R.id.recycleViewFragmentTeachers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final TeacherAdapter teacherAdapter = new TeacherAdapter();
        databaseViewModel.getAllTeachers().observe(getViewLifecycleOwner(), new Observer<List<Teachers>>() {
            @Override
            public void onChanged(List<Teachers> teachers) {
                teacherAdapter.setTeachers(teachers);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTeacherActivity.class);
                startActivityForResult(intent, ADD_TEACHER_REQUEST);
            }
        });

        recyclerView.setAdapter(teacherAdapter);
        return v;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TEACHER_REQUEST && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(getContext(), "Something", Toast.LENGTH_SHORT).show();
            } else {
                String teacherName = data.getStringExtra(AddTeacherActivity.EXTRA_NAME_TEACHER);
                String teacherPhoneNumber = data.getStringExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER);

                final Teachers teachers = new Teachers(teacherName, teacherPhoneNumber);
                databaseViewModel.insert(teachers);
            }
        } else {
            Toast.makeText(getContext(), "Teacher not added", Toast.LENGTH_SHORT).show();
        }
    }
}
