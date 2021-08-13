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
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class Fragment_Settings extends Fragment {
    private Toolbar toolbar;
    private FloatingActionButton floatingActionButton;
    public Fragment_Settings(final Toolbar toolbar, final FloatingActionButton floatingActionButton){
        this.toolbar= toolbar;
        this.floatingActionButton = floatingActionButton;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Settings");
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        return inflater.inflate(R.layout.fragment_settings, container, false);
    }
}
