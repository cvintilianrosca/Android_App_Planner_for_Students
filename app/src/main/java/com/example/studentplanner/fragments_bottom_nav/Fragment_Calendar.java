package com.example.studentplanner.fragments_bottom_nav;

import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.TaskOrExam;
import com.example.studentplanner.adapters.MultiViewCalendarAdapter;
import com.example.studentplanner.adapters.TaskOrExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Tasks;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Calendar extends Fragment {

    CompactCalendarView compactCalendarView;
    private SimpleDateFormat dateFormatMonth = new SimpleDateFormat("MMMM yyyy", Locale.getDefault());
    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;
    private RecyclerView recyclerView;
    private TaskOrExamAdapter taskOrExamAdapter;
    private FloatingActionButton floatingActionButton;
    private FloatingActionButton floatingActionButtonAddTask;
    private FloatingActionButton floatingActionButtonAddExam;
    private MultiViewCalendarAdapter multiViewCalendarAdapter;
    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;


    public Fragment_Calendar(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View v = inflater.inflate(R.layout.fragment_calendar, container, false);
       Date date = new Date();
       toolbar.setTitle(dateFormatMonth.format(date.getTime()));
       compactCalendarView = v.findViewById(R.id.compactcalendar_view);
       compactCalendarView.setUseThreeLetterAbbreviation(true);
       recyclerView = v.findViewById(R.id.recycleViewCalendar);
       rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
       rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
       fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
       toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);

        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        multiViewCalendarAdapter = new MultiViewCalendarAdapter();
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
            }
        });

        floatingActionButtonAddExam = v.findViewById(R.id.floatingActionButtonAddExamCalendar);
        floatingActionButtonAddTask = v.findViewById(R.id.floatingActionButtonAddTaskCalendar);

        floatingActionButtonAddTask.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Task", Toast.LENGTH_SHORT).show();
            }
        });

        floatingActionButtonAddExam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "Exam", Toast.LENGTH_SHORT).show();
            }
        });


        final SimpleDateFormat sdf = new SimpleDateFormat("dd-M-yyyy");
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        databaseViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                for (Tasks it: tasks) {
                    String dateString = it.getDateDeadline();
                    Date date1 = null;
                    try {
                        date1 = sdf.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    compactCalendarView.addEvent(new Event(Color.RED, date1.getTime(), (it.getTitle()+ ":1:" + it.getDetails())));
                }
            }
        });

        databaseViewModel.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exams>>() {
            @Override
            public void onChanged(List<Exams> exams) {
                for (Exams it : exams) {
                    String dateString = it.getDate();
                    Date date1 = null;
                    try {
                        date1 = sdf.parse(dateString);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    compactCalendarView.addEvent(new Event(Color.RED, date1.getTime(), (it.getName() + ":2:" + it.getDetails())));
                }
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

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                final String currentDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(dateClicked);

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
            }



            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
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
        } else {
            floatingActionButtonAddExam.setVisibility(View.INVISIBLE);
            floatingActionButtonAddTask.setVisibility(View.INVISIBLE);
        }
    }
    public void  setAnimation(boolean clicked){
        if (!clicked){
            floatingActionButtonAddTask.setAnimation(fromBottom);
            floatingActionButtonAddExam.setAnimation(fromBottom);
            floatingActionButton.setAnimation(rotateOpen);
        } else {
            floatingActionButtonAddTask.setAnimation(toBottom);
            floatingActionButtonAddExam.setAnimation(toBottom);
            floatingActionButton.setAnimation(rotateClose);
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

}
