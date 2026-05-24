package com.pao.project.Platforma_e_learning.model;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record Rezultat(long idCursant, long idQuiz, double valoare, String data) {
    public Rezultat(long idCursant, long idQuiz, double valoare, LocalDate data) {
        this(idCursant, idQuiz, valoare, data.format(DateTimeFormatter.ofPattern("dd-MMM-yyyy")));

    }

}
