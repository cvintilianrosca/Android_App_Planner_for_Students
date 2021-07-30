package com.example.studentplanner;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class AddSubject extends AppCompatActivity {

    private EditText editTextName;
    private EditText editTextRoom;
    private EditText editTextTeacher;
    private EditText editTextNote;
    private Toolbar toolbar;
    public static final String EXTRA_NAME = "com.example.studentplanner.EXTRA_NAME";
    public static final String EXTRA_TEACHER = "com.example.studentplanner.EXTRA_TEACHER";
    public static final String EXTRA_ROOM = "com.example.studentplanner.EXTRA_ROOM";
    public static final String EXTRA_NOTE = "com.example.studentplanner.EXTRA_NOTE";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_subject);
        editTextName = findViewById(R.id.editTextNameSubject);
        editTextRoom = findViewById(R.id.editTextRoomSubject);
        editTextTeacher = findViewById(R.id.editTextTeacherSubject);
        editTextNote = findViewById(R.id.editTextNote);
        toolbar = findViewById(R.id.toolbarSubject);
        toolbar.inflateMenu(R.menu.add_subject_menu);
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
                    case R.id.save_subject:
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
        String Teacher = editTextTeacher.getText().toString();
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
        setResult(RESULT_OK, data);
        finish();
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}