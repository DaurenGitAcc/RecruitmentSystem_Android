package com.example.myapplication.database;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class DatabaseHelper extends SQLiteOpenHelper {
    private static String DB_PATH; // полный путь к базе данных
    private static String DB_NAME = "DelayJob.db";
    private static final int SCHEMA = 1; // версия базы данных
    static final String TABLE_USER = "User"; // название таблицы в бд
    static final String TABLE_ROLE = "Role";
    static final String TABLE_COMPANY = "Company";
    static final String TABLE_APPLICANT = "Applicant";
    static final String TABLE_VACANCY = "Vacancy";
    static final String TABLE_RESUME = "Resume";
    static final String TABLE_RESPONSE = "Response";

    // названия столбцов User
    static final String USER_COLUMN_ID = "id";
    static final String USER_COLUMN_EMAIL = "email";
    static final String USER_COLUMN_TEL = "tel_number";
    static final String USER_COLUMN_PASSWORD = "password";
    static final String USER_COLUMN_ROLEID = "role_id";

    // названия столбцов Role
    static final String ROLE_COLUMN_ID = "id";
    static final String ROLE_COLUMN_NAME = "name";

    // названия столбцов Company
    static final String COMPANY_COLUMN_ID = "id";
    static final String COMPANY_COLUMN_USERID = "user_id";
    static final String COMPANY_COLUMN_NAME = "name";
    static final String COMPANY_COLUMN_DESCRIPTION = "description";
    static final String COMPANY_COLUMN_LOGO = "logo";
    static final String COMPANY_COLUMN_ADDRESS = "address";

    // названия столбцов Applicant
    static final String APPLICANT_COLUMN_ID = "id";
    static final String APPLICANT_COLUMN_USERID = "user_id";
    static final String APPLICANT_COLUMN_NAME = "name";
    static final String APPLICANT_COLUMN_SURNAME = "surname";
    static final String APPLICANT_COLUMN_SELFDESCRIPTION = "self_description";
    static final String APPLICANT_COLUMN_BIRTHDATE = "birth_date";
    static final String APPLICANT_COLUMN_PHOTO = "photo";

    // названия столбцов Vacancy
    static final String VACANCY_COLUMN_ID = "id";
    static final String VACANCY_COLUMN_COMPANYID = "company_id";
    static final String VACANCY_COLUMN_POSITION = "position";
    static final String VACANCY_COLUMN_SALARY = "salary";
    static final String VACANCY_COLUMN_REQUIREMENTS = "requirements";
    static final String VACANCY_COLUMN_SCHEDULE = "schedule";
    static final String VACANCY_COLUMN_ABOUTPOSITION = "about_position";

    // названия столбцов Resume
    static final String RESUME_COLUMN_ID = "id";
    static final String RESUME_COLUMN_APPLICANTID = "applicant_id";
    static final String RESUME_COLUMN_EXPERIENCE = "experience";
    static final String RESUME_COLUMN_SALARY = "salary";
    static final String RESUME_COLUMN_SCHEDULE = "schedule";
    static final String RESUME_COLUMN_TECH_STACK = "tech_stack";
    static final String RESUME_COLUMN_EDUCATION = "education";
    static final String RESUME_COLUMN_SPECIALTY = "specialty";
    static final String RESUME_COLUMN_PERSON_DESCR = "person_description";

    // названия столбцов Response
    static final String RESPONSE_COLUMN_ID = "id";
    static final String RESPONSE_COLUMN_COMPANYID = "company_id";
    static final String RESPONSE_COLUMN_APPLICANTID = "applicant_id";

    //------------------------------------------------------------------

    private Context myContext;

    DatabaseHelper(Context context) {
        super(context, DB_NAME, null, SCHEMA);
        this.myContext=context;
        DB_PATH =context.getFilesDir().getPath() + DB_NAME;
    }

    @Override
    public void onCreate(SQLiteDatabase db) { }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,  int newVersion) { }

    void create_db(){

        File file = new File(DB_PATH);
        if (!file.exists()) {
            //получаем локальную бд как поток
            try(InputStream myInput = myContext.getAssets().open(DB_NAME);
                // Открываем пустую бд
                OutputStream myOutput = new FileOutputStream(DB_PATH)) {

                // побайтово копируем данные
                byte[] buffer = new byte[1024];
                int length;
                while ((length = myInput.read(buffer)) > 0) {
                    myOutput.write(buffer, 0, length);
                }
                myOutput.flush();
            }
            catch(IOException ex){
                Log.d("DatabaseHelper", ex.getMessage());
            }
        }
    }
    public SQLiteDatabase open()throws SQLException {

        return SQLiteDatabase.openDatabase(DB_PATH, null, SQLiteDatabase.OPEN_READWRITE);
    }
}
