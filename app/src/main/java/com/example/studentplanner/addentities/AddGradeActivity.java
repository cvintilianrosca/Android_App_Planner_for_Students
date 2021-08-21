package com.example.studentplanner.addentities;

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

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Subject;

import java.util.Calendar;
import java.util.List;

public class AddGradeActivity extends AppCompatActivity implements DatePickerDialog.OnDateSetListener {

    private EditText editTextValueGrade;
    private TextView textViewSubjectGrade;
    private TextView textViewDateGrade;
    private TextView textViewTermGrade;
    private TextView textViewFormGrade;
    private EditText noteEditText;

    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;
    public static final String EXTRA_GRADE = "com.example.studentplanner.EXTRA_GRADE";
    public static final String EXTRA_SUBJECT_NAME = "com.example.studentplanner.EXTRA_SUBJECT_NAME";
    public static final String EXTRA_DATE_GRADE = "com.example.studentplanner.EXTRA_DATE_GRADE";
    public static final String EXTRA_TERM_GRADE = "com.example.studentplanner.EXTRA_TERM_GRADE";
    public static final String EXTRA_FORM_GRADE = "com.example.studentplanner.EXTRA_FORM_GRADE";
    public static final String EXTRA_NOTE_GRADE = "com.example.studentplanner.EXTRA_NOTE_GRADE";
    public static final String EXTRA_GRADE_ID = "com.example.studentplanner.EXTRA_GRADE_ID";

    private String[] listSubjects;
    private Dialog dialog;

    public static final int ADD_SUBJECT_REQUEST = 1;
    public static final int EDIT_SUBJECT_REQUEST = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);
        editTextValueGrade = findViewById(R.id.editTextTitleExam);
        textViewSubjectGrade = findViewById(R.id.textViewPickSubjectExam);
        textViewDateGrade = findViewById(R.id.textViewPickDateExam);
        textViewTermGrade = findViewById(R.id.textViewTermGrade);
        textViewFormGrade = findViewById(R.id.textViewTypeExam);
        noteEditText = findViewById(R.id.editTextAddNoteGrade);

        toolbar = findViewById(R.id.toolbarTeacher);
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        final int checkedItem = 0;
        toolbar.inflateMenu(R.menu.add_entity_menu);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorBlueApp), PorterDuff.Mode.SRC_IN);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        final Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_GRADE_ID)){
//            toolbar.setTitle("Edit Grade");
//            toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
            editTextValueGrade.setText(String.valueOf(intent.getIntExtra(EXTRA_GRADE, -1)));
            textViewSubjectGrade.setText(intent.getStringExtra(EXTRA_SUBJECT_NAME));
            textViewDateGrade.setText(intent.getStringExtra(EXTRA_DATE_GRADE));
            textViewTermGrade.setText(intent.getStringExtra(EXTRA_TERM_GRADE));
            textViewFormGrade.setText(intent.getStringExtra(EXTRA_FORM_GRADE));
            noteEditText.setText(intent.getStringExtra(EXTRA_NOTE_GRADE));
        } else {
//            toolbar.setTitle("Add Grade");
//            toolbar.setTitleTextColor(getResources().getColor(R.color.colorBlack));
        }
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);

        textViewSubjectGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                databaseViewModel.getAllSubjects().observe(AddGradeActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddGradeActivity.this);
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
                                        textViewSubjectGrade.setText(listSubjects[saveChecked[0]]);
                                    } else if (saveChecked[0] == -1 && listSubjects.length > 0) {
                                        textViewSubjectGrade.setText(listSubjects[0]);
                                    }
                                }
                            });

                            builder.setNeutralButton("Add new subject",
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                            Intent intent = new Intent(AddGradeActivity.this, AddSubjectActivity.class);
                                            startActivityForResult(intent, ADD_SUBJECT_REQUEST);
                                            dialog.cancel();
                                        }
                                    });
                            builder.show();


                        } else {
                            dialog = new Dialog(AddGradeActivity.this);
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
                                    Intent intent = new Intent(AddGradeActivity.this, AddSubjectActivity.class);
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
                        saveGrade();
                        return true;
                    default:
                        return false;
                }
            }
        });



        textViewDateGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDateString();
            }
        });


        textViewTermGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGradeActivity.this);
                builder.setTitle("Select term");
                final String[] listTerms = new String[2];
                listTerms[0] = "1st Term";
                listTerms[1] = "2nd Term";

                final int[] checkedItem = {-1};
                builder.setSingleChoiceItems(listTerms, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem[0] = which;
                    }
                });

                builder.setCancelable(true);
                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkedItem[0] != -1) {
                            textViewTermGrade.setText(listTerms[checkedItem[0]]);
                        } else if (checkedItem[0] == -1 && listTerms.length > 0) {
                            textViewTermGrade.setText(listTerms[0]);
                        }
                    }
                });
                builder.show();
            }
        });

        textViewFormGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(AddGradeActivity.this);
                builder.setTitle("Select type");
                final String[] listTypes = new String[3];
                listTypes[0] = "Written";
                listTypes[1] = "Oral";
                listTypes[2] = "Practical";

                final int[] checkedItem = {-1};
                builder.setSingleChoiceItems(listTypes, 0, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        checkedItem[0] = which;
                    }
                });
                builder.setCancelable(true);
                builder.setPositiveButton("SELECT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (checkedItem[0] != -1) {
                            textViewFormGrade.setText(listTypes[checkedItem[0]]);
                        } else if (checkedItem[0] == -1 && listTypes.length > 0) {
                            textViewFormGrade.setText(listTypes[0]);
                        }
                    }
                });
                builder.show();

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
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
       String datePicked = dayOfMonth + "-" + (month + 1) + "-" + year;
        textViewDateGrade.setText(datePicked);
    }


    private void saveGrade() {

        if (editTextValueGrade.getText().toString().trim().isEmpty() || textViewSubjectGrade.getText().toString().trim().isEmpty() ||
        textViewDateGrade.getText().toString().trim().isEmpty()){
            editTextValueGrade.setHintTextColor(getResources().getColor(R.color.colorRed));
            textViewSubjectGrade.setHintTextColor(getResources().getColor(R.color.colorRed));
            textViewDateGrade.setHintTextColor(getResources().getColor(R.color.colorRed));
//            Toast.makeText(this, "Please Insert Grade Data", Toast.LENGTH_SHORT).show();
            return;
        }  else {
            int gradeValue = Integer.parseInt(editTextValueGrade.getText().toString());
            String nameSubject = textViewSubjectGrade.getText().toString();
            String date = textViewDateGrade.getText().toString();
            String term = textViewTermGrade.getText().toString();
            String type = textViewFormGrade.getText().toString();
            String note = noteEditText.getText().toString();
            Intent data = new Intent();
            data.putExtra(EXTRA_GRADE, gradeValue);
            data.putExtra(EXTRA_SUBJECT_NAME, nameSubject);
            data.putExtra(EXTRA_DATE_GRADE, date);
            data.putExtra(EXTRA_TERM_GRADE, term);
            data.putExtra(EXTRA_FORM_GRADE, type);
            data.putExtra(EXTRA_NOTE_GRADE, note);
            int id = getIntent().getIntExtra(EXTRA_GRADE_ID, -1);
            if (id != -1){
                data.putExtra(EXTRA_GRADE_ID, id);
            }
            setResult(RESULT_OK, data);
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
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