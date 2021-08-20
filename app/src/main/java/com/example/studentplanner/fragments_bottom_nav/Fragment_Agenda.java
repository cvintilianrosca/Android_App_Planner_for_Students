package com.example.studentplanner.fragments_bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
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

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.TaskOrExam;
import com.example.studentplanner.adapters.MultiViewCalendarAdapter;
import com.example.studentplanner.adapters.TaskOrExamAdapter;
import com.example.studentplanner.addentities.AddExamActivity;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;

public class Fragment_Agenda extends Fragment {
    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;
    private RecyclerView recyclerView;
    private TaskOrExamAdapter taskOrExamAdapter;
    private FloatingActionButton floatingActionButton;
    private MultiViewCalendarAdapter multiViewCalendarAdapter;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private FloatingActionButton floatingActionButtonAddTask;
    private FloatingActionButton floatingActionButtonAddExam;
    private View backgroundDimmer;
    private TextView textViewTaskAgenda;
    private TextView textViewExamAgenda;


    public static final int ADD_TASK_REQUEST = 5;
    public static final int EDIT_TASK_REQUEST = 11;
    public static final int ADD_EXAM_REQUEST= 4;
    public static final int EDIT_EXAM_REQUEST= 504;


    public Fragment_Agenda(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        toolbar.setTitle("Agenda");
        View v = inflater.inflate(R.layout.fragment_agenda, container, false);
        recyclerView = v.findViewById(R.id.recycleViewFragmentAgenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });
        taskOrExamAdapter = new TaskOrExamAdapter();
        multiViewCalendarAdapter = new MultiViewCalendarAdapter();
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
        backgroundDimmer = v.findViewById(R.id.background_dimmerAgenda);

        floatingActionButtonAddExam = v.findViewById(R.id.floatingActionButtonAddExamAgenda);
        floatingActionButtonAddTask = v.findViewById(R.id.floatingActionButtonAddTaskAgenda);

        textViewExamAgenda = v.findViewById(R.id.textViewExamsAgenda);
        textViewTaskAgenda = v.findViewById(R.id.textViewTasksAgenda);

        Animation animationInitial = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_initial_anim);
        floatingActionButton.startAnimation(animationInitial);

        floatingActionButtonAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddExamActivity.class);
                startActivityForResult(intent, ADD_EXAM_REQUEST);
                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        floatingActionButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTaskActivity.class);
                startActivityForResult(intent, ADD_TASK_REQUEST);
                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        final String currentDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());

        databaseViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                final List<Object> objectList = new ArrayList<>();
                for (Tasks it: tasks) {

                    if (it.getDateDeadline().compareTo(currentDate) == 0){
                        objectList.add(it);
                    }
                }
                databaseViewModel.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exams>>() {
                    @Override
                    public void onChanged(List<Exams> exams) {
                        for (Exams ex : exams){
                            if (ex.getDate().compareTo(currentDate) == 0){
                                objectList.add(ex);
                            }
                        }
                        multiViewCalendarAdapter.setObjectList(objectList);
                        recyclerView.setAdapter(multiViewCalendarAdapter);
                    }
                });
            }
        });
        multiViewCalendarAdapter.setOnItemClickListener(new MultiViewCalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {
//                Toast.makeText(getContext(), "IOOO", Toast.LENGTH_SHORT).show();
                if (object instanceof Exams){
                    Toast.makeText(getContext(), "Exams", Toast.LENGTH_SHORT).show();
                }
                if (object instanceof Tasks){
                    Toast.makeText(getContext(), "Tasks", Toast.LENGTH_SHORT).show();
                }
            }
        });
       return v;
    }

    private  boolean clicked = false;

    private void onAddButtonClicked(){

        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }
    public void setVisibility(boolean clicked){
        if (!clicked){
            floatingActionButtonAddExam.setVisibility(View.VISIBLE);
            floatingActionButtonAddTask.setVisibility(View.VISIBLE);
            textViewTaskAgenda.setVisibility(View.VISIBLE);
            textViewExamAgenda.setVisibility(View.VISIBLE);
        } else {
            floatingActionButtonAddExam.setVisibility(View.INVISIBLE);
            floatingActionButtonAddTask.setVisibility(View.INVISIBLE);
            textViewExamAgenda.setVisibility(View.INVISIBLE);
            textViewTaskAgenda.setVisibility(View.INVISIBLE);
        }
    }
    public void  setAnimation(boolean clicked){
        if (!clicked){
            backgroundDimmer.setVisibility(View.VISIBLE);
            floatingActionButtonAddTask.startAnimation(fromBottom);
            floatingActionButtonAddExam.startAnimation(fromBottom);
            textViewTaskAgenda.startAnimation(fromBottom);
            textViewExamAgenda.startAnimation(fromBottom);
            floatingActionButton.startAnimation(rotateOpen);
        } else {
            backgroundDimmer.setVisibility(View.INVISIBLE);
            floatingActionButtonAddTask.startAnimation(toBottom);
            floatingActionButtonAddExam.startAnimation(toBottom);
            textViewExamAgenda.startAnimation(toBottom);
            textViewTaskAgenda.startAnimation(toBottom);
            floatingActionButton.startAnimation(rotateClose);
        }
    }

    public void setClickable(boolean clicked){
        if (!clicked){
            floatingActionButtonAddExam.setClickable(true);
            floatingActionButtonAddTask.setClickable(true);
        } else{
            floatingActionButtonAddTask.setClickable(false);
            floatingActionButtonAddExam.setClickable(false);
        }
    }


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
                        if (subjects.size()==0){
                            final Tasks tasks = new Tasks("No subject added", taskDate, subjects.get(0).getId(), taskNoteDetails);
                            databaseViewModel.insert(tasks);
                        } else {
                            id[0] = subjects.get(0).getId();
                            final Tasks tasks = new Tasks(taskTitle, taskDate, subjects.get(0).getId(), taskNoteDetails);
                            databaseViewModel.insert(tasks);
                        }
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
            listLiveDataSubject.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                @Override
                public void onChanged(List<Subject> subjects) {
                    final Tasks tasks = new Tasks(title, date, subjects.get(0).getId(), details);
                    tasks.setId(id);
                    databaseViewModel.update(tasks);
                }
            });

//            Toast.makeText(getContext(), "Task Updated", Toast.LENGTH_SHORT).show();
        }else {
//            Toast.makeText(getContext(), "Task not added", Toast.LENGTH_SHORT).show();
        }


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
