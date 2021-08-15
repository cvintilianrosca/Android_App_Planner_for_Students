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
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentplanner.AddTimetableItemActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Timetable;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import android.text.Html;

public class Fragment_Timetable extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private TimetableView timetableView;
    private DatabaseViewModel databaseViewModel;
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


        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        databaseViewModel.getAllTimetables().observe(getViewLifecycleOwner(), new Observer<List<Timetable>>() {
            @Override
            public void onChanged(List<Timetable> timetables) {

                for (Timetable time : timetables) {
                    ArrayList<Schedule> schedules = new ArrayList<>();
                    Schedule schedule = new Schedule();
                    schedule.setClassTitle(time.getSubject());
                    schedule.setClassPlace(time.getLocation()); // sets place
                    schedule.setProfessorName(time.getTeacher()); // sets professor
                    String aux[] = time.getStart().split(":");
                    schedule.setStartTime(new Time(Integer.parseInt(aux[0]),Integer.parseInt(aux[1])));
                    aux = time.getEnd().split(":");
                    schedule.setEndTime(new Time(Integer.parseInt(aux[0]),Integer.parseInt(aux[1])));
                    switch (time.getDay()){
                        case "Monday":
                            schedule.setDay(0);
                            break;
                        case "Tuesday":
                            schedule.setDay(1);
                            break;
                        case "Wednesday":
                            schedule.setDay(2);
                            break;
                        case "Thursday":
                            schedule.setDay(3);
                            break;
                        case "Friday":
                            schedule.setDay(5);
                            break;

                    }
                    schedules.add(schedule);
                    timetableView.add(schedules);
                }

            }
        });



//        timetableView.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
//            @Override
//            public void OnStickerSelected(int idx, ArrayList<Schedule> schedules) {
//                schedules.get(idx).setDay(2);
//                timetableView.edit(idx, schedules) ;
//            }
//        });

        return v;
    }
}
