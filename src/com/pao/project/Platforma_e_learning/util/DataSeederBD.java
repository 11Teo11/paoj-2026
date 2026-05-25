package com.pao.project.Platforma_e_learning.util;

import com.pao.project.Platforma_e_learning.model.NivelDificultate;
import com.pao.project.Platforma_e_learning.model.TipUtilizator;
import com.pao.project.Platforma_e_learning.service.*;

import java.io.IOException;
import java.sql.SQLException;

public class DataSeederBD {

    public static void populeazaDate() {
        UtilizatorServiceBD us = UtilizatorServiceBD.getInstance();
        CursServiceBD cs = CursServiceBD.getInstance();
        QuizServiceBD qs = QuizServiceBD.getInstance();

        System.out.println("=== Popularea bazei de date ===");
        try {
            // profesori
            long idPopescu  = us.inregistreazaUtilizator("Mihai", "Popescu", "popescu.m@facultate.ro", TipUtilizator.PROFESOR, "Informatica");
            long idTiriac   = us.inregistreazaUtilizator("Stefan", "Tiriac", "tiriac.s@facultate.ro", TipUtilizator.PROFESOR, "Informatica");
            long idIonescu  = us.inregistreazaUtilizator("Ana", "Ionescu", "ionescu.a@facultate.ro", TipUtilizator.PROFESOR, "Matematica");
            long idVasilescu= us.inregistreazaUtilizator("Maria", "Vasilescu", "vasilescu.m@facultate.ro", TipUtilizator.PROFESOR, "Matematica");

            // cursanti
            long idCocea   = us.inregistreazaUtilizator("Andrei", "Cocea", "cocea.a@student.ro", TipUtilizator.CURSANT, null);
            long idDumitru = us.inregistreazaUtilizator("Elena", "Dumitru", "dumitru.e@student.ro", TipUtilizator.CURSANT, null);
            long idCozma   = us.inregistreazaUtilizator("Matei", "Cozma", "cozma.m@student.ro", TipUtilizator.CURSANT, null);

            // cursuri
            long idPAOJ  = cs.adaugaCurs("Programare Avansata pe Obiecte in Java", idPopescu);
            long idSD    = cs.adaugaCurs("Structuri de Date", idTiriac);
            long idAA1   = cs.adaugaCurs("Algoritmi Avansati", idPopescu);
            long idAA2   = cs.adaugaCurs("Algoritmi Avansati", idTiriac);
            long idSAI   = cs.adaugaCurs("Structuri Algebrice in Informatica", idVasilescu);
            long idCDI   = cs.adaugaCurs("Calcul Diferential si Integral", idIonescu);

            // lectii
            cs.adaugaLectie("Clase si Obiecte", "Concepte de baza...", idPAOJ);
            cs.adaugaLectie("Mostenire si Polimorfism", "Cum se folosesc mostenirea si polimorfismul...", idPAOJ);
            cs.adaugaLectie("Interfete si Clase Abstracte", "Diferentele dintre interfete si clase abstracte...", idPAOJ);
            cs.adaugaLectie("Liste si Stive", "Implementarea listelor si stivelor...", idSD);
            cs.adaugaLectie("Probleme P vs NP", "Discutii despre problemele P si NP...", idAA1);
            cs.adaugaLectie("Vertex Cover si Traveling Salesman", "Algoritmi pentru problemele de vertex cover si traveling salesman...", idAA1);
            cs.adaugaLectie("Algoritmi Genetici", "Cum functioneaza algoritmii genetici...", idAA2);
            cs.adaugaLectie("Grupuri si Ineluri", "Definitii si proprietati...", idSAI);
            cs.adaugaLectie("Derivate si Integrale", "Tehnici de calcul pentru derivate si integrale...", idCDI);
            cs.adaugaLectie("Serii si Sume", "Converenta seriilor si tehnici de calcul pentru sume...", idCDI);

            // inscrieri
            cs.inscrieCursant(idCocea, idPAOJ);
            cs.inscrieCursant(idCocea, idSD);
            cs.inscrieCursant(idCocea, idAA2);
            cs.inscrieCursant(idDumitru, idPAOJ);
            cs.inscrieCursant(idDumitru, idSAI);
            cs.inscrieCursant(idCozma, idCDI);
            cs.inscrieCursant(idCozma, idAA1);

            // quizuri
            long idQuizPAOJ = qs.adaugaQuiz("Test OOP de Baza", NivelDificultate.USOR, idPAOJ);
            long idQ1 = qs.adaugaIntrebare("Ce este incapsularea?", 30, idQuizPAOJ);
            qs.adaugaVarianta("Mostenirea proprietatilor", false, idQ1);
            qs.adaugaVarianta("Ascunderea detaliilor de implementare", true, idQ1);
            qs.adaugaVarianta("Definirea unei interfete", false, idQ1);

            long idQ2 = qs.adaugaIntrebare("Ce este polimorfismul?", 30, idQuizPAOJ);
            qs.adaugaVarianta("Ascunderea detaliilor de implementare", false, idQ2);
            qs.adaugaVarianta("Definirea unei interfete", false, idQ2);
            qs.adaugaVarianta("Capacitatea unui obiect de a lua mai multe forme", true, idQ2);

            long idQ3 = qs.adaugaIntrebare("Care este scopul unei interfete in Java?", 30, idQuizPAOJ);
            qs.adaugaVarianta("Definirea unui contract pentru clasele care o implementeaza", true, idQ3);
            qs.adaugaVarianta("Ascunderea detaliilor de implementare", false, idQ3);
            qs.adaugaVarianta("Mostenirea proprietatilor", true, idQ3);

            long idQuizAA = qs.adaugaQuiz("Test Algoritmi Genetici", NivelDificultate.MEDIU, idAA2);
            long idQ4 = qs.adaugaIntrebare("Ce este un algoritm genetic?", 20, idQuizAA);
            qs.adaugaVarianta("Un algoritm de sortare", false, idQ4);
            qs.adaugaVarianta("Un algoritm inspirat de evolutie biologica", true, idQ4);
            qs.adaugaVarianta("Un algoritm de cautare", false, idQ4);

            long idQ5 = qs.adaugaIntrebare("Care este scopul operatorului de mutatie intr-un algoritm genetic??", 35, idQuizAA);
            qs.adaugaVarianta("Selectia indivizilor pentru reproducere", false, idQ5);
            qs.adaugaVarianta("Introducerea de variatie in populatie", true, idQ5);
            qs.adaugaVarianta("Recombinarea genelor intre indivizi", false, idQ5);

            long idQ6 = qs.adaugaIntrebare("Ce este selectia prin ruleta", 35, idQuizAA);
            qs.adaugaVarianta("Selectia indivizilor cu cele mai bune fitness-uri", false, idQ6);
            qs.adaugaVarianta("Selectia indivizilor in mod aleator", false, idQ6);
            qs.adaugaVarianta("Selectia indivizilor in functie de o probabilitate proportionala cu fitness-ul lor", true, idQ6);

            // rezultate
            qs.inregistreazaRezultat(idCocea, idQuizPAOJ, 70);
            qs.inregistreazaRezultat(idDumitru, idQuizPAOJ, 100);
            qs.inregistreazaRezultat(idCocea, idQuizAA, 65);
            qs.inregistreazaRezultat(idCozma, idQuizAA, 80);

            System.out.println("=== Baza de date populata cu succes ===");

        } catch (SQLException | IOException e) {
            System.err.println("Eroare la popularea BD: " + e.getMessage());
        }
    }

    public static boolean esteGoala() {
        try {
            UtilizatorServiceBD us = UtilizatorServiceBD.getInstance();
            return us.totiUtilizatorii().isEmpty();
        } catch (SQLException | IOException e) {
            return true;
        }
    }
}