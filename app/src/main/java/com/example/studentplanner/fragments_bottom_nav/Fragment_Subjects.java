package com.example.studentplanner.fragments_bottom_nav;

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

import com.example.studentplanner.addentities.AddSubjectActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.SubjectAdapter;
import com.example.studentplanner.database.entities.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Subjects extends Fragment {
    public static final int ADD_SUBJECT_REQUEST=1;
    public static final int EDIT_SUBJECT_REQUEST=2;
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
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                startActivityForResult(intent, ADD_SUBJECT_REQUEST);
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
                databaseViewModel.delete(subjectAdapter.getSubjectAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        subjectAdapter.setOnItemClickListener(new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Subject subject) {
                Intent intent = new Intent(getContext(), AddSubjectActivity.class);
                intent.putExtra(AddSubjectActivity.EXTRA_NAME, subject.getName());
                intent.putExtra(AddSubjectActivity.EXTRA_NOTE, subject.getAdditional_info());
                intent.putExtra(AddSubjectActivity.EXTRA_ROOM, subject.getRoom());
                intent.putExtra(AddSubjectActivity.EXTRA_TEACHER, subject.getTeacher());
                intent.putExtra(AddSubjectActivity.EXTRA_ID, subject.getId());
                startActivityForResult(intent, EDIT_SUBJECT_REQUEST);
            }
        });

        recyclerView.setAdapter(subjectAdapter);
        return v;

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            databaseViewModel.insert(subject);
            Toast.makeText(getContext(), "Subject added", Toast.LENGTH_SHORT).show();

        }else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddSubjectActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            subject.setId(id);
            databaseViewModel.update(subject);
            Toast.makeText(getContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getContext(), "Subject not added", Toast.LENGTH_SHORT).show();
        }
    }
}
