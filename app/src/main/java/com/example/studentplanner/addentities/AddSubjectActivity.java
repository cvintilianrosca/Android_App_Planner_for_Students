package com.example.studentplanner.addentities;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

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
import android.widget.EditText;
import android.widget.TextView;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Teachers;

import java.util.List;

public class AddSubjectActivity extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextRoom;
    private TextView textViewTeacher;
    private EditText editTextNote;
    private Toolbar toolbar;
    private Dialog dialog;

    public static final int ADD_TEACHER_REQUEST=2;
    public static final int EDIT_TEACHER_REQUEST=7;

    private DatabaseViewModel databaseViewModel;
    public static final String EXTRA_NAME = "com.example.studentplanner.EXTRA_NAME";
    public static final String EXTRA_TEACHER = "com.example.studentplanner.EXTRA_TEACHER";
    public static final String EXTRA_ROOM = "com.example.studentplanner.EXTRA_ROOM";
    public static final String EXTRA_NOTE = "com.example.studentplanner.EXTRA_NOTE";
    public static final String EXTRA_ID = "com.example.studentplanner.EXTRA_ID";
    private String[] listTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        editTextNote = findViewById(R.id.rETAddSubject);
        final int checkedItem = 0;
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN);
        editTextName = findViewById(R.id.editTextTitleExam);
        editTextRoom = findViewById(R.id.editTextRoomSubject);
        toolbar = findViewById(R.id.toolbarTeacher);

        textViewTeacher = findViewById(R.id.textViewTeacherSubject);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextRoom.setText(intent.getStringExtra(EXTRA_ROOM));
            textViewTeacher.setText(intent.getStringExtra(EXTRA_TEACHER));
            editTextNote.setText(intent.getStringExtra(EXTRA_NOTE));
        } else {
        }
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorBlueApp), PorterDuff.Mode.SRC_IN);

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        textViewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllTeachers().observe(AddSubjectActivity.this, new Observer<List<Teachers>>() {
                    @Override
                    public void onChanged(List<Teachers> teachers) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubjectActivity.this);
                        if (teachers.size() > 0) {
                            builder.setTitle("Select a teacher");
                            listTeacher = new String[teachers.size()];
                            for (int i = 0; i < teachers.size(); i++) {
                                listTeacher[i] = teachers.get(i).getName();
                            }
                            final int[] saveChecked = {-1};
                            builder.setSingleChoiceItems(listTeacher, checkedItem, new DialogInterface.OnClickListener() {
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
                                        textViewTeacher.setText(listTeacher[saveChecked[0]]);
                                    } else if (saveChecked[0] == -1 && listTeacher.length > 0) {
                                        textViewTeacher.setText(listTeacher[0]);
                                    }
                                }
                            });
                            builder.setNeutralButton("Add new teacher",
                                    new DialogInterface.OnClickListener()
                                    {
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                    Intent intent = new Intent(AddSubjectActivity.this, AddTeacherActivity.class);
                                    startActivityForResult(intent, ADD_TEACHER_REQUEST);
                                    dialog.cancel();
                                        }
                                    });

                            builder.show();
                        } else {
                            dialog = new Dialog(AddSubjectActivity.this);
                            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                            dialog.setCancelable(true);
                            dialog.setContentView(R.layout.dialog_layout_add_teacher);
                            TextView pick = dialog.findViewById(R.id.textViewAddNewTeacherDialog);
                            Button cancel = dialog.findViewById(R.id.buttonCancelTeacherDialog);
                            dialog.show();
                            pick.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    dialog.cancel();
                                    Intent intent = new Intent(AddSubjectActivity.this, AddTeacherActivity.class);
                                    startActivityForResult(intent, ADD_TEACHER_REQUEST);

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
                        saveSubject();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }



    private void saveSubject() {
        String Name = editTextName.getText().toString();
        String Room = editTextRoom.getText().toString();
        String Teacher = textViewTeacher.getText().toString();
        String Note = editTextNote.getText().toString();

        if (Name.trim().isEmpty() || Teacher.trim().isEmpty()){
            editTextName.setHintTextColor(getResources().getColor(R.color.colorRed));
            textViewTeacher.setHintTextColor(getResources().getColor(R.color.colorRed));
            return;
        }

        Intent data = new Intent();
        data.putExtra(EXTRA_NAME, Name);
        data.putExtra(EXTRA_ROOM, Room);
        data.putExtra(EXTRA_TEACHER, Teacher);
        data.putExtra(EXTRA_NOTE, Note);

        int id = getIntent().getIntExtra(EXTRA_ID, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID, id);
        }
        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TEACHER_REQUEST && resultCode == RESULT_OK){
            if (data == null){
//                Toast.makeText(getContext(), "Something", Toast.LENGTH_SHORT).show();
            } else {
                String teacherName = data.getStringExtra(AddTeacherActivity.EXTRA_NAME_TEACHER);
                String teacherSurname = data.getStringExtra(AddTeacherActivity.EXTRA_SURNAME_TEACHER);
                String teacherPhoneNumber = data.getStringExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER);
                String teacherEmail = data.getStringExtra(AddTeacherActivity.EXTRA_EMAIL_TEACHER);
                String teacherAddress = data.getStringExtra(AddTeacherActivity.EXTRA_ADDRESS_TEACHER);

                final Teachers teachers = new Teachers(teacherName, teacherSurname, teacherPhoneNumber,
                        teacherEmail, teacherAddress);
                databaseViewModel.insert(teachers);
            }
        }else if (requestCode == EDIT_TEACHER_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddTeacherActivity.EXTRA_ID_TEACHER, -1);
            if (id == -1) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }

            String teacherName = data.getStringExtra(AddTeacherActivity.EXTRA_NAME_TEACHER);
            String teacherSurname = data.getStringExtra(AddTeacherActivity.EXTRA_SURNAME_TEACHER);
            String teacherPhoneNumber = data.getStringExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER);
            String teacherEmail = data.getStringExtra(AddTeacherActivity.EXTRA_EMAIL_TEACHER);
            String teacherAddress = data.getStringExtra(AddTeacherActivity.EXTRA_ADDRESS_TEACHER);

            final Teachers teachers = new Teachers(teacherName, teacherSurname, teacherPhoneNumber,
                    teacherEmail, teacherAddress);
            teachers.setId(id);
            databaseViewModel.update(teachers);
//            Toast.makeText(getContext(), "Teacher Updated", Toast.LENGTH_SHORT).show();
        } else {

        }
    }
}