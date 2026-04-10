package com.pao.laboratory07.exercise3;

import java.util.*;
import java.util.stream.Collectors;

import com.pao.laboratory07.exercise2.Comanda;
import com.pao.laboratory07.exercise2.ComandaGratuita;
import com.pao.laboratory07.exercise2.ComandaRedusa;
import com.pao.laboratory07.exercise2.ComandaStandard;

public class Main {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        List<Comanda> comenzi = new ArrayList<>();

        for (int i = 0; i < n; i++) {
            String tip = sc.next();
            String nume = sc.next();

            switch (tip) {
                case "STANDARD" -> {
                    double pret = sc.nextDouble();
                    String client = sc.next();

                    Comanda c = new ComandaStandard(nume, pret, client);
                    comenzi.add(c);
                }
                case "DISCOUNTED" -> {
                    double pret = sc.nextDouble();
                    int discount = sc.nextInt();
                    String client = sc.next();

                    Comanda c = new ComandaRedusa(nume, pret, discount, client);
                    comenzi.add(c);
                }
                case "GIFT" -> {
                    String client = sc.next();

                    Comanda c = new ComandaGratuita(nume, client);
                    comenzi.add(c);
                }
            }
        }

        for (Comanda c : comenzi) {
            System.out.println(c.descriere());
        }

        while (true){
            String comanda = sc.next();

            switch (comanda){
                case "STATS" -> {
                    System.out.println("\n--- STATS ---");

                    Map<String, Double> medii = comenzi.stream()
                            .collect(Collectors.groupingBy(
                                    c -> {
                                        if (c instanceof ComandaStandard) return "STANDARD";
                                        if (c instanceof ComandaRedusa) return "DISCOUNTED";
                                        return "GIFT";
                                    },
                                    Collectors.averagingDouble(Comanda::pretFinal)
                            ));
                    if (medii.containsKey("STANDARD")) {
                        System.out.printf("STANDARD: medie = %.2f lei\n", medii.get("STANDARD"));
                    }
                    if (medii.containsKey("DISCOUNTED")) {
                        System.out.printf("DISCOUNTED: medie = %.2f lei\n", medii.get("DISCOUNTED"));
                    }
                    if (medii.containsKey("GIFT")) {
                        System.out.printf("GIFT: medie = %.2f lei\n", medii.get("GIFT"));
                    }
                }
                case "FILTER" -> {
                    double threshold = sc.nextDouble();
                    System.out.printf("\n--- FILTER (>= %.2f) ---\n", threshold);

                    comenzi.stream()
                            .filter(c -> c.pretFinal() >= threshold)
                            .forEach(c -> System.out.println(c.descriere()));
                }
                case "SORT" -> {
                    System.out.println("\n--- SORT (by client, then by pret) ---");

                    comenzi.stream()
                            .sorted(Comparator.comparing(Comanda::getClient)
                                    .thenComparing(Comanda::pretFinal))
                            .forEach(c -> System.out.println(c.descriere()));
                }
                case "SPECIAL" -> {
                    System.out.println("\n--- SPECIAL (discount > 15%) ---");

                    comenzi.stream()
                            .filter(c -> c instanceof ComandaRedusa cr && cr.getDiscountProcent() > 15)
                            .forEach(c -> System.out.println(c.descriere()));
                }
                case "QUIT" -> {return;}
                default -> System.out.println("Comanda invalida");

            }
        }
    }
}
