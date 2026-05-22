package com.pao.project.Platforma_e_learning.service;

import com.pao.project.Platforma_e_learning.exception.DateUtilizatorInvalide;
import com.pao.project.Platforma_e_learning.exception.EntitateNegasitaException;
import com.pao.project.Platforma_e_learning.model.Cursant;
import com.pao.project.Platforma_e_learning.model.Profesor;
import com.pao.project.Platforma_e_learning.model.TipUtilizator;
import com.pao.project.Platforma_e_learning.model.Utilizator;

import java.util.*;

public class UtilizatorService {
    private static UtilizatorService instance;
    private final Map<String, Utilizator> utilizatori;

    private UtilizatorService() {
        this.utilizatori = new HashMap<>();
    }

    public static UtilizatorService getInstance() {
        if (instance == null) {
            instance = new UtilizatorService();
        }
        return instance;
    }

    public void inregistreazaUtilizator(TipUtilizator tip, String prenume, String nume, String email, String departament) throws DateUtilizatorInvalide {
        valideazaDateIntrare(prenume, nume, email);

        if (utilizatori.containsKey(email))
            throw new DateUtilizatorInvalide("Un utilizator cu acest email exista deja.");

        Utilizator utilizator;

        switch (tip){
            case PROFESOR -> {
                if (departament == null || departament.isBlank())
                    throw new DateUtilizatorInvalide("Departamentul este obligatoriu pentru un profesor.");
                utilizator = new Profesor(prenume, nume, email, departament);
                System.out.println("Succes: Profesorul " + prenume + " " + nume + " a fost inregistrat cu succes.");
            }
            case CURSANT -> {
                utilizator = new Cursant(prenume, nume, email);
                System.out.println("Succes: Cursantul " + prenume + " " + nume + " a fost inregistrat cu succes.");
            }
            default -> throw new DateUtilizatorInvalide("Tip de utilizator necunoscut.");
        }

        utilizatori.put(email, utilizator);

    }

    public void valideazaDateIntrare(String prenume, String nume, String email) throws DateUtilizatorInvalide {
        if (prenume == null || prenume.isBlank()) {
            throw new DateUtilizatorInvalide("Prenumele este obligatoriu.");
        }
        if (nume == null || nume.isBlank()) {
            throw new DateUtilizatorInvalide("Numele este obligatoriu.");
        }
        if (email == null || email.isBlank() || !email.contains("@")) {
            throw new DateUtilizatorInvalide("Emailul este invalid.");
        }
    }

    public void stergeUtilizitor(String email) throws EntitateNegasitaException {
        if (!utilizatori.containsKey(email))
            throw new EntitateNegasitaException("Nu exista un utilizator cu acest email.");

        utilizatori.remove(email);
        System.out.println("Succes: Utilizatorul cu email " + email + " a fost sters cu succes.");
    }


    public void listeazaTopCursantiNrQuizuri(){
        List<Cursant> cursanti = extrageCursanti();

        cursanti.sort((c1, c2) -> {
            int res = Long.compare(c2.getNrQuizuriDistincte(), c1.getNrQuizuriDistincte());
            if (res == 0) {
                return c1.getNume().compareTo(c2.getNume());
            }
            return res;
        });

        afiseazaTop("Top cursanti dupa numarul de quizuri completate", cursanti);

    }

    public void listeazaTopCursantiPunctajMediu(){
        List<Cursant> cursanti = extrageCursanti();

        Collections.sort(cursanti);

        afiseazaTop("Top cursanti dupa punctajul mediu", cursanti);
    }

    public List<Cursant> extrageCursanti() {
        List<Cursant> cursanti = new ArrayList<>();
        for (Utilizator u : utilizatori.values()) {
            if (u instanceof Cursant) {
                cursanti.add((Cursant) u);
            }
        }
        return cursanti;
    }

    private List<Profesor> extrageProfesori() {
        List<Profesor> profesori = new ArrayList<>();
        for (Utilizator u: utilizatori.values()) {
            if (u instanceof Profesor) {
                profesori.add((Profesor) u);
            }
        }
        return profesori;
    }

    private void afiseazaTop(String titlu, List<Cursant> cursanti) {
        System.out.println("\n=== " + titlu + " ===");
        if (cursanti.isEmpty()) {
            System.out.println("Nu exista cursanti inregistrati.");
            return;
        }
        int loc = 1;
        for (Cursant c : cursanti) {
            System.out.println(loc + ". " + c.getNume() + " " + c.getPrenume() + " | Quizuri completate: " + c.getNrQuizuriDistincte() + " | Punctaj mediu: " + c.getPunctajMediu());
            loc++;
        }
    }

    public void listeazaCursanti(){
        List<Cursant> cursanti = extrageCursanti();
        System.out.println("\n=== Lista cursanti inregistrati ===");
        if (cursanti.isEmpty()) {
            System.out.println("Nu exista cursanti inregistrati.");
            return;
        }
        for (Cursant c : cursanti) {
            System.out.println("- " + c.getNume() + " " + c.getPrenume() + " | Email: " + c.getEmail());
        }
    }

    public void listeazaProfesori() {
        List<Profesor> profesori = extrageProfesori();
        System.out.println("\n=== Lista profesori inregistrati ===");
        if (profesori.isEmpty()) {
            System.out.println("Nu exista profesori inregistrati.");
            return;
        }
        for (Profesor p : profesori) {
            System.out.println("- " + p.getNume() + " " + p.getPrenume() + " | Email: " + p.getEmail() + " | Departament: " + p.getDepartament());
        }
    }

    public void cautaUtilizatorDupaNume(String termenCautare) throws EntitateNegasitaException {
        boolean gasit = false;
        String termen = termenCautare.toLowerCase();

        for (Utilizator u : utilizatori.values()) {
            String numeComplet = (u.getPrenume() + " " + u.getNume()).toLowerCase();
            String numeCompletInvers = (u.getNume() + " " + u.getPrenume()).toLowerCase();

            if (numeComplet.contains(termen) || numeCompletInvers.contains(termen)) {
                System.out.println();
                u.afiseazaDashboard();
                gasit = true;
            }
        }

        if (!gasit) {
            throw new EntitateNegasitaException("Nu a fost gasit niciun utilizator care sa corespunda cautarii.");
        }
    }

    public Utilizator getUtilizatorByEmail(String email) {
        return utilizatori.get(email);
    }

}
