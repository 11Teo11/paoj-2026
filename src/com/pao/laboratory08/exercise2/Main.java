package com.pao.laboratory08.exercise2;

import java.io.*;
import java.util.*;

// importam clasele create la exercitiul 1
import com.pao.laboratory08.exercise1.Student;
import com.pao.laboratory08.exercise1.Adresa;

public class Main {
    private static final String INPUT_FILE = "src/com/pao/laboratory08/tests/studenti.txt";
    private static final String OUTPUT_FILE = "src/com/pao/laboratory08/exercise2/rezultate.txt";

    public static void main(String[] args) throws Exception {
        List<Student> studenti = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(INPUT_FILE))) {
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
        if (!scanner.hasNextInt()) {
            scanner.close();
            return;
        }
        int prag = scanner.nextInt();
        scanner.close();

        List<Student> studentiFiltrati = new ArrayList<>();
        for (Student s : studenti) {
            if (s.getVarsta() >= prag) {
                studentiFiltrati.add(s);
            }
        }

        System.out.println("Filtru: varsta >= " + prag);
        System.out.println("Rezultate: " + studentiFiltrati.size() + " studenti");
        System.out.println();

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(OUTPUT_FILE))) {
            for (Student s : studentiFiltrati) {
                System.out.println(s);

                bw.write(s.toString());
                bw.newLine();
            }
        }
    }
}