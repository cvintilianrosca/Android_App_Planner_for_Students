package com.example.studentplanner.fragment_slide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.studentplanner.R;

public class Fragment_Tasks extends Fragment {
    private Toolbar toolbar;
    public Fragment_Tasks(final Toolbar toolbar){
        this.toolbar = toolbar;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Tasks");
        return inflater.inflate(R.layout.fragment_tasks, container, false);
    }
}
