package com.pao.laboratory08.exercise1;

import java.io.*;
import java.util.*;

public class Main {
    private static final String FILE_PATH = "src/com/pao/laboratory08/tests/studenti.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(FILE_PATH))) {
            String linie;
            while ((linie = br.readLine()) != null) {
                String[] parti = linie.split(",");
                if (parti.length == 4) {
                    String nume = parti[0];
                    int varsta = Integer.parseInt(parti[1]);
                    String oras = parti[2];
                    String strada = parti[3];

                    Adresa adresa = new Adresa(oras, strada);
                    Student student = new Student(nume, varsta, adresa);
                    studenti.add(student);
                }
            }
        }

        Scanner scanner = new Scanner(System.in);
        if (!scanner.hasNextLine())
            return;

        String comanda = scanner.nextLine().trim();

        if (comanda.equals("PRINT")) {
            for (Student s : studenti) {
                System.out.println(s);
            }
        } else if (comanda.startsWith("SHALLOW") || comanda.startsWith("DEEP")) {
            String[] elementeComanda = comanda.split(" ", 2);
            String tip = elementeComanda[0];
            String numeCautat = elementeComanda[1];

            // cautam studentul cerut
            Student original = null;
            for (Student s : studenti) {
                if (s.getNume().equals(numeCautat)) {
                    original = s;
                    break;
                }
            }

            if (original != null) {
                Student clona;

                if (tip.equals("SHALLOW")) {
                    clona = original.shallowClone();
                } else {
                    clona = original.deepClone();
                }

                clona.getAdresa().setOras("MODIFICAT");

                System.out.println("Original: " + original);
                System.out.println("Clona: " + clona);
            }
        }

        scanner.close();
    }
}