package com.example.studentplanner.addentities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Subject;

import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextValueTitle;
    private TextView textViewSubjectPicked;
    private TextView textViewDatePicked;
    private EditText editTextNoteDetails;

    public static final int ADD_SUBJECT_REQUEST = 1;
    public static final int EDIT_SUBJECT_REQUEST = 2;

    private Dialog dialog;
    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;

    public static final String EXTRA_TITLE = "com.example.studentplanner.EXTRA_TITLE";
    public static final String EXTRA_SUBJECT_PICKED = "com.example.studentplanner.EXTRA_SUBJECT_PICKED";
    public static final String EXTRA_DATE_PICKED = "com.example.studentplanner.EXTRA_DATE_PICKED";
    public static final String EXTRA_NOTE_DETAILS = "com.example.studentplanner.EXTRA_NOTE_DETAILS";
    public static final String EXTRA_ID = "com.example.studentplanner.EXTRA_ID";
    private String[] listSubjects;
    private String datePicked;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editTextValueTitle = findViewById(R.id.editTextTitleExam);
        textViewSubjectPicked = findViewById(R.id.textViewPickSubjectExam);
        textViewDatePicked = findViewById(R.id.textViewPickDateExam);
        editTextNoteDetails = findViewById(R.id.editTextTaskDetails);
        textViewDatePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateString();
            }
        });
        editTextNoteDetails.setMaxHeight(4000);
        final int checkedItem = 0;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        toolbar = findViewById(R.id.toolbarTeacher);
        toolbar.inflateMenu(R.menu.add_entity_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorBlueApp), PorterDuff.Mode.SRC_IN);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)) {
            toolbar.setTitle("Edit Task");
            editTextValueTitle.setText(intent.getStringExtra(EXTRA_TITLE));
            editTextNoteDetails.setText(intent.getStringExtra(EXTRA_NOTE_DETAILS));
            textViewDatePicked.setText(intent.getStringExtra(EXTRA_DATE_PICKED));
            textViewSubjectPicked.setText(intent.getStringExtra(EXTRA_SUBJECT_PICKED));
        } else {
            toolbar.setTitle("Add Task");
        }
        textViewSubjectPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllSubjects().observe(AddTaskActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
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
                            builder.setNeutralButton("Add new subject",
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            Intent intent = new Intent(AddTaskActivity.this, AddSubjectActivity.class);
                                            startActivityForResult(intent, ADD_SUBJECT_REQUEST);
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();


                        } else {
                            dialog = new Dialog(AddTaskActivity.this);
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
                                    Intent intent = new Intent(AddTaskActivity.this, AddSubjectActivity.class);
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


        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_entity:
                        saveTask();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void getDateString() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, this,
                Calendar.getInstance().get(Calendar.YEAR),
                Calendar.getInstance().get(Calendar.MONTH),
                Calendar.getInstance().get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void saveTask() {
        String title = editTextValueTitle.getText().toString();
        String subject = textViewSubjectPicked.getText().toString();
        String date = textViewDatePicked.getText().toString();
        String note = editTextNoteDetails.getText().toString();
        if (title.trim().isEmpty() || subject.trim().isEmpty() || date.trim().isEmpty()) {
            editTextValueTitle.setHintTextColor(getResources().getColor(R.color.colorRed));
            textViewSubjectPicked.setHintTextColor(getResources().getColor(R.color.colorRed));
            textViewDatePicked.setHintTextColor(getResources().getColor(R.color.colorRed));
//            Toast.makeText(this, "Please insert Title, Subject and Date", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_SUBJECT_PICKED, subject);
            data.putExtra(EXTRA_DATE_PICKED, date);
            data.putExtra(EXTRA_NOTE_DETAILS, note);
            int id = getIntent().getIntExtra(EXTRA_ID, -1);
            if (id != -1) {
                data.putExtra(EXTRA_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datePicked = dayOfMonth + "-" + (month + 1) + "-" + year;
        textViewDatePicked.setText(datePicked);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            databaseViewModel.insert(subject);
        } else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddSubjectActivity.EXTRA_ID, -1);
            if (id == -1) {
                return;
            }
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            subject.setId(id);
            databaseViewModel.update(subject);
        } else {
        }
    }

}