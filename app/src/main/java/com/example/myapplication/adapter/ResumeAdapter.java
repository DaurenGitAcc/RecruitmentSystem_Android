package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.model.Resume;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class ResumeAdapter extends RecyclerView.Adapter<ResumeAdapter.ResumeViewHolder> {


    public interface OnStateClickListener{
        void onStateClick(Resume resume, int position);
    }

    private final OnStateClickListener onClickListener;

    Context context;
    List<Resume> resumes;

    private ArrayList<Resume> arraylist;
    int type;

    public ResumeAdapter(Context context, List<Resume> resumes,int type, OnStateClickListener onClickListener) {
        this.context = context;
        this.resumes = resumes;
        this.onClickListener = onClickListener;
        this.arraylist = new ArrayList<Resume>();
        this.arraylist.addAll(resumes);
        this.type = type;
    }

    @NonNull
    @Override
    public ResumeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View resumeItem = LayoutInflater.from(context).inflate(R.layout.resume_item, parent, false);

        return new ResumeViewHolder(resumeItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ResumeViewHolder holder, int position) {
        holder.applicationName.setText(resumes.get(position).getApplicant().getSurname()+" "+resumes.get(position).getApplicant().getName());
//        if(resumes.get(position).getExperience()==null || resumes.get(position).getExperience()==""){
//            holder.resumeExperience.setText("Нет опыта");
//        }
//        else{
//           // holder.resumeExperience.setText(resumes.get(position).getExperience());
//        }
//
//        //holder.resumeEducation.setText(resumes.get(position).getEducation());

        holder.divider.setVisibility(View.GONE);
        switch (type){
            case 0:
                break;
            case 1:
//                holder.organizationName.setVisibility(View.GONE);
//                holder.salary.setVisibility(View.GONE);
//                holder.descriptionPosition.setVisibility(View.GONE);
//                holder.LinkText.setText("Подробнее");
//                holder.divider.setVisibility(View.VISIBLE);
//                holder.positionName.setTextSize(12);
                break;
            case 2:

             //   holder.descriptionPosition.setVisibility(View.GONE);
            //    holder.LinkText.setText("Подробнее");
                holder.divider.setVisibility(View.VISIBLE);
               // holder.resumeEducation.setVisibility(View.GONE);
               // holder.resumeExperience.setVisibility(View.GONE);
                break;
        }

        holder.responseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(resumes.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return resumes.size();
    }

    public static final class ResumeViewHolder extends RecyclerView.ViewHolder {
        TextView applicationName;
        TextView resumeExperience;
        TextView resumeEducation;
        LinearLayout responseButton;
        View divider;

        public ResumeViewHolder(@NonNull View itemView) {
            super(itemView);

            applicationName=itemView.findViewById(R.id.applicant_name);
           // resumeExperience=itemView.findViewById(R.id.resume_experience);
           // resumeEducation=itemView.findViewById(R.id.resume_education);

            responseButton=itemView.findViewById(R.id.responseButton);
            divider=itemView.findViewById(R.id.divider2);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        resumes.clear();
        if (charText.length() == 0) {
            resumes.addAll(arraylist);
        } else {
            for (Resume wp : arraylist) {
                if (wp.getApplicant().getName().toLowerCase(Locale.getDefault()).contains(charText)) {
                    resumes.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
