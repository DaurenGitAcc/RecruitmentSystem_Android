package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;

import com.example.myapplication.adapter.VacancyAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

import java.util.List;

public class EditVacancyActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    RecyclerView vacancyRecycler;
    VacancyAdapter vacancyAdapter;

    private DatabaseAdapter adapter;

    User user;
    Company company;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vacancy);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        adapter.open();

        user = adapter.getUserByEmail(email);
        company = adapter.getCompanyByUserId(user.getId());
        //List<Vacancy> vacancies = adapter.getVacancies(company.getId());

        adapter.close();

    }

    @Override
    protected void onResume() {
        super.onResume();

        adapter.open();
        List<Vacancy> vacancies = adapter.getVacancies(company.getId());
        adapter.close();
        setVacancyRecycler(vacancies);
    }

    private void setVacancyRecycler(List<Vacancy> vacancies) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        vacancyRecycler = findViewById(R.id.resumeRecycler);
        vacancyRecycler.setLayoutManager(layoutManager);

        VacancyAdapter.OnStateClickListener vacancyClickListener = new VacancyAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Vacancy vacancy, int position) {
                Intent intent = new Intent(EditVacancyActivity.this, EditVacancyDataActivity.class);
                intent.putExtra("vacancyId", vacancy.getId());
                intent.putExtra("variant", 2);
                startActivity(intent);
            }
        };

        vacancyAdapter = new VacancyAdapter(this,vacancies,1,vacancyClickListener);
        vacancyRecycler.setAdapter(vacancyAdapter);
    }

    public void AddVacancy(View view){
        Intent intent = new Intent(this, EditVacancyDataActivity.class);
        intent.putExtra("variant", 1);
        startActivity(intent);
    }

    public void backToProfileEdit(View view){
        Intent intent = new Intent(this, EditVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

}