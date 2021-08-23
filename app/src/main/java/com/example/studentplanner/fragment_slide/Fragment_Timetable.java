package com.example.studentplanner.fragment_slide;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
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
import com.example.studentplanner.database.entities.Timetable;
import com.github.tlaabs.timetableview.Schedule;
import com.github.tlaabs.timetableview.Time;
import com.github.tlaabs.timetableview.TimetableView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Timetable extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    private TimetableView timetableView;
    private DatabaseViewModel databaseViewModel;
    private Dialog dialog;
    public static final int ADD_TIMETABLE_THING = 101;
    public static final int EDIT_TIMETABLE_THING = 111;

    public Fragment_Timetable(final Toolbar toolbar, final FloatingActionButton floatingActionButton) {
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
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
                    schedule.setStartTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                    aux = time.getEnd().split(":");
                    schedule.setEndTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                    switch (time.getDay()) {
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
                            schedule.setDay(4);
                            break;
                        case "Saturday":
                            schedule.setDay(5);
                            break;

                    }
                    schedules.add(schedule);
                    timetableView.add(schedules);
                }

            }
        });


        timetableView.setOnStickerSelectEventListener(new TimetableView.OnStickerSelectedListener() {
            @Override
            public void OnStickerSelected(final int idx, final ArrayList<Schedule> schedules) {

                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.dialog_layout_timetable);
                TextView subject = dialog.findViewById(R.id.textViewProfessorName);
                TextView day = dialog.findViewById(R.id.textViewProfessorPhone);
                TextView hours = dialog.findViewById(R.id.textViewProfessorEmail);
                Button delete = dialog.findViewById(R.id.buttonDeleteProfessor);
                Button edit = dialog.findViewById(R.id.buttonEditProfessor);
                Button cancel = dialog.findViewById(R.id.buttonCancelProfessorDialog);
                TextView location = dialog.findViewById(R.id.textViewProfessorAddress);
                subject.setText(schedules.get(0).getClassTitle());
                switch (schedules.get(0).getDay()) {
                    case 0:
                        day.setText("Monday");
                        break;
                    case 1:
                        day.setText("Tuesday");
                        break;
                    case 2:
                        day.setText("Wednesday");
                        break;
                    case 3:
                        day.setText("Thursday");
                        break;
                    case 4:
                        day.setText("Friday");
                        break;
                    case 5:
                        day.setText("Saturday");
                        break;
                }
                hours.setText("from " + schedules.get(0).getStartTime().getHour() + ":" +
                        schedules.get(0).getStartTime().getMinute() + " to " + schedules.get(0).getEndTime().getHour()
                        + ":" + schedules.get(0).getEndTime().getMinute());
                if (schedules.get(0).getClassPlace() == null || schedules.get(0).getClassPlace().isEmpty()) {
                    location.setText("Not selected location");
                } else {
                    location.setText(schedules.get(0).getClassPlace());
                }
                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseViewModel.getAllTimetables().observe(getViewLifecycleOwner(), new Observer<List<Timetable>>() {
                            @Override
                            public void onChanged(List<Timetable> timetables) {
                                Timetable save = null;
                                for (Timetable t : timetables) {
                                    if (t.getSubject().compareTo(schedules.get(0).getClassTitle()) == 0) {
                                        if ((schedules.get(0).getStartTime().getHour() + ":" + schedules.get(0).getStartTime().getMinute()).compareTo(t.getStart()) == 0) {
                                            if ((schedules.get(0).getEndTime().getHour() + ":" + schedules.get(0).getEndTime().getMinute()).compareTo(t.getEnd()) == 0) {
                                                save = t;
                                                databaseViewModel.delete(t);
                                            }
                                        }
                                    }
                                }

                                timetableView.removeAll();
                                for (Timetable time : timetables) {
                                    if (!time.equals(save)) {
                                        ArrayList<Schedule> schedules = new ArrayList<>();
                                        Schedule schedule = new Schedule();
                                        schedule.setClassTitle(time.getSubject());
                                        schedule.setClassPlace(time.getLocation()); // sets place
                                        schedule.setProfessorName(time.getTeacher()); // sets professor
                                        String aux[] = time.getStart().split(":");
                                        schedule.setStartTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                                        aux = time.getEnd().split(":");
                                        schedule.setEndTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                                        switch (time.getDay()) {
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
                                                schedule.setDay(4);
                                                break;
                                            case "Saturday":
                                                schedule.setDay(5);
                                                break;

                                        }
                                        schedules.add(schedule);
                                        timetableView.add(schedules);
                                    }

                                }

                            }
                        });
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseViewModel.getAllTimetables().observe(getViewLifecycleOwner(), new Observer<List<Timetable>>() {
                            @Override
                            public void onChanged(List<Timetable> timetables) {
                                for (Timetable t : timetables) {
                                    if (t.getSubject().compareTo(schedules.get(0).getClassTitle()) == 0) {
                                        if ((schedules.get(0).getStartTime().getHour() + ":" + schedules.get(0).getStartTime().getMinute()).compareTo(t.getStart()) == 0) {
                                            if ((schedules.get(0).getEndTime().getHour() + ":" + schedules.get(0).getEndTime().getMinute()).compareTo(t.getEnd()) == 0) {
//                                                Toast.makeText(getContext(), t.getDay(), Toast.LENGTH_SHORT).show();
                                                int day = -1;

                                                switch (t.getDay()) {
                                                    case "Monday":
                                                        day = 0;
                                                        break;
                                                    case "Tuesday":
                                                        day = 1;
                                                        break;
                                                    case "Wednesday":
                                                        day = 2;
                                                        break;
                                                    case "Thursday":
                                                        day = 3;
                                                        break;
                                                    case "Friday":
                                                        day = 4;
                                                        break;
                                                    case "Saturday":
                                                        day = 5;
                                                        break;
                                                }
                                                if (day == schedules.get(0).getDay()) {
                                                    if (schedules.get(0).getClassPlace().compareTo(t.getLocation()) == 0 ){
                                                        if (schedules.get(0).getProfessorName().compareTo(t.getTeacher()) == 0){
                                                            Intent intent = new Intent(getActivity(), AddTimetableItemActivity.class);
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_SUBJECT, t.getSubject());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_DAY, t.getDay());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_START_HOUR, t.getStart());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_END_HOUR, t.getEnd());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_LOCATION, t.getLocation()+ " ");
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_TEACHER, t.getTeacher());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_NOTE_DETAILS, t.getNote());
                                                            intent.putExtra(AddTimetableItemActivity.EXTRA_ID, t.getId());
                                                            startActivityForResult(intent, EDIT_TIMETABLE_THING);
                                                            dialog.dismiss();
                                                            break;
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        });
                    }
                });


                dialog.show();
            }
        });

        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EDIT_TIMETABLE_THING && resultCode == RESULT_OK) {
            timetableView.removeAll();
            int id = data.getIntExtra(AddTimetableItemActivity.EXTRA_ID, -1);
            String subject = data.getStringExtra(AddTimetableItemActivity.EXTRA_SUBJECT);
            String day = data.getStringExtra(AddTimetableItemActivity.EXTRA_DAY);
            String startHour = data.getStringExtra(AddTimetableItemActivity.EXTRA_START_HOUR);
            String endHour = data.getStringExtra(AddTimetableItemActivity.EXTRA_END_HOUR);
            String locations = data.getStringExtra(AddTimetableItemActivity.EXTRA_LOCATION);
            String teacher = data.getStringExtra(AddTimetableItemActivity.EXTRA_TEACHER);
            String noteDetails = data.getStringExtra(AddTimetableItemActivity.EXTRA_NOTE_DETAILS);
            if (id != -1) {
                Timetable timetable = new Timetable(subject, day, startHour, endHour, locations, teacher, noteDetails);
                timetable.setId(id);
                databaseViewModel.update(timetable);
            }
            databaseViewModel.getAllTimetables().observe(getViewLifecycleOwner(), new Observer<List<Timetable>>() {
                @Override
                public void onChanged(List<Timetable> timetables) {

                    for (Timetable time : timetables) {
                        ArrayList<Schedule> schedules = new ArrayList<>();
                        Schedule schedule = new Schedule();
                        schedule.setClassTitle(time.getSubject());
                        schedule.setClassPlace(time.getLocation());
                        schedule.setProfessorName(time.getTeacher());
                        String aux[] = time.getStart().split(":");
                        schedule.setStartTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                        aux = time.getEnd().split(":");
                        schedule.setEndTime(new Time(Integer.parseInt(aux[0]), Integer.parseInt(aux[1])));
                        switch (time.getDay()) {
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
                                schedule.setDay(4);
                                break;
                            case "Saturday":
                                schedule.setDay(5);
                                break;

                        }
                        schedules.add(schedule);
                        timetableView.add(schedules);
                    }
                }
            });
        } else if (requestCode == ADD_TIMETABLE_THING && resultCode == RESULT_OK) {
            String subject = data.getStringExtra(AddTimetableItemActivity.EXTRA_SUBJECT);
            String day = data.getStringExtra(AddTimetableItemActivity.EXTRA_DAY);
            String startHour = data.getStringExtra(AddTimetableItemActivity.EXTRA_START_HOUR);
            String endHour = data.getStringExtra(AddTimetableItemActivity.EXTRA_END_HOUR);
            String locations = data.getStringExtra(AddTimetableItemActivity.EXTRA_LOCATION);
            String teacher = data.getStringExtra(AddTimetableItemActivity.EXTRA_TEACHER);
            String noteDetails = data.getStringExtra(AddTimetableItemActivity.EXTRA_NOTE_DETAILS);
            Timetable timetable = new Timetable(subject, day, startHour, endHour, locations, teacher, noteDetails);
            databaseViewModel.insert(timetable);
        }
    }
}
