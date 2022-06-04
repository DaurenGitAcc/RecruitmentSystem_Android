package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.User;

public class ProfileCompanyEdit2Activity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    EditText CompanyEmail;
    EditText CompanyPassword;

    private DatabaseAdapter adapter;

    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company_edit2);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);


        CompanyEmail = findViewById(R.id.editCompanyEmail);
        CompanyPassword = findViewById(R.id.editCompanyPassword);

        adapter.open();
        user = adapter.getUserByEmail(email);
        if(user!=null){
            CompanyEmail.setText(user.getEmail());
            CompanyPassword.setText(user.getPassword());
        }
        adapter.close();

    }

    public void Save(View view){

        int chck=0;
        String strCompanyEmail = CompanyEmail.getText().toString();
        String strCompanyPassword = CompanyPassword.getText().toString();

        if(TextUtils.isEmpty(strCompanyEmail)) {
            CompanyEmail.setError("Обязательное поле");
            chck++;
        }
        if(TextUtils.isEmpty(strCompanyPassword)) {
            CompanyPassword.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }
        user.setEmail(strCompanyEmail);
        user.setPassword(strCompanyPassword);

        adapter.open();
        long provement=adapter.updateUser(user);
        adapter.close();
        if(provement!=-1){
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString(EMAIL_KEY, user.getEmail());
            editor.apply();

            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileCompanyEditActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }

    }



    public void backToProfileEdit(View view){
        Intent intent = new Intent(this, ProfileCompanyEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }
}