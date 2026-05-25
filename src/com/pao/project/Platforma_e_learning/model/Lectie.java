package com.pao.project.Platforma_e_learning.model;

import java.util.UUID;

public class Lectie {
    private long id;
    private String titlu;
    private String continut;
    private long cursId;

    public Lectie(String titlu, String continut){
        this.titlu = titlu;
        this.continut = continut;
    }

    public long getId() { return id;}
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public String getContinut() { return continut; }
    public void setId(long id) {this.id = id;}
    public void setContinut(String continut) { this.continut = continut; }
    public long getCursId() { return cursId; }
    public void setCursId(long cursId) { this.cursId = cursId; }
}
