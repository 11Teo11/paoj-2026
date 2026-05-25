package com.pao.project.Platforma_e_learning.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Intrebare {
    private long id;
    private String text;
    private List<Varianta> variante;
    private int punctaj;
    private long quizId;

    public Intrebare(String text, int punctaj){
        this.text = text;
        this.punctaj = punctaj;
        this.variante = new ArrayList<>();
    }

    public void adaugaVarianta(Varianta v){
        if (v != null)
            this.variante.add(v);
    }

    public long getId() { return id;}
    public String getText() { return text; }
    public List<Varianta> getVariante() { return variante; }
    public int getPunctaj() { return punctaj; }
    public void setId(long id) {this.id = id;}
    public long getQuizId() { return quizId; }
    public void setQuizId(long quizId) { this.quizId = quizId; }
}
