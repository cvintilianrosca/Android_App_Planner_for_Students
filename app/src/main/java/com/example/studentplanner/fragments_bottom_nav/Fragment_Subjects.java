package com.example.studentplanner.fragments_bottom_nav;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.addentities.AddGradeActivity;
import com.example.studentplanner.addentities.AddSubjectActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.SubjectAdapter;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Subjects extends Fragment {
    public static final int ADD_SUBJECT_REQUEST=1;
    public static final int EDIT_SUBJECT_REQUEST=2;
    public static final int ADD_GRADE_REQUEST = 3;
    public static final int EDIT_GRADE_REQUEST = 404;

    private Toolbar toolbar;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    private FloatingActionButton floatingActionButton;

    private Animation rotateOpen;
    private Animation rotateClose;
    private Animation fromBottom;
    private Animation toBottom;

    private FloatingActionButton floatingActionButtonAddSubject;
    private FloatingActionButton floatingActionButtonAddGrade;
    private View backgroundDimmer;
    private TextView textViewSubjectSubject;
    private TextView textViewGradeSubject;



    public Fragment_Subjects(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.floatingActionButton = floatingActionButton;
        this.toolbar = toolbar;
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        toolbar.setTitle("Subjects");
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);

        View v = inflater.inflate(R.layout.fragments_subjects, container, false);
        recyclerView = v.findViewById(R.id.recycleViewFragmentSubjects);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        final SubjectAdapter subjectAdapter = new SubjectAdapter();

        rotateOpen = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_open_anim);
        rotateClose = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_close_anim);
        fromBottom = AnimationUtils.loadAnimation(getContext(), R.anim.from_bottom_anim);
        toBottom = AnimationUtils.loadAnimation(getContext(), R.anim.to_bottom_anim);
        backgroundDimmer = v.findViewById(R.id.background_dimmerSubjects);

        floatingActionButtonAddGrade = v.findViewById(R.id.floatingActionButtonAddGradeSubject);
        floatingActionButtonAddSubject = v.findViewById(R.id.floatingActionButtonAddSubjectSubject);

        textViewGradeSubject = v.findViewById(R.id.textViewSubjectSubject);
        textViewSubjectSubject = v.findViewById(R.id.textViewGradeSubject);


        Animation animationInitial = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_initial_anim);
        floatingActionButton.startAnimation(animationInitial);

        databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
            @Override
            public void onChanged(final List<Subject> subjects) {
                subjectAdapter.submitList(subjects);
            }
        });


        floatingActionButtonAddGrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGradeActivity.class);
                startActivityForResult(intent, ADD_GRADE_REQUEST);

                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        floatingActionButtonAddSubject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddSubjectActivity.class);
                startActivityForResult(intent, ADD_SUBJECT_REQUEST);

                setVisibility(clicked);
                setAnimation(clicked);
                setClickable(clicked);
                clicked = !clicked;
            }
        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onAddButtonClicked();
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
                databaseViewModel.delete(subjectAdapter.getSubjectAtPosition(viewHolder.getAdapterPosition()));
            }
        }).attachToRecyclerView(recyclerView);

        subjectAdapter.setOnItemClickListener(new SubjectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Subject subject) {
                Intent intent = new Intent(getContext(), AddSubjectActivity.class);
                intent.putExtra(AddSubjectActivity.EXTRA_NAME, subject.getName());
                intent.putExtra(AddSubjectActivity.EXTRA_NOTE, subject.getAdditional_info());
                intent.putExtra(AddSubjectActivity.EXTRA_ROOM, subject.getRoom());
                intent.putExtra(AddSubjectActivity.EXTRA_TEACHER, subject.getTeacher());
                intent.putExtra(AddSubjectActivity.EXTRA_ID, subject.getId());
                startActivityForResult(intent, EDIT_SUBJECT_REQUEST);
            }
        });

        recyclerView.setAdapter(subjectAdapter);
        return v;

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
            Toast.makeText(getContext(), "Subject added", Toast.LENGTH_SHORT).show();

        }else if (requestCode == EDIT_SUBJECT_REQUEST && resultCode == RESULT_OK) {
            int id = data.getIntExtra(AddSubjectActivity.EXTRA_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            String name = data.getStringExtra(AddSubjectActivity.EXTRA_NAME);
            String room = data.getStringExtra(AddSubjectActivity.EXTRA_ROOM);
            String teacher = data.getStringExtra(AddSubjectActivity.EXTRA_TEACHER);
            String note = data.getStringExtra(AddSubjectActivity.EXTRA_NOTE);
            final Subject subject = new Subject(name, room, teacher, note);
            subject.setId(id);
            databaseViewModel.update(subject);
            Toast.makeText(getContext(), "Note Updated", Toast.LENGTH_SHORT).show();
        }
        else {
//            Toast.makeText(getContext(), "Subject not added", Toast.LENGTH_SHORT).show();
        }
        if (requestCode == ADD_GRADE_REQUEST && resultCode == RESULT_OK){
            if (data == null){
                Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            } else {
                final int grade = data.getIntExtra(AddGradeActivity.EXTRA_GRADE, -1);
                String subjectName = data.getStringExtra(AddGradeActivity.EXTRA_SUBJECT_NAME);
                LiveData<List<Subject>> subjectInfo =  databaseViewModel.getSubjectWithName(subjectName);
                subjectInfo.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        databaseViewModel.insert(new Grades(subjects.get(0).getId(), grade));
                    }
                });
                Toast.makeText(getContext(), "Done", Toast.LENGTH_SHORT).show();
            }
        }else if (requestCode == EDIT_GRADE_REQUEST&& resultCode == RESULT_OK) {
            assert data != null;
            final int id = data.getIntExtra(AddGradeActivity.EXTRA_GRADE_ID, -1);
            if (id == -1) {
                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            final int grade = data.getIntExtra(AddGradeActivity.EXTRA_GRADE, -1);
            String subjectName = data.getStringExtra(AddGradeActivity.EXTRA_SUBJECT_NAME);
            LiveData<List<Subject>> subjectInfo =  databaseViewModel.getSubjectWithName(subjectName);
            subjectInfo.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                @Override
                public void onChanged(List<Subject> subjects) {
                    Grades grades = new Grades(subjects.get(0).getId(), grade);
                    grades.setId(id);
                    databaseViewModel.update(grades);
                }
            });
//            Toast.makeText(getContext(), "Grade Updated", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "Grade not added", Toast.LENGTH_SHORT).show();
        }
    }


    private  boolean clicked = false;

    private void onAddButtonClicked(){

        setVisibility(clicked);
        setAnimation(clicked);
        setClickable(clicked);
        clicked = !clicked;
    }
    public void setVisibility(boolean clicked){
        if (!clicked){
            floatingActionButtonAddGrade.setVisibility(View.VISIBLE);
            floatingActionButtonAddSubject.setVisibility(View.VISIBLE);
            textViewSubjectSubject.setVisibility(View.VISIBLE);
            textViewGradeSubject.setVisibility(View.VISIBLE);
        } else {
            floatingActionButtonAddGrade.setVisibility(View.INVISIBLE);
            floatingActionButtonAddSubject.setVisibility(View.INVISIBLE);
            textViewGradeSubject.setVisibility(View.INVISIBLE);
            textViewSubjectSubject.setVisibility(View.INVISIBLE);
        }
    }
    public void  setAnimation(boolean clicked){
        if (!clicked){
            backgroundDimmer.setVisibility(View.VISIBLE);
            floatingActionButtonAddSubject.startAnimation(fromBottom);
            floatingActionButtonAddGrade.startAnimation(fromBottom);
            textViewSubjectSubject.startAnimation(fromBottom);
            textViewGradeSubject.startAnimation(fromBottom);
            floatingActionButton.startAnimation(rotateOpen);
        } else {
            backgroundDimmer.setVisibility(View.INVISIBLE);
            floatingActionButtonAddSubject.startAnimation(toBottom);
            floatingActionButtonAddGrade.startAnimation(toBottom);
            textViewGradeSubject.startAnimation(toBottom);
            textViewSubjectSubject.startAnimation(toBottom);
            floatingActionButton.startAnimation(rotateClose);
        }
    }

    public void setClickable(boolean clicked){
        if (!clicked){
            floatingActionButtonAddGrade.setClickable(true);
            floatingActionButtonAddSubject.setClickable(true);
        } else{
            floatingActionButtonAddSubject.setClickable(false);
            floatingActionButtonAddGrade.setClickable(false);
        }
    }
}
