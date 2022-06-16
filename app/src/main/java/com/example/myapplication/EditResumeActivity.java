package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Resume;
import com.example.myapplication.model.User;

public class EditResumeActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;


    EditText ResumeEducation;
    EditText ResumeSpecialty;
    EditText ResumePersonDescr;

    EditText ResumeExperience;
    EditText ResumeSalary;
    EditText ResumeSchedule;
    EditText ResumeTechStack;
    Button DeleteResumeButton;

    private DatabaseAdapter adapter;

    Resume resume;
    User user;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_resume);


        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        ResumeEducation = findViewById(R.id.editEducation);
        ResumeSpecialty = findViewById(R.id.editSpecialty);
        ResumePersonDescr = findViewById(R.id.editPersonDescription);
        ResumeExperience = findViewById(R.id.editExperience);
        ResumeSalary = findViewById(R.id.editSalary);
        ResumeSchedule = findViewById(R.id.editSchedule);
        ResumeTechStack = findViewById(R.id.editTechStack);
        DeleteResumeButton = findViewById(R.id.deleteResumeButton);

        adapter.open();
        user = adapter.getUserByEmail(email);
        resume = adapter.getResumeByUserId(adapter.getApplicantByUserId(user.getId()).getId());
        if(resume!=null){
            ResumeExperience.setText(resume.getExperience());
            ResumeSalary.setText(resume.getSalary());
            ResumeSchedule.setText(resume.getSchedule());
            ResumeTechStack.setText(resume.getTech_stack());
            ResumeEducation.setText(resume.getEducation());
            ResumeSpecialty.setText(resume.getSpecialty());
            ResumePersonDescr.setText(resume.getPerson_description());
            DeleteResumeButton.setVisibility(View.VISIBLE);
        }
        else{
            DeleteResumeButton.setVisibility(View.GONE);
        }
        adapter.close();

    }
    public void DeleteResume(View view){
        adapter.open();
        adapter.deleteResume(resume.getId());
        adapter.close();
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
    public void SaveResume(View view){
        String strResumeExperience = ResumeExperience.getText().toString();
        String strResumeSalary = ResumeSalary.getText().toString();
        String strResumeSchedule = ResumeSchedule.getText().toString();
        String strResumeTechStack = ResumeTechStack.getText().toString();
        String strResumeEducation = ResumeEducation.getText().toString();
        String strResumeSpecialty = ResumeSpecialty.getText().toString();
        String strResumePersonDescr = ResumePersonDescr.getText().toString();

        adapter.open();



        if(resume==null){
            resume = new Resume();
            resume.setExperience(strResumeExperience);
            resume.setSalary(strResumeSalary);
            resume.setSchedule(strResumeSchedule);
            resume.setTech_stack(strResumeTechStack);
            resume.setEducation(strResumeEducation);
            resume.setSpecialty(strResumeSpecialty);
            resume.setPerson_description(strResumePersonDescr);
            resume.setApplicant(adapter.getApplicantByUserId(user.getId()));
            adapter.insertResume(resume);
        }
        else{
            resume.setExperience(strResumeExperience);
            resume.setSalary(strResumeSalary);
            resume.setSchedule(strResumeSchedule);
            resume.setTech_stack(strResumeTechStack);
            resume.setEducation(strResumeEducation);
            resume.setSpecialty(strResumeSpecialty);
            resume.setPerson_description(strResumePersonDescr);
            long a =adapter.updateResume(resume);
        }
        adapter.close();

        finish();
    }

    public void backToProfileEdit(View view){
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}