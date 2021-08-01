package com.example.studentplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Teachers;

import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends RecyclerView.Adapter<GradeAdapter.GradesHolder> {
    private List<Grades> list = new ArrayList<>();

    @NonNull
    @Override
    public GradesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_item, parent, false);

        return new GradesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesHolder holder, int position) {
        Grades grades = list.get(position);
        holder.name.setText(String.valueOf(grades.getValue()));
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setGrades(List<Grades> grades){
        this.list = grades;
        notifyDataSetChanged();
    }

    class GradesHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public GradesHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewGradeSubject);
        }
    }
}
