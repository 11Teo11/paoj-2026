package model;

import java.util.UUID;

public class Varianta {
    private String id;
    private String text;
    private boolean corecta;

    public Varianta(String text, boolean corecta){
        this.id = UUID.randomUUID().toString();
        this.text = text;
        this.corecta = corecta;
    }

    public String getId() { return id;}
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    public boolean esteCorecta() { return corecta; }
}
