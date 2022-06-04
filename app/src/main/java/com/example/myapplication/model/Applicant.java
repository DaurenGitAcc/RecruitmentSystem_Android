package com.example.myapplication.model;

import android.graphics.Bitmap;

public class Applicant {
    private long id;
    private String name;
    private String surname;
    private String self_description;
    private String birthdate;
    private Bitmap photo;
    private User user;

    public Applicant() {
    }

    public Applicant(long id, String name, String surname, String self_description, String birthdate, Bitmap photo, User user) {
        this.id = id;
        this.name = name;
        this.surname = surname;
        this.self_description = self_description;
        this.birthdate = birthdate;
        this.photo = photo;
        this.user = user;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getSelf_description() {
        return self_description;
    }

    public void setSelf_description(String self_description) {
        this.self_description = self_description;
    }

    public String getBirthdate() {
        return birthdate;
    }

    public void setBirthdate(String birthdate) {
        this.birthdate = birthdate;
    }

    public Bitmap getPhoto() {
        return photo;
    }

    public void setPhoto(Bitmap photo) {
        this.photo = photo;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
