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
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.AddTimetableItemActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.Model;
import com.example.studentplanner.ModelAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.addentities.AddExamActivity;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.database.entities.Timetable;
import com.example.studentplanner.fragment_slide.Fragment_Exams;
import com.example.studentplanner.fragment_slide.Fragment_Grades;
import com.example.studentplanner.fragment_slide.Fragment_Tasks;
import com.example.studentplanner.fragment_slide.Fragment_Teachers;
import com.example.studentplanner.fragment_slide.Fragment_Timetable;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Home extends Fragment {
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ArrayList<Model> arrayListModel;
    public static final int ADD_TIMETABLE_THING = 101;
    public static final int EDIT_TIMETABLE_THING = 111;
    public static final int ADD_TASK_REQUEST = 5;
    public static final int EDIT_TASK_REQUEST = 11;
    public static final int ADD_EXAM_REQUEST= 4;
    public static final int EDIT_EXAM_REQUEST= 504;

    private DatabaseViewModel databaseViewModel;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;


    private FloatingActionButton floatingActionButtonAddClassesHome;
    private FloatingActionButton floatingActionButtonAddTaskHome;
    private FloatingActionButton floatingActionButtonAddExamHome;

    private View backgroundDimmer;
    private TextView textViewClassesHome;
    private TextView textViewTaskHome;
    private TextView textViewExamHome;




    public Fragment_Home(final Toolbar toolbar,FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       toolbar.setTitle("Home");
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.id_recycleViewMain);
        arrayListModel = new ArrayList<>();
        arrayListModel.add(new Model("Tasks"));
        arrayListModel.add(new Model("Exams"));
        arrayListModel.add(new Model("Grades"));
        arrayListModel.add(new Model("Timetable"));

        arrayListModel.add(new Model("Teachers"));

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
        backgroundDimmer = v.findViewById(R.id.background_dimmerHome);

        floatingActionButtonAddTaskHome = v.findViewById(R.id.floatingActionButtonAddTaskHome);
        floatingActionButtonAddClassesHome = v.findViewById(R.id.floatingActionButtonAddClassesHome);
        floatingActionButtonAddExamHome = v.findViewById(R.id.floatingActionButtonAddExamHome);

        floatingActionButtonAddClassesHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableItemActivity.class);
                startActivityForResult(intent, ADD_TIMETABLE_THING);

                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });


        floatingActionButtonAddExamHome.setOnClickListener(new View.OnClickListener() {
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

        floatingActionButtonAddTaskHome.setOnClickListener(new View.OnClickListener() {
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


        textViewTaskHome = v.findViewById(R.id.textViewTaskHome);
        textViewClassesHome = v.findViewById(R.id.textViewClassesHome);
        textViewExamHome = v.findViewById(R.id.textViewExamsHome);

        Animation animationInitial = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_initial_anim);
        floatingActionButton.startAnimation(animationInitial);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              onAddButtonClicked();
          }
      });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ModelAdapter modelAdapter = new ModelAdapter( arrayListModel, getContext());
        recyclerView.setAdapter(modelAdapter);
        modelAdapter.setOnItemClickListener(new ModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Model model) {
                Fragment selectedFragment = null;
                switch (model.titleFragment){
                    case "Tasks":
                        selectedFragment = new Fragment_Tasks(toolbar, floatingActionButton);
                        break;
                    case "Exams":
                        selectedFragment = new Fragment_Exams(toolbar, floatingActionButton);
                        break;
                    case "Teachers":
                        selectedFragment = new Fragment_Teachers(toolbar, floatingActionButton);
                        break;
                    case "Timetable":
                        selectedFragment = new Fragment_Timetable(toolbar, floatingActionButton);
                        break;
                    case "Grades":
                        selectedFragment = new Fragment_Grades(toolbar, floatingActionButton);
                        break;
                }
                if (selectedFragment != null){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
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
            floatingActionButtonAddTaskHome.setVisibility(View.VISIBLE);
            floatingActionButtonAddClassesHome.setVisibility(View.VISIBLE);
            textViewClassesHome.setVisibility(View.VISIBLE);
            textViewTaskHome.setVisibility(View.VISIBLE);
            textViewExamHome.setVisibility(View.VISIBLE);
        } else {
            floatingActionButtonAddTaskHome.setVisibility(View.INVISIBLE);
            floatingActionButtonAddClassesHome.setVisibility(View.INVISIBLE);
            textViewTaskHome.setVisibility(View.INVISIBLE);
            textViewClassesHome.setVisibility(View.INVISIBLE);
            textViewExamHome.setVisibility(View.VISIBLE);
        }
    }
    public void  setAnimation(boolean clicked){
        if (!clicked){
            backgroundDimmer.setVisibility(View.VISIBLE);
            floatingActionButtonAddClassesHome.startAnimation(fromBottom);
            floatingActionButtonAddTaskHome.startAnimation(fromBottom);
            floatingActionButtonAddExamHome.startAnimation(fromBottom);
            textViewClassesHome.startAnimation(fromBottom);
            textViewTaskHome.startAnimation(fromBottom);
            textViewExamHome.startAnimation(fromBottom);
            floatingActionButton.startAnimation(rotateOpen);
        } else {
            backgroundDimmer.setVisibility(View.INVISIBLE);
            floatingActionButtonAddClassesHome.startAnimation(toBottom);
            floatingActionButtonAddTaskHome.startAnimation(toBottom);
            floatingActionButtonAddExamHome.startAnimation(toBottom);
            textViewTaskHome.startAnimation(toBottom);
            textViewClassesHome.startAnimation(toBottom);
            textViewExamHome.startAnimation(toBottom);
            floatingActionButton.startAnimation(rotateClose);
        }
    }

    public void setClickable(boolean clicked){
        if (!clicked){
            floatingActionButtonAddTaskHome.setClickable(true);
            floatingActionButtonAddClassesHome.setClickable(true);
        } else{
            floatingActionButtonAddClassesHome.setClickable(false);
            floatingActionButtonAddTaskHome.setClickable(false);
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
