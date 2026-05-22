package com.pao.project.Platforma_e_learning.model;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public non-sealed class Cursant extends Utilizator implements Comparable<Cursant>{

    private Set<Curs> cursuriInscrise;
    private List<Rezultat> istoricNote;

    public Cursant(String prenume, String nume, String email){
        super(prenume, nume, email);
        this.cursuriInscrise = new HashSet<>();
        this.istoricNote = new ArrayList<>();
    }

    @Override
    public String getRol() {
        return "cursant";
    }

    @Override
    public void afiseazaDashboard() {
        System.out.println("=== Dashboard cursant: " + this.nume + " ===");
        System.out.println("Cursuri active: " + this.cursuriInscrise.size());
        System.out.print("Nume cursuri: ");
        for (Curs c : cursuriInscrise) {
            System.out.println("   - " + c.getNume());
        }
        System.out.println("Quiz-uri completate: " + getNrQuizuriDistincte());
        System.out.println("Punctaj mediu: " + getPunctajMediu());
    }

    public Set<Curs> getCursuriInscrise() { return cursuriInscrise; }
    public List<Rezultat> getIstoricNote() { return istoricNote; }

    public double getPunctajMediu() {
        if (istoricNote.isEmpty())
            return 0;
        double suma = 0;

        for(Rezultat r: istoricNote)
            suma += r.valoare();
        return suma / istoricNote.size();
    }

    public long getNrQuizuriDistincte(){
        return istoricNote.stream().map(Rezultat::idQuiz).distinct().count();
    }

    public void inscriereCurs(Curs curs) {
        if (curs != null)
            this.cursuriInscrise.add(curs);
    }

    public void adaugaRezultat(Rezultat rez){
        if (rez != null)
            this.istoricNote.add(rez);
    }

    @Override
    public int compareTo(Cursant c){
        return Double.compare(c.getPunctajMediu(), this.getPunctajMediu());
    }

}
