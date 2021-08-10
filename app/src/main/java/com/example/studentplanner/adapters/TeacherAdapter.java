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
import com.example.studentplanner.database.entities.Teachers;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends ListAdapter<Teachers, TeacherAdapter.TeacherHolder> {
    private TeacherAdapter.OnItemClickListener listener;

    public TeacherAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Teachers> DIFF_CALLBACK = new DiffUtil.ItemCallback<Teachers>() {
        @Override
        public boolean areItemsTheSame(@NonNull Teachers oldItem, @NonNull Teachers newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Teachers oldItem, @NonNull Teachers newItem) {
            return oldItem.getName().equals(newItem.getName()) &&
                    oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
        }
    };
    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);

        return new TeacherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        Teachers teachers = getItem(position);
        holder.name.setText(teachers.getName());
    }



    public Teachers getTeacherAtPosition(int position){
        return getItem(position);
    }

    class TeacherHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public TeacherHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewTeacherItem);
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
        void onItemClick(Teachers teachers);
    }

    public  void setOnItemClickListener(TeacherAdapter.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}
