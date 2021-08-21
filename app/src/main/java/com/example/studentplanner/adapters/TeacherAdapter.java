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

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherHolder> {
    private TeacherAdapter.OnItemClickListener listener;
           List<Teachers> teachersArrayList = new ArrayList<>();
//    public TeacherAdapter() {
//        super(DIFF_CALLBACK);
//    }

//    private static final DiffUtil.ItemCallback<Teachers> DIFF_CALLBACK = new DiffUtil.ItemCallback<Teachers>() {
//        @Override
//        public boolean areItemsTheSame(@NonNull Teachers oldItem, @NonNull Teachers newItem) {
//            return oldItem.getId() == newItem.getId();
//        }
//
//        @Override
//        public boolean areContentsTheSame(@NonNull Teachers oldItem, @NonNull Teachers newItem) {
//            return oldItem.getName().equals(newItem.getName()) &&
//                    oldItem.getPhoneNumber().equals(newItem.getPhoneNumber());
//        }
//    };

    @NonNull
    @Override
    public TeacherHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.teacher_item, parent, false);
        return new TeacherHolder(view);
    }



    @Override
    public int getItemCount() {
        return teachersArrayList.size();
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherHolder holder, int position) {
        Teachers teachers = teachersArrayList.get(position);
        holder.name.setText(teachers.getName() + " " + teachers.getSurname());
        String s = teachers.getName();
        if (s.length() >= 2 && teachers.getFlagFirst() == 101){
            s = s.toUpperCase();
            holder.letter.setText(String.valueOf(s.charAt(0)));
        } else {
            holder.letter.setText("  ");
        }

    }

    public Teachers getTeacherAtPosition(int position){
        return teachersArrayList.get(position);
    }

    public void setTeachersArrayList(List<Teachers> teachersArrayList){
        this.teachersArrayList = teachersArrayList;
        notifyDataSetChanged();
    }
    class TeacherHolder extends RecyclerView.ViewHolder{
         private TextView name;
         private TextView letter;
        public TeacherHolder(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.textViewTeacherItem);
            letter = itemView.findViewById(R.id.textViewLetter);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(teachersArrayList.get(position));
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
