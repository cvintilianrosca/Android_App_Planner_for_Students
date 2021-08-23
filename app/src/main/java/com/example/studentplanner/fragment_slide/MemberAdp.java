package com.example.studentplanner.fragment_slide;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.adapters.ExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;

import java.util.ArrayList;
import java.util.List;

public class MemberAdp extends RecyclerView.Adapter<MemberAdp.ViewHolder> {

    private MemberAdp.OnItemClickListener listener;
     List<Grades> arrayList = new ArrayList<>();
    public MemberAdp(List<Grades> arrayList){
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_member, parent, false);
        return new MemberAdp.ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Grades grades = arrayList.get(position);
        String[] vector = grades.getDate().split("-");
        if (vector.length == 3){
            switch (Integer.parseInt(vector[1])){
                case 1:
                    holder.textViewDate.setText("Jan. " + vector[0]);
                    break;
                case 2:
                    holder.textViewDate.setText("Feb. " + vector[0]);
                    break;
                case 3:
                    holder.textViewDate.setText("Mar. " + vector[0]);
                    break;
                case 4:
                    holder.textViewDate.setText("Apr. " + vector[0]);
                    break;
                case 5:
                    holder.textViewDate.setText("May " + vector[0]);
                    break;
                case 6:
                    holder.textViewDate.setText("Jun. " + vector[0]);
                    break;
                case 7:
                    holder.textViewDate.setText("Jul. " + vector[0]);
                    break;
                case 8:
                    holder.textViewDate.setText("Aug. " + vector[0]);
                    break;
                case 9:
                    holder.textViewDate.setText("Sep. " + vector[0]);
                    break;
                case 10:
                    holder.textViewDate.setText("Oct. " + vector[0]);
                    break;
                case 11:
                    holder.textViewDate.setText("Nov. " + vector[0]);
                    break;
                case 12:
                    holder.textViewDate.setText("Dec. " + vector[0]);
                    break;
            }
        }

        holder.textViewName.setText(String.valueOf(grades.getValue()));
    }

    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textViewName;
        private TextView textViewDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.tv_name);
            textViewDate = itemView.findViewById(R.id.textViewDateGrade);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(arrayList.get(position));
                    }
                }
            });
        }
    }


    public interface OnItemClickListener{
        void onItemClick(Grades grades);
    }

    public  void setOnItemClickListener(MemberAdp.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }

}
