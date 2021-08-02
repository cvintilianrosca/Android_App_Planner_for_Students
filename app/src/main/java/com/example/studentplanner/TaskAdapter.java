package com.example.studentplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.database.entities.Tasks;
import com.example.studentplanner.database.entities.Teachers;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskHolder> {
    private List<Tasks> list = new ArrayList<>();

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Tasks tasks = list.get(position);
        holder.name.setText(tasks.getTitle());
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setTasks(List<Tasks> tasks){
        this.list = tasks;
        notifyDataSetChanged();
    }

    class TaskHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewTaskItem);
        }
    }
}
