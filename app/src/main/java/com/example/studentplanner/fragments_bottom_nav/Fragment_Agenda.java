package com.example.studentplanner.fragments_bottom_nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
import com.example.studentplanner.adapters.TaskOrExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Tasks;

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

    public Fragment_Agenda(final Toolbar toolbar){
        this.toolbar = toolbar;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        toolbar.setTitle("Agenda");
        View v = inflater.inflate(R.layout.fragment_agenda, container, false);
        recyclerView = v.findViewById(R.id.recycleViewFragmentAgenda);
        recyclerView.setLayoutManager(new LinearLayoutManager(this.getContext()));
        recyclerView.setHasFixedSize(true);

        taskOrExamAdapter = new TaskOrExamAdapter();

        databaseViewModel = ViewModelProviders.of(this).get(DatabaseViewModel.class);
        final List<TaskOrExam> taskOrExamAdapterList = new ArrayList<>();
        final String currentDate = new SimpleDateFormat("d-M-yyyy", Locale.getDefault()).format(new Date());

        databaseViewModel.getAllTasks().observe(getViewLifecycleOwner(), new Observer<List<Tasks>>() {
            @Override
            public void onChanged(List<Tasks> tasks) {
                for (Tasks it: tasks) {
                    if (it.getDateDeadline().compareTo(currentDate) == 0){
                        taskOrExamAdapterList.add(new TaskOrExam(it.getTitle(), it.getDetails(), 1));
                    }
                }
                taskOrExamAdapter.setTaskOrExams(taskOrExamAdapterList);
                recyclerView.setAdapter(taskOrExamAdapter);
//                Toast.makeText(getContext(), String.valueOf(taskOrExamAdapterList.size()), Toast.LENGTH_SHORT).show();
            }
        });
//        Toast.makeText(getContext(), String.valueOf(taskOrExamAdapterList.size()), Toast.LENGTH_SHORT).show();

        databaseViewModel.getAllExams().observe(getViewLifecycleOwner(), new Observer<List<Exams>>() {
            @Override
            public void onChanged(List<Exams> exams) {
                for (Exams it : exams) {
                    if (it.getDate().compareTo(currentDate) == 0){
//                        Toast.makeText(getContext(), it.getDate(), Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), currentDate, Toast.LENGTH_SHORT).show();
//                        Toast.makeText(getContext(), String.valueOf(taskOrExamAdapterList.size()), Toast.LENGTH_SHORT).show();
                        taskOrExamAdapterList.add(new TaskOrExam(it.getName(), it.getDetails(), 2));

                    }
                }
                taskOrExamAdapter.setTaskOrExams(taskOrExamAdapterList);
                recyclerView.setAdapter(taskOrExamAdapter);
            }
        });


        taskOrExamAdapter.setTaskOrExams(taskOrExamAdapterList);
        recyclerView.setAdapter(taskOrExamAdapter);
       return v;
    }
}
