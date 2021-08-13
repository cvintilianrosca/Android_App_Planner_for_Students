package com.example.studentplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;


import com.autofit.et.lib.AutoFitEditText;
import com.example.studentplanner.addentities.AddExamActivity;
import com.example.studentplanner.database.entities.Subject;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.List;
import java.util.Locale;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddTimetableItemActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener  {

   private AutoFitEditText editTextDetails;
   private TextView textViewSubjectPicked;
   private TextView textViewDaySelected;
   private TextView textViewStartHour;
   private TextView textViewEndHour;
   private EditText editTextLocation;
   private TextView textViewTeacherSelected;
   private Toolbar toolbar;
   private DatabaseViewModel databaseViewModel;
   private String[] listSubjects;
   private MaterialDayPicker materialDayPicker;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_timetable_item);
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        getWindow().setSoftInputMode(
                WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        editTextDetails = findViewById(R.id.rET);
        //in onCreate in Activity/Fragment
        editTextDetails.setEnabled(true);
        editTextDetails.setFocusableInTouchMode(true);
        editTextDetails.setFocusable(true);
        editTextDetails.setEnableSizeCache(false);
        //might cause crash on some devices
        editTextDetails.setMovementMethod(null);
        // can be added after layout inflation;
        editTextDetails.setMaxHeight(1000);
        final int checkedItem = 0;
        //don't forget to add min text size programmatically
        editTextDetails.setMinTextSize(70f);
//        AutoFitEditTextUtil.setNormalization(this, , mAutoFitEditText);
        textViewSubjectPicked = findViewById(R.id.textViewPickSubjectTimetable);
        textViewDaySelected = findViewById(R.id.textViewPickDayTimetable);
        textViewStartHour = findViewById(R.id.textViewPickStartLesson);
        textViewEndHour = findViewById(R.id.textViewPickEndLesson);
        editTextLocation = findViewById(R.id.editTextLocationTimetable);
        textViewTeacherSelected = findViewById(R.id.textViewPickTeacher);
        materialDayPicker = findViewById(R.id.day_picker_timetable);
        materialDayPicker.setFirstDayOfWeek( MaterialDayPicker.Weekday.MONDAY);  // or any other day
//        materialDayPicker.disableDay(MaterialDayPicker.Weekday.SATURDAY);
//        materialDayPicker.disableDay(MaterialDayPicker.Weekday.SUNDAY);
        toolbar = findViewById(R.id.toolbarTimetable);
        toolbar.inflateMenu(R.menu.add_entity_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_entity:
//                        saveExam();
                        return true;
                    default:
                        return false;
                }
            }
        });

        textViewSubjectPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllSubjects().observe(AddTimetableItemActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTimetableItemActivity.this);
                        builder.setTitle("Choose Subject");
                        if (subjects.size() == 0){
                            listSubjects = new String[1];
                            listSubjects[0] = "No subject added";
                        } else {
                            listSubjects = new String[subjects.size()];
                            for (int i = 0; i < subjects.size() ; i++) {
                                listSubjects[i] = subjects.get(i).getName();
                            }
                        }


                        final int[] saveChecked = {-1};
                        builder.setSingleChoiceItems(listSubjects, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveChecked[0] = which;
                            }
                        });

                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (saveChecked[0] != -1){
                                    textViewSubjectPicked.setText(listSubjects[saveChecked[0]]);
                                } else if (saveChecked[0] == -1 && listSubjects.length > 0){
                                    textViewSubjectPicked.setText(listSubjects[0]);
                                }
                            }
                        });

                        builder.show();
//                        Toast.makeText(AddSubjectActivity.this, teachers.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        materialDayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(MaterialDayPicker.Weekday weekday, boolean b) {
                String s = "";
                if (MaterialDayPicker.Weekday.MONDAY.equals(weekday)){
                    s = "Monday";
                }
                if (MaterialDayPicker.Weekday.TUESDAY.equals(weekday)){
                    s = "Tuesday";
                }
                if (MaterialDayPicker.Weekday.WEDNESDAY.equals(weekday)){
                    s = "Wednesday";
                }
                if (MaterialDayPicker.Weekday.THURSDAY.equals(weekday)){
                    s = "Thursday";
                }
                if (MaterialDayPicker.Weekday.FRIDAY.equals(weekday)){
                    s = "Friday";
                }
                if (MaterialDayPicker.Weekday.SATURDAY.equals(weekday)){
                    s = "Saturday";
                }
                if (MaterialDayPicker.Weekday.SUNDAY.equals(weekday)){
                    s = "Sunday";
                }
                textViewDaySelected.setText(s);
            }
        });
            // handle weekday selection

        textViewStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "time picker");
            }
        });

        textViewEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment dialogFragment = new TimePickerFragment();
                dialogFragment.show(getSupportFragmentManager(), "time picker");
            }
        });

    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
         textViewStartHour.setText(hourOfDay + ": " + minute);
    }
}