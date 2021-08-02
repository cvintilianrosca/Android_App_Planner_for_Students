package com.example.studentplanner;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.studentplanner.database.entities.Subject;

import java.util.Calendar;
import java.util.List;

public class AddTaskActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextValueTitle;
    private TextView textViewSubjectPicked;
    private TextView textViewDatePicked;
    private EditText editTextNoteDetails;

    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;

    public static final String EXTRA_TITLE = "com.example.studentplanner.EXTRA_TITLE";
    public static final String EXTRA_SUBJECT_PICKED = "com.example.studentplanner.EXTRA_SUBJECT_PICKED";
    public static final String EXTRA_DATE_PICKED = "com.example.studentplanner.EXTRA_DATE_PICKED";
    public static final String EXTRA_NOTE_DETAILS = "com.example.studentplanner.EXTRA_NOTE_DETAILS";
    private String[] listSubjects;
    private String datePicked;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);
        editTextValueTitle = findViewById(R.id.editTextTaskTitle);
        textViewSubjectPicked = findViewById(R.id.textViewPickSubjectTask);
        textViewDatePicked = findViewById(R.id.textViewPickDateTask);
        editTextNoteDetails = findViewById(R.id.editTextTaskDetails);
        final int checkedItem = 0;
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);

        textViewSubjectPicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllSubjects().observe(AddTaskActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddTaskActivity.this);
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
                                }
                            }
                        });
                        builder.show();

//                        Toast.makeText(AddSubjectActivity.this, teachers.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        textViewDatePicked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateString();
            }
        });

        toolbar = findViewById(R.id.toolbarTask);
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
                        saveTask();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private void getDateString() {
        DatePickerDialog datePickerDialog = new DatePickerDialog(this,this,
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

    public void saveTask(){
        String title = editTextValueTitle.getText().toString();
        String subject = textViewSubjectPicked.getText().toString();
        String date = textViewDatePicked.getText().toString();
        String note = editTextNoteDetails.getText().toString();
        if (title.trim().isEmpty() || subject.trim().isEmpty() || date.trim().isEmpty() || note.trim().isEmpty()){
            Toast.makeText(this, "Please insert Task data", Toast.LENGTH_SHORT).show();
            return;
        } else {
            Intent data = new Intent();
            data.putExtra(EXTRA_TITLE, title);
            data.putExtra(EXTRA_SUBJECT_PICKED, subject);
            data.putExtra(EXTRA_DATE_PICKED, date);
            data.putExtra(EXTRA_NOTE_DETAILS, note);
            setResult(RESULT_OK, data);
            finish();
        }
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        datePicked = dayOfMonth+ "-" +(month+1)+ "-" + year;
        textViewDatePicked.setText(datePicked);
    }
}