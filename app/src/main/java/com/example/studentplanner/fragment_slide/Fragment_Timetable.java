package com.example.studentplanner.fragment_slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.studentplanner.AddTimetableItemActivity;
import com.example.studentplanner.R;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import android.text.Html;

public class Fragment_Timetable extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private TimetableView timetableView;
    public static final int ADD_TIMETABLE_THING = 101;
    public Fragment_Timetable(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton= floatingActionButton;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Timetable");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTimetableItemActivity.class);
                startActivityForResult(intent, ADD_TIMETABLE_THING);
            }
        });
        View v = inflater.inflate(R.layout.fragment_timetable, container, false);
        timetableView = v.findViewById(R.id.timetable);


        ArrayList<Schedule> schedules = new ArrayList<Schedule>();
        Schedule schedule = new Schedule();
        schedule.setClassTitle("Data Structure"); // sets subject
        schedule.setClassPlace("IT-601"); // sets place
        schedule.setProfessorName("Won Kim"); // sets professor
        schedule.setStartTime(new Time(10,0)); // sets the beginning of class time (hour,minute)
        schedule.setEndTime(new Time(13,30)); // sets the end of class time (hour,minute)
        schedules.add(schedule);
//.. add one or more schedules
        timetableView.add(schedules);

        timetableView.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
                schedules.get(idx).setDay(2);
                timetableView.edit(idx, schedules) ;
            }
        });

        return v;
    }
}
