package com.example.studentplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Exams;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends RecyclerView.Adapter<ExamAdapter.ExamHolder> {
    private List<Exams> list = new ArrayList<>();

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_item, parent, false);
        return new ExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        Exams exams = list.get(position);
        holder.name.setText(exams.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setExams(List<Exams> exams){
        this.list = exams;
        notifyDataSetChanged();
    }

    public Exams getExamAtPosition(int position){
        return list.get(position);
    }

    class ExamHolder extends RecyclerView.ViewHolder{
         private TextView name;
        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewExamItem);
        }
    }
}
