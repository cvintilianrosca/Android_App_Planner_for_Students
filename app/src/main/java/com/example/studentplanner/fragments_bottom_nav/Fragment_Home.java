package com.example.studentplanner.fragments_bottom_nav;
import android.content.Intent;
import android.os.Bundle;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.MainActivity;
import com.example.studentplanner.Model;
import com.example.studentplanner.ModelAdapter;
import com.example.studentplanner.R;
import com.example.studentplanner.adapters.TaskAdapter;
import com.example.studentplanner.addentities.AddTaskActivity;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.fragment_slide.Fragment_Exams;
import com.example.studentplanner.fragment_slide.Fragment_Grades;
import com.example.studentplanner.fragment_slide.Fragment_Tasks;
import com.example.studentplanner.fragment_slide.Fragment_Teachers;
import com.example.studentplanner.fragment_slide.Fragment_Timetable;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class Fragment_Home extends Fragment {
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;
    private RecyclerView recyclerView;
    private ArrayList<Model> arrayListModel;


    public Fragment_Home(final Toolbar toolbar,FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       toolbar.setTitle("Home");
        final View v = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = v.findViewById(R.id.id_recycleViewMain);
        arrayListModel = new ArrayList<>();
        arrayListModel.add(new Model("Tasks"));
        arrayListModel.add(new Model("Exams"));
        arrayListModel.add(new Model("Grades"));
        arrayListModel.add(new Model("Timetable"));

        arrayListModel.add(new Model("Teachers"));


        floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              toolbar.setTitle("Home1");
          }
      });

        LinearLayoutManager layoutManager = new LinearLayoutManager(
                getContext(), LinearLayoutManager.HORIZONTAL, false
        );
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        ModelAdapter modelAdapter = new ModelAdapter( arrayListModel, getContext());
        recyclerView.setAdapter(modelAdapter);
        modelAdapter.setOnItemClickListener(new ModelAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(final Model model) {
                Fragment selectedFragment = null;
                switch (model.titleFragment){
                    case "Tasks":
                        selectedFragment = new Fragment_Tasks(toolbar, floatingActionButton);
                        break;
                    case "Exams":
                        selectedFragment = new Fragment_Exams(toolbar, floatingActionButton);
                        break;
                    case "Teachers":
                        selectedFragment = new Fragment_Teachers(toolbar, floatingActionButton);
                        break;
                    case "Timetable":
                        selectedFragment = new Fragment_Timetable(toolbar, floatingActionButton);
                        break;
                    case "Grades":
                        selectedFragment = new Fragment_Grades(toolbar, floatingActionButton);
                        break;
                }
                if (selectedFragment != null){
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .replace(R.id.fragment_container, selectedFragment, "findThisFragment")
                            .addToBackStack(null)
                            .commit();
                }
            }
        });
         return v;
    }
}
