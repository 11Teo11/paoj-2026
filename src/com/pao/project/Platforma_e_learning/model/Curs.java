package com.pao.project.Platforma_e_learning.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Curs {
    private String id;
    private String nume;
    private Profesor profesor;
    private List<Lectie> lectii;
    private List<Quiz> quizuri;

    public Curs(String nume, Profesor profesor){
        this.id = UUID.randomUUID().toString();
        this.nume = nume;
        this.profesor = profesor;
        this.lectii = new ArrayList<>();
        this.quizuri = new ArrayList<>();
    }

    public void adaugaLectie(Lectie lectie){
        if (lectie != null)
            this.lectii.add(lectie);
    }

    public void adaugaQuiz(Quiz quiz){
        if (quiz != null)
            this.quizuri.add(quiz);
    }

    public String getId() { return id; }
    public String getNume() { return nume; }
    public Profesor getProfesor() { return profesor; }
    public List<Lectie> getLectii() { return lectii; }
    public List<Quiz> getQuizuri() { return quizuri; }

    public int getNrLectii() {
        if (lectii.isEmpty())
            return 0;
        return lectii.size();
    }

    public int getNrQuizuri(){
        if (quizuri.isEmpty())
            return 0;
        return quizuri.size();
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Curs c = (Curs) o;
        return Objects.equals(id, c.id);
    }

    @Override
    public int hashCode(){
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "curs{nume='" +
                "nume = '" + nume + '\'' +
                ", nr. lectii = '" + getNrLectii() + '\'' +
                ", nr. quiz-uri = '" + getNrQuizuri() + "'}";
    }
}
