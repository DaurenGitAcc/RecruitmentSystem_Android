package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

public class EditVacancyDataActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    EditText VacancyPosition;
    EditText VacancySalary;
    EditText VacancySchedule;
    EditText VacancyRequirements;
    EditText VacancyDescription;

    Button DeleteVac;

    private DatabaseAdapter adapter;

    User user;
    Vacancy vacancy;

    int variant=0;
    long vacancyId=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_vacancy_data);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);


        VacancyPosition = findViewById(R.id.editVacancyPosition);
        VacancySalary = findViewById(R.id.editVacancySalary);
        VacancySchedule = findViewById(R.id.editVacancySchedule);
        VacancyRequirements = findViewById(R.id.editVacancyDescription);
        VacancyDescription = findViewById(R.id.editVacancyPositionDescription);
        DeleteVac = findViewById(R.id.deleteVacancyButton);

        adapter.open();

        user = adapter.getUserByEmail(email);




        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            variant = extras.getInt("variant");
            vacancyId = extras.getLong("vacancyId");
        }
        switch (variant){
            case 1:
                DeleteVac.setVisibility(View.GONE);
                break;
            case 2:
                DeleteVac.setVisibility(View.VISIBLE);
                vacancy=adapter.getVacancy(vacancyId);

                VacancyPosition.setText(vacancy.getPosition());
                VacancySalary.setText(vacancy.getSalary());
                VacancySchedule.setText(vacancy.getSchedule());
                VacancyRequirements.setText(vacancy.getRequirements());
                VacancyDescription.setText(vacancy.getPositionDescription());

                break;
        }
        adapter.close();
    }


    public void SaveVacancy(View view){
        int chck=0;
        String strVacancyPosition = VacancyPosition.getText().toString();
        String strVacancySalary = VacancySalary.getText().toString();
        String strVacancySchedule = VacancySchedule.getText().toString();
        String strVacancyRequirements = VacancyRequirements.getText().toString();
        String strVacancyDescription = VacancyDescription.getText().toString();

        if(TextUtils.isEmpty(strVacancyPosition)) {
            VacancyPosition.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strVacancySalary)) {
            VacancySalary.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strVacancyRequirements)) {
            VacancyRequirements.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }

        adapter.open();

        Vacancy vacancy= new Vacancy();
        Company company = adapter.getCompanyByUserId(user.getId());
        vacancy.setPosition(strVacancyPosition);
        vacancy.setRequirements(strVacancyRequirements);
        vacancy.setSalary(strVacancySalary);
        vacancy.setSchedule(strVacancySchedule);
        vacancy.setPositionDescription(strVacancyDescription);
        vacancy.setCompany(company);
        switch (variant){
            case 1:
                adapter.insertVacancy(vacancy);
                break;
            case 2:
                vacancy.setId(vacancyId);
                adapter.updateVacancy(vacancy);
                break;
            default:
                return;
        }

        adapter.close();

        Intent intent = new Intent(this, EditVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();

    }
    public void DeleteVacancy(View view){

        adapter.open();

        adapter.deleteVacancy(vacancyId);

        adapter.close();

        Intent intent = new Intent(this, EditVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();

    }
    public void backToEditVacanciesEdit(View view){
        Intent intent = new Intent(this, EditVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}