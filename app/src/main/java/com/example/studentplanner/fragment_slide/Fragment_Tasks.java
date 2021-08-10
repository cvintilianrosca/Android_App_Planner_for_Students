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

import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.TaskAdapter;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Tasks extends Fragment {
    private Toolbar toolbar;
    public static final int ADD_TASK_REQUEST = 5;
    public static final int EDIT_TASK_REQUEST = 11;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;

    public Fragment_Tasks(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Tasks");
        final View v =  inflater.inflate(R.layout.fragment_tasks, container, false);

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        recyclerView = v.findViewById(R.id.recycleViewFragmentTasks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final TaskAdapter taskAdapter = new TaskAdapter();

        databaseViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                taskAdapter.submitList(tasks);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST);
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
                databaseViewModel.delete(taskAdapter.getTaskAtPosition(viewHolder.getAdapterPosition()));
                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
            }
        }).attachToRecyclerView(recyclerView);

        recyclerView.setAdapter(taskAdapter);

        taskAdapter.setOnItemClickListener(new TaskAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Tasks tasks) {
                final Intent intent = new Intent(getContext(), AddTaskActivity.class);
                intent.putExtra(AddTaskActivity.EXTRA_TITLE, tasks.getTitle());
                intent.putExtra(AddTaskActivity.EXTRA_NOTE_DETAILS, tasks.getDetails());
                intent.putExtra(AddTaskActivity.EXTRA_DATE_PICKED, tasks.getDateDeadline());
                intent.putExtra(AddTaskActivity.EXTRA_ID, tasks.getId());
                databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        for (Subject subject : subjects) {
                            if (subject.getId() == tasks.getSubjectId()){
                                intent.putExtra(AddTaskActivity.EXTRA_SUBJECT_PICKED, subject.getName());
                            }
                        }
                    }
                });

                startActivityForResult(intent, EDIT_TASK_REQUEST);
            }
        });
        return v;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TASK_REQUEST && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(getContext(), "Something wrong", Toast.LENGTH_SHORT).show();
            } else {
                final String taskTitle = data.getStringExtra(AddTaskActivity.EXTRA_TITLE);
                String taskSubject = data.getStringExtra(AddTaskActivity.EXTRA_SUBJECT_PICKED);
                final String taskDate = data.getStringExtra(AddTaskActivity.EXTRA_DATE_PICKED);
                final String taskNoteDetails = data.getStringExtra(AddTaskActivity.EXTRA_NOTE_DETAILS);
                LiveData<List<Subject>> listLiveDataSubject = databaseViewModel.getSubjectWithName(taskSubject);
                final int[] id = {0};
                listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        id[0] = subjects.get(0).getId();
                        final Tasks tasks = new Tasks(taskTitle, taskDate, subjects.get(0).getId(), taskNoteDetails);
                        databaseViewModel.insert(tasks);
                    }
                });


            }
        } else if (requestCode == EDIT_TASK_REQUEST && resultCode == RESULT_OK) {
            final int id = data.getIntExtra(AddTaskActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            final String title = data.getStringExtra(AddTaskActivity.EXTRA_TITLE);
            final String date = data.getStringExtra(AddTaskActivity.EXTRA_DATE_PICKED);
            final String details = data.getStringExtra(AddTaskActivity.EXTRA_NOTE_DETAILS);
            String subjectPicked = data.getStringExtra(AddTaskActivity.EXTRA_SUBJECT_PICKED);
            LiveData<List<Subject>> listLiveDataSubject = databaseViewModel.getSubjectWithName(subjectPicked);
            final int[] ida = {0};
            listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                @Override
                public void onChanged(List<Subject> subjects) {
                    ida[0] = subjects.get(0).getId();
                    final Tasks tasks = new Tasks(title, date, subjects.get(0).getId(), details);
                    tasks.setId(id);
                    databaseViewModel.update(tasks);
                }
            });

            Toast.makeText(getContext(), "Task Updated", Toast.LENGTH_SHORT).show();
        }else {
            Toast.makeText(getContext(), "Task not added", Toast.LENGTH_SHORT).show();
        }
    }

}
