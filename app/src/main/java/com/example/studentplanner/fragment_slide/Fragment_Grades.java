package com.example.studentplanner.fragment_slide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.adapters.SubjectAdapter;
import com.example.studentplanner.addentities.AddGradeActivity;
import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.adapters.GradeAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.addentities.AddSubjectActivity;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.relations.SubjectWithGrades;
import com.example.studentplanner.database.relations.SubjectWithGrades2;
import com.example.studentplanner.fragments_bottom_nav.Fragment_Subjects;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class Fragment_Grades extends Fragment {
    private Toolbar toolbar=null;
    private FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private DatabaseViewModel databaseViewModel;
    public static final int ADD_GRADE_REQUEST = 3;
    public static final int EDIT_GRADE_REQUEST = 404;

    private ArrayList<String> arrayListGroup;
    LinearLayoutManager layoutManagerGroup;
    GroupAdp adapterGroup;


    public Fragment_Grades(){}
    public Fragment_Grades(final Toolbar toolbar, final FloatingActionButton floatingActionButton) {
//        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_grades, container, false);
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        recyclerView = v.findViewById(R.id.recycleViewFragmentGrades);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        adapterGroup = new GroupAdp(getActivity());
        arrayListGroup = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            arrayListGroup.add("Group " + i);
        }

//        databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
//            @Override
//            public void onChanged(List<Subject> subjects) {
//                final ArrayList<SubjectWithGrades2> subjectWithGradesArrayList = new ArrayList<>();
//                for (final Subject s : subjects) {
////                    databaseViewModel.getSubjectsWithGrades(s.getId()).observe(getViewLifecycleOwner(), new Observer<List<SubjectWithGrades>>() {
////                        @Override
////                        public void onChanged(List<SubjectWithGrades> subjectWithGrades) {
////                            if (subjectWithGrades.get(0).listGrades.size() > 0) {
////                                subjectWithGradesArrayList.add(subjectWithGrades.get(0));
////                            }
////                            adapterGroup.setArrayListGroup(subjectWithGradesArrayList);
////                            recyclerView.setAdapter(adapterGroup);
//////                            Toast.makeText(getContext(), subjectWithGradesArrayList.size()+ "", Toast.LENGTH_SHORT).show();
////                        }
////                    });
//                    databaseViewModel.getAllGrades().observe(getViewLifecycleOwner(), new Observer<List<Grades>>() {
//                        @Override
//                        public void onChanged(List<Grades> grades) {
//                            List<Grades> arrayListGradesTemp = new ArrayList<>();
//                            for (Grades g : grades) {
//                                if (g.getNameSubject().compareTo(s.getName()) == 0) {
//                                    arrayListGradesTemp.add(g);
//                                }
//                            }
//                            if (arrayListGradesTemp.size() > 0) {
//                                subjectWithGradesArrayList.add(new SubjectWithGrades2(s, arrayListGradesTemp));
//                            }
//                            adapterGroup.setArrayListGroup(new ArrayList<SubjectWithGrades2>());
//                            adapterGroup.destroy();
//                            adapterGroup.setArrayListGroup(subjectWithGradesArrayList);
//                            recyclerView.setAdapter(adapterGroup);
//                        }
//                    });
//                }
//
//            }
//        });

        databaseViewModel.getAllCombinationsOfSubjectsWithGrades().observe(getViewLifecycleOwner(), new Observer<List<SubjectWithGrades>>() {
            @Override
            public void onChanged(List<SubjectWithGrades> subjectWithGrades) {
                ArrayList<SubjectWithGrades> subjectWithGradesArrayList = new ArrayList<>();
                for (SubjectWithGrades s: subjectWithGrades) {
                    if (s.listGrades.size()>0){
                        subjectWithGradesArrayList.add(s);
                    }
//                    Toast.makeText(getContext(), s.subject.getName(), Toast.LENGTH_SHORT).show();
                }
                adapterGroup.setArrayListGroup(subjectWithGradesArrayList);
                recyclerView.setAdapter(adapterGroup);
            }
        });

//      layoutManagerGroup = new LinearLayoutManager(getContext());
//      recyclerView.setLayoutManager(layoutManagerGroup);


//      memberAdp.setOnItemClickListener(new MemberAdp.OnItemClickListener() {
//          @Override
//          public void onItemClick(String string) {
//              Toast.makeText(getContext(), "MUAHAHA", Toast.LENGTH_SHORT).show();
//          }
//      });

//        final GradeAdapter gradeAdapter = new GradeAdapter();
//        databaseViewModel.getAllGrades().observe(getViewLifecycleOwner(), new Observer<List<Grades>>() {
//            @Override
//            public void onChanged(List<Grades> grades) {
//                gradeAdapter.submitList(grades);
//            }
//        });

        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddGradeActivity.class);
                startActivityForResult(intent, ADD_GRADE_REQUEST);
            }
        });

//        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
//                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
//            @Override
//            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
//                return false;
//            }
//
//            @Override
//            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
//                databaseViewModel.delete(gradeAdapter.getGradeAtPosition((viewHolder.getAdapterPosition())));
//                Toast.makeText(getContext(), "Task Deleted", Toast.LENGTH_SHORT).show();
//            }
//        }).attachToRecyclerView(recyclerView);


//        gradeAdapter.setOnItemClickListener(new GradeAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(final Grades grades) {
//                final Intent intent = new Intent(getContext(), AddGradeActivity.class);
//                intent.putExtra(AddGradeActivity.EXTRA_GRADE, grades.getValue());
//                intent.putExtra(AddGradeActivity.EXTRA_GRADE_ID, grades.getId());
//                intent.putExtra(AddGradeActivity.EXTRA_SUBJECT_NAME, grades.getNameSubject());
//                intent.putExtra(AddGradeActivity.EXTRA_TERM_GRADE, grades.getTerm());
//                intent.putExtra(AddGradeActivity.EXTRA_FORM_GRADE, grades.getType());
//                intent.putExtra(AddGradeActivity.EXTRA_NOTE_GRADE, grades.getNote());
//                intent.putExtra(AddGradeActivity.EXTRA_DATE_GRADE, grades.getDate());
//                databaseViewModel.getAllSubjects().observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
//                    @Override
//                    public void onChanged(List<Subject> subjects) {
//                        for (Subject subject : subjects) {
//                            if (subject.getId() == grades.getSubjectId()){
//                                intent.putExtra(AddGradeActivity.EXTRA_SUBJECT_NAME, subject.getName());
//                            }
//                        }
//                    }
//                });
//                startActivityForResult(intent, EDIT_GRADE_REQUEST);
//            }
//        });

//        recyclerView.setAdapter(gradeAdapter);
        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ADD_GRADE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
//                Toast.makeText(getContext(), "Something wrong happened", Toast.LENGTH_SHORT).show();
            } else {
                final int grade = data.getIntExtra(AddGradeActivity.EXTRA_GRADE, -1);
                final String subjectName = data.getStringExtra(AddGradeActivity.EXTRA_SUBJECT_NAME);
                final String date = data.getStringExtra(AddGradeActivity.EXTRA_DATE_GRADE);
                final String term = data.getStringExtra(AddGradeActivity.EXTRA_TERM_GRADE);
                final String type = data.getStringExtra(AddGradeActivity.EXTRA_FORM_GRADE);
                final String note = data.getStringExtra(AddGradeActivity.EXTRA_NOTE_GRADE);

                LiveData<List<Subject>> subjectInfo = databaseViewModel.getSubjectWithName(subjectName);
                subjectInfo.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                    @Override
                    public void onChanged(List<Subject> subjects) {
                        databaseViewModel.insert(new Grades(subjects.get(0).getId(), subjectName, grade,
                                date, term, type, note));
//                        FragmentTransaction tr = getFragmentManager().beginTransaction();
//                        tr.replace(R.id.fragment_container, new Fragment_Grades(toolbar, floatingActionButton));
//                        tr.commit();
                    }
                });



            }
        } else if (requestCode == EDIT_GRADE_REQUEST && resultCode == RESULT_OK) {
            assert data != null;
            final int id = data.getIntExtra(AddGradeActivity.EXTRA_GRADE_ID, -1);
            if (id == -1) {
//                Toast.makeText(getContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                return;
            }
            final int grade = data.getIntExtra(AddGradeActivity.EXTRA_GRADE, -1);
            final String subjectName = data.getStringExtra(AddGradeActivity.EXTRA_SUBJECT_NAME);
            final String date = data.getStringExtra(AddGradeActivity.EXTRA_DATE_GRADE);
            final String term = data.getStringExtra(AddGradeActivity.EXTRA_TERM_GRADE);
            final String type = data.getStringExtra(AddGradeActivity.EXTRA_FORM_GRADE);
            final String note = data.getStringExtra(AddGradeActivity.EXTRA_NOTE_GRADE);

            LiveData<List<Subject>> subjectInfo = databaseViewModel.getSubjectWithName(subjectName);
            subjectInfo.observe(getViewLifecycleOwner(), new Observer<List<Subject>>() {
                @Override
                public void onChanged(List<Subject> subjects) {
                    Grades grades = new Grades(subjects.get(0).getId(), subjectName, grade,
                            date, term, type, note);
                    grades.setId(id);
                    databaseViewModel.update(grades);
                }
            });
//            Toast.makeText(getContext(), "Grade Updated", Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(getContext(), "Grade not added", Toast.LENGTH_SHORT).show();
        }
    }
}
