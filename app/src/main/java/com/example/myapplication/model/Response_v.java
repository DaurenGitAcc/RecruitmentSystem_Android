package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class Response_v {
    private long id;
    private Vacancy vacancy;
    private List<Resume> resumes;

    public Response_v() {
        resumes= new ArrayList<>();
    }

    public Response_v(long id, Vacancy vacancy, List<Resume> resumes) {
        this.id = id;
        this.vacancy = vacancy;
        this.resumes = resumes;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Vacancy getVacancy() {
        return vacancy;
    }

    public void setVacancy(Vacancy vacancy) {
        this.vacancy = vacancy;
    }

    public List<Resume> getResumes() {
        return resumes;
    }

    public void setResumes(List<Resume> resumes) {
        this.resumes = resumes;
    }
}
