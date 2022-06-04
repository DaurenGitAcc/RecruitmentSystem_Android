package com.example.myapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.SearchVacancyActivity;
import com.example.myapplication.model.Vacancy;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class VacancyAdapter extends RecyclerView.Adapter<VacancyAdapter.VacancyViewHolder> {


    public interface OnStateClickListener{
        void onStateClick(Vacancy vacancy, int position);
    }

    private final OnStateClickListener onClickListener;

    Context context;
    List<Vacancy> vacancies;

    private ArrayList<Vacancy> arraylist;
    boolean type;

    public VacancyAdapter(Context context, List<Vacancy> vacancies,boolean type, OnStateClickListener onClickListener) {
        this.context = context;
        this.vacancies = vacancies;
        this.onClickListener = onClickListener;
        this.arraylist = new ArrayList<Vacancy>();
        this.arraylist.addAll(vacancies);
        this.type = type;
    }

    @NonNull
    @Override
    public VacancyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View vacancyItem = LayoutInflater.from(context).inflate(R.layout.vacancy_item, parent, false);

        return new VacancyViewHolder(vacancyItem);
    }

    @Override
    public void onBindViewHolder(@NonNull VacancyViewHolder holder, int position) {
        holder.positionName.setText(vacancies.get(position).getPosition());
        holder.organizationName.setText("TOO ENGINEERING");
        holder.salary.setText(vacancies.get(position).getSalary());
        holder.descriptionPosition.setText(vacancies.get(position).getPositionDescription());
        holder.divider.setVisibility(View.GONE);
        if(type){
            holder.organizationName.setVisibility(View.GONE);
            holder.salary.setVisibility(View.GONE);
            holder.descriptionPosition.setVisibility(View.GONE);
            holder.LinkText.setText("Подробнее");
            holder.divider.setVisibility(View.VISIBLE);
            holder.positionName.setTextSize(12);
        }

        holder.responseButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v)
            {
                onClickListener.onStateClick(vacancies.get(position),position);
            }
        });

    }

    @Override
    public int getItemCount() {
        return vacancies.size();
    }

    public static final class VacancyViewHolder extends RecyclerView.ViewHolder {
        TextView positionName;
        TextView organizationName;
        TextView salary;
        TextView descriptionPosition;
        TextView LinkText;
        LinearLayout responseButton;
        View divider;

        public VacancyViewHolder(@NonNull View itemView) {
            super(itemView);

            positionName=itemView.findViewById(R.id.positionName);
            organizationName=itemView.findViewById(R.id.organizationName);
            salary=itemView.findViewById(R.id.salary);
            descriptionPosition=itemView.findViewById(R.id.descriptionPosition);
            responseButton=itemView.findViewById(R.id.responseButton);
            LinkText=itemView.findViewById(R.id.buttonText);
            divider=itemView.findViewById(R.id.divider2);

        }
    }

    public void filter(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        vacancies.clear();
        if (charText.length() == 0) {
            vacancies.addAll(arraylist);
        } else {
            for (Vacancy wp : arraylist) {
                if (wp.getPosition().toLowerCase(Locale.getDefault()).contains(charText)) {
                    vacancies.add(wp);
                }
            }
        }
        notifyDataSetChanged();
    }

}
