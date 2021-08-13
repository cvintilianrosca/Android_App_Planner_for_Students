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
import com.example.studentplanner.database.entities.Tasks;

import java.util.ArrayList;
import java.util.List;

public class ExamAdapter extends ListAdapter<Exams, ExamAdapter.ExamHolder> {
    private ExamAdapter.OnItemClickListener listener;

    public ExamAdapter() {
        super(DIFF_CALLBACK);
    }

    private static final DiffUtil.ItemCallback<Exams> DIFF_CALLBACK = new DiffUtil.ItemCallback<Exams>() {
        @Override
        public boolean areItemsTheSame(@NonNull Exams oldItem, @NonNull Exams newItem) {
            return oldItem.getId() == newItem.getId();
        }

        @Override
        public boolean areContentsTheSame(@NonNull Exams oldItem, @NonNull Exams newItem) {
            return  oldItem.getDate().equals(newItem.getDate())&&
                    oldItem.getDetails().equals(newItem.getDetails()) && oldItem.getFormExam() == newItem.getFormExam()
                    && oldItem.getName().equals(newItem.getName());
        }
    };

    @NonNull
    @Override
    public ExamHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.exam_item, parent, false);
        return new ExamHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ExamHolder holder, int position) {
        Exams exams = getItem(position);
        holder.name.setText(exams.getName());
    }




    public Exams getExamAtPosition(int position){
        return getItem(position);
    }

    class ExamHolder extends RecyclerView.ViewHolder{
         private TextView name;
        public ExamHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewExamItem);

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
        void onItemClick(Exams exams);
    }

    public  void setOnItemClickListener(ExamAdapter.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }
}
