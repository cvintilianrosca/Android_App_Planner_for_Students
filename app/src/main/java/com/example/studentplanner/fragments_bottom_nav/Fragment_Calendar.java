package com.example.studentplanner.fragments_bottom_nav;

import android.app.usage.UsageEvents;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.TaskOrExam;
import com.example.studentplanner.TaskOrExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Tasks;
import com.github.sundeepk.compactcalendarview.CompactCalendarView;
import com.github.sundeepk.compactcalendarview.domain.Event;

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

    public Fragment_Calendar(final Toolbar toolbar){
        this.toolbar= toolbar;
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
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        taskOrExamAdapter = new TaskOrExamAdapter();

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
//                    Toast.makeText(getContext(), it.getDateDeadline(), Toast.LENGTH_SHORT).show();
                    compactCalendarView.addEvent(new Event(Color.RED, date1.getTime(), (it.getTitle()+ " 1 " + it.getDetails())));
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
                    compactCalendarView.addEvent(new Event(Color.RED, date1.getTime(), (it.getName() + " 2 " + it.getDetails())));
                }
            }
        });

        final List<TaskOrExam> listForRecycleView = new ArrayList<>();

        compactCalendarView.setListener(new CompactCalendarView.CompactCalendarViewListener() {
            @Override
            public void onDayClick(Date dateClicked) {
                taskOrExamAdapter.deleteAll();
                List<Event> events = compactCalendarView.getEvents(dateClicked);
                for (Event event : events) {
                    String s = (String)event.getData();
                    String[] tokens = s.split(" ");
                    listForRecycleView.add(new TaskOrExam(tokens[0], tokens[2] , Integer.parseInt(tokens[1])));
                }
                taskOrExamAdapter.setTaskOrExams(listForRecycleView);
                recyclerView.setAdapter(taskOrExamAdapter);
            }

            @Override
            public void onMonthScroll(Date firstDayOfNewMonth) {
                toolbar.setTitle(dateFormatMonth.format(firstDayOfNewMonth));
            }
        });
       return v;
    }
}
