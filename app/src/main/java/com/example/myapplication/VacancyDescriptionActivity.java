package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.myapplication.adapter.ResumeAdapter;
import com.example.myapplication.adapter.VacancyAdapter;
import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Resume;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

import java.util.List;

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
    TextView textResumeHolder;
    Button Response;
    ImageView Icon;

    RecyclerView resumeRecycler;
    ResumeAdapter resumeAdapter;

    private DatabaseAdapter adapter;
    User user;
    long vacancyId;
    boolean ownVacancy;

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
        textResumeHolder = findViewById(R.id.textResumeHolder);
        Icon = findViewById(R.id.profileIcon);

        resumeRecycler = findViewById(R.id.resumeRecycler);

        Response=findViewById(R.id.response);
    }
    @Override
    protected void onResume() {
        super.onResume();
        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        Bundle extras = getIntent().getExtras();
        vacancyId=0;
        if (extras != null) {
            vacancyId = extras.getLong("vacancyId");
            ownVacancy = extras.getBoolean("ownVacancy",false);
        }

        adapter.open();
        Vacancy vacancy = adapter.getVacancy(vacancyId);

        user = adapter.getUserByEmail(email);

        if(user.getRole().getId()==1){
            Applicant applicant = adapter.getApplicantByUserId(user.getId());
            Resume resume = adapter.getResumeByUserId(applicant.getId());
            if(resume==null){
                Response.setVisibility(View.VISIBLE);
                resumeRecycler.setVisibility(View.GONE);
                textResumeHolder.setVisibility(View.GONE);
            }
            else{
                if(adapter.isExistResponse(vacancyId,resume.getId())){
                    resumeRecycler.setVisibility(View.GONE);
                    textResumeHolder.setVisibility(View.GONE);
                    //Response.setEnabled(false);
                    Response.setText("Резюме отправлено");
                }
            }
        }
        else{
            Response.setVisibility(View.GONE);
            if(ownVacancy){
                resumeRecycler.setVisibility(View.VISIBLE);
                textResumeHolder.setVisibility(View.VISIBLE);
                setResumeRecycler(adapter.getAllVResponse(adapter.getVacancy(vacancyId)).getResumes());
            }
            else{
                resumeRecycler.setVisibility(View.GONE);
                textResumeHolder.setVisibility(View.GONE);
            }

        }

        VacancyPosition.setText(vacancy.getPosition());
        VacancySalary.setText(vacancy.getSalary());
        VacancySchedule.setText(vacancy.getSchedule());
        VacancyRequirements.setText(vacancy.getRequirements());
        CompanyName.setText(vacancy.getCompany().getName());
        Icon.setImageBitmap(vacancy.getCompany().getLogo());

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

    private void setResumeRecycler(List<Resume> resumes) {
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this,RecyclerView.VERTICAL,false);
        resumeRecycler.setLayoutManager(layoutManager);

        ResumeAdapter.OnStateClickListener resumeClickListener = new ResumeAdapter.OnStateClickListener() {
            @Override
            public void onStateClick(Resume resume, int position) {
                Intent intent = new Intent(VacancyDescriptionActivity.this, ProfileEmployeeActivity.class);
                //TO BE CONTINUE
                intent.putExtra("applicantEmail", resume.getApplicant().getUser().getEmail());
                startActivity(intent);
            }
        };

        resumeAdapter = new ResumeAdapter(this,resumes,2,resumeClickListener);
        resumeRecycler.setAdapter(resumeAdapter);
    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }

    public void toResponse(View view){
        adapter.open();
        Applicant applicant = adapter.getApplicantByUserId(user.getId());
        Resume resume = adapter.getResumeByUserId(applicant.getId());

        if(resume==null){
            Intent intent = new Intent(this, EditResumeActivity.class);
            startActivity(intent);
        }
        else{
            if(adapter.isExistResponse(vacancyId,resume.getId())){
                adapter.deleteResponse(vacancyId,resume.getId());
            }
            else{
                adapter.insertResponse(vacancyId,resume.getId());
            }
        }
        adapter.close();
        recreate();
    }

}