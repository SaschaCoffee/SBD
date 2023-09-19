package com.example.benchsquatdl2.model.modelApi.Country;

public class germancountry {
    private Long id;
    private String name, instagram, state, federation, date, country,
            sex, age, squat, bench, deadlift;

    public germancountry() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInstagram() {
        return instagram;
    }

    public void setInstagram(String instagram) {
        this.instagram = instagram;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getFederation() {
        return federation;
    }

    public void setFederation(String federation) {
        this.federation = federation;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getSquat() {
        return squat;
    }

    public void setSquat(String squat) {
        this.squat = squat;
    }

    public String getBench() {
        return bench;
    }

    public void setBench(String bench) {
        this.bench = bench;
    }

    public String getDeadlift() {
        return deadlift;
    }

    public void setDeadlift(String deadlift) {
        this.deadlift = deadlift;
    }

    public germancountry(Long id, String name, String instagram, String state, String federation, String date, String country, String sex, String age, String squat, String bench, String deadlift) {
        this.id = id;
        this.name = name;
        this.instagram = instagram;
        this.state = state;
        this.federation = federation;
        this.date = date;
        this.country = country;
        this.sex = sex;
        this.age = age;
        this.squat = squat;
        this.bench = bench;
        this.deadlift = deadlift;
    }
}
