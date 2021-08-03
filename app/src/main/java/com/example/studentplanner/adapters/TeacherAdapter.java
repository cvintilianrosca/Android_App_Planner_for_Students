package com.example.studentplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Teachers;

import java.util.ArrayList;
import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherHolder> {
    private List<Teachers> list = new ArrayList<>();
    private TeacherAdapter.OnItemClickListener listener;
    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);

        return new TeacherHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        Teachers teachers = list.get(position);
        holder.name.setText(teachers.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void setTeachers(List<Teachers> teachers){
        this.list = teachers;
        notifyDataSetChanged();
    }

    public Teachers getTeacherAtPosition(int position){
        return list.get(position);
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
                        listener.onItemClick(list.get(position));
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
