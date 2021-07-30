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
import com.example.studentplanner.SubjectAdapter;
import com.example.studentplanner.database.entities.Subject;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class Fragment_Home extends Fragment {
    private Toolbar toolbar;
    FloatingActionButton floatingActionButton;

    public Fragment_Home(final Toolbar toolbar,FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       toolbar.setTitle("Home");
        View v = inflater.inflate(R.layout.fragment_home, container, false);

      floatingActionButton.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              toolbar.setTitle("Home1");
          }
      });
         return v;
    }
}
