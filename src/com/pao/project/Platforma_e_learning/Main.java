package com.pao.project.Platforma_e_learning;

import com.pao.project.Platforma_e_learning.model.*;
import com.pao.project.Platforma_e_learning.service.*;
import com.pao.project.Platforma_e_learning.util.*;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);
        AuditService audit = AuditService.getInstance();

        // =========================================================
        // Selectie mod
        // =========================================================
        System.out.println("=== Sistem E-Learning ===");
        System.out.println("1. Mod memorie (date temporare)");
        System.out.println("2. Mod baza de date (date persistente)");
        System.out.print("optiune: ");
        int mod = Integer.parseInt(scanner.nextLine());

        if (mod == 1) {
            DataSeeder.populeazaDate();
            rulezaModMemorie(scanner, audit);

        } else if (mod == 2) {
            try {
                DatabaseConnection.getInstance();
                System.out.println("Conexiune la baza de date reusita!");
            } catch (Exception e) {
                System.out.println("Eroare conexiune: " + e.getMessage());
                return;
            }

            if (DataSeederBD.esteGoala())
                DataSeederBD.populeazaDate();
            rulezaModBD(scanner, audit);
        } else {
            System.out.println("Optiune invalida.");
        }

        System.out.println("La revedere!");
        scanner.close();
    }

    // =========================================================
    // MOD MEMORIE RAM
    // =========================================================
    private static void rulezaModMemorie(Scanner scanner, AuditService audit) {
        CursService cs = CursService.getInstance();
        QuizService qs = QuizService.getInstance();
        UtilizatorService us = UtilizatorService.getInstance();

        boolean ruleaza = true;
        while (ruleaza) {
            afiseazaMeniu();
            String optiune = scanner.nextLine();
            try {
                switch (optiune) {
                    case "1" -> {
                        System.out.print("    prenume: "); String p = scanner.nextLine();
                        System.out.print("       nume: "); String n = scanner.nextLine();
                        System.out.print("      email: "); String e = scanner.nextLine();
                        System.out.print("departament: "); String d = scanner.nextLine();
                        us.inregistreazaUtilizator(TipUtilizator.PROFESOR, p, n, e, d);
                        audit.log("inregistreaza_profesor");
                    }
                    case "2" -> {
                        System.out.print("prenume: "); String p = scanner.nextLine();
                        System.out.print("   nume: "); String n = scanner.nextLine();
                        System.out.print("  email: "); String e = scanner.nextLine();
                        us.inregistreazaUtilizator(TipUtilizator.CURSANT, p, n, e, null);
                        audit.log("inregistreaza_cursant");
                    }
                    case "3" -> {
                        System.out.print("email de sters: ");
                        us.stergeUtilizitor(scanner.nextLine());
                        audit.log("sterge_utilizator");
                    }
                    case "4" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        cs.adaugaCurs(t, e);
                        audit.log("adauga_curs");
                    }
                    case "5" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        System.out.print("  titlu lectie: "); String tl = scanner.nextLine();
                        System.out.print("      continut: "); String c = scanner.nextLine();
                        cs.adaugaLectieLaCurs(t, e, tl, c);
                        audit.log("adauga_lectie");
                    }
                    case "6" -> {
                        System.out.print("    titlu curs: "); String t = scanner.nextLine();
                        System.out.print("email profesor: "); String e = scanner.nextLine();
                        cs.afiseazaLectiiCurs(t, e);
                        audit.log("afiseaza_lectii_curs");
                    }
                    case "7" -> {
                        System.out.print(" email cursant: "); String ec = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        cs.inscrieCursant(ec, ep, nc);
                        audit.log("inscrie_cursant_la_curs");
                    }
                    case "8" -> {
                        System.out.print("email cursant: ");
                        cs.listeazaCursuriCursant(scanner.nextLine());
                        audit.log("cursuri_cursant");
                    }
                    case "9" -> {
                        System.out.print("email profesor: ");
                        cs.listeazaCursuriProfesor(scanner.nextLine());
                        audit.log("cursuri_profesor");
                    }
                    case "10" -> {
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("    titlu quiz: "); String tq = scanner.nextLine();
                        qs.adaugaQuiz(nc, ep, tq, NivelDificultate.MEDIU);
                        audit.log("creeaza_quiz");
                    }
                    case "11" -> {
                        System.out.print("email cursant: "); String ec = scanner.nextLine();
                        System.out.print("      id quiz: "); long idq = Long.parseLong(scanner.nextLine());
                        System.out.print("      punctaj: "); int pct = Integer.parseInt(scanner.nextLine());
                        qs.inregistreazaRezultatQuiz(ec, idq, pct, java.time.LocalDate.now());
                        audit.log("inregistreaza_rezultat");
                    }
                    case "12" -> {
                        us.listeazaTopCursantiNrQuizuri();
                        us.listeazaTopCursantiPunctajMediu();
                        audit.log("top_cursanti");
                    }
                    case "13" -> { cs.listeazaTopCursuri(); audit.log("top_cursuri"); }
                    case "14" -> { us.listeazaCursanti(); audit.log("listeaza_cursanti"); }
                    case "15" -> { us.listeazaProfesori(); audit.log("listeaza_profesori"); }
                    case "16" -> { cs.listeazaToateCursurile(); audit.log("listeaza_cursuri"); }
                    case "17" -> {
                        System.out.print("nume utilizator: ");
                        us.cautaUtilizatorDupaNume(scanner.nextLine());
                        audit.log("cauta_utilizator");
                    }
                    case "18" -> {
                        System.out.print("     nume curs: "); String nc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        Curs curs = cs.getCurs(nc, ep);
                        if (curs != null) System.out.println(curs);
                        else System.out.println("Cursul nu exista.");
                        audit.log("cauta_curs");
                    }
                    case "19" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        cs.stergeCurs(tc, ep);
                        audit.log("sterge_curs");
                    }
                    case "20" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("  titlu lectie: "); String tl = scanner.nextLine();
                        cs.stergeLectie(tc, ep, tl);
                        audit.log("sterge_lectie");
                    }
                    case "21" -> {
                        System.out.print("    titlu curs: "); String tc = scanner.nextLine();
                        System.out.print("email profesor: "); String ep = scanner.nextLine();
                        System.out.print("    titlu quiz: "); String tq = scanner.nextLine();
                        qs.stergeQuiz(tc, ep, tq);
                        audit.log("sterge_quiz");
                    }
                    case "0" -> ruleaza = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    // =========================================================
    // MOD BAZA DE DATE
    // =========================================================
    private static void rulezaModBD(Scanner scanner, AuditService audit) {
        UtilizatorServiceBD us = UtilizatorServiceBD.getInstance();
        CursServiceBD cs = CursServiceBD.getInstance();
        QuizServiceBD qs = QuizServiceBD.getInstance();

        boolean ruleaza = true;
        while (ruleaza) {
            afiseazaMeniu();
            String optiune = scanner.nextLine();
            try {
                switch (optiune) {
                    case "1" -> {
                        System.out.print("    prenume: "); String p = scanner.nextLine();
                        System.out.print("       nume: "); String n = scanner.nextLine();
                        System.out.print("      email: "); String e = scanner.nextLine();
                        System.out.print("departament: "); String d = scanner.nextLine();
                        us.inregistreazaUtilizator(p, n, e, TipUtilizator.PROFESOR, d);
                        audit.log("inregistreaza_profesor");
                    }
                    case "2" -> {
                        System.out.print("prenume: "); String p = scanner.nextLine();
                        System.out.print("   nume: "); String n = scanner.nextLine();
                        System.out.print("  email: "); String e = scanner.nextLine();
                        us.inregistreazaUtilizator(p, n, e, TipUtilizator.CURSANT, null);
                        audit.log("inregistreaza_cursant");
                    }
                    case "3" -> {
                        System.out.println("Sterge:");
                        System.out.println("1. Cursant");
                        System.out.println("2. Profesor");
                        System.out.print("optiune: ");
                        String tipSters = scanner.nextLine();
                        Utilizator desters = switch (tipSters) {
                            case "1" -> alegeCursantDinLista(us, scanner);
                            case "2" -> alegeProfesorDinLista(us, scanner);
                            default -> { System.out.println("Optiune invalida."); yield null; }
                        };
                        if (desters == null) break;
                        us.stergeUtilizator(desters.getId());
                        System.out.println("Utilizator sters: " + desters.getNume() + " " + desters.getPrenume());
                        audit.log("sterge_utilizator");
                    }
                    case "4" -> {
                        System.out.print("titlu curs: "); String t = scanner.nextLine();
                        Utilizator prof = alegeProfesorDinLista(us, scanner);
                        if (prof == null) break;
                        cs.adaugaCurs(t, prof.getId());
                        audit.log("adauga_curs");
                    }
                    case "5" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        if (cursuri.isEmpty()) { System.out.println("Nu exista cursuri."); break; }
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume() +
                                    " | prof. " + cursuri.get(i).getProfesor().getNume() +
                                    " " + cursuri.get(i).getProfesor().getPrenume().charAt(0) + ".");
                        System.out.print("Alege curs: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        System.out.print("titlu lectie: "); String tl = scanner.nextLine();
                        System.out.print("    continut: "); String con = scanner.nextLine();
                        cs.adaugaLectie(tl, con, cursuri.get(ic).getId());
                        audit.log("adauga_lectie");
                    }
                    case "6" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        if (cursuri.isEmpty()) { System.out.println("Nu exista cursuri."); break; }
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume() +
                                    " | prof. " + cursuri.get(i).getProfesor().getNume() +
                                    " " + cursuri.get(i).getProfesor().getPrenume().charAt(0) + ".");
                        System.out.print("Alege curs: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        List<Lectie> lectii = cs.lectiiCurs(cursuri.get(ic).getId());
                        if (lectii.isEmpty()) {
                            System.out.println("Nicio lectie la acest curs.");
                            break;
                        }
                        System.out.println("\n--- Lectii ---");
                        for (int i = 0; i < lectii.size(); i++)
                            System.out.println((i+1) + ". " + lectii.get(i).getTitlu());
                        System.out.print("Alege lectia pentru a vedea continutul (0 = inapoi): ");
                        int il = Integer.parseInt(scanner.nextLine());
                        if (il > 0 && il <= lectii.size()) {
                            Lectie aleasa = lectii.get(il - 1);
                            System.out.println("\n=== " + aleasa.getTitlu() + " ===");
                            System.out.println(aleasa.getContinut());
                        }
                        audit.log("afiseaza_lectii_curs");
                    }
                    case "7" -> {
                        Utilizator cursant = alegeCursantDinLista(us, scanner);
                        if (cursant == null) break;
                        Curs curs = alegeCursDinLista(cs, scanner);
                        if (curs == null) break;
                        cs.inscrieCursant(cursant.getId(), curs.getId());
                        audit.log("inscrie_cursant_la_curs");
                    }
                    case "8" -> {
                        Utilizator cursant = alegeCursantDinLista(us, scanner);
                        if (cursant == null) break;
                        cs.getCursuriCursant(cursant.getId()).forEach(System.out::println);
                        audit.log("cursuri_cursant");
                    }
                    case "9" -> {
                        Utilizator prof = alegeProfesorDinLista(us, scanner);
                        if (prof == null) break;
                        List<Curs> cursuri = cs.getCursuriProfesor(prof.getId());
                        if (cursuri.isEmpty()) {
                            System.out.println("Profesorul nu preda niciun curs.");
                        } else {
                            cursuri.forEach(c -> System.out.println("- " + c.getNume()));
                        }
                        audit.log("cursuri_profesor");
                    }
                    case "10" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        if (cursuri.isEmpty()) { System.out.println("Nu exista cursuri."); break; }
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume());
                        System.out.print("Alege curs: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        System.out.print("titlu quiz: "); String tq = scanner.nextLine();
                        System.out.print("dificultate (USOR/MEDIU/GREU): ");
                        NivelDificultate dif = NivelDificultate.valueOf(scanner.nextLine().toUpperCase());
                        System.out.print("nr intrebari: "); int nrI = Integer.parseInt(scanner.nextLine());
                        long idQuiz = qs.adaugaQuiz(tq, dif, cursuri.get(ic).getId());
                        for (int i = 0; i < nrI; i++) {
                            System.out.print("text intrebare " + (i+1) + ": "); String ti = scanner.nextLine();
                            System.out.print("punctaj: "); int pct = Integer.parseInt(scanner.nextLine());
                            long idI = qs.adaugaIntrebare(ti, pct, idQuiz);
                            System.out.print("nr variante: "); int nrV = Integer.parseInt(scanner.nextLine());
                            for (int j = 0; j < nrV; j++) {
                                System.out.print("text varianta " + (j+1) + ": "); String tv = scanner.nextLine();
                                System.out.print("corecta? (da/nu): "); boolean cor = scanner.nextLine().equalsIgnoreCase("da");
                                qs.adaugaVarianta(tv, cor, idI);
                            }
                        }
                        audit.log("creeaza_quiz");
                    }
                    case "11" -> {
                        Utilizator cursant = alegeCursantDinLista(us, scanner);
                        if (cursant == null) break;
                        Curs curs = alegeCursDinLista(cs, scanner);
                        if (curs == null) break;
                        Quiz quiz = alegeQuizDinLista(qs, curs.getId(), scanner);
                        if (quiz == null) break;
                        System.out.print("punctaj: "); double pct = Double.parseDouble(scanner.nextLine());
                        qs.inregistreazaRezultat(cursant.getId(), quiz.getId(), pct);
                        audit.log("inregistreaza_rezultat");
                    }
                    case "12" -> {
                        System.out.println("--- Top cursanti dupa punctaj mediu ---");
                        us.topCursantiDupaPunctajMediu().forEach(System.out::println);
                        System.out.println();
                        System.out.println("--- Top cursanti dupa nr quizuri ---");
                        us.topCursantiDupaNrQuizuri().forEach(System.out::println);
                        audit.log("top_cursanti");
                    }
                    case "13" -> {
                        cs.topCursuriDupaCursanti().forEach(System.out::println);
                        audit.log("top_cursuri");
                    }
                    case "14" -> {
                        us.totiCursantii().forEach(u -> System.out.println(
                                "- " + u.getNume() + " " + u.getPrenume() + " | " + u.getEmail()));
                        audit.log("listeaza_cursanti");
                    }
                    case "15" -> {
                        us.totiProfesorii().forEach(u -> System.out.println(
                                "- " + u.getNume() + " " + u.getPrenume() + " | " + u.getEmail()));
                        audit.log("listeaza_profesori");
                    }
                    case "16" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        if (cursuri.isEmpty()) {
                            System.out.println("Nu exista cursuri.");
                        } else {
                            cursuri.forEach(c -> System.out.println("- " + c.getNume() +
                                    " | prof. " + c.getProfesor().getNume() +
                                    " " + c.getProfesor().getPrenume().charAt(0) + "."));
                        }
                        audit.log("listeaza_cursuri");
                    }
                    case "17" -> {
                        System.out.print("   nume: "); String n = scanner.nextLine();
                        System.out.print("prenume: "); String p = scanner.nextLine();
                        List<Utilizator> gasiti = us.cautaDupaNume(n, p);
                        if (gasiti.isEmpty()) System.out.println("Niciun utilizator gasit.");
                        else gasiti.forEach(u -> System.out.println(
                                u.getNume() + " " + u.getPrenume() +
                                        " | " + u.getEmail() +
                                        " | " + (u instanceof Profesor ? "Profesor" : "Cursant")));
                        audit.log("cauta_utilizator");
                    }
                    case "18" -> {
                        System.out.print("nume curs: "); String nc = scanner.nextLine();
                        List<Curs> cursuri = cs.toateCursurile();
                        List<Curs> gasite = cursuri.stream()
                                .filter(c -> c.getNume().equalsIgnoreCase(nc))
                                .toList();
                        if (gasite.isEmpty()) {
                            System.out.println("Niciun curs gasit.");
                        } else {
                            int nr = 1;
                            for (Curs c : gasite) {
                                System.out.println("\n" + nr + ". " + c.getNume() + " | " +
                                        c.getProfesor().getNume() + " " + c.getProfesor().getPrenume());
                                List<Lectie> lectii = cs.lectiiCurs(c.getId());
                                if (lectii.isEmpty()) {
                                    System.out.println("   (nicio lectie)");
                                } else {
                                    for (Lectie l : lectii) {
                                        System.out.println("   * " + l.getTitlu());
                                    }
                                }
                                nr++;
                            }
                        }
                        audit.log("cauta_curs");
                    }
                    case "19" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume());
                        System.out.print("Alege curs de sters: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        cs.stergeCurs(cursuri.get(ic).getId());
                        audit.log("sterge_curs");
                    }
                    case "20" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume());
                        System.out.print("Alege curs: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        List<Lectie> lectii = cs.lectiiCurs(cursuri.get(ic).getId());
                        if (lectii.isEmpty()) { System.out.println("Nicio lectie."); break; }
                        for (int i = 0; i < lectii.size(); i++)
                            System.out.println((i+1) + ". " + lectii.get(i).getTitlu());
                        System.out.print("Alege lectie de sters: "); int il = Integer.parseInt(scanner.nextLine()) - 1;
                        cs.stergeLectie(lectii.get(il).getId());
                        audit.log("sterge_lectie");
                    }
                    case "21" -> {
                        List<Curs> cursuri = cs.toateCursurile();
                        for (int i = 0; i < cursuri.size(); i++)
                            System.out.println((i+1) + ". " + cursuri.get(i).getNume());
                        System.out.print("Alege curs: "); int ic = Integer.parseInt(scanner.nextLine()) - 1;
                        List<Quiz> quizuri = qs.quizuriCurs(cursuri.get(ic).getId());
                        if (quizuri.isEmpty()) { System.out.println("Niciun quiz."); break; }
                        for (int i = 0; i < quizuri.size(); i++)
                            System.out.println((i+1) + ". " + quizuri.get(i).getTitlu());
                        System.out.print("Alege quiz de sters: "); int iq = Integer.parseInt(scanner.nextLine()) - 1;
                        qs.stergeQuiz(quizuri.get(iq).getId());
                        audit.log("sterge_quiz");
                    }
                    case "22" -> {
                        Curs curs = alegeCursDinLista(cs, scanner);
                        if (curs == null) break;
                        Quiz quiz = alegeQuizDinLista(qs, curs.getId(), scanner);
                        if (quiz == null) break;

                        System.out.println("\n=== " + quiz.getTitlu() +
                                " | " + quiz.getDificultate() + " ===");

                        List<Intrebare> intrebari = qs.intrebariQuiz(quiz.getId());
                        if (intrebari.isEmpty()) {
                            System.out.println("Nicio intrebare la acest quiz.");
                            break;
                        }

                        for (int i = 0; i < intrebari.size(); i++) {
                            Intrebare intr = intrebari.get(i);
                            System.out.println("\n" + (i+1) + ". " + intr.getText() +
                                    " (" + intr.getPunctaj() + " puncte)");
                            List<Varianta> variante = qs.varianteIntrebare(intr.getId());
                            for (int j = 0; j < variante.size(); j++) {
                                System.out.println("   " + (char)('a' + j) + ") " +
                                        variante.get(j).getText());
                            }
                        }

                        System.out.println("\n=== BAREM ===");
                        for (int i = 0; i < intrebari.size(); i++) {
                            List<Varianta> variante = qs.varianteIntrebare(intrebari.get(i).getId());
                            for (int j = 0; j < variante.size(); j++) {
                                if (variante.get(j).esteCorecta()) {
                                    System.out.println((i+1) + ". " + (char)('a' + j));
                                    break;
                                }
                            }
                        }
                        audit.log("vezi_quiz");
                    }
                    case "0" -> ruleaza = false;
                    default -> System.out.println("Optiune invalida.");
                }
            } catch (Exception e) {
                System.out.println("Eroare: " + e.getMessage());
            }
        }
    }

    // =========================================================
    // Helper
    // =========================================================

    private static Utilizator alegeCursantDinLista(UtilizatorServiceBD us, Scanner scanner)
            throws SQLException, IOException {
        List<Utilizator> cursanti = us.totiCursantii();
        if (cursanti.isEmpty()) { System.out.println("Nu exista cursanti."); return null; }
        System.out.println("\n--- Cursanti ---");
        for (int i = 0; i < cursanti.size(); i++)
            System.out.println((i+1) + ". " + cursanti.get(i).getNume() +
                    " " + cursanti.get(i).getPrenume() +
                    " | " + cursanti.get(i).getEmail());
        System.out.print("Alege cursant: ");
        int ales = Integer.parseInt(scanner.nextLine()) - 1;
        return cursanti.get(ales);
    }

    private static Curs alegeCursDinLista(CursServiceBD cs, Scanner scanner)
            throws SQLException, IOException {
        List<Curs> cursuri = cs.toateCursurile();
        if (cursuri.isEmpty()) { System.out.println("Nu exista cursuri."); return null; }
        System.out.println("\n--- Cursuri ---");
        for (int i = 0; i < cursuri.size(); i++)
            System.out.println((i+1) + ". " + cursuri.get(i).getNume() +
                    " | prof. " + cursuri.get(i).getProfesor().getNume() +
                    " " + cursuri.get(i).getProfesor().getPrenume().charAt(0) + ".");
        System.out.print("Alege curs: ");
        int ales = Integer.parseInt(scanner.nextLine()) - 1;
        return cursuri.get(ales);
    }

    private static Quiz alegeQuizDinLista(QuizServiceBD qs, long cursId, Scanner scanner)
            throws SQLException, IOException {
        List<Quiz> quizuri = qs.quizuriCurs(cursId);
        if (quizuri.isEmpty()) { System.out.println("Niciun quiz la acest curs."); return null; }
        System.out.println("\n--- Quizuri ---");
        for (int i = 0; i < quizuri.size(); i++)
            System.out.println((i+1) + ". " + quizuri.get(i).getTitlu() +
                    " | " + quizuri.get(i).getDificultate());
        System.out.print("Alege quiz: ");
        int ales = Integer.parseInt(scanner.nextLine()) - 1;
        return quizuri.get(ales);
    }

    private static Utilizator alegeDinLista(List<Utilizator> lista, Scanner scanner) throws IOException {
        for (int i = 0; i < lista.size(); i++)
            System.out.println((i+1) + ". " + lista.get(i).getNume() +
                    " " + lista.get(i).getPrenume() +
                    " | " + lista.get(i).getEmail());
        System.out.print("Alege: ");
        int ales = Integer.parseInt(scanner.nextLine()) - 1;
        return lista.get(ales);
    }

    private static Utilizator alegeProfesorDinLista(UtilizatorServiceBD us, Scanner scanner)
            throws SQLException, IOException {
        List<Utilizator> profesori = us.totiProfesorii();
        if (profesori.isEmpty()) { System.out.println("Nu exista profesori."); return null; }
        System.out.println("\n--- Profesori ---");
        for (int i = 0; i < profesori.size(); i++)
            System.out.println((i+1) + ". " + profesori.get(i).getNume() +
                    " " + profesori.get(i).getPrenume() +
                    " | " + profesori.get(i).getEmail());
        System.out.print("Alege profesor: ");
        int ales = Integer.parseInt(scanner.nextLine()) - 1;
        return profesori.get(ales);
    }

    private static void afiseazaMeniu() {
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
        System.out.println("21. Sterge quiz               22. Vezi quiz");
        System.out.println(" 0.  Iesire");
        System.out.print("optiune: ");
    }
}