package com.example.studentplanner.fragment_slide;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.studentplanner.R;
import com.example.studentplanner.adapters.ExamAdapter;
import com.example.studentplanner.database.entities.Exams;
import com.example.studentplanner.database.entities.Grades;
import com.example.studentplanner.database.entities.Subject;
import com.example.studentplanner.database.relations.SubjectWithGrades;
import com.example.studentplanner.database.relations.SubjectWithGrades2;

import java.util.ArrayList;

public class GroupAdp extends RecyclerView.Adapter<GroupAdp.ViewHolder> {

    private Activity activity;
    private ArrayList<SubjectWithGrades> arrayListGroup = new ArrayList<>();
    private MemberAdp memberAdp;
    private GroupAdp.OnItemClickListener listener;

    public MemberAdp getMemberAdp() {
        return memberAdp;
    }

    public void destroy(){
//        arrayListGroup.clear();
        notifyDataSetChanged();
    }
    public void setArrayListGroup(ArrayList<SubjectWithGrades> arrayListGroup) {
//        arrayListGroup.clear();
        this.arrayListGroup = arrayListGroup;
        notifyDataSetChanged();
    }

    public GroupAdp(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_row_group ,parent, false);
        return new GroupAdp.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SubjectWithGrades subjectWithGrades = arrayListGroup.get(position);
        holder.tvName.setText(subjectWithGrades.subject.getName());
        memberAdp = new MemberAdp(subjectWithGrades.listGrades);
        LinearLayoutManager layoutManager = new LinearLayoutManager(
                activity, LinearLayoutManager.HORIZONTAL, false
        );
        holder.recyclerView.setLayoutManager(layoutManager);
        holder.recyclerView.setItemAnimator(new DefaultItemAnimator());
        holder.recyclerView.setAdapter(memberAdp);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return arrayListGroup.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvName;
        RecyclerView recyclerView;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            recyclerView = itemView.findViewById(R.id.rv_member);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION){
                        listener.onItemClick(arrayListGroup.get(position));
                    }
                }
            });

        }
    }

    public interface OnItemClickListener{
        void onItemClick(SubjectWithGrades subjectWithGrades);
    }

    public  void setOnItemClickListener(GroupAdp.OnItemClickListener onItemClickListener){
        this.listener = onItemClickListener;
    }

}
