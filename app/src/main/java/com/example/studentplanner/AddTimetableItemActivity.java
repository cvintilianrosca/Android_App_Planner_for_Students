package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.app.DirectAction;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.CancellationSignal;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


import com.autofit.et.lib.AutoFitEditText;
import com.example.studentplanner.addentities.AddExamActivity;
import com.example.studentplanner.addentities.AddGradeActivity;
import com.example.studentplanner.addentities.AddSubjectActivity;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;
import com.example.studentplanner.database.entities.Timetable;
import com.google.android.material.datepicker.MaterialDatePicker;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;

import ca.antonious.materialdaypicker.MaterialDayPicker;

public class AddTimetableItemActivity extends AppCompatActivity {

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
    private boolean start = false;
    private boolean end = false;
    private String[] listTeacher;
    private Dialog dialog;

    public static final int ADD_SUBJECT_REQUEST = 1;
    public static final int EDIT_SUBJECT_REQUEST = 2;

    public static final String EXTRA_SUBJECT = "com.example.studentplanner.EXTRA_SUBJECT";
    public static final String EXTRA_DAY = "com.example.studentplanner.EXTRA_DAY";
    public static final String EXTRA_START_HOUR = "com.example.studentplanner.EXTRA_START_HOUR";
    public static final String EXTRA_END_HOUR = "com.example.studentplanner.EXTRA_END_HOUR";
    public static final String EXTRA_LOCATION = "com.example.studentplanner.EXTRA_LOCATION";
    public static final String EXTRA_TEACHER = "com.example.studentplanner.EXTRA_TEACHER";
    public static final String EXTRA_NOTE_DETAILS = "com.example.studentplanner.EXTRA_NOTE_DETAILS";
    public static final String EXTRA_ID = "com.example.studentplanner.EXTRA_ID";
    public static final int EDIT_TIMETABLE = 202;
    private int ID = -2;


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
        editTextDetails.setMaxHeight(4000);
        final int checkedItem = 0;
        //don't forget to add min text size programmatically
        editTextDetails.setMinTextSize(70f);
//        AutoFitEditTextUtil.setNormalization(this, , mAutoFitEditText);
        textViewSubjectPicked = findViewById(R.id.textViewPickSubjectTimetable);
        textViewDaySelected = findViewById(R.id.textViewPickDayTimetable);
        textViewStartHour = findViewById(R.id.textViewPickStartLesson);
        textViewEndHour = findViewById(R.id.textViewPickEndLesson);
        editTextLocation = findViewById(R.id.editTextLocationTimetable);
        editTextLocation = findViewById(R.id.editTextLocationTimetable);
        textViewTeacherSelected = findViewById(R.id.textViewPickTeacher);
        materialDayPicker = findViewById(R.id.day_picker_timetable);

        materialDayPicker.setFirstDayOfWeek(MaterialDayPicker.Weekday.MONDAY);
        materialDayPicker.disableDay(MaterialDayPicker.Weekday.SUNDAY);// or any other day
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
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorBlueApp), PorterDuff.Mode.SRC_IN);
        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_entity:
                        saveTimetable();
                        return true;
                    default:
                        return false;
                }
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            textViewSubjectPicked.setText(intent.getStringExtra(EXTRA_SUBJECT));
            textViewDaySelected.setText(intent.getStringExtra(EXTRA_DAY));
            textViewStartHour.setText(intent.getStringExtra(EXTRA_START_HOUR));
            textViewEndHour.setText(intent.getStringExtra(EXTRA_END_HOUR));
            textViewTeacherSelected.setText(intent.getStringExtra(EXTRA_TEACHER));
            editTextLocation.setText(intent.getStringExtra(EXTRA_LOCATION));
            editTextDetails.setText(intent.getStringExtra(EXTRA_NOTE_DETAILS));
            ID = intent.getIntExtra(EXTRA_ID, -2);
        }
        textViewSubjectPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseViewModel.getAllSubjects().observe(AddTimetableItemActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {


                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTimetableItemActivity.this);
                        if (subjects.size() > 0) {
                            builder.setTitle("Pick a subject");
                            listSubjects = new String[subjects.size()];
                            for (int i = 0; i < subjects.size(); i++) {
                                listSubjects[i] = subjects.get(i).getName();
                            }
                            final int[] saveChecked = {-1};
                            builder.setSingleChoiceItems(listSubjects, checkedItem, new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    saveChecked[0] = which;
                                }
                            });

                            builder.setCancelable(true);
                            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    if (saveChecked[0] != -1) {
                                        textViewSubjectPicked.setText(listSubjects[saveChecked[0]]);
                                    } else if (saveChecked[0] == -1 && listSubjects.length > 0) {
                                        textViewSubjectPicked.setText(listSubjects[0]);
                                    }
                                }
                            });
                            builder.show();


                        } else {
                            dialog = new Dialog(AddTimetableItemActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_layout_add_subject);
                            TextView pick = dialog.findViewById(R.id.textViewAddNewSubjectDialog);
                            Button cancel = dialog.findViewById(R.id.buttonCancelSubjectDialog);
                            dialog.show();
                            pick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(AddTimetableItemActivity.this, AddSubjectActivity.class);
                                    startActivityForResult(intent, ADD_SUBJECT_REQUEST);

                                }
                            });


                            cancel.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.dismiss();
                                }
                            });

                        }
                    }
                });

            }
        });

        materialDayPicker.setDayPressedListener(new MaterialDayPicker.DayPressedListener() {
            @Override
            public void onDayPressed(MaterialDayPicker.Weekday weekday, boolean b) {
                String s = "";
                if (MaterialDayPicker.Weekday.MONDAY.equals(weekday)) {
                    s = "Monday";
                }
                if (MaterialDayPicker.Weekday.TUESDAY.equals(weekday)) {
                    s = "Tuesday";
                }
                if (MaterialDayPicker.Weekday.WEDNESDAY.equals(weekday)) {
                    s = "Wednesday";
                }
                if (MaterialDayPicker.Weekday.THURSDAY.equals(weekday)) {
                    s = "Thursday";
                }
                if (MaterialDayPicker.Weekday.FRIDAY.equals(weekday)) {
                    s = "Friday";
                }
                if (MaterialDayPicker.Weekday.SATURDAY.equals(weekday)) {
                    s = "Saturday";
                }
                textViewDaySelected.setText(s);
            }
        });
        // handle weekday selection


        textViewStartHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] timeSetStatus = {0}; // A clicked button result.
                final Calendar calendar = Calendar.getInstance();
                final TimePickerDialog dialog = new TimePickerDialog(AddTimetableItemActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch (hourOfDay){
                            case  1:
                                hourOfDay = 13;
                                break;
                            case 2:
                                hourOfDay = 14;
                                break;
                            case 3:
                                hourOfDay = 15;
                                break;
                            case 4:
                                hourOfDay = 16;
                                break;
                            case 5:
                                hourOfDay = 17;
                                break;
                            case 6:
                                hourOfDay = 18;
                                break;
                            case 7:
                                hourOfDay = 19;
                                break;
                        }
                        timeSetStatus[0] = 1;
                        textViewStartHour.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), 0, true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        timeSetStatus[0] = 2;
                        // Actions on clicks outside the dialog.
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (timeSetStatus[0] == 0) {
                            // Actions on Cancel button click.
                        }
                    }
                });
                dialog.show();
            }
        });

        textViewEndHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final int[] timeSetStatus = {0}; // A clicked button result.
                final Calendar calendar = Calendar.getInstance();
                final TimePickerDialog dialog = new TimePickerDialog(AddTimetableItemActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        switch (hourOfDay){
                            case  1:
                                hourOfDay = 13;
                                break;
                            case 2:
                                hourOfDay = 14;
                                break;
                            case 3:
                                hourOfDay = 15;
                                break;
                            case 4:
                                hourOfDay = 16;
                                break;
                            case 5:
                                hourOfDay = 17;
                                break;
                            case 6:
                                hourOfDay = 18;
                                break;
                            case 7:
                                hourOfDay = 19;
                                break;
                        }
                        timeSetStatus[0] = 1;
                        textViewEndHour.setText(hourOfDay + ":" + minute);
                    }
                }, calendar.get(Calendar.HOUR_OF_DAY), 0, true);

                dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                    @Override
                    public void onCancel(DialogInterface dialog) {
                        timeSetStatus[0] = 2;
                        // Actions on clicks outside the dialog.
                    }
                });

                dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        if (timeSetStatus[0] == 0) {
                            // Actions on Cancel button click.
                        }
                    }
                });
                dialog.show();
            }
        });


        textViewTeacherSelected.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllTeachers().observe(AddTimetableItemActivity.this, new Observer<List<Teachers>>() {
                    @Override
                    public void onChanged(List<Teachers> teachers) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTimetableItemActivity.this);
                        builder.setTitle("Choose Teacher");
                        if (teachers.size() == 0) {
                            listTeacher = new String[1];
                            listTeacher[0] = "No teacher added";
                        } else {
                            listTeacher = new String[teachers.size()];
                            for (int i = 0; i < teachers.size(); i++) {
                                listTeacher[i] = teachers.get(i).getName();
                            }
                        }


                        final int[] saveChecked = {-1};
                        builder.setSingleChoiceItems(listTeacher, checkedItem, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveChecked[0] = which;
                            }
                        });

                        builder.setCancelable(false);
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                if (saveChecked[0] != -1) {
                                    textViewTeacherSelected.setText(listTeacher[saveChecked[0]]);
                                } else if (saveChecked[0] == -1 && listTeacher.length > 0) {
                                    textViewTeacherSelected.setText(listTeacher[0]);
                                }
                            }
                        });
                        builder.show();
                    }
                });
            }
        });
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveTimetable() {
        String subject = textViewSubjectPicked.getText().toString();
        String day = textViewDaySelected.getText().toString();
        String startHour = textViewStartHour.getText().toString();
        String endHour = textViewEndHour.getText().toString();
        String locations = editTextLocation.getText().toString();
        String teacher = textViewTeacherSelected.getText().toString();
        String noteDetails = editTextDetails.getText().toString();

        if (subject.trim().isEmpty() || day.trim().isEmpty() || startHour.trim().isEmpty() || endHour.trim().isEmpty()) {
            Toast.makeText(this, "Please insert: Subject, Day, Hour start and end", Toast.LENGTH_SHORT).show();
        } else if (ID != -2) {
            Timetable timetable = new Timetable(subject, day, startHour, endHour, locations, teacher, noteDetails);
            timetable.setId(ID);
            databaseViewModel.update(timetable);
            Intent intent = new Intent();
            setResult(RESULT_OK, intent);
            finish();
        } else {
            Timetable timetable = new Timetable(subject, day, startHour, endHour, locations, teacher, noteDetails);
            databaseViewModel.insert(timetable);
            finish();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK){
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            databaseViewModel.insert(subject);
//            Toast.makeText(getContext(), "Subject added", Toast.LENGTH_SHORT).show();

        }else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddSubjectActivity.EXTRA_ID, -1);
            if (id == -1) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            subject.setId(id);
            databaseViewModel.update(subject);
//            Toast.makeText(getContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(getContext(), "Subject not added", Toast.LENGTH_SHORT).show();
        }
    }
}