package com.example.studentplanner.addentities;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
        editTextName = findViewById(R.id.editTextNameSubject);
        editTextRoom = findViewById(R.id.editTextRoomSubject);
        editTextNote = findViewById(R.id.editTextNote);
        toolbar = findViewById(R.id.toolbarSubject);
        textViewTeacher = findViewById(R.id.textViewTeacherSubject);
        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID)){
            toolbar.setTitle("Edit Subject");
            editTextName.setText(intent.getStringExtra(EXTRA_NAME));
            editTextRoom.setText(intent.getStringExtra(EXTRA_ROOM));
            textViewTeacher.setText(intent.getStringExtra(EXTRA_TEACHER));
            editTextNote.setText(intent.getStringExtra(EXTRA_NOTE));
        } else {
            toolbar.setTitle("Add Subject");
        }
        final int checkedItem = 0;
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        textViewTeacher.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllTeachers().observe(AddSubjectActivity.this, new Observer<List<Teachers>>() {
                    @Override
                    public void onChanged(List<Teachers> teachers) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddSubjectActivity.this);
                        builder.setTitle("Choose Teacher");
                        if (teachers.size() == 0){
                            listTeacher = new String[1];
                            listTeacher[0] = "No teacher added";
                        } else {
                            listTeacher = new String[teachers.size()];
                            for (int i = 0; i < teachers.size() ; i++) {
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
                                if (saveChecked[0] != -1){
                                    textViewTeacher.setText(listTeacher[saveChecked[0]]);
                                }
                            }
                        });
                        builder.show();

//                        Toast.makeText(AddSubjectActivity.this, teachers.get(0).getName(), Toast.LENGTH_SHORT).show();
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

        if (Name.trim().isEmpty() || Room.trim().isEmpty() || Teacher.trim().isEmpty() || Note.trim().isEmpty()){
            Toast.makeText(this, "Please insert Subject data", Toast.LENGTH_SHORT).show();
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

}