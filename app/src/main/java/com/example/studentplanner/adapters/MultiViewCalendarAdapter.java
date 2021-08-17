package com.example.studentplanner.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.entities.Tasks;

import java.util.List;

public class MultiViewCalendarAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {


    private final int task = 1;
    private final int exam = 2;

    List<Object> objectList;
    Context context;
    private MultiViewCalendarAdapter.OnItemClickListener listener;

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View modelView;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == task){
            modelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            viewHolder = new TaskViewHolder(modelView);
        } else if (viewType == exam){
            modelView = LayoutInflater.from(parent.getContext()).inflate(R.layout.exam_item, parent, false);
            viewHolder = new ExamViewHolder(modelView);
        }
        return viewHolder;
    }


    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() == task){
            Tasks tasks = (Tasks) objectList.get(position);
            TaskViewHolder taskViewHolder = (TaskViewHolder) holder;
            taskViewHolder.taskViewName.setText(tasks.getTitle());
        } else if (holder.getItemViewType() == exam){
            Exams exams = (Exams) objectList.get(position);
            ExamViewHolder examViewHolder = (ExamViewHolder) holder;
            examViewHolder.examViewName.setText(exams.getName());
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (objectList.get(position) instanceof Exams) {
            return exam;
        } else if (objectList.get(position) instanceof Tasks){
            return task;
        } else {
            return -1;
        }
    }


    @Override
    public int getItemCount() {
        return objectList.size();
    }

    public class TaskViewHolder extends RecyclerView.ViewHolder {

        TextView taskViewName;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskViewName = itemView.findViewById(R.id.textViewTaskItem);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(objectList.get(position));
                    }
                }
            });
        }
    }


    public void setObjectList(List<Object> objectList) {
        this.objectList = objectList;
    }

    public class ExamViewHolder extends RecyclerView.ViewHolder {

        TextView examViewName;

        public ExamViewHolder(@NonNull View itemView) {
            super(itemView);
            examViewName = itemView.findViewById(R.id.textViewExamItem);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(objectList.get(position));
                    }
                }
            });
        }
    }

    public interface OnItemClickListener{
        void onItemClick(Object object);
    }

    public  void setOnItemClickListener(MultiViewCalendarAdapter.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }

}
