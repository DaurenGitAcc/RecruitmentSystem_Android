package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.myapplication.adapter.VacancyAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.Response_r;
import com.example.myapplication.model.Response_v;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

import java.util.ArrayList;
import java.util.List;

public class ResponseActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    RecyclerView vacancyRecycler;
    VacancyAdapter vacancyAdapter;

    TextView textHolder;

    private DatabaseAdapter adapter;

    User user;
    Applicant applicant;
    Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_response);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        adapter.open();

        user = adapter.getUserByEmail(email);
        if(user.getRole().getId()==1){
            applicant = adapter.getApplicantByUserId(user.getId());
        }else{
            company = adapter.getCompanyByUserId(user.getId());
        }

        //Response_r response_r=adapter.getAllRResponse(adapter.getResumeByUserId(applicant.getId()));

        adapter.close();
    }

    @Override
    protected void onResume() {
        super.onResume();
        textHolder = findViewById(R.id.textView30);

        adapter.open();
        if(user.getRole().getId()==1){
            Response_r response_r=adapter.getAllRResponse(adapter.getResumeByUserId(applicant.getId()));
            setVacancyRecycler(response_r.getVacancies());
        }
        else{
            textHolder.setText("Опубликованные вами вакансии:");
            textHolder.setTextSize(18);
            List<Vacancy> vacancies = adapter.getVacancies(company.getId());

            setVacancyRecycler(vacancies);
        }

        adapter.close();

    }

    private void setVacancyRecycler(List<Vacancy> vacancies) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        vacancyRecycler = findViewById(R.id.resumeRecycler);
        vacancyRecycler.setLayoutManager(layoutManager);

        VacancyAdapter.OnStateClickListener vacancyClickListener = new VacancyAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Vacancy vacancy, int position) {
                Intent intent = new Intent(ResponseActivity.this, VacancyDescriptionActivity.class);
                intent.putExtra("vacancyId", vacancy.getId());
                if(user.getRole().getId()==2){
                    intent.putExtra("ownVacancy", true);
                }
                startActivity(intent);
            }
        };

        vacancyAdapter = new VacancyAdapter(this,vacancies,2,vacancyClickListener);
        vacancyRecycler.setAdapter(vacancyAdapter);
    }

    public void toProfile(View view) {
        Intent intent;

        if(user.getRole().getId()==1){
            intent = new Intent(this, ProfileEmployeeActivity.class);
        }
        else{
            intent = new Intent(this, ProfileCompanyActivity.class);
        }
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

}