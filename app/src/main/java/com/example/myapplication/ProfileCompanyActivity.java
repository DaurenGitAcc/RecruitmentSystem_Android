package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.Resume;
import com.example.myapplication.model.User;

public class ProfileCompanyActivity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";
    public static final String EMAIL_KEY = "email_key";
    SharedPreferences sharedpreferences;
    String email;

    TextView CompanyName;
    TextView Address;
    TextView AboutCompany;
    TextView Email;

    ImageView Icon;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        CompanyName = findViewById(R.id.textCompanyName);
        Address = findViewById(R.id.textCompanyAddress);
        AboutCompany = findViewById(R.id.textAboutCompany);
        Email = findViewById(R.id.textCompanyEmail);
        Icon = findViewById(R.id.profileIcon);

    }

    @Override
    protected void onResume() {
        super.onResume();
        email = sharedpreferences.getString(EMAIL_KEY, null);
        if(email==null){
            finish();
        }
        adapter.open();
        User user = adapter.getUserByEmail(email);

        if(user!=null){
            Company company=adapter.getCompanyByUserId(user.getId());
            if(company!=null){

                if(TextUtils.isEmpty(company.getName())){
                    CompanyName.setText("Название компании не указано");
                }
                else{
                    CompanyName.setText(company.getName());
                }
                if(company.getAddress()==null || TextUtils.isEmpty(company.getAddress())){
                    Address.setText("Расположение: не указано");
                }
                else{
                    Address.setText("Расположение: "+company.getAddress());
                }

                if(TextUtils.isEmpty(company.getDescription())){
                    AboutCompany.setText("Описание отсутствует");
                }
                else{
                    AboutCompany.setText(company.getDescription());
                }

                Email.setText(user.getEmail());

                if(company.getLogo()!=null){
                    Icon.setImageBitmap(company.getLogo());
                }
            }
        }
        adapter.close();
    }

    public void toSearch(View view) {
        Intent intent = new Intent(this, SearchVacancyActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    public void toEditPage(View v){
        Intent intent = new Intent(this, ProfileCompanyEditActivity.class);
        startActivity(intent);
    }


}