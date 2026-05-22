package com.pao.project.Platforma_e_learning.model;

public enum NivelDificultate {
    USOR("potrivit pentru incepatori absoluti"),
    MEDIU("necesita cateva cunostinte de baza"),
    GREU("recomandat doar cursantilor avansati");

    private final String descriere;

    NivelDificultate(String descriere) {
        this.descriere = descriere;
    }

    public String getDescriere() { return descriere; }

    @Override
    public String toString(){
        return name() + " (" + descriere + ") ";
    }
}
