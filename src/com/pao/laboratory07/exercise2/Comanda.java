package com.pao.laboratory07.exercise2;

import com.pao.laboratory07.exercise1.StareComanda;

public abstract sealed class Comanda permits ComandaStandard, ComandaRedusa, ComandaGratuita {
    protected String nume;
    protected StareComanda stare;
    protected String client;

    public Comanda(String nume, String client){
        this.nume = nume;
        this.client = client;
        this.stare = StareComanda.PLACED;
    }

    public String getClient(){ return client; }

    public abstract double pretFinal();
    public abstract String descriere();
}