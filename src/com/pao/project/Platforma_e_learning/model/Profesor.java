package com.pao.project.Platforma_e_learning.model;

import java.util.ArrayList;
import java.util.List;

public non-sealed class Profesor extends Utilizator{
    private String departament;
    private List<Curs> cursuriPredate;

    public Profesor(String prenume, String nume, String email, String departament){
        super(prenume, nume, email);
        this.departament = departament;
        this.cursuriPredate = new ArrayList<>();
    }

    public void adaugaCurs(Curs curs){
        if (curs != null)
            this.cursuriPredate.add(curs);
    }

    @Override
    public String getRol() {
        return "profesor";
    }

    @Override
    public void afiseazaDashboard() {
        System.out.println("=== Dashboard profesor: " + this.nume + " ===");
        System.out.println("Departament: " + departament);
        System.out.println("Cursuri predate:");
        for (Curs c: cursuriPredate)
            System.out.println("   - " + c.getNume());
    }

    public String getDepartament() { return departament;}
    public List<Curs> getCursuriPredate() { return cursuriPredate; }

}
