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
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.User;

public class LoginActivity extends AppCompatActivity {

    // creating constant keys for shared preferences.
    public static final String SHARED_PREFS = "shared_prefs";

    // key for storing email.
    public static final String EMAIL_KEY = "email_key";
    public static final String ROLE_KEY = "role_key";


    // variable for shared preferences.
    SharedPreferences sharedpreferences;
    String email;
    long role_id;



    EditText UserEmail;
    EditText UserPassword;
    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        UserEmail=findViewById(R.id.editEmailNumber);
        UserPassword=findViewById(R.id.editPassword);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        email = sharedpreferences.getString(EMAIL_KEY, null);
        role_id = sharedpreferences.getLong(ROLE_KEY, 0);


    }

    public void Login(View view) {
        int chck=0;
        String strUserEmail = UserEmail.getText().toString();
        String strUserPassword = UserPassword.getText().toString();

        if(TextUtils.isEmpty(strUserEmail)) {
            UserEmail.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strUserPassword)) {
            UserPassword.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }

        adapter.open();
        User user = adapter.getUserByEmail(strUserEmail);
        if(user!=null){
            if(!user.getPassword().equals(strUserPassword)){
                UserPassword.setError("Неправильный пароль!");
                return;
            }
            if(user.getRole().getId()==1){
                Applicant applicant=adapter.getApplicantByUserId(user.getId());
            }
            else{
                Company company=adapter.getCompanyByUserId(user.getId());
            }
            adapter.close();
            if (TextUtils.isEmpty(user.getEmail()) && TextUtils.isEmpty(user.getPassword())) {
                // this method will call when email and password fields are empty.
                Toast.makeText(LoginActivity.this, "Please Enter Email and Password", Toast.LENGTH_SHORT).show();
            } else {
                SharedPreferences.Editor editor = sharedpreferences.edit();

                // below two lines will put values for
                // email and password in shared preferences.
                editor.putString(EMAIL_KEY, user.getEmail());
                editor.putLong(ROLE_KEY, user.getRole().getId());

                // to save our data with key and value.
                editor.apply();

                // starting new activity.
                Intent i = new Intent(this, SearchVacancyActivity.class);
                startActivity(i);
                finish();
            }
        }

    }

    @Override
    protected void onStart() {
        super.onStart();
        if (email != null) {
            Intent i = new Intent(this, SearchVacancyActivity.class);
            startActivity(i);
        }
    }


    public void toRegister(View view) {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }



    public void toTest(View view) {
        Intent intent = new Intent(this, PhotoTest.class);
        startActivity(intent);
    }
}