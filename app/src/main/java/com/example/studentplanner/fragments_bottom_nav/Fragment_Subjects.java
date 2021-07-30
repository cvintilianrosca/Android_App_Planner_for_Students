package com.example.studentplanner.fragments_bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.AddSubject;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.MainActivity;
import com.example.studentplanner.R;
import com.example.studentplanner.SubjectAdapter;
import com.example.studentplanner.database.entities.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Subjects extends Fragment {
    public static final int ADD_SUBJECT_REQUEST=1;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;

    public Fragment_Subjects(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.floatingActionButton = floatingActionButton;
        this.toolbar = toolbar;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Subjects");
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        View v = inflater.inflate(R.layout.fragments_subjects, container, false);
        recyclerView = v.findViewById(R.id.recycleViewFragmentSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final SubjectAdapter subjectAdapter = new SubjectAdapter();
        databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
            @Override
            public void onChanged(List<Subject> subjects) {
                subjectAdapter.setSubjects(subjects);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubject.class);
                startActivityForResult(intent, ADD_SUBJECT_REQUEST);
            }
        });
        recyclerView.setAdapter(subjectAdapter);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddSubject.EXTRA_NAME);
            String room = data.getStringExtra(AddSubject.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubject.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubject.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            databaseViewModel.insert(subject);
            Toast.makeText(getContext(), "Subject added", Toast.LENGTH_SHORT).show();

        } else {
            Toast.makeText(getContext(), "Subject not added", Toast.LENGTH_SHORT).show();
        }
    }
}
