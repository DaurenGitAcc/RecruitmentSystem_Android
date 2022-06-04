package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.database.DatabaseHelper;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.User;

public class MainActivity extends AppCompatActivity {

    EditText UserName;
    EditText UserSurname;
    EditText CompanyName;
    EditText UserEmail;
    EditText UserPassword;
    RadioGroup radGrp;

    private DatabaseAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        UserName = findViewById(R.id.editPersonName);
        UserSurname = findViewById(R.id.editPersonSurname);
        CompanyName = findViewById(R.id.editCompanyName);
        UserEmail = findViewById(R.id.editEmailNumber);
        UserPassword = findViewById(R.id.editPassword);

        CompanyName.setVisibility(View.GONE);

        adapter = new DatabaseAdapter(this);


        radGrp = (RadioGroup)findViewById(R.id.choice);
        // обработка переключения состояния переключателя
        radGrp.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup arg0, int id) {
                switch(id) {
                    case R.id.worker:
                        UserName.setVisibility(View.VISIBLE);
                        UserSurname.setVisibility(View.VISIBLE);
                        CompanyName.setVisibility(View.GONE);
                        break;
                    case R.id.hr:
                        UserName.setVisibility(View.GONE);
                        UserSurname.setVisibility(View.GONE);
                        CompanyName.setVisibility(View.VISIBLE);
                        break;
                    default:
                        break;
                }
            }});
    }

    public void Registration(View view){
        int chck=0;
        String strUserName = UserName.getText().toString();
        String strUserSurname = UserSurname.getText().toString();
        String strCompanyName = CompanyName.getText().toString();
        String strUserEmail = UserEmail.getText().toString();
        String strUserPassword = UserPassword.getText().toString();

        if(TextUtils.isEmpty(strUserName) && radGrp.getCheckedRadioButtonId()==R.id.worker) {
            UserName.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strUserSurname) && radGrp.getCheckedRadioButtonId()==R.id.worker) {
            UserSurname.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strCompanyName) && radGrp.getCheckedRadioButtonId()==R.id.hr) {
            CompanyName.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strUserEmail)) {
            UserEmail.setError("Обязательное поле");
            chck++;
        }if(TextUtils.isEmpty(strUserPassword)) {
            UserPassword.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }
        boolean successRegistration;
        adapter.open();
        User user = new User();
        long user_id;
        successRegistration=true;
        switch(radGrp.getCheckedRadioButtonId()) {
            case R.id.worker:
                user.setEmail(strUserEmail);
                user.setPassword(strUserPassword);
                user.setRole(adapter.getRole(1));
                user_id = adapter.insertUser(user);
                Applicant applicant=new Applicant();
                applicant.setName(strUserName);
                applicant.setSurname(strUserSurname);
                applicant.setUser(adapter.getUser(user_id));
                if(adapter.insertApplicant(applicant)==-1){
                    Toast.makeText(this, "Такой Email уже существует", Toast.LENGTH_SHORT).show();
                    successRegistration=false;
                }
                break;

            case R.id.hr:


                user.setEmail(strUserEmail);
                user.setPassword(strUserPassword);
                user.setRole(adapter.getRole(2));
                user_id = adapter.insertUser(user);

                Company company=new Company();
                company.setName(strCompanyName);
                company.setUser(adapter.getUser(user_id));
                if(adapter.insertCompany(company)==-1){
                    Toast.makeText(this, "Такой Email уже существует", Toast.LENGTH_SHORT).show();
                    successRegistration=false;
                }
                break;




        }
        adapter.close();
        if(successRegistration){
            Intent intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent);
            finish();
        }

    }


}