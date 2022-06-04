package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

public class ImgTest extends AppCompatActivity {

    private final static String FILE_NAME = "PhotoTESTe.jpg";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_img_test);



        File imgFile = new File("/data/data/com.example.myapplication/files/PhotoTESTe.jpg");


        if(imgFile.exists()){

            Bitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());

            ImageView img = findViewById(R.id.imageView9);

            img.setImageBitmap(myBitmap);

        }

    }
}