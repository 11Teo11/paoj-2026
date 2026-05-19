package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.Tranzactie;

import java.time.LocalDate;

public class ProcessorThread implements Runnable {
    public volatile boolean activ = true;
    private final CoadaTranzactii coada;

    public ProcessorThread(CoadaTranzactii coada) {
        this.coada = coada;
    }

    @Override
    public void run() {
        while (activ) {
            try {
                Tranzactie t = coada.extrage();
                System.out.println("[Processor] Factura #" + t.getId()
                        + " - " + t.getSuma() + " RON | " + LocalDate.now());
                Thread.sleep(80);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}