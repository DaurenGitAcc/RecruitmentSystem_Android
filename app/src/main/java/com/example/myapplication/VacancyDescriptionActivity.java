package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

public class VacancyDescriptionActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    public static final String ROLE_KEY = "role_key";
    SharedPreferences sharedpreferences;
    String email;

    TextView VacancyPosition;
    TextView VacancySalary;
    TextView VacancySchedule;
    TextView VacancyRequirements;
    TextView CompanyName;
    TextView CompanyDescription;
    TextView VacancyDescription;
    TextView CompanyEmail;
    TextView CompanyDescriptionHolder;
    TextView VacancyDescriptionHolder;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vacancy_description);

        adapter = new DatabaseAdapter(this);



        VacancyPosition = findViewById(R.id.textVacancyPosition);
        VacancySalary = findViewById(R.id.textVacancySalary);
        VacancySchedule = findViewById(R.id.textVacancySchedule);
        VacancyRequirements = findViewById(R.id.textAboutPosition);
        CompanyName = findViewById(R.id.textCompanyName);
        CompanyDescription = findViewById(R.id.textAboutCompany);
        VacancyDescription = findViewById(R.id.textAboutVacancyPosition);
        CompanyEmail = findViewById(R.id.textCompanyEmail);
        CompanyDescriptionHolder = findViewById(R.id.textAboutCompanyHolder);
        VacancyDescriptionHolder = findViewById(R.id.textAboutVacancyPositionHolder);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        Bundle extras = getIntent().getExtras();
        long vacancyId=0;
        if (extras != null) {
            vacancyId = extras.getLong("vacancyId");
        }

        adapter.open();
        Vacancy vacancy = adapter.getVacancy(vacancyId);

        VacancyPosition.setText(vacancy.getPosition());
        VacancySalary.setText(vacancy.getSalary());
        VacancySchedule.setText(vacancy.getSchedule());
        VacancyRequirements.setText(vacancy.getRequirements());
        CompanyName.setText(vacancy.getCompany().getName());

        if(vacancy.getPositionDescription()==null){
            VacancyDescription.setVisibility(View.GONE);
            VacancyDescriptionHolder.setVisibility(View.GONE);
        }
        else{
            VacancyDescription.setText(vacancy.getPositionDescription());
        }

        if(vacancy.getCompany().getDescription()==null){
            CompanyDescription.setVisibility(View.GONE);
            CompanyDescriptionHolder.setVisibility(View.GONE);
        }
        else{
            CompanyDescription.setText(vacancy.getCompany().getDescription());
        }
        CompanyEmail.setText(vacancy.getCompany().getUser().getEmail());

        adapter.close();



    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

}