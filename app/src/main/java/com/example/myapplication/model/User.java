package com.example.myapplication.model;

import com.example.myapplication.R;

public class User {
    private long id;
    private String email;
    private String tel_number;
    private String password;
    private Role role;

    public User(){
    }

    public User(long id, String email, String tel_number, String password, Role role) {
        this.id = id;
        this.email = email;
        this.tel_number = tel_number;
        this.password = password;
        this.role = role;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel_number() {
        return tel_number;
    }

    public void setTel_number(String tel_number) {
        this.tel_number = tel_number;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }
}
