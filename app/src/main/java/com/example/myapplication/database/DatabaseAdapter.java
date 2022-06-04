package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.Resume;
import com.example.myapplication.model.Role;
import com.example.myapplication.model.User;
import com.example.myapplication.model.Vacancy;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseAdapter {
    private final DatabaseHelper dbHelper;
    private SQLiteDatabase database;

    public DatabaseAdapter(Context context){
        dbHelper = new DatabaseHelper(context.getApplicationContext());
        dbHelper.create_db();
    }

    public DatabaseAdapter open(){
        database = dbHelper.open();
        return this;
    }

    public void close(){
        dbHelper.close();
    }

    public Role getRole(long id){
        open();
        Role role = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_ROLE, DatabaseHelper.ROLE_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.ROLE_COLUMN_NAME));
            role = new Role(id, name);
        }
        cursor.close();
        return  role;
    }

    public User getUser(long id){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_USER, DatabaseHelper.USER_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String email = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_EMAIL));
            String tel_number = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_TEL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_PASSWORD));
            long role_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_ROLEID));
            user = new User(id, email,tel_number,password,getRole(role_id));
        }
        cursor.close();
        return  user;
    }

    public long insertUser(User user){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.USER_COLUMN_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.USER_COLUMN_PASSWORD, user.getPassword());
        cv.put(DatabaseHelper.USER_COLUMN_ROLEID, user.getRole().getId());

        return  database.insert(DatabaseHelper.TABLE_USER, null, cv);
    }

    public long insertApplicant(Applicant applicant){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.APPLICANT_COLUMN_NAME, applicant.getName());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_SURNAME, applicant.getSurname());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_USERID, applicant.getUser().getId());

        return  database.insert(DatabaseHelper.TABLE_APPLICANT, null, cv);
    }

    public long insertCompany(Company company){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COMPANY_COLUMN_NAME, company.getName());
        cv.put(DatabaseHelper.COMPANY_COLUMN_USERID, company.getUser().getId());

        return  database.insert(DatabaseHelper.TABLE_COMPANY, null, cv);
    }

    public long updateCompany(Company company){

        String whereClause = DatabaseHelper.COMPANY_COLUMN_ID + "=" + company.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.COMPANY_COLUMN_NAME, company.getName());
        cv.put(DatabaseHelper.COMPANY_COLUMN_ADDRESS, company.getAddress());
        cv.put(DatabaseHelper.COMPANY_COLUMN_DESCRIPTION, company.getDescription());
        Bitmap photo = company.getLogo();
        byte[] byteArray = null;
        if(photo!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        cv.put(DatabaseHelper.COMPANY_COLUMN_LOGO, byteArray);
        return database.update(DatabaseHelper.TABLE_COMPANY, cv, whereClause, null);
    }

    public User getUserByEmail(String email){
        User user = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_USER, DatabaseHelper.USER_COLUMN_EMAIL);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(email)});
        if(cursor.moveToFirst()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_ID));
            String tel_number = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_TEL));
            String password = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_PASSWORD));
            long role_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.USER_COLUMN_ROLEID));
            user = new User(id, email,tel_number,password,getRole(role_id));
        }
        cursor.close();
        return  user;
    }

    public Applicant getApplicant(long id){
        Applicant applicant = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_APPLICANT, DatabaseHelper.APPLICANT_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_NAME));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_SURNAME));
            String self_description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_SELFDESCRIPTION));
            String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_BIRTHDATE));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_PHOTO));
            long user_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_USERID));

            Bitmap bitmap = null;
            if(photo!=null){
                bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            }
            applicant = new Applicant(id, name,surname,self_description,birthdate,bitmap,getUser(user_id));
        }
        cursor.close();
        return  applicant;
    }

    public Applicant getApplicantByUserId(long user_id){
        Applicant applicant = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_APPLICANT, DatabaseHelper.APPLICANT_COLUMN_USERID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(user_id)});
        if(cursor.moveToFirst()){
            System.out.println("Success getapplicant");
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_NAME));
            String surname = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_SURNAME));
            String self_description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_SELFDESCRIPTION));
            String birthdate = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_BIRTHDATE));
            byte[] photo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_PHOTO));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_ID));
            Bitmap bitmap = null;
            if(photo!=null){
                bitmap = BitmapFactory.decodeByteArray(photo, 0, photo.length);
            }
            applicant = new Applicant(id, name,surname,self_description,birthdate,bitmap,getUser(user_id));
        }
        cursor.close();
        return  applicant;
    }

    public Company getCompany(long id){
        Company company = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_COMPANY, DatabaseHelper.COMPANY_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_DESCRIPTION));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_ADDRESS));
            byte[] logo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_LOGO));
            long user_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_USERID));

            Bitmap bitmap = null;
            if(logo!=null){
                bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length);
            }
            company = new Company(id, name,description,address,bitmap,getUser(user_id));
        }
        cursor.close();
        return  company;
    }

    public Company getCompanyByUserId(long user_id){
        Company company = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_COMPANY, DatabaseHelper.COMPANY_COLUMN_USERID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(user_id)});
        if(cursor.moveToFirst()){
            String name = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_NAME));
            String description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_DESCRIPTION));
            String address = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_ADDRESS));
            byte[] logo = cursor.getBlob(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_LOGO));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_ID));

            Bitmap bitmap = null;
            if(logo!=null){
                bitmap = BitmapFactory.decodeByteArray(logo, 0, logo.length);
            }

            company = new Company(id, name,description,address,bitmap,getUser(user_id));
        }
        cursor.close();
        return  company;
    }

    public long updateApplicant(Applicant applicant){

        String whereClause = DatabaseHelper.APPLICANT_COLUMN_ID + "=" + applicant.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.APPLICANT_COLUMN_NAME, applicant.getName());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_SURNAME, applicant.getSurname());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_BIRTHDATE, applicant.getBirthdate());
        Bitmap photo = applicant.getPhoto();
        byte[] byteArray = null;
        if(photo!=null){
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.PNG, 100, stream);
            byteArray = stream.toByteArray();
        }

        cv.put(DatabaseHelper.APPLICANT_COLUMN_PHOTO, byteArray);
        return database.update(DatabaseHelper.TABLE_APPLICANT, cv, whereClause, null);
    }

    public long updateApplicantResume(Applicant applicant){

        String whereClause = DatabaseHelper.APPLICANT_COLUMN_ID + "=" + applicant.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.APPLICANT_COLUMN_NAME, applicant.getName());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_SURNAME, applicant.getSurname());
        cv.put(DatabaseHelper.APPLICANT_COLUMN_BIRTHDATE, applicant.getBirthdate());

        return database.update(DatabaseHelper.TABLE_APPLICANT, cv, whereClause, null);
    }

    public long updateUser(User user){

        String whereClause = DatabaseHelper.USER_COLUMN_ID + "=" + user.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.USER_COLUMN_EMAIL, user.getEmail());
        cv.put(DatabaseHelper.USER_COLUMN_TEL, user.getTel_number());
        cv.put(DatabaseHelper.USER_COLUMN_PASSWORD, user.getPassword());

        return database.update(DatabaseHelper.TABLE_USER, cv, whereClause, null);
    }
    public long deleteUser(long id){

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_USER, whereClause, whereArgs);
    }


    public long insertResume(Resume resume){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.RESUME_COLUMN_APPLICANTID, resume.getApplicant().getId());
        cv.put(DatabaseHelper.RESUME_COLUMN_SALARY, resume.getSalary());
        cv.put(DatabaseHelper.RESUME_COLUMN_SCHEDULE, resume.getSchedule());
        cv.put(DatabaseHelper.RESUME_COLUMN_EXPERIENCE, resume.getExperience());
        cv.put(DatabaseHelper.RESUME_COLUMN_TECH_STACK, resume.getTech_stack());
        cv.put(DatabaseHelper.RESUME_COLUMN_EDUCATION, resume.getEducation());
        cv.put(DatabaseHelper.RESUME_COLUMN_SPECIALTY, resume.getSpecialty());
        cv.put(DatabaseHelper.RESUME_COLUMN_PERSON_DESCR, resume.getPerson_description());

        return  database.insert(DatabaseHelper.TABLE_RESUME, null, cv);
    }

    public long updateResume(Resume resume){

        String whereClause = DatabaseHelper.RESUME_COLUMN_ID + "=" + resume.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.RESUME_COLUMN_SALARY, resume.getSalary());
        cv.put(DatabaseHelper.RESUME_COLUMN_SCHEDULE, resume.getSchedule());
        cv.put(DatabaseHelper.RESUME_COLUMN_EXPERIENCE, resume.getExperience());
        cv.put(DatabaseHelper.RESUME_COLUMN_TECH_STACK, resume.getTech_stack());
        cv.put(DatabaseHelper.RESUME_COLUMN_EDUCATION, resume.getEducation());
        cv.put(DatabaseHelper.RESUME_COLUMN_SPECIALTY, resume.getSpecialty());
        cv.put(DatabaseHelper.RESUME_COLUMN_PERSON_DESCR, resume.getPerson_description());

        return database.update(DatabaseHelper.TABLE_RESUME, cv, whereClause, null);
    }

    public Resume getResumeByUserId(long user_id){
        Resume resume = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_RESUME, DatabaseHelper.RESUME_COLUMN_APPLICANTID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(user_id)});
        if(cursor.moveToFirst()){

            String salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SALARY));
            String schedule = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SCHEDULE));
            String experience = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_EXPERIENCE));
            String tech_stack = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_TECH_STACK));
            String education = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_EDUCATION));
            String specialty = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SPECIALTY));
            String person_description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_PERSON_DESCR));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_ID));

            resume = new Resume(id,experience,salary,schedule,tech_stack,education,specialty,person_description,getApplicantByUserId(user_id));
        }
        cursor.close();
        return  resume;
    }

    public long deleteResume(long id){

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_RESUME, whereClause, whereArgs);
    }

    public long insertVacancy(Vacancy vacancy){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.VACANCY_COLUMN_POSITION, vacancy.getPosition());
        cv.put(DatabaseHelper.VACANCY_COLUMN_SALARY, vacancy.getSalary());
        cv.put(DatabaseHelper.VACANCY_COLUMN_SCHEDULE, vacancy.getSchedule());
        cv.put(DatabaseHelper.VACANCY_COLUMN_REQUIREMENTS, vacancy.getRequirements());
        cv.put(DatabaseHelper.VACANCY_COLUMN_COMPANYID, vacancy.getCompany().getId());
        cv.put(DatabaseHelper.VACANCY_COLUMN_ABOUTPOSITION, vacancy.getPositionDescription());

        return  database.insert(DatabaseHelper.TABLE_VACANCY, null, cv);
    }
    public long updateVacancy(Vacancy vacancy){

        String whereClause = DatabaseHelper.VACANCY_COLUMN_ID + "=" + vacancy.getId();
        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.VACANCY_COLUMN_POSITION, vacancy.getPosition());
        cv.put(DatabaseHelper.VACANCY_COLUMN_SALARY, vacancy.getSalary());
        cv.put(DatabaseHelper.VACANCY_COLUMN_SCHEDULE, vacancy.getSchedule());
        cv.put(DatabaseHelper.VACANCY_COLUMN_REQUIREMENTS, vacancy.getRequirements());
        cv.put(DatabaseHelper.VACANCY_COLUMN_ABOUTPOSITION, vacancy.getPositionDescription());

        return database.update(DatabaseHelper.TABLE_VACANCY, cv, whereClause, null);
    }
    public long deleteVacancy(long id){

        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(id)};
        return database.delete(DatabaseHelper.TABLE_VACANCY, whereClause, whereArgs);
    }
    public List<Vacancy> getVacancies(long comp_id){
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_VACANCY, DatabaseHelper.VACANCY_COLUMN_COMPANYID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(comp_id)});
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ID));
            String position = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_POSITION));
            String salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SALARY));
            String schedule = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SCHEDULE));
            String positionDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ABOUTPOSITION));
            String requirements = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_REQUIREMENTS));
            long company_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_COMPANYID));
            Company company=getCompany(company_id);

            vacancies.add(new Vacancy(id, position,requirements,salary,schedule,positionDescription,company));
        }
        cursor.close();
        return  vacancies;
    }

    public List<Vacancy> getAllVacancies(){
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        Cursor cursor = database.rawQuery(String.format("SELECT * FROM %s",DatabaseHelper.TABLE_VACANCY),null);
        while (cursor.moveToNext()){
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ID));
            String position = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_POSITION));
            String salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SALARY));
            String schedule = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SCHEDULE));
            String positionDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ABOUTPOSITION));
            String requirements = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_REQUIREMENTS));
            long company_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_COMPANYID));
            Company company=getCompany(company_id);

            vacancies.add(new Vacancy(id, position,requirements,salary,schedule,positionDescription,company));
        }
        cursor.close();
        return  vacancies;
    }

    public Vacancy getVacancy(long id){
        Vacancy vacancy = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_VACANCY, DatabaseHelper.VACANCY_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(id)});
        if(cursor.moveToFirst()){
            String position = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_POSITION));
            String salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SALARY));
            String schedule = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_SCHEDULE));
            String positionDescription = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ABOUTPOSITION));
            String requirements = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_REQUIREMENTS));
            long comp_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_COMPANYID));

            vacancy = new Vacancy(id,position,requirements,salary,schedule,positionDescription,getCompany(comp_id));
        }
        cursor.close();
        return  vacancy;
    }


}
