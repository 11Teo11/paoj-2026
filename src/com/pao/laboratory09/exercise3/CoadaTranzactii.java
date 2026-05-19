package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

import java.util.LinkedList;

public class CoadaTranzactii {
    private final LinkedList<Tranzactie> lista = new LinkedList<>();
    private final int CAPACITATE = 2;

    public synchronized void adauga(Tranzactie t) throws InterruptedException {
        while (lista.size() == CAPACITATE) {
            wait();
        }
        lista.add(t);
        notifyAll();
    }

    public synchronized Tranzactie extrage() throws InterruptedException {
        while (lista.isEmpty()) {
            wait();
        }
        Tranzactie t = lista.poll();
        notifyAll();
        return t;
    }
}