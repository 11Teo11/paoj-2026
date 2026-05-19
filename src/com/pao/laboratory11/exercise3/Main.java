package com.pao.laboratory11.exercise3;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {

        // date hardcodate — 3 luni, 2 tari, 3 canale
        List<Transaction> data = List.of(
                new Transaction(1,  new BigDecimal("1500.00"), LocalDate.of(2024,1,15), "RO", "WEB"),
                new Transaction(2,  new BigDecimal("750.50"),  LocalDate.of(2024,1,22), "RU", "ATM"),
                new Transaction(3,  new BigDecimal("200.00"),  LocalDate.of(2024,1,30), "RO", "APP"),
                new Transaction(4,  new BigDecimal("2000.00"), LocalDate.of(2024,2,5),  "NG", "WEB"),
                new Transaction(5,  new BigDecimal("300.00"),  LocalDate.of(2024,2,14), "RO", "ATM"),
                new Transaction(6,  new BigDecimal("850.00"),  LocalDate.of(2024,2,20), "RO", "WEB"),
                new Transaction(7,  new BigDecimal("1200.00"), LocalDate.of(2024,3,3),  "RO", "APP"),
                new Transaction(8,  new BigDecimal("450.00"),  LocalDate.of(2024,3,11), "RU", "ATM"),
                new Transaction(9,  new BigDecimal("980.00"),  LocalDate.of(2024,3,22), "RO", "WEB"),
                new Transaction(10, new BigDecimal("120.00"),  LocalDate.of(2024,3,28), "NG", "APP")
        );

        // colectare cu collector custom — top 3
        Snapshot snap = data.stream().collect(CustomCollectors.toSnapshot(3));

        // --- interogare 1: Top 3 tranzactii dupa suma ---
        System.out.println("=== Top 3 tranzactii ===");
        snap.getTopTransactions().forEach(System.out::println);

        // --- interogare 2: Numar tranzactii per tara (descendent) ---
        System.out.println("\n=== Tranzactii per tara ===");
        snap.getCountByCountry().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // --- Interogare 3: canale ordonate dupa numar de tranzactii ---
        System.out.println("\n=== Tranzactii per canal ===");
        snap.getCountByChannel().entrySet().stream()
                .sorted(Map.Entry.<String, Long>comparingByValue(Comparator.reverseOrder())
                        .thenComparing(Map.Entry.comparingByKey()))
                .forEach(e -> System.out.println(e.getKey() + ": " + e.getValue()));

        // --- Interogare 4: total general ---
        System.out.println("\n=== Total general ===");
        System.out.printf("Total: %.2f RON%n", snap.getTotalAmount());
    }
}