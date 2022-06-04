package com.example.myapplication.model;

import java.util.ArrayList;
import java.util.List;

public class Response_r {
    private long id;
    private Resume resume;
    private List<Vacancy> vacancies;

    public Response_r() {
        vacancies = new ArrayList<>();
    }

    public Response_r(long id, Resume resume, List<Vacancy> vacancies) {
        this.id = id;
        this.resume = resume;
        this.vacancies = vacancies;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Resume getResume() {
        return resume;
    }

    public void setResume(Resume resume) {
        this.resume = resume;
    }

    public List<Vacancy> getVacancies() {
        return vacancies;
    }

    public void setVacancies(List<Vacancy> vacancies) {
        this.vacancies = vacancies;
    }
}
