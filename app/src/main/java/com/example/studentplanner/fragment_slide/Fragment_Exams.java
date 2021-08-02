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

import com.example.studentplanner.AddExamActivity;
import com.example.studentplanner.AddTaskActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.ExamAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Exams extends Fragment {
    private Toolbar toolbar;
    public static final int ADD_EXAM_REQUEST= 4;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;


    public Fragment_Exams(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Exams");
       View v = inflater.inflate(R.layout.fragment_exams, container, false);

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        recyclerView = v.findViewById(R.id.recycleViewFragmentExams);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final ExamAdapter examAdapter = new ExamAdapter();

        databaseViewModel.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exams>>() {
            @Override
            public void onChanged(List<Exams> exams) {
                examAdapter.setExams(exams);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddExamActivity.class);
                startActivityForResult(intent, ADD_EXAM_REQUEST);
            }
        });

        recyclerView.setAdapter(examAdapter);
       return v;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_EXAM_REQUEST && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            } else {
                String examTitle = data.getStringExtra(AddExamActivity.EXTRA_TITLE);
                String examSubject = data.getStringExtra(AddExamActivity.EXTRA_SUBJECT_PICKED);
                String examDate = data.getStringExtra(AddExamActivity.EXTRA_DATE_PICKED);
                String examNoteDetails = data.getStringExtra(AddExamActivity.EXTRA_NOTE_DETAILS);
                LiveData<List<Subject>> listLiveDataSubject = databaseViewModel.getSubjectWithName(examSubject);
                final int[] id = {0};
                listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        id[0] = subjects.get(0).getId();
                    }
                });
                final Exams exams = new Exams(examTitle, id[0], examDate, 1, examNoteDetails);
                databaseViewModel.insert(exams);
            }
        } else {
            Toast.makeText(getContext(), "Exam not added", Toast.LENGTH_SHORT).show();
        }
    }

}


