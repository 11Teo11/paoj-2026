package com.pao.laboratory05.audit;

import com.pao.laboratory05.biblioteca.Carte;

public class Angajat
        implements  Comparable<Angajat>{

    private String nume;
    private Departament departament;
    private double salariu;

    Angajat(String nume, Departament departament, double salariu){
        this.nume = nume;
        this.departament = departament;
        this.salariu = salariu;
    }

    public String getNume() { return nume; }
    public Departament getDepartament() { return departament; }
    public Double getSalariu() { return salariu; }

    @Override
    public String toString() {
        return "Angajat{" +
                "nume='" + nume + '\'' +
                ", departament=" + departament +
                ", salariu=" + salariu +
                '}';
    }

    @Override
    public int compareTo(Angajat other) {
        return -Double.compare(this.salariu, other.salariu);
    }
}
