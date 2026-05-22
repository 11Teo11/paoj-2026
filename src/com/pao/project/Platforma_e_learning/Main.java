package com.pao.project.Platforma_e_learning;

import com.pao.project.Platforma_e_learning.model.Curs;
import com.pao.project.Platforma_e_learning.model.NivelDificultate;
import com.pao.project.Platforma_e_learning.model.TipUtilizator;
import com.pao.project.Platforma_e_learning.service.CursService;
import com.pao.project.Platforma_e_learning.service.QuizService;
import com.pao.project.Platforma_e_learning.service.UtilizatorService;
import com.pao.project.Platforma_e_learning.util.DataSeeder;

import java.time.LocalDate;
import java.util.Scanner;

public class Main{
    public static void main(String[] args) {
        DataSeeder.populeazaDate();

        UtilizatorService us = UtilizatorService.getInstance();
        CursService cs = CursService.getInstance();
        QuizService qs = QuizService.getInstance();
        Scanner scanner = new Scanner(System.in);

        boolean ruleaza = true;

        while (ruleaza) {
            System.out.println("\n--- Meniu sistem e-learning ---");
            System.out.println("1. Inregistreaza profesor     2.  Inregistreaza cursant");
            System.out.println("3. Sterge utilizator          4.  Adauga curs");
            System.out.println("5. Adauga lectie              6.  Afiseaza lectii curs");
            System.out.println("7. Inscrie cursant la curs    8.  Cursuri cursant");
            System.out.println("9. Cursuri profesor           10. Creeaza quiz");
            System.out.println("11. Inregistreaza rezultat    12. Top cursanti");
            System.out.println("13. Top cursuri               14. Listeaza cursanti");
            System.out.println("15. Listeaza profesori        16. Listeaza toate cursurile");
            System.out.println("17. Cauta utilizator (nume)   18. Cauta curs (nume)");
            System.out.println("19. Sterge curs               20. Sterge lectie");
            System.out.println("21. Sterge quiz                0.  Iesire");
            System.out.print("optiune: ");

            String optiune = scanner.nextLine();

            try {
                switch (optiune) {
                    case "1" -> {
                        System.out.print("    prenume: "); String p = scanner.nextLine();
                        System.out.print("       nume: "); String n = scanner.nextLine();
                        System.out.print("      email: "); String e = scanner.nextLine();
                        System.out.print("departament: "); String d = scanner.nextLine();
                        us.inregistreazaUtilizator(TipUtilizator.PROFESOR, p, n, e, d);
                    }
                    case "2" -> {
                        System.out.print("prenume: "); String p = scanner.nextLine();
                        System.out.print("   nume: "); String n = scanner.nextLine();
                        System.out.print("   email: "); String e = scanner.nextLine();
                        us.inregistreazaUtilizator(TipUtilizator.CURSANT, p, n, e, null);
                    }
                    case "3" -> {
                        System.out.print("email de sters: ");
                        us.stergeUtilizitor(scanner.nextLine());
                    }
                    case "4" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        cs.adaugaCurs(t, e);
                    }
                    case "5" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        System.out.print("  titlu lectie: "); String tl = scanner.nextLine();
                        System.out.print("      continut: "); String c = scanner.nextLine();
                        cs.adaugaLectieLaCurs(t, e, tl, c);
                    }
                    case "6" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        cs.afiseazaLectiiCurs(t, e);
                    }
                    case "7" -> {
                        System.out.print(" email cursant: "); String ec = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        cs.inscrieCursant(ec, ep, nc);
                    }
                    case "8" -> {
                        System.out.print("email cursant: ");
                        cs.listeazaCursuriCursant(scanner.nextLine());
                    }
                    case "9" -> {
                        System.out.print("email profesor: ");
                        cs.listeazaCursuriProfesor(scanner.nextLine());
                    }
                    case "10" -> {
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("    titlu quiz: "); String tq = scanner.nextLine();
                        qs.adaugaQuiz(nc, ep, tq, NivelDificultate.MEDIU);
                    }
                    case "11" -> {
                        System.out.print("email cursant: "); String ec = scanner.nextLine();
                        System.out.print("      id quiz: "); String idq = scanner.nextLine();
                        System.out.print("      punctaj: "); int pct = Integer.parseInt(scanner.nextLine());
                        qs.inregistreazaRezultatQuiz(ec, idq, pct, LocalDate.now());
                    }
                    case "12" -> {
                        us.listeazaTopCursantiNrQuizuri();
                        us.listeazaTopCursantiPunctajMediu();
                    }
                    case "13" -> cs.listeazaTopCursuri();
                    case "14" -> us.listeazaCursanti();
                    case "15" -> us.listeazaProfesori();
                    case "16" -> cs.listeazaToateCursurile();
                    case "17" -> {
                        System.out.print("nume utilizator: ");
                        us.cautaUtilizatorDupaNume(scanner.nextLine());
                    }
                    case "18" -> {
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        Curs curs = cs.getCurs(nc, ep);
                        if(curs != null)
                            System.out.println(curs);
                        else
                            System.out.println("Cursul " + nc + " predat de profesorul cu email-ul " + ep + " nu exista.");
                    }
                    case "19" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        cs.stergeCurs(tc, ep);
                    }
                    case "20" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("  titlu lectie: "); String tl = scanner.nextLine();
                        cs.stergeLectie(tc, ep, tl);
                    }
                    case "21" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("    titlu quiz: "); String tq = scanner.nextLine();
                        qs.stergeQuiz(tc, ep, tq);
                    }
                    case "0" -> ruleaza = false;
                    default -> System.out.println("optiune invalida.");
                }
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
        System.out.println("La revedere!");
    }
}