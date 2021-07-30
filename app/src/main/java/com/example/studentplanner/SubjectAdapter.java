package com.example.studentplanner;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.database.entities.Subject;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends RecyclerView.Adapter<SubjectAdapter.SubjectHolder> {
    private List<Subject> list = new ArrayList<>();

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

    public void setSubjects(List<Subject> subjects){
        this.list = subjects;
        notifyDataSetChanged();
    }

    class SubjectHolder extends RecyclerView.ViewHolder{
         private TextView name;

        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewSubjectName);
        }
    }
}
