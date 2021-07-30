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

public class Fragment_Agenda extends Fragment {
    private Toolbar toolbar;

    public Fragment_Agenda(final Toolbar toolbar){
        this.toolbar = toolbar;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        toolbar.setTitle("Agenda");
        return inflater.inflate(R.layout.fragment_agenda, container, false);

    }
}
