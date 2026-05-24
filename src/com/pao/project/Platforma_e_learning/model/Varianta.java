package com.pao.project.Platforma_e_learning.model;

import java.util.UUID;

public class Varianta {
    private long id;
    private String text;
    private boolean corecta;

    public Varianta(String text, boolean corecta){
        this.text = text;
        this.corecta = corecta;
    }

    public long getId() { return id;}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean esteCorecta() { return corecta; }
}
