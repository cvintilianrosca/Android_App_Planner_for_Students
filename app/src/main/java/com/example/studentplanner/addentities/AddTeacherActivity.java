package com.example.studentplanner.addentities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.studentplanner.R;

public class AddTeacherActivity extends AppCompatActivity {

    private EditText editTextNameTeacher;
    private EditText editTextSurnameTeacher;
    private EditText editTextPhoneNumberTeacher;
    private EditText editTextEmailTeacher;
    private EditText editTextAddressTeacher;

    private Toolbar toolbar;
    public static final  String EXTRA_NAME_TEACHER = "com.example.studentplanner.EXTRA_NAME_TEACHER";
    public static final  String EXTRA_SURNAME_TEACHER = "com.example.studentplanner.EXTRA_SURNAME_TEACHER";
    public static final  String EXTRA_PHONE_TEACHER = "com.example.studentplanner.EXTRA_PHONE_TEACHER";
    public static final  String EXTRA_EMAIL_TEACHER = "com.example.studentplanner.EXTRA_EMAIL_TEACHER";
    public static final  String EXTRA_ADDRESS_TEACHER = "com.example.studentplanner.EXTRA_ADDRESS_TEACHER";
    public static final  String EXTRA_ID_TEACHER = "com.example.studentplanner.EXTRA_ID_TEACHER";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_teacher);
        editTextNameTeacher = findViewById(R.id.editTextNameTeacher);
        editTextSurnameTeacher = findViewById(R.id.editTextSurNameTeacher);
        editTextPhoneNumberTeacher = findViewById(R.id.editTextPhoneTeacher);
        editTextEmailTeacher = findViewById(R.id.editTextEmailTeacher);
        editTextAddressTeacher = findViewById(R.id.editTextAddressTeacher);

        toolbar = findViewById(R.id.toolbarTeacher);
        toolbar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.colorBlueApp), PorterDuff.Mode.SRC_IN);
        toolbar.inflateMenu(R.menu.add_entity_menu);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        Intent intent = getIntent();
        if (intent.hasExtra(EXTRA_ID_TEACHER)){
//            toolbar.setTitle("Edit Teacher");
            editTextNameTeacher.setText(intent.getStringExtra(EXTRA_NAME_TEACHER));
            editTextSurnameTeacher.setText(intent.getStringExtra(EXTRA_SURNAME_TEACHER));
            editTextPhoneNumberTeacher.setText(intent.getStringExtra(EXTRA_PHONE_TEACHER));
            editTextEmailTeacher.setText(intent.getStringExtra(EXTRA_EMAIL_TEACHER));
            editTextAddressTeacher.setText(intent.getStringExtra(EXTRA_ADDRESS_TEACHER));
        } else {
//            toolbar.setTitle("Add Teacher");
        }

        toolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.save_entity:
                        saveTeacher();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }

    private  void  saveTeacher(){
        String teacherName = editTextNameTeacher.getText().toString();
        String teacherSurname = editTextSurnameTeacher.getText().toString();
        String teacherPhone = editTextPhoneNumberTeacher.getText().toString();
        String teacherEmail = editTextEmailTeacher.getText().toString();
        String teacherAddress = editTextAddressTeacher.getText().toString();

        if (teacherName.trim().isEmpty() || teacherSurname.trim().isEmpty()){
            editTextNameTeacher.setHintTextColor(getResources().getColor(R.color.colorRed));
            editTextSurnameTeacher.setHintTextColor(getResources().getColor(R.color.colorRed));
            return;
        }
        Intent data = new Intent();
        data.putExtra(EXTRA_NAME_TEACHER, teacherName);
        data.putExtra(EXTRA_SURNAME_TEACHER, teacherSurname);
        data.putExtra(EXTRA_PHONE_TEACHER, teacherPhone);
        data.putExtra(EXTRA_EMAIL_TEACHER, teacherEmail);
        data.putExtra(EXTRA_ADDRESS_TEACHER, teacherAddress);
        int id = getIntent().getIntExtra(EXTRA_ID_TEACHER, -1);
        if (id != -1){
            data.putExtra(EXTRA_ID_TEACHER, id);
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