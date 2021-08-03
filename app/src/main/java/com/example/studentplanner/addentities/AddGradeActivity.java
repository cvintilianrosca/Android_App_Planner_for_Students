package com.example.studentplanner.addentities;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Subject;

import java.util.List;

public class AddGradeActivity extends AppCompatActivity {

    private EditText editTextValueGrade;
    private TextView textViewSubjectGrade;
    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;
    public static final String EXTRA_GRADE = "com.example.studentplanner.EXTRA_GRADE";
    public static final String EXTRA_SUBJECT_NAME = "com.example.studentplanner.EXTRA_SUBJECT_NAME";
    private String[] listSubjects;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_grade);
        editTextValueGrade = findViewById(R.id.editTextGradeValue);
        textViewSubjectGrade = findViewById(R.id.textViewPickSubject);
        final int checkedItem = 0;

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        textViewSubjectGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                databaseViewModel.getAllSubjects().observe(AddGradeActivity.this, new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        AlertDialog.Builder builder = new AlertDialog.Builder(AddGradeActivity.this);
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
                                    textViewSubjectGrade.setText(listSubjects[saveChecked[0]]);
                                }
                            }
                        });
                        builder.show();

//                        Toast.makeText(AddSubjectActivity.this, teachers.get(0).getName(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        toolbar = findViewById(R.id.toolbarGrade);
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
                        saveGrade();
                        return true;
                    default:
                        return false;
                }
            }
        });
    }



    private void saveGrade() {

        if (editTextValueGrade.getText().toString().trim().isEmpty() || textViewSubjectGrade.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Please Insert Grade Data", Toast.LENGTH_SHORT).show();
            return;
        }  else {
            int gradeValue = Integer.parseInt(editTextValueGrade.getText().toString());
            String nameSubject = textViewSubjectGrade.getText().toString();
            Intent data = new Intent();
            data.putExtra(EXTRA_GRADE, gradeValue);
            data.putExtra(EXTRA_SUBJECT_NAME, nameSubject);
            setResult(RESULT_OK, data);
            finish();
        }
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

}