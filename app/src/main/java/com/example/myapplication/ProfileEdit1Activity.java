package com.example.myapplication;

import androidx.annotation.DrawableRes;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;

public class ProfileEdit1Activity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";
    public static final String ROLE_KEY = "role_key";

    SharedPreferences sharedpreferences;
    String email;
    long role_id;

    EditText UserName;
    EditText UserSurname;
    EditText UserBirthdate;
    ImageView Photo;

    private DatabaseAdapter adapter;

    Applicant applicant;


    private final int Pick_image = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_edit1);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);
        role_id = sharedpreferences.getLong(ROLE_KEY, 0);


        UserName = findViewById(R.id.editName);
        UserSurname = findViewById(R.id.editSurname);
        UserBirthdate = findViewById(R.id.editBirthdate);
        Photo = findViewById(R.id.profileImg);

        adapter.open();
        User user = adapter.getUserByEmail(email);
        if(user!=null){
            applicant=adapter.getApplicantByUserId(user.getId());
            if(applicant!=null){
                UserName.setText(applicant.getName());
                UserSurname.setText(applicant.getSurname());
                UserBirthdate.setText(applicant.getBirthdate());
                if(applicant.getPhoto()!=null){
                    Photo.setImageBitmap(applicant.getPhoto());
                }
            }
        }
        adapter.close();



        TextWatcher tw = new TextWatcher() {
            private String current = "";
            private String ddmmyyyy = "ДДММГГГГ";
            private Calendar cal = Calendar.getInstance();

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    String clean = s.toString().replaceAll("[^\\d.]|\\.", "");
                    String cleanC = current.replaceAll("[^\\d.]|\\.", "");

                    int cl = clean.length();
                    int sel = cl;
                    for (int i = 2; i <= cl && i < 6; i += 2) {
                        sel++;
                    }
                    //Fix for pressing delete next to a forward slash
                    if (clean.equals(cleanC)) sel--;

                    if (clean.length() < 8){
                        clean = clean + ddmmyyyy.substring(clean.length());
                    }else{
                        //This part makes sure that when we finish entering numbers
                        //the date is correct, fixing it otherwise
                        int day  = Integer.parseInt(clean.substring(0,2));
                        int mon  = Integer.parseInt(clean.substring(2,4));
                        int year = Integer.parseInt(clean.substring(4,8));

                        mon = mon < 1 ? 1 : mon > 12 ? 12 : mon;
                        cal.set(Calendar.MONTH, mon-1);
                        year = (year<1900)?1900:(year>2100)?2100:year;
                        cal.set(Calendar.YEAR, year);
                        // ^ first set year for the line below to work correctly
                        //with leap years - otherwise, date e.g. 29/02/2012
                        //would be automatically corrected to 28/02/2012

                        day = (day > cal.getActualMaximum(Calendar.DATE))? cal.getActualMaximum(Calendar.DATE):day;
                        clean = String.format("%02d%02d%02d",day, mon, year);
                    }

                    clean = String.format("%s/%s/%s", clean.substring(0, 2),
                            clean.substring(2, 4),
                            clean.substring(4, 8));

                    sel = sel < 0 ? 0 : sel;
                    current = clean;
                    UserBirthdate.setText(current);
                    UserBirthdate.setSelection(sel < current.length() ? sel : current.length());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) { }
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) { }
        };

        UserBirthdate.addTextChangedListener(tw);
    }

    public void SelectImg(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        //Тип получаемых объектов - image:
        photoPickerIntent.setType("image/*");
        //Запускаем переход с ожиданием обратного результата в виде информации об изображении:
        startActivityForResult(photoPickerIntent, Pick_image);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent imageReturnedIntent) {
        super.onActivityResult(requestCode, resultCode, imageReturnedIntent);

        switch(requestCode) {
            case Pick_image:
                if(resultCode == RESULT_OK){
                    try {

                        //Получаем URI изображения, преобразуем его в Bitmap
                        //объект и отображаем в элементе ImageView нашего интерфейса:
                        final Uri imageUri = imageReturnedIntent.getData();
                        final InputStream imageStream = getContentResolver().openInputStream(imageUri);
                        final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);

                        applicant.setPhoto(selectedImage);


                        //---------------------



                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        Photo.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }

//    public void Delete(View view){
//        adapter.open();
//
//        adapter.deleteUser(adapter.getUserByEmail(email).getId());
//
//        adapter.close();
//
//        SharedPreferences.Editor editor = sharedpreferences.edit();
//        editor.clear();
//        editor.apply();
//
//        Intent i = new Intent(this, LoginActivity.class);
//        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//        startActivity(i);
//        finish();
//    }


    public void Save(View view){

        int chck=0;
        String strUserName = UserName.getText().toString();
        String strUserSurname = UserSurname.getText().toString();
        String strUserBirthdate = UserBirthdate.getText().toString();

        if(TextUtils.isEmpty(strUserName)) {
            UserName.setError("Обязательное поле");
            chck++;
        }
        if(TextUtils.isEmpty(strUserSurname)) {
            UserSurname.setError("Обязательное поле");
            chck++;
        }
        if(TextUtils.isEmpty(strUserBirthdate)) {
            UserBirthdate.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }
        applicant.setName(strUserName);
        applicant.setSurname(strUserSurname);
        applicant.setBirthdate(strUserBirthdate);


        adapter.open();
        long provement=adapter.updateApplicant(applicant);
        adapter.close();
        if(provement!=-1){
            Toast.makeText(this, "Сохранено", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ProfileEditActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
            startActivity(intent);
            finish();
        }
        else{
            Toast.makeText(this, "Ошибка", Toast.LENGTH_SHORT).show();
        }

    }


    public void backToProfileEdit(View view){
        Intent intent = new Intent(this, ProfileEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }

}