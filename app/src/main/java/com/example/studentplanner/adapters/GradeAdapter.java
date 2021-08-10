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
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;

import java.util.ArrayList;
import java.util.List;

public class GradeAdapter extends ListAdapter<Grades, GradeAdapter.GradesHolder> {
    private GradeAdapter.OnItemClickListener listener;

    public GradeAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Grades> DIFF_CALLBACK = new DiffUtil.ItemCallback<Grades>() {
        @Override
        public boolean areItemsTheSame(@NonNull Grades oldItem, @NonNull Grades newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Grades oldItem, @NonNull Grades newItem) {
            return oldItem.getSubjectId() == newItem.getSubjectId() && oldItem.getId() == newItem.getId() &&
                    oldItem.getValue() == newItem.getValue();
        }
    };

    @NonNull
    @Override
    public GradesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.grade_item, parent, false);

        return new GradesHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GradesHolder holder, int position) {
        Grades grades = getItem(position);
        holder.name.setText(String.valueOf(grades.getValue()));
    }




    public Grades getGradeAtPosition(int position){
        return getItem(position);
    }

    class GradesHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public GradesHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewGradeSubject);
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
        void onItemClick(Grades grades);
    }

    public  void setOnItemClickListener(GradeAdapter.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}
