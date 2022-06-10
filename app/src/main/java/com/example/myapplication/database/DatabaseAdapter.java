package com.example.myapplication.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.example.myapplication.model.Applicant;
import com.example.myapplication.model.Company;
import com.example.myapplication.model.Response_r;
import com.example.myapplication.model.Response_v;
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
    public long deleteApplicant(long applicant_id){

        long user_id=-1;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_APPLICANT, DatabaseHelper.APPLICANT_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(applicant_id)});
        if(cursor.moveToFirst()){
            user_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.APPLICANT_COLUMN_USERID));
        }

        long resume_id=-1;
        query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_RESUME, DatabaseHelper.RESUME_COLUMN_APPLICANTID);
        cursor = database.rawQuery(query, new String[]{ String.valueOf(applicant_id)});
        if(cursor.moveToFirst()){
            resume_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_ID));
        }
        cursor.close();
        deleteResume(resume_id);
        deleteUser(user_id);
        String whereClause = String.format("%s=?",DatabaseHelper.APPLICANT_COLUMN_USERID);
        String[] whereArgs = new String[]{String.valueOf(user_id)};
        return database.delete(DatabaseHelper.TABLE_APPLICANT, whereClause, whereArgs);
    }
    public long deleteResume(long resume_id){
        deleteAllApplicantResponse(resume_id);
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(resume_id)};
        return database.delete(DatabaseHelper.TABLE_RESUME, whereClause, whereArgs);
    }
    public long deleteAllApplicantResponse(long resume_id){
        String whereClause = String.format("%s=?",DatabaseHelper.RESPONSE_COLUMN_RESUMEID);
        String[] whereArgs = new String[]{String.valueOf(resume_id)};
        return database.delete(DatabaseHelper.TABLE_RESPONSE, whereClause, whereArgs);
    }


    public long deleteCompany(long company_id){

        long user_id=-1;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_COMPANY, DatabaseHelper.COMPANY_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(company_id)});
        if(cursor.moveToFirst()){
            user_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.COMPANY_COLUMN_USERID));
        }

        long vacancy_id=-1;
        query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_VACANCY, DatabaseHelper.VACANCY_COLUMN_COMPANYID);
        cursor = database.rawQuery(query, new String[]{ String.valueOf(company_id)});
        while (cursor.moveToNext()){
            vacancy_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.VACANCY_COLUMN_ID));
            deleteVacancy(vacancy_id);
        }
        cursor.close();
        deleteUser(user_id);
        String whereClause = String.format("%s=?",DatabaseHelper.COMPANY_COLUMN_ID);
        String[] whereArgs = new String[]{String.valueOf(company_id)};
        return database.delete(DatabaseHelper.TABLE_COMPANY, whereClause, whereArgs);
    }

    public long deleteVacancy(long vacancy_id){
        deleteAllVacancyResponse(vacancy_id);
        String whereClause = "id = ?";
        String[] whereArgs = new String[]{String.valueOf(vacancy_id)};
        return database.delete(DatabaseHelper.TABLE_VACANCY, whereClause, whereArgs);
    }

    public long deleteAllVacancyResponse(long vacancy_id){
        String whereClause = String.format("%s=?",DatabaseHelper.RESPONSE_COLUMN_VACANCYID);
        String[] whereArgs = new String[]{String.valueOf(vacancy_id)};
        return database.delete(DatabaseHelper.TABLE_RESPONSE, whereClause, whereArgs);
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

    public Resume getResume(long resume_id){
        Resume resume = null;
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_RESUME, DatabaseHelper.RESUME_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(resume_id)});
        if(cursor.moveToFirst()){

            String salary = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SALARY));
            String schedule = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SCHEDULE));
            String experience = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_EXPERIENCE));
            String tech_stack = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_TECH_STACK));
            String education = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_EDUCATION));
            String specialty = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_SPECIALTY));
            String person_description = cursor.getString(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_PERSON_DESCR));
            long id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_ID));
            long applicant_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESUME_COLUMN_APPLICANTID));

            resume = new Resume(id,experience,salary,schedule,tech_stack,education,specialty,person_description,getApplicant(applicant_id));
        }
        cursor.close();
        return  resume;
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

    public boolean ownVacancy(long comp_id,long vacancy_id){
        ArrayList<Vacancy> vacancies = new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",DatabaseHelper.TABLE_VACANCY, DatabaseHelper.VACANCY_COLUMN_COMPANYID,DatabaseHelper.VACANCY_COLUMN_ID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(comp_id), String.valueOf(vacancy_id)});

        return cursor.moveToFirst();
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

    public long insertResponse(long V_id,long R_id){

        ContentValues cv = new ContentValues();
        cv.put(DatabaseHelper.RESPONSE_COLUMN_VACANCYID, V_id);
        cv.put(DatabaseHelper.RESPONSE_COLUMN_RESUMEID, R_id);

        return  database.insert(DatabaseHelper.TABLE_RESPONSE, null, cv);
    }

    public long deleteResponse(long vacancy_id,long resume_id){

        String whereClause = String.format("%s=? AND %s=?",DatabaseHelper.RESPONSE_COLUMN_VACANCYID, DatabaseHelper.RESPONSE_COLUMN_RESUMEID);
        String[] whereArgs = new String[]{String.valueOf(vacancy_id),String.valueOf(resume_id)};
        return database.delete(DatabaseHelper.TABLE_RESPONSE, whereClause, whereArgs);
    }



    public Response_v getAllVResponse(Vacancy vacancy){
        Response_v response_v = new Response_v();
        response_v.setVacancy(vacancy);
        ArrayList<Resume> resumes =new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_RESPONSE, DatabaseHelper.RESPONSE_COLUMN_VACANCYID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(vacancy.getId())});
        while (cursor.moveToNext()){
            long resume_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESPONSE_COLUMN_RESUMEID));

            Resume resume=getResume(resume_id);

            resumes.add(resume);
        }
        response_v.setResumes(resumes);
        cursor.close();
        return  response_v;
    }

    public Response_r getAllRResponse(Resume resume){
        Response_r response_r = new Response_r();
        response_r.setResume(resume);
        ArrayList<Vacancy> vacancies =new ArrayList<>();
        String query = String.format("SELECT * FROM %s WHERE %s=?",DatabaseHelper.TABLE_RESPONSE, DatabaseHelper.RESPONSE_COLUMN_RESUMEID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(resume.getId())});
        while (cursor.moveToNext()){
            long vacancy_id = cursor.getLong(cursor.getColumnIndexOrThrow(DatabaseHelper.RESPONSE_COLUMN_VACANCYID));

            Vacancy vacancy=getVacancy(vacancy_id);

            vacancies.add(vacancy);
        }
        response_r.setVacancies(vacancies);
        cursor.close();
        return  response_r;
    }

    public boolean isExistResponse(long V_id,long R_id){
        String query = String.format("SELECT * FROM %s WHERE %s=? AND %s=?",DatabaseHelper.TABLE_RESPONSE, DatabaseHelper.RESPONSE_COLUMN_VACANCYID,DatabaseHelper.RESPONSE_COLUMN_RESUMEID);
        Cursor cursor = database.rawQuery(query, new String[]{ String.valueOf(V_id),String.valueOf(R_id)});

        return cursor.moveToFirst();
    }


}
