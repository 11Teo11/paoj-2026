package com.pao.laboratory10.exercise3;

import com.pao.laboratory10.exercise1.TipTranzactie;

import java.util.*;
import java.util.stream.*;

// Extindem Tranzactie local cu contSursa
class Tranzactie {
    private int id;
    private double suma;
    private String data;
    private TipTranzactie tip;
    private String contSursa;

    public Tranzactie(int id, double suma, String data, TipTranzactie tip, String contSursa) {
        this.id = id;
        this.suma = suma;
        this.data = data;
        this.tip = tip;
        this.contSursa = contSursa;
    }

    public int getId()            { return id; }
    public double getSuma()       { return suma; }
    public String getData()       { return data; }
    public TipTranzactie getTip() { return tip; }
    public String getContSursa()  { return contSursa; }

    @Override
    public String toString() {
        return String.format("[%d] %s %s: %.2f RON", id, data, tip, suma);
    }
}

public class Main {
    public static void main(String[] args) {

        // --- Date hardcodate: 10 tranzactii, 3 luni, ambele tipuri ---
        List<Tranzactie> tranzactii = List.of(
                new Tranzactie(1,  1500.00, "2024-01-15", TipTranzactie.CREDIT, "RO49AAAA1"),
                new Tranzactie(2,   750.50, "2024-01-22", TipTranzactie.DEBIT,  "RO49BBBB2"),
                new Tranzactie(3,   200.00, "2024-01-30", TipTranzactie.DEBIT,  "RO49AAAA1"),
                new Tranzactie(4,  2000.00, "2024-02-05", TipTranzactie.CREDIT, "RO49CCCC3"),
                new Tranzactie(5,   300.00, "2024-02-14", TipTranzactie.DEBIT,  "RO49BBBB2"),
                new Tranzactie(6,   850.00, "2024-02-20", TipTranzactie.CREDIT, "RO49AAAA1"),
                new Tranzactie(7,  1200.00, "2024-03-03", TipTranzactie.CREDIT, "RO49CCCC3"),
                new Tranzactie(8,   450.00, "2024-03-11", TipTranzactie.DEBIT,  "RO49DDDD4"),
                new Tranzactie(9,   980.00, "2024-03-22", TipTranzactie.CREDIT, "RO49BBBB2"),
                new Tranzactie(10,  120.00, "2024-03-28", TipTranzactie.DEBIT,  "RO49DDDD4")
        );

        // --- 1. filter: doar CREDIT ---
        System.out.println("=== 1. Tranzactii CREDIT ===");
        tranzactii.stream()
                .filter(t -> t.getTip() == TipTranzactie.CREDIT)
                .forEach(System.out::println);

        // --- 2. mapToDouble + sum ---
        System.out.println("\n=== 2. Total procesat ===");
        double total = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .sum();
        System.out.printf("Total procesat: %.2f RON%n", total);

        // --- 3. groupingBy luna + summingDouble ---
        System.out.println("\n=== 3. Suma per luna ===");
        Map<String, Double> sumaPerLuna = tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,                          // sortare cronologica
                        Collectors.summingDouble(Tranzactie::getSuma)
                ));
        sumaPerLuna.forEach((luna, suma) ->
                System.out.printf("%s: %.2f RON%n", luna, suma));

        // --- 4. sorted descrescator + limit(3) ---
        System.out.println("\n=== 4. Top 3 tranzactii ===");
        System.out.println("Top 3 tranzactii:");
        tranzactii.stream()
                .sorted(Comparator.comparingDouble(Tranzactie::getSuma).reversed())
                .limit(3)
                .forEach(System.out::println);

        // --- 5. map(contSursa) + distinct ---
        System.out.println("\n=== 5. Conturi sursa unice ===");
        List<String> conturiUnice = tranzactii.stream()
                .map(Tranzactie::getContSursa)
                .distinct()
                .collect(Collectors.toList());
        System.out.println("Conturi sursa unice: " + conturiUnice);

        // --- 6. average ---
        System.out.println("\n=== 6. Suma medie ===");
        double medie = tranzactii.stream()
                .mapToDouble(Tranzactie::getSuma)
                .average()
                .orElse(0.0);
        System.out.printf("Suma medie: %.2f RON%n", medie);

        // --- 7. extras de cont lunar ---
        System.out.println("\n=== 7. Extras de cont lunar ===");
        tranzactii.stream()
                .collect(Collectors.groupingBy(
                        t -> t.getData().substring(0, 7),
                        TreeMap::new,
                        Collectors.toList()
                ))
                .forEach((luna, lista) -> {
                    double totalLuna = lista.stream()
                            .mapToDouble(Tranzactie::getSuma)
                            .sum();
                    System.out.printf("EXTRAS DE CONT - %s: %d tranzactii, total: %.2f RON%n",
                            luna, lista.size(), totalLuna);
                });
    }
}