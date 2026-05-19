package com.pao.laboratory09.exercise3;

public class Main {
    public static void main(String[] args) throws InterruptedException {
        CoadaTranzactii coada = new CoadaTranzactii();

        // 1. creeaza producatorii si consumatorul
        ATMThread atm1 = new ATMThread(1, coada);
        ATMThread atm2 = new ATMThread(2, coada);
        ATMThread atm3 = new ATMThread(3, coada);
        ProcessorThread processorThread = new ProcessorThread(coada);
        Thread processorFir = new Thread(processorThread);

        // 2 si 3. pornește toate firele
        atm1.start();
        atm2.start();
        atm3.start();
        processorFir.start();

        // 4. așteapta terminarea tuturor producatorilor
        atm1.join();
        atm2.join();
        atm3.join();

        // 5. oprire gratioasa a consumatorului
        processorThread.activ = false;
        synchronized (coada) {
            coada.notifyAll(); // trezește consumatorul din wait() daca e suspendat
        }

        // 6. așteapta terminarea consumatorului
        processorFir.join();

        System.out.println("Toate tranzactiile procesate. Total: 12");
    }
}