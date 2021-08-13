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
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.adapters.SubjectAdapter;
import com.example.studentplanner.addentities.AddExamActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.adapters.ExamAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.addentities.AddSubjectActivity;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Exams extends Fragment {
    private Toolbar toolbar;
    public static final int ADD_EXAM_REQUEST= 4;
    public static final int EDIT_EXAM_REQUEST= 504;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;


    public Fragment_Exams(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                examAdapter.submitList(exams);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddExamActivity.class);
                startActivityForResult(intent, ADD_EXAM_REQUEST);
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
                databaseViewModel.delete(examAdapter.getExamAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        examAdapter.setOnItemClickListener(new ExamAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Exams exams) {
                final Intent intent = new Intent(getContext(), AddExamActivity.class);
                intent.putExtra(AddExamActivity.EXTRA_TITLE, exams.getName());
                intent.putExtra(AddExamActivity.EXTRA_DATE_PICKED, exams.getDate());
                intent.putExtra(AddExamActivity.EXTRA_NOTE_DETAILS, exams.getDetails());
                intent.putExtra(AddExamActivity.EXTRA_ID, exams.getId());

                databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        for (Subject subject : subjects) {
//                            Toast.makeText(getContext(), subject.getName(), Toast.LENGTH_SHORT).show();
                            if (subject.getId() == exams.getSubjectId()){
                                intent.putExtra(AddExamActivity.EXTRA_SUBJECT_PICKED, subject.getName());
//                                Toast.makeText(getContext(), "UUUUUU +" + exams.getSubjectId(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });

                startActivityForResult(intent, EDIT_EXAM_REQUEST);
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
                final String examTitle = data.getStringExtra(AddExamActivity.EXTRA_TITLE);
                String examSubject = data.getStringExtra(AddExamActivity.EXTRA_SUBJECT_PICKED);
                final String examDate = data.getStringExtra(AddExamActivity.EXTRA_DATE_PICKED);
                final String examNoteDetails = data.getStringExtra(AddExamActivity.EXTRA_NOTE_DETAILS);
                LiveData<List<Subject>> listLiveDataSubject = databaseViewModel.getSubjectWithName(examSubject);
                listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        final Exams exams = new Exams(examTitle, subjects.get(0).getId(), examDate, 1, examNoteDetails);
                        databaseViewModel.insert(exams);
                    }
                });

            }
        }else if (requestCode == EDIT_EXAM_REQUEST && resultCode == RESULT_OK) {
            final int id = data.getIntExtra(AddTaskActivity.EXTRA_ID, -1);
            if (id == -1) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            final String title = data.getStringExtra(AddExamActivity.EXTRA_TITLE);
            final String date = data.getStringExtra(AddExamActivity.EXTRA_DATE_PICKED);
            final String details = data.getStringExtra(AddExamActivity.EXTRA_NOTE_DETAILS);
            String subjectPicked = data.getStringExtra(AddExamActivity.EXTRA_SUBJECT_PICKED);

            LiveData<List<Subject>> listLiveDataSubject = databaseViewModel.getSubjectWithName(subjectPicked);
            listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                @Override
                public void onChanged(List<Subject> subjects) {
                    final Exams exams = new Exams(title, subjects.get(0).getId(), date, 1, details);
                    exams.setId(id);
                    databaseViewModel.update(exams);
                }
            });

//            Toast.makeText(getContext(), "Exam Updated", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "Exam not added", Toast.LENGTH_SHORT).show();
        }
    }

}


