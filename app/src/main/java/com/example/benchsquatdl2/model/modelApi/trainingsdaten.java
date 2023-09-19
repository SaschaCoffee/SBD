package com.example.benchsquatdl2.model.modelApi;

public class trainingsdaten {

    private Long id;
    private String ub_bezeichnung, date, rep1, rep2, rep3, rep4, rep5, kg1, kg2, kg3, kg4, kg5;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUb_bezeichnung() {
        return ub_bezeichnung;
    }

    public void setUb_bezeichnung(String ub_bezeichnung) {
        this.ub_bezeichnung = ub_bezeichnung;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRep1() {
        return rep1;
    }

    public void setRep1(String rep1) {
        this.rep1 = rep1;
    }

    public String getRep2() {
        return rep2;
    }

    public void setRep2(String rep2) {
        this.rep2 = rep2;
    }

    public String getRep3() {
        return rep3;
    }

    public void setRep3(String rep3) {
        this.rep3 = rep3;
    }

    public String getRep4() {
        return rep4;
    }

    public void setRep4(String rep4) {
        this.rep4 = rep4;
    }

    public String getRep5() {
        return rep5;
    }

    public void setRep5(String rep5) {
        this.rep5 = rep5;
    }

    public String getKg1() {
        return kg1;
    }

    public void setKg1(String kg1) {
        this.kg1 = kg1;
    }

    public String getKg2() {
        return kg2;
    }

    public void setKg2(String kg2) {
        this.kg2 = kg2;
    }

    public String getKg3() {
        return kg3;
    }

    public void setKg3(String kg3) {
        this.kg3 = kg3;
    }

    public String getKg4() {
        return kg4;
    }

    public void setKg4(String kg4) {
        this.kg4 = kg4;
    }

    public String getKg5() {
        return kg5;
    }

    public void setKg5(String kg5) {
        this.kg5 = kg5;
    }

    public trainingsdaten(String ub_bezeichnung, String date, String rep1, String rep2, String rep3, String rep4, String rep5, String kg1, String kg2, String kg3, String kg4, String kg5) {
        this.ub_bezeichnung = ub_bezeichnung;
        this.date = date;
        this.rep1 = rep1;
        this.rep2 = rep2;
        this.rep3 = rep3;
        this.rep4 = rep4;
        this.rep5 = rep5;
        this.kg1 = kg1;
        this.kg2 = kg2;
        this.kg3 = kg3;
        this.kg4 = kg4;
        this.kg5 = kg5;
    }
}
