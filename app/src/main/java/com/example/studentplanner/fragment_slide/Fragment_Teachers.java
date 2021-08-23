package com.example.studentplanner.fragment_slide;

import android.app.Dialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.addentities.AddTeacherActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.TeacherAdapter;
import com.example.studentplanner.database.entities.Teachers;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.Comparator;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Teachers extends Fragment {

    public static final int ADD_TEACHER_REQUEST = 2;
    public static final int EDIT_TEACHER_REQUEST = 7;
    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private TeacherAdapter teacherAdapter;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;
    private Dialog dialog;

    public Fragment_Teachers(final Toolbar toolbar, final FloatingActionButton floatingActionButton) {
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Teachers");
        View v = inflater.inflate(R.layout.fragment_teachers, container, false);

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        recyclerView = v.findViewById(R.id.recycleViewFragmentTeachers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        teacherAdapter = new TeacherAdapter();

        databaseViewModel.getAllTeachers().observe(getViewLifecycleOwner(), new Observer<List<Teachers>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onChanged(List<Teachers> teachers) {
                for (Teachers t: teachers ){
                    t.setFlagFirst(-1);
                }
                for (Teachers t: teachers) {
                    if (t.getFlagFirst() == 101){
                        Toast.makeText(getContext(), "HULO", Toast.LENGTH_SHORT).show();

                    }                }
                teachers.sort(new Comparator<Teachers>() {
                    @Override
                    public int compare(Teachers o1, Teachers o2) {
                        return o1.getName().compareTo(o2.getName());
                    }
                });
                for (int i = teachers.size()-1; i > 0; i--) {
                    if (teachers.get(i).getName().charAt(0) != teachers.get(i - 1).getName().charAt(0)) {
                        teachers.get(i).setFlagFirst(101);
                    }
                }
                if (teachers.size() >0){
                    teachers.get(0).setFlagFirst(101);
                }
                teacherAdapter.setTeachersArrayList(teachers);
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTeacherActivity.class);
                startActivityForResult(intent, ADD_TEACHER_REQUEST);
            }
        });

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                databaseViewModel.delete(teacherAdapter.getTeacherAtPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        teacherAdapter.setOnItemClickListener(new TeacherAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Teachers teachers) {

                dialog = new Dialog(getContext());
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setCancelable(true);

                dialog.setContentView(R.layout.dialog_layout_teacher);
                TextView professorName = dialog.findViewById(R.id.textViewProfessorName);
                TextView phone = dialog.findViewById(R.id.textViewProfessorPhone);
                TextView email = dialog.findViewById(R.id.textViewProfessorEmail);

                Button delete = dialog.findViewById(R.id.buttonDeleteProfessor);
                Button edit = dialog.findViewById(R.id.buttonEditProfessor);
                Button cancel = dialog.findViewById(R.id.buttonCancelProfessorDialog);
                TextView location = dialog.findViewById(R.id.textViewProfessorAddress);
                professorName.setText(teachers.getName()+ " " + teachers.getSurname());
                if (teachers.getPhoneNumber().trim().isEmpty()){
                    phone.setText("Not inserted");
                } else {
                    phone.setText(teachers.getPhoneNumber());
                }

                if (teachers.getEmail().trim().isEmpty()){
                    email.setText("Not inserted");
                } else {
                    email.setText(teachers.getEmail());
                }

                if (teachers.getAddress().trim().isEmpty()){
                    location.setText("Not inserted");

                } else {
                    location.setText(teachers.getAddress());
                }


                cancel.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                    }
                });

                edit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(getContext(), AddTeacherActivity.class);
                        intent.putExtra(AddTeacherActivity.EXTRA_NAME_TEACHER, teachers.getName());
                        intent.putExtra(AddTeacherActivity.EXTRA_SURNAME_TEACHER, teachers.getSurname());
                        intent.putExtra(AddTeacherActivity.EXTRA_PHONE_TEACHER, teachers.getPhoneNumber());
                        intent.putExtra(AddTeacherActivity.EXTRA_EMAIL_TEACHER, teachers.getEmail());
                        intent.putExtra(AddTeacherActivity.EXTRA_ADDRESS_TEACHER, teachers.getAddress());
                        intent.putExtra(AddTeacherActivity.EXTRA_ID_TEACHER, teachers.getId());
                        startActivityForResult(intent, EDIT_TEACHER_REQUEST);
                        dialog.dismiss();
                    }
                });

                delete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        databaseViewModel.delete(teachers);
                        dialog.dismiss();
                    }
                });
                dialog.show();
            }
        });

        recyclerView.setAdapter(teacherAdapter);
        return v;
    }

    ///////////////////////////////////////////////////////////////////////////////////////
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ADD_TEACHER_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
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
        } else if (requestCode == EDIT_TEACHER_REQUEST && resultCode == RESULT_OK) {
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
//            Toast.makeText(getContext(), "Teacher not added", Toast.LENGTH_SHORT).show();
        }
    }
}
