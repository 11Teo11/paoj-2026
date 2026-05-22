package com.pao.project.Platforma_e_learning.model;


import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class Intrebare {
    private String id;
    private String text;
    private List<Varianta> variante;
    private int punctaj;

    public Intrebare(String text, int punctaj){
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.punctaj = punctaj;
        this.variante = new ArrayList<>();
    }

    public void adaugaVarianta(Varianta v){
        if (v != null)
            this.variante.add(v);
    }

    public String getId() { return id;}
    public String getText() { return text; }
    public List<Varianta> getVariante() { return variante; }
    public int getPunctaj() { return punctaj; }
}
