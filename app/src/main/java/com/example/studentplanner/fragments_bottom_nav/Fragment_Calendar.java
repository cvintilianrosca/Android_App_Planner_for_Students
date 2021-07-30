package com.example.studentplanner.fragments_bottom_nav;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.example.studentplanner.R;

public class Fragment_Calendar extends Fragment {
    private Toolbar toolbar;
    public Fragment_Calendar(final Toolbar toolbar){
        this.toolbar= toolbar;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       toolbar.setTitle("Calendar");
        return inflater.inflate(R.layout.fragment_calendar, container, false);
    }
}
