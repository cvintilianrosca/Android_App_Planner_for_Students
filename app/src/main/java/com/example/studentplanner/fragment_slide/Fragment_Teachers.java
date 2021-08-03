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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.addentities.AddTeacherActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.TeacherAdapter;
import com.example.studentplanner.database.entities.Teachers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Teachers extends Fragment {

    public static final int ADD_TEACHER_REQUEST=2;
    public static final int EDIT_TEACHER_REQUEST=7;
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
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                databaseViewModel.delete(teacherAdapter.getTeacherAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        teacherAdapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Teachers teachers) {
                Intent intent = new Intent(getContext(), AddTeacherActivity.class);
                intent.putExtra(AddTeacherActivity.EXTRA_NAME_TEACHER, teachers.getName());
                intent.putExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER, teachers.getPhoneNumber());
                intent.putExtra(AddTeacherActivity.EXTRA_ID_TEACHER, teachers.getId());
                startActivityForResult(intent, EDIT_TEACHER_REQUEST);
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
        }else if (requestCode == EDIT_TEACHER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddTeacherActivity.EXTRA_ID_TEACHER, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }

            String name = data.getStringExtra(AddTeacherActivity.EXTRA_NAME_TEACHER);
            String phone  = data.getStringExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER);

           final Teachers teachers = new Teachers(name, phone);
           teachers.setId(id);
            databaseViewModel.update(teachers);
            Toast.makeText(getContext(), "Teacher Updated", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getContext(), "Teacher not added", Toast.LENGTH_SHORT).show();
        }
    }
}
