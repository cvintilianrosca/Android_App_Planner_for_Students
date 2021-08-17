package com.example.studentplanner.fragments_bottom_nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.DatabaseViewModel;
import com.example.studentplanner.R;
import com.example.studentplanner.TaskOrExam;
import com.example.studentplanner.adapters.MultiViewCalendarAdapter;
import com.example.studentplanner.adapters.TaskOrExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Tasks;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Fragment_Agenda extends Fragment {
    private Toolbar toolbar;
    private DatabaseViewModel databaseViewModel;
    private RecyclerView recyclerView;
    private TaskOrExamAdapter taskOrExamAdapter;
    private FloatingActionButton floatingActionButton;
    private MultiViewCalendarAdapter multiViewCalendarAdapter;


    public Fragment_Agenda(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar = toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        toolbar.setTitle("Agenda");
        View v = inflater.inflate(R.layout.fragment_agenda, container, false);
        recyclerView = v.findViewById(R.id.recycleViewFragmentAgenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        taskOrExamAdapter = new TaskOrExamAdapter();
        multiViewCalendarAdapter = new MultiViewCalendarAdapter();
        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        final String currentDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());

        databaseViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                final List<Object> objectList = new ArrayList<>();
                for (Tasks it: tasks) {

                    if (it.getDateDeadline().compareTo(currentDate) == 0){
                        objectList.add(it);
                    }
                }
                databaseViewModel.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exams>>() {
                    @Override
                    public void onChanged(List<Exams> exams) {
                        for (Exams ex : exams){
                            if (ex.getDate().compareTo(currentDate) == 0){
                                objectList.add(ex);
                            }
                        }
                        multiViewCalendarAdapter.setObjectList(objectList);
                        recyclerView.setAdapter(multiViewCalendarAdapter);
                    }
                });
            }
        });
        multiViewCalendarAdapter.setOnItemClickListener(new MultiViewCalendarAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(Object object) {
//                Toast.makeText(getContext(), "IOOO", Toast.LENGTH_SHORT).show();
                if (object instanceof Exams){
                    Toast.makeText(getContext(), "Exams", Toast.LENGTH_SHORT).show();
                }
                if (object instanceof Tasks){
                    Toast.makeText(getContext(), "Tasks", Toast.LENGTH_SHORT).show();
                }
            }
        });
       return v;
    }
}
