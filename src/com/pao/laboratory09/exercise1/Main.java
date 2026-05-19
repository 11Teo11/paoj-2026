package com.pao.laboratory09.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String OUTPUT_FILE = "output/lab09_ex1.ser";

    public static void main(String[] args) throws Exception {
        // TODO: Implementează conform Readme.md
        //
        // 1. Citește N din stdin, apoi cele N tranzacții (id suma data contSursa contDestinatie tip)
        Scanner scanner = new Scanner(System.in);
        int N = Integer.parseInt(scanner.nextLine());
        List<Tranzactie> tranzactii = new ArrayList<>();
        for (int i = 0; i < N; i++) {
            String[] parts = scanner.nextLine().split(" ");
            int id = Integer.parseInt(parts[0]);
            double suma = Double.parseDouble(parts[1]);
            String data = parts[2];
            String contSursa = parts[3];
            String contDestinatie = parts[4];
            TipTranzactie tip = TipTranzactie.valueOf(parts[5]);
            Tranzactie tranzactie = new Tranzactie(id,suma,data,contSursa,contDestinatie,tip);
            tranzactii.add(tranzactie);
        }

        // 2. Setează câmpul note = "procesat" pe fiecare tranzacție înainte de serializare
        for(Tranzactie t: tranzactii)
            t.setNote("procesat");

        // 3. Serializează lista de tranzacții în OUTPUT_FILE cu ObjectOutputStream (try-with-resources)
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(OUTPUT_FILE))){
            oos.writeObject(tranzactii);
        }

        // 4. Deserializează lista din OUTPUT_FILE cu ObjectInputStream (try-with-resources)
        List<Tranzactie> tranzactiiDeserializate;
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(OUTPUT_FILE))){
            tranzactiiDeserializate = (List<Tranzactie>) ois.readObject();
        }

        // 5. Procesează comenzile din stdin până la EOF:
        //    - LIST          → afișează toate tranzacțiile, câte una pe linie
        //    - FILTER yyyy-MM → afișează tranzacțiile cu data care începe cu yyyy-MM
        //                       sau "Niciun rezultat." dacă nu există
        //    - NOTE id        → afișează "NOTE[id]: <valoarea câmpului note>"
        //                       sau "NOTE[id]: not found" dacă id-ul nu există
        //
        // Format linie tranzacție:
        //   [id] data tip: suma RON | contSursa -> contDestinatie
        //   Ex: [1] 2024-01-15 CREDIT: 1500.00 RON | RO01SRC1 -> RO01DST1

        while(scanner.hasNextLine()){
            String[] linie = scanner.nextLine().split(" ");
            switch (linie[0]) {
                case "LIST" -> {
                    for (Tranzactie t : tranzactiiDeserializate) {
                        System.out.println(t);
                    }
                    break;
                }
                case "FILTER" -> {
                    boolean exista = false;
                    for (Tranzactie t : tranzactiiDeserializate) {
                        if (t.getData().startsWith(linie[1])) {
                            exista = true;
                            System.out.println(t);
                        }
                    }
                    if (!exista){
                        System.out.println("Niciun rezultat.");
                    }
                }
                case "NOTE" -> {
                    boolean exista = false;
                    for (Tranzactie t: tranzactiiDeserializate){
                        if (t.getId() == Integer.parseInt(linie[1])){
                            exista = true;
                            System.out.println("NOTE[" + linie[1] + "]: " + t.note);
                        }
                    }
                    if (!exista){
                        System.out.println("NOTE[" + linie[1] + "]: not found");
                    }
                }
            }
        }

//        System.out.println("TODO: implementează exercițiul 1");
    }
}
