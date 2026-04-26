package model;

import java.util.UUID;

public class Lectie {
    private String id;
    private String titlu;
    private String continut;

    public Lectie(String titlu, String continut){
        this.id = UUID.randomUUID().toString();
        this.titlu = titlu;
        this.continut = continut;
    }

    public String getId() { return id;}
    public String getTitlu() { return titlu; }
    public void setTitlu(String titlu) { this.titlu = titlu; }
    public String getContinut() { return continut; }
    public void setContinut(String continut) { this.continut = continut; }
}
