package com.pao.project.Platforma_e_learning.model;

import java.util.UUID;

public class Varianta {
    private long id;
    private String text;
    private boolean corecta;
    private long intrebareId;

    public Varianta(String text, boolean corecta){
        this.text = text;
        this.corecta = corecta;
    }

    public long getId() { return id;}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean esteCorecta() { return corecta; }
    public void setId(long id) {this.id = id;}
    public long getIntrebareId() { return intrebareId; }
    public void setIntrebareId(long intrebareId) { this.intrebareId = intrebareId; }
}
