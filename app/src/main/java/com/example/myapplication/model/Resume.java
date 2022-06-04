package com.example.myapplication.model;

public class Resume {

    private long id;
    private String experience;
    private String salary;
    private String schedule;
    private String tech_stack;
    private String education;
    private String specialty;
    private String person_description;
    private Applicant applicant;

    public Resume() {
    }

    public Resume(long id, String experience, String salary, String schedule, Applicant applicant,String tech_stack) {
        this.id = id;
        this.experience = experience;
        this.salary = salary;
        this.schedule = schedule;
        this.applicant = applicant;
        this.tech_stack = tech_stack;
    }

    public Resume(long id, String experience, String salary, String schedule, String tech_stack, String education, String specialty, String person_description, Applicant applicant) {
        this.id = id;
        this.experience = experience;
        this.salary = salary;
        this.schedule = schedule;
        this.tech_stack = tech_stack;
        this.education = education;
        this.specialty = specialty;
        this.person_description = person_description;
        this.applicant = applicant;
    }

    public String getEducation() {
        return education;
    }

    public void setEducation(String education) {
        this.education = education;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }

    public String getPerson_description() {
        return person_description;
    }

    public void setPerson_description(String person_description) {
        this.person_description = person_description;
    }

    public String getTech_stack() {
        return tech_stack;
    }

    public void setTech_stack(String tech_stack) {
        this.tech_stack = tech_stack;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Applicant getApplicant() {
        return applicant;
    }

    public void setApplicant(Applicant applicant) {
        this.applicant = applicant;
    }
}
