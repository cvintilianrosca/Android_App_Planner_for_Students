package com.example.studentplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Teachers;

import java.util.ArrayList;
import java.util.List;

public class TaskOrExamAdapter extends RecyclerView.Adapter<TaskOrExamAdapter.TaskOrExamHolder> {
    private List<TaskOrExam> list = new ArrayList<>();

    @NonNull
    @Override
    public TaskOrExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_or_exam_item, parent, false);
        return new TaskOrExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskOrExamHolder holder, int position) {
        TaskOrExam taskOrExam = list.get(position);
        holder.name.setText(taskOrExam.getTitle());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setTaskOrExams(List<TaskOrExam> taskOrExams){
        this.list = taskOrExams;
        notifyDataSetChanged();
    }

    public void deleteAll(){
        list.clear();
        notifyDataSetChanged();
    }

    class TaskOrExamHolder extends RecyclerView.ViewHolder{
        private TextView name;

        public TaskOrExamHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewTaskExamItem);
        }
    }
}
