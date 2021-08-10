package com.example.studentplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Tasks;

import java.util.ArrayList;
import java.util.List;

public class TaskAdapter extends ListAdapter<Tasks, TaskAdapter.TaskHolder> {
    private TaskAdapter.OnItemClickListener listener;

    public TaskAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Tasks> DIFF_CALLBACK = new DiffUtil.ItemCallback<Tasks>() {
        @Override
        public boolean areItemsTheSame(@NonNull Tasks oldItem, @NonNull Tasks newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Tasks oldItem, @NonNull Tasks newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getSubjectId() == newItem.getSubjectId() &&
                    oldItem.getDateDeadline().equals(newItem.getDateDeadline()) &&
                    oldItem.getDetails().equals(newItem.getDetails());
        }
    };

    @NonNull
    @Override
    public TaskHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.task_item, parent, false);
        return new TaskHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskHolder holder, int position) {
        Tasks tasks = getItem(position);
        holder.name.setText(tasks.getTitle());
    }


    public Tasks getTaskAtPosition(int position){
        return getItem(position);
    }



    class TaskHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public TaskHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewTaskItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(getItem(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Tasks tasks);
    }

    public  void setOnItemClickListener(TaskAdapter.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}
