package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.myapplication.database.DatabaseAdapter;
import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.User;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class ProfileCompanyEdit1Activity extends AppCompatActivity {

    public static final String SHARED_PREFS = "shared_prefs";

    public static final String EMAIL_KEY = "email_key";

    SharedPreferences sharedpreferences;
    String email;

    EditText CompanyName;
    EditText Address;
    EditText AboutCompany;
    ImageView Icon;

    private DatabaseAdapter adapter;

    Company company;
    User user;

    private final int Pick_image = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_company_edit1);

        adapter = new DatabaseAdapter(this);

        sharedpreferences = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        email = sharedpreferences.getString(EMAIL_KEY, null);

        CompanyName = findViewById(R.id.editCompanyName);
        Address = findViewById(R.id.editCompanyAddress);
        AboutCompany = findViewById(R.id.editCompanyDescription);
        Icon = findViewById(R.id.profileIcon);

        adapter.open();
        user = adapter.getUserByEmail(email);
        if(user!=null){
            company=adapter.getCompanyByUserId(user.getId());
            if(company!=null){
                CompanyName.setText(company.getName());
                Address.setText(company.getAddress());
                AboutCompany.setText(company.getDescription());
                if(company.getLogo()!=null){
                    Icon.setImageBitmap(company.getLogo());
                }
            }
        }
        adapter.close();

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
                        Bitmap selectedImage;
                        try{
                            selectedImage = BitmapFactory.decodeStream(imageStream);
                        }
                        catch (OutOfMemoryError e){
                            BitmapFactory.Options options = new BitmapFactory.Options();
                            options.inSampleSize=2;
                            selectedImage = BitmapFactory.decodeStream(imageStream,null,options);
                        }
                        int nh = (int) ( selectedImage.getHeight() * (512.0 / selectedImage.getWidth()) );
                        Bitmap scaled = Bitmap.createScaledBitmap(selectedImage, 512, nh, true);

                        company.setLogo(scaled);


                        //---------------------



                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        selectedImage.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        Icon.setImageBitmap(selectedImage);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
        }
    }


    public void Save(View view){

        int chck=0;
        String strCompanyName = CompanyName.getText().toString();
        String strAddress = Address.getText().toString();
        String strAboutCompany = AboutCompany.getText().toString();

        if(TextUtils.isEmpty(strCompanyName)) {
            CompanyName.setError("Обязательное поле");
            chck++;
        }
        if(TextUtils.isEmpty(strAddress)) {
            Address.setError("Обязательное поле");
            chck++;
        }
        if(TextUtils.isEmpty(strAboutCompany)) {
            AboutCompany.setError("Обязательное поле");
            chck++;
        }
        if(chck>0){
            return;
        }
        company.setName(strCompanyName);
        company.setAddress(strAddress);
        company.setDescription(strAboutCompany);


        adapter.open();
        long provement=adapter.updateCompany(company);
        adapter.close();
        if(provement!=-1){
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
    public void Delete(View view){
        adapter.open();

        adapter.deleteUser(user.getId());

        adapter.close();

        Intent i = new Intent(this, LoginActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);
        finish();
    }



    public void backToProfileEdit(View view){
        Intent intent = new Intent(this, ProfileCompanyEditActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(intent);
        finish();
    }


}