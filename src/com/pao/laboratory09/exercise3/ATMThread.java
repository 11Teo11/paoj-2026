package com.pao.laboratory09.exercise3;

import com.pao.laboratory09.exercise1.TipTranzactie;
import com.pao.laboratory09.exercise1.Tranzactie;

import java.time.LocalDate;
import java.util.Random;

public class ATMThread extends Thread {
    private final int idATM;
    private final CoadaTranzactii coada;
    private static int contor = 0;

    public ATMThread(int idATM, CoadaTranzactii coada) {
        this.idATM = idATM;
        this.coada = coada;
    }

    @Override
    public void run() {
        Random rand = new Random();
        for (int i = 0; i < 4; i++) {
            try {
                int idTranzactie;
                synchronized (ATMThread.class) {
                    idTranzactie = ++contor; // thread-safe increment
                }
                double suma = 100 + rand.nextInt(900);
                Tranzactie t = new Tranzactie(idTranzactie, suma, LocalDate.now().toString(), "ContATM" + idATM, "ContDest", TipTranzactie.CREDIT);

                System.out.println("[ATM-" + idATM + "] trimite: Tranzactie #"
                        + idTranzactie + " " + suma + " RON");
                coada.adauga(t);

                Thread.sleep(50);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}