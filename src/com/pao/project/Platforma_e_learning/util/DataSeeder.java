package com.pao.project.Platforma_e_learning.util;

import com.pao.project.Platforma_e_learning.exception.*;
import com.pao.project.Platforma_e_learning.model.*;
import com.pao.project.Platforma_e_learning.service.*;

import java.time.LocalDate;

public class DataSeeder {
    public static void populeazaDate(){
        System.out.println("=== Popularea datelor de test ===");
        try {
            UtilizatorService us = UtilizatorService.getInstance();
            CursService cs = CursService.getInstance();
            QuizService qs = QuizService.getInstance();

            us.inregistreazaUtilizator(TipUtilizator.PROFESOR, "Mihai", "Popescu", "popescu.m@facultate.ro", "Informatica");
            us.inregistreazaUtilizator(TipUtilizator.PROFESOR, "Stefan", "Tiriac", "tiriac.s@facultate.ro", "Informatica");
            us.inregistreazaUtilizator(TipUtilizator.PROFESOR, "Ana", "Ionescu", "ionescu.a@facultate.ro", "Matematica");
            us.inregistreazaUtilizator(TipUtilizator.PROFESOR, "Maria", "Vasilescu", "vasilescu.m@facultate.ro", "Matematica");
            us.inregistreazaUtilizator(TipUtilizator.CURSANT, "Andrei", "Cocea", "cocea.a@student.ro", null);
            us.inregistreazaUtilizator(TipUtilizator.CURSANT, "Elena", "Dumitru", "dumitru.e@student.ro", null);
            us.inregistreazaUtilizator(TipUtilizator.CURSANT, "Matei", "Cozma", "comza.m@student.ro", null);

            cs.adaugaCurs("Programare Avansata pe Obiecte in Java", "popescu.m@facultate.ro");
            cs.adaugaCurs("Structuri de Date", "tiriac.s@facultate.ro");
            cs.adaugaCurs("Algoritmi Avansati", "popescu.m@facultate.ro");
            cs.adaugaCurs("Algoritmi Avansati", "tiriac.s@facultate.ro");
            cs.adaugaCurs("Structuri Algebrice in Informatica", "vasilescu.m@facultate.ro");
            cs.adaugaCurs("Calcul Diferential si Integral", "ionescu.a@facultate.ro");

            cs.adaugaLectieLaCurs("Programare Avansata pe Obiecte in Java", "popescu.m@facultate.ro", "Clase si Obiecte", "Concepte de baza...");
            cs.adaugaLectieLaCurs("Programare Avansata pe Obiecte in Java", "popescu.m@facultate.ro", "Mostenire si Polimorfism", "Cum se folosesc mostenirea si polimorfismul...");
            cs.adaugaLectieLaCurs("Programare Avansata pe Obiecte in Java", "popescu.m@facultate.ro", "Interfete si Clase Abstracte", "Diferentele dintre interfete si clase abstracte...");
            cs.adaugaLectieLaCurs("Structuri de Date", "tiriac.s@facultate.ro", "Liste si Stive", "Implementarea listelor si stivelor...");
            cs.adaugaLectieLaCurs("Algoritmi Avansati", "popescu.m@facultate.ro", "Probleme P vs NP", "Discutii despre problemele P si NP...");
            cs.adaugaLectieLaCurs("Algoritmi Avansati", "popescu.m@facultate.ro", "Vertex Cover si Traveling Salesman", "Algoritmi pentru problemele de vertex cover si traveling salesman...");
            cs.adaugaLectieLaCurs("Algoritmi Avansati", "tiriac.s@facultate.ro", "Algoritmi Genetici", "Cum functioneaza algoritmii genetici...");
            cs.adaugaLectieLaCurs("Structuri Algebrice in Informatica", "vasilescu.m@facultate.ro", "Grupuri si Ineluri", "Definitii si proprietati...");
            cs.adaugaLectieLaCurs("Calcul Diferential si Integral", "ionescu.a@facultate.ro", "Derivate si Integrale", "Tehnici de calcul pentru derivate si integrale...");
            cs.adaugaLectieLaCurs("Calcul Diferential si Integral", "ionescu.a@facultate.ro", "Serii si Sume", "Converenta seriilor si tehnici de calcul pentru sume...");

            cs.inscrieCursant("cocea.a@student.ro", "popescu.m@facultate.ro", "Programare Avansata pe Obiecte in Java");
            cs.inscrieCursant("cocea.a@student.ro", "tiriac.s@facultate.ro", "Structuri de Date");
            cs.inscrieCursant("cocea.a@student.ro", "tiriac.s@facultate.ro", "Algoritmi Avansati");
            cs.inscrieCursant("dumitru.e@student.ro", "popescu.m@facultate.ro", "Programare Avansata pe Obiecte in Java");
            cs.inscrieCursant("dumitru.e@student.ro", "vasilescu.m@facultate.ro", "Structuri Algebrice in Informatica");
            cs.inscrieCursant("comza.m@student.ro", "ionescu.a@facultate.ro", "Calcul Diferential si Integral");
            cs.inscrieCursant("comza.m@student.ro", "popescu.m@facultate.ro", "Algoritmi Avansati");


            Quiz quizPAOJ = qs.adaugaQuiz("Programare Avansata pe Obiecte in Java", "popescu.m@facultate.ro", "Test OOP de Baza", NivelDificultate.USOR);

            Intrebare q1 = new Intrebare("Ce este incapsularea?", 30);
            q1.adaugaVarianta(new Varianta("Mostenirea proprietatilor", false));
            q1.adaugaVarianta(new Varianta("Ascunderea detaliilor de implementare", true));
            q1.adaugaVarianta(new Varianta("Definirea unei interfete", false));
            qs.adaugaIntrebareLaQuiz(quizPAOJ.getId(), q1);

            Intrebare q2 = new Intrebare("Ce este polimorfismul?", 30);
            q2.adaugaVarianta(new Varianta("Ascunderea detaliilor de implementare", false));
            q2.adaugaVarianta(new Varianta("Definirea unei interfete", false));
            q2.adaugaVarianta(new Varianta("Capacitatea unui obiect de a lua mai multe forme", true));
            qs.adaugaIntrebareLaQuiz(quizPAOJ.getId(), q2);

            Intrebare q3 = new Intrebare("Care este scopul unei interfete in Java?", 30);
            q3.adaugaVarianta(new Varianta("Definirea unui contract pentru clasele care o implementeaza", true));
            q3.adaugaVarianta(new Varianta("Ascunderea detaliilor de implementare", false));
            q3.adaugaVarianta(new Varianta("Mostenirea proprietatilor", false));
            qs.adaugaIntrebareLaQuiz(quizPAOJ.getId(), q3);

            Quiz quizAA = qs.adaugaQuiz("Algoritmi Avansati", "tiriac.s@facultate.ro", "Test Algoritmi Genetici", NivelDificultate.MEDIU);

            Intrebare q4 = new Intrebare("Ce este un algoritm genetic?", 20);
            q4.adaugaVarianta(new Varianta("Un algoritm de sortare", false));
            q4.adaugaVarianta(new Varianta("Un algoritm inspirat de procesul de evolutie biologica", true));
            q4.adaugaVarianta(new Varianta("Un algoritm de cautare", false));
            qs.adaugaIntrebareLaQuiz(quizAA.getId(), q4);

            Intrebare q5 = new Intrebare("Care este scopul operatorului de mutatie intr-un algoritm genetic?", 35);
            q5.adaugaVarianta(new Varianta("Selectia indivizilor pentru reproducere", false));
            q5.adaugaVarianta(new Varianta("Introducerea de variatie in populatie", true));
            q5.adaugaVarianta(new Varianta("Recombinarea genelor intre indivizi", false));
            qs.adaugaIntrebareLaQuiz(quizAA.getId(), q5);

            Intrebare q6 = new Intrebare("Ce este selectia prin ruleta", 35);
            q6.adaugaVarianta(new Varianta("Selectia indivizilor cu cele mai bune fitness-uri", false));
            q6.adaugaVarianta(new Varianta("Selectia indivizilor in mod aleator", false));
            q6.adaugaVarianta(new Varianta("Selectia indivizilor in functie de o probabilitate proportionala cu fitness-ul lor", true));
            qs.adaugaIntrebareLaQuiz(quizAA.getId(), q6);


            qs.inregistreazaRezultatQuiz("cocea.a@student.ro", quizPAOJ.getId(), 70, LocalDate.now());
            qs.inregistreazaRezultatQuiz("cocea.a@student.ro", quizAA.getId(), 65, LocalDate.now());
            qs.inregistreazaRezultatQuiz("dumitru.e@student.ro", quizPAOJ.getId(), 100, LocalDate.now());
            qs.inregistreazaRezultatQuiz("comza.m@student.ro", quizAA.getId(), 80, LocalDate.now());

            System.out.println("=== Datele de test au fost adaugate cu succes ===");

        } catch (DateUtilizatorInvalide | DateCursInvalide | DateQuizInvalide | DateRezultatInvalide | EntitateNegasitaException e) {
                System.err.println("Eroare la popularea datelor de test: " + e.getMessage());
        }
    }
}
