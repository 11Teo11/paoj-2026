package com.pao.project.Platforma_e_learning.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Quiz {
    private long id;
    private String titlu;
    private List<Intrebare> intrebari;
    private NivelDificultate dificultate;
    private long cursId;

    public Quiz(String titlu, NivelDificultate dificultate){
        this.titlu = titlu;
        this.intrebari = new ArrayList<>();
        this.dificultate = dificultate;
    }

    public void adaugaIntrebare(Intrebare intrebare){
        if (intrebare != null)
            this.intrebari.add(intrebare);
    }

    public int getPunctajMaxim(){
        int total = 0;
        for (Intrebare i: intrebari)
            total += i.getPunctaj();
        return total;
    }

    public long getId() { return id; }
    public String getTitlu() { return titlu; }
    public List<Intrebare> getIntrebari() { return intrebari; }
    public NivelDificultate getDificultate() { return dificultate; }
    public void setId(long id) {this.id = id;}
    public long getCursId() { return cursId; }
    public void setCursId(long cursId) { this.cursId = cursId; }
}
