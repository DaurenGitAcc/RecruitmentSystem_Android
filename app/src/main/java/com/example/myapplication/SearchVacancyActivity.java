package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.myapplication.adapter.VacancyAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

import java.util.List;

public class SearchVacancyActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    RecyclerView vacancyRecycler;
    VacancyAdapter vacancyAdapter;

    SearchView searchView;

    User user;
    private DatabaseAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchvacancy);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
    }

    @Override
    protected void onResume() {
        super.onResume();
        email = sharedpreferences.getString(EMAIL_KEY, null);



        adapter.open();

        user=adapter.getUserByEmail(email);
        try {
            user.getEmail();
        }
        catch (Exception ex){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.clear();
            editor.apply();
            Intent i = new Intent(SearchVacancyActivity.this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);
            finish();
        }

        setVacancyRecycler(adapter.getAllVacancies());

        adapter.close();

        Toast.makeText(this, "Welcome ", Toast.LENGTH_SHORT).show();
        if(email==null){
            /*Intent i = new Intent(this, LoginActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i);*/
            finish();
        }
    }

    public void toProfile(View view) {
        Intent intent;
        if(user.getRole().getId()==1){
            intent = new Intent(this, ProfileEmployeeActivity.class);
        }
        else{
            intent = new Intent(this, ProfileCompanyActivity.class);
        }

        startActivity(intent);
    }
    public void toResponse(View view) {
        Intent intent = new Intent(this, ResponseActivity.class);
        startActivity(intent);
    }

    private void setVacancyRecycler(List<Vacancy> vacancies) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        vacancyRecycler = findViewById(R.id.resumeRecycler);
        vacancyRecycler.setLayoutManager(layoutManager);

        VacancyAdapter.OnStateClickListener vacancyClickListener = new VacancyAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Vacancy vacancy, int position) {
                Intent intent;
                intent = new Intent(SearchVacancyActivity.this, VacancyDescriptionActivity.class);
                intent.putExtra("vacancyId", vacancy.getId());
                startActivity(intent);

            }
        };
        vacancyAdapter = new VacancyAdapter(this,vacancies,0,vacancyClickListener);
        vacancyRecycler.setAdapter(vacancyAdapter);
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        String text = newText;
        vacancyAdapter.filter(text);
        return false;
    }
}