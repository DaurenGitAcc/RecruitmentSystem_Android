package com.example.myapplication.model;

public class Vacancy {
    private long id;
    private String position;
    private String requirements;
    private String salary;
    private String schedule;
    private String positionDescription;
    private Company company;

    public Vacancy() {
    }

    public Vacancy(long id, String position, String requirements, String salary) {
        this.id = id;
        this.position = position;
        this.requirements = requirements;
        this.salary = salary;
    }

    public Vacancy(long id, String position, String requirements, String salary, String schedule, Company company) {
        this.id = id;
        this.position = position;
        this.requirements = requirements;
        this.salary = salary;
        this.schedule = schedule;
        this.company = company;
    }

    public Vacancy(long id, String position, String requirements, String salary, String schedule,String positionDescription, Company company) {
        this.id = id;
        this.position = position;
        this.requirements = requirements;
        this.salary = salary;
        this.schedule = schedule;
        this.company = company;
        this.positionDescription = positionDescription;
    }

    public String getPositionDescription() {
        return positionDescription;
    }

    public void setPositionDescription(String positionDescription) {
        this.positionDescription = positionDescription;
    }

    public String getSchedule() {
        return schedule;
    }

    public void setSchedule(String schedule) {
        this.schedule = schedule;
    }

    public Company getCompany() {
        return company;
    }

    public void setCompany(Company company) {
        this.company = company;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }
}
