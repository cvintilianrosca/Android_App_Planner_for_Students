package com.example.studentplanner.adapters;

import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;
import com.example.studentplanner.R;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.relations.SubjectWithGrades;

import java.util.ArrayList;
import java.util.List;

public class SubjectAdapter extends ListAdapter<Subject, SubjectAdapter.SubjectHolder> {
    private OnItemClickListener listener;

    public SubjectAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Subject> DIFF_CALLBACK = new DiffUtil.ItemCallback<Subject>() {
        @Override
        public boolean areItemsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getId()== newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Subject oldItem, @NonNull Subject newItem) {
            return oldItem.getId() == newItem.getId() && oldItem.getName().equals(newItem.getName())
                    && oldItem.getRoom().equals(newItem.getRoom())
                    && oldItem.getAdditional_info().equals(newItem.getAdditional_info());
        }
    };

    @NonNull
    @Override
    public SubjectHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.subject_item, parent, false);

        return new SubjectHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onBindViewHolder(@NonNull SubjectHolder holder, int position) {
        Subject subject = getItem(position);
        holder.name.setText(subject.getName());
    }


    class SubjectHolder extends RecyclerView.ViewHolder{
         private TextView name;
         private TextView avg;
         private ProgressBar progressBar;

        public SubjectHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewSubjectName);
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
        void onItemClick(Subject subject);
    }

    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }

    public Subject getSubjectAtPosition(int position){
        return getItem(position);
    }

}

