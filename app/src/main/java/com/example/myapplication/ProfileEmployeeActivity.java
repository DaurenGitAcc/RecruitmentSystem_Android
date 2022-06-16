package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.Resume;
import com.example.myapplication.model.User;

import java.io.File;

public class ProfileEmployeeActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    SharedPreferences sharedpreferences;
    String email;

    TextView UserNameSurname;
    TextView UserBirthdate;
    TextView UserEducation;
    TextView UserSpecialty;
    TextView UserPersonDescription;
    TextView UserEmail;
    TextView UserTelNumber;
    ImageView Photo;

    TextView UserEducationTextHolder;
    TextView UserSpecialtyTextHolder;
    TextView UserPersonDescrTextHolder;
    TextView UserExperienceTextHolder;
    TextView UserSalaryTextHolder;
    TextView UserScheduleTextHolder;
    TextView UserTechStackTextHolder;
    TextView UserExperience;
    TextView UserSalary;
    TextView UserSchedule;
    TextView UserTechStack;

    Button EditButton;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_employee);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        UserNameSurname = findViewById(R.id.textNameSurname);
        UserBirthdate = findViewById(R.id.textBirthdate);
        UserEmail = findViewById(R.id.textEmail);
        Photo = findViewById(R.id.profileImg);

        UserEducation = findViewById(R.id.textEducation);
        UserSpecialty = findViewById(R.id.textSpecialty);
        UserPersonDescription = findViewById(R.id.textPersonalDescription);
        UserTelNumber = findViewById(R.id.textTelNumber);

        UserExperienceTextHolder = findViewById(R.id.textView15);
        UserSalaryTextHolder = findViewById(R.id.textView6);
        UserScheduleTextHolder = findViewById(R.id.textView9);
        UserTechStackTextHolder = findViewById(R.id.textView11);

        UserExperience = findViewById(R.id.textExperience);
        //UserSalary = findViewById(R.id.textSalary);
        UserSchedule = findViewById(R.id.textSchedule);
        UserTechStack = findViewById(R.id.textTechStack);

        EditButton = findViewById(R.id.button2);


    }

    @Override
    protected void onResume() {
        super.onResume();
        email = sharedpreferences.getString(EMAIL_KEY, null);
        if(email==null){

            finish();
        }
        adapter.open();

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            email = extras.getString("applicantEmail");
            EditButton.setVisibility(View.GONE);
            EditButton.setEnabled(false);
        }

        User user = adapter.getUserByEmail(email);

        Applicant applicant=null;
        if(user!=null){

            try{
                applicant=adapter.getApplicantByUserId(user.getId());
            }
            catch (Exception ex){
                ex.printStackTrace();
            }

            if(applicant!=null){
                UserNameSurname.setText(applicant.getSurname()+" "+applicant.getName());
                UserBirthdate.setText("Дата рождения: "+applicant.getBirthdate());
                UserEmail.setText(user.getEmail());
                if(applicant.getPhoto()!=null){
                    Photo.setImageBitmap(applicant.getPhoto());
                    System.out.println("Eroro");
                }
            }
        }

        Resume resume = adapter.getResumeByUserId(applicant.getId());
        adapter.close();

        if(resume!=null){
            UserExperienceTextHolder.setVisibility(View.VISIBLE);
            UserSalaryTextHolder.setVisibility(View.VISIBLE);
            UserScheduleTextHolder.setVisibility(View.VISIBLE);
            UserTechStackTextHolder.setVisibility(View.VISIBLE);

            UserExperience.setVisibility(View.VISIBLE);
           // UserSalary.setVisibility(View.GONE);
            UserSchedule.setVisibility(View.VISIBLE);
            UserTechStack.setVisibility(View.VISIBLE);

            UserEducation.setVisibility(View.VISIBLE);
            UserSpecialty.setVisibility(View.VISIBLE);
            UserPersonDescription.setVisibility(View.VISIBLE);

            if(TextUtils.isEmpty(user.getTel_number())){
                UserTelNumber.setText(user.getTel_number());
            }
            else{
                UserTelNumber.setVisibility(View.GONE);
            }
            if(TextUtils.isEmpty(resume.getEducation())){
                UserEducation.setText("Образование\nНе указано");
            }
            else{
                UserEducation.setText("Образование\n"+resume.getEducation());
            }
            if(TextUtils.isEmpty(resume.getSpecialty())){
                UserSpecialty.setVisibility(View.GONE);
            }
            else{
                UserSpecialty.setText("Специальность\n"+resume.getSpecialty());
            }
            if(TextUtils.isEmpty(resume.getPerson_description())){
                UserPersonDescription.setText("Личные Качества:\nНе указаны");
            }
            else{
                UserPersonDescription.setText("Личные Качества:\n"+resume.getPerson_description());
            }

            if(TextUtils.isEmpty(resume.getExperience())){
                UserExperience.setText("Нет опыта");
            }
            else{
                UserExperience.setText(resume.getExperience());
            }
            if(TextUtils.isEmpty(resume.getSalary())){
                UserSalaryTextHolder.setText("Желаемая заработная плата: По договоренности");
            }
            else{
                UserSalaryTextHolder.setText("Желаемая заработная плата: "+resume.getSalary());
            }
            if(TextUtils.isEmpty(resume.getSchedule())){
                UserSchedule.setText("По договоренности");
            }
            else{
                UserSchedule.setText(resume.getSchedule());
            }
            if(TextUtils.isEmpty(resume.getTech_stack())){
                UserTechStack.setText("Нет стэка");
            }
            else{
                UserTechStack.setText(resume.getTech_stack());
            }

        }
        else{
            UserExperienceTextHolder.setVisibility(View.GONE);
            UserSalaryTextHolder.setVisibility(View.GONE);
            UserScheduleTextHolder.setVisibility(View.GONE);
            UserTechStackTextHolder.setVisibility(View.GONE);

            UserExperience.setVisibility(View.GONE);
           // UserSalary.setVisibility(View.GONE);
            UserSchedule.setVisibility(View.GONE);
            UserTechStack.setVisibility(View.GONE);

            UserEducation.setVisibility(View.GONE);
            UserSpecialty.setVisibility(View.GONE);
            UserPersonDescription.setVisibility(View.GONE);
        }
    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void toEditPage(View v){
        Intent intent = new Intent(this, ProfileEditActivity.class);
        startActivity(intent);
    }

}