package com.example.studentplanner.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
    private List<Subject> list = new ArrayList<>();
    private OnItemClickListener listener;
    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_item, parent, false);

        return new SubjectHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        Subject subject = list.get(position);
        holder.name.setText(subject.getName());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public Subject getSubjectAtPosition(int position){
        return list.get(position);
    }
    public void setSubjects(List<Subject> subjects){
        this.list = subjects;
        notifyDataSetChanged();
    }

    class SubjectHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewSubjectName);
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
        void onItemClick(Subject subject);
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}

