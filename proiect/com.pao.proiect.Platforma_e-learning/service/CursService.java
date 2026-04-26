package service;

import exception.DateCursInvalide;
import exception.DateUtilizatorInvalide;
import exception.EntitateNegasitaException;
import model.*;

import java.util.*;

public class CursService {
    private static CursService instance;
    private final Map<String, Curs> cursuri;

    private CursService() {
        this.cursuri = new HashMap<>();
    }

    public static CursService getInstance() {
        if (instance == null) {
            instance = new CursService();
        }
        return instance;
    }

    public void valideazaDateIntrare(String titlu, String emailProfesor) throws DateCursInvalide, EntitateNegasitaException {
        if (titlu == null || titlu.isBlank())
            throw new DateCursInvalide("Titlul cursului este obligatoriu.");

        if (emailProfesor == null || emailProfesor.isBlank() || !emailProfesor.contains("@"))
            throw new DateCursInvalide("Email-ul profesorului este invalid.");

        Utilizator u = UtilizatorService.getInstance().getUtilizatorByEmail(emailProfesor);
        if (!(u instanceof Profesor))
            throw new EntitateNegasitaException("Profesorul cu email-ul " + emailProfesor + " nu a fost gasit.");
    }

    public void adaugaCurs(String titlu, String email) throws DateCursInvalide {
        valideazaDateIntrare(titlu, email);
        if (getCurs(titlu, email) != null)
            throw new DateCursInvalide("Cursul " + titlu + " predat de profesorul cu email-ul " + email + " exista deja.");

        Profesor p = (Profesor) UtilizatorService.getInstance().getUtilizatorByEmail(email);

        Curs curs = new Curs(titlu, p);
        cursuri.put(curs.getId(), curs);
        System.out.println("Succes: Cursul \"" + titlu + "\" predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". a fost adaugat cu succes.");
    }


    public Curs getCurs(String titlu, String email){
        for (Curs c : cursuri.values()) {
            if (c.getNume().equalsIgnoreCase(titlu) && c.getProfesor().getEmail().equalsIgnoreCase(email)) {
                return c;
            }
        }
        return null;
    }

    public void adaugaLectieLaCurs(String titlu, String email, String lectie, String continut) throws DateCursInvalide {
        valideazaDateIntrare(titlu, email);
        Curs curs = getCurs(titlu, email);
        Profesor p = (Profesor) UtilizatorService.getInstance().getUtilizatorByEmail(email);
        if (curs == null) {
            throw new DateCursInvalide("Cursul " + titlu + " predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". nu a fost gasit.");
        }
        curs.adaugaLectie(new Lectie(lectie, continut));
        System.out.println("Succes: Lectia \"" + lectie + "\" a fost adaugata cu succes la cursul \"" + titlu + "\".");
    }

    public void afiseazaLectiiCurs(String titlu, String email) throws DateCursInvalide {
        valideazaDateIntrare(titlu, email);
        Curs curs = getCurs(titlu, email);
        Profesor p = (Profesor) UtilizatorService.getInstance().getUtilizatorByEmail(email);
        if (curs == null) {
            throw new DateCursInvalide("Cursul " + titlu + " predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". nu a fost gasit.");
        }

        if (curs.getLectii().isEmpty()) {
            System.out.println("   Acest curs nu are nicio lectie adaugata momentan.");
            return;
        }

        System.out.println("\n--- Lectii pentru cursul: " + titlu + " ---");
        for (Lectie l: curs.getLectii()){
            System.out.println("   - titlu: " + l.getTitlu());
            System.out.println("     continut: " + l.getContinut());
            System.out.println();
        }
    }

    public void inscrieCursant(String emailCursant, String emailProfesor, String numeCurs) throws EntitateNegasitaException{
        Utilizator u = UtilizatorService.getInstance().getUtilizatorByEmail(emailCursant);
        if (!(u instanceof Cursant c)){
            throw new EntitateNegasitaException("Cursantul cu email-ul " + emailCursant + " nu a fost gasit.");
        }
        valideazaDateIntrare(numeCurs, emailProfesor);
        Curs curs = getCurs(numeCurs, emailProfesor);
        c.inscriereCurs(curs);
        System.out.println("Succes: Cursantul " + c.getNume() + " " + c.getPrenume().charAt(0) + ". s-a inscris cu succes la cursul \"" + numeCurs + "\".");
    }

    public void listeazaCursuriCursant(String email) throws EntitateNegasitaException {
        Utilizator u = UtilizatorService.getInstance().getUtilizatorByEmail(email);
        if (!(u instanceof Cursant c)) {
            throw new EntitateNegasitaException("Cursantul cu email-ul " + email + " nu a fost gasit.");
        }
        for (Curs curs : c.getCursuriInscrise()) {
            // aici trebuia detaliul despre profesor
            System.out.println("- " + curs.getNume() + " (profesor: " +
                    curs.getProfesor().getNume() + " " +
                    curs.getProfesor().getPrenume() + ")");
        }
    }

    public void listeazaCursuriProfesor(String email) throws EntitateNegasitaException{
        Utilizator u = UtilizatorService.getInstance().getUtilizatorByEmail(email);
        if (!(u instanceof Profesor p)) {
            throw new EntitateNegasitaException("Profesorul cu email-ul " + email + " nu a fost gasit.");
        }
        System.out.println("\n--- Cursurile predate de: " + p.getNume() + " " + p.getPrenume() + " ---");
        for (Curs curs : cursuri.values()) {
            if (curs.getProfesor().getEmail().equals(email)) {
                // aici e suficient doar numele cursului
                System.out.println("- " + curs.getNume());
            }
        }
    }

    public void listeazaTopCursuri() {
        Map<Curs, Integer> nrCursanti = new HashMap<>();
        for (Curs c : cursuri.values())
            nrCursanti.put(c, 0);

        for (Cursant cursant : UtilizatorService.getInstance().extrageCursanti()) {
            for(Curs c : cursant.getCursuriInscrise())
                if (nrCursanti.containsKey(c)) {
                    nrCursanti.put(c, nrCursanti.get(c) + 1);
            }
        }

        List<Map.Entry<Curs, Integer>> listaSortata = new ArrayList<>(nrCursanti.entrySet());
        listaSortata.sort((e1, e2) -> Integer.compare(e2.getValue(), e1.getValue()));

        System.out.println("\n--- Top cursuri (dupa nr. de cursanti inscrisi) --- ");
        int loc = 1;
        for (Map.Entry<Curs, Integer> entry : listaSortata) {
            System.out.println(loc + ". " + entry.getKey().getNume() + " | inscrisi: " + entry.getValue());
            loc++;
        }
    }

    public void listeazaToateCursurile() {
        System.out.println("\n--- Toate cursurile disponibile ---");
        int cnt = 0;
        for (Curs c : cursuri.values()) {
            System.out.println(cnt + ". " + c.getNume() + " (Profesor: " + c.getProfesor().getNume() + " " + c.getProfesor().getPrenume() + ")");
            System.out.println("    - Nr. lectii: " + c.getNrLectii());
            System.out.println("    - Nr. quizuri: " + c.getNrQuizuri());
            cnt++;
        }
    }

    public void stergeCurs(String titlu, String emailProfesor) throws EntitateNegasitaException {
        valideazaDateIntrare(titlu, emailProfesor);

        Curs curs = getCurs(titlu, emailProfesor);
        Profesor p = (Profesor) UtilizatorService.getInstance().getUtilizatorByEmail(emailProfesor);

        if (curs == null) {
            throw new EntitateNegasitaException("Cursul \"" + titlu + "\" predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". nu a fost gasit.");
        }


        for (Cursant c : UtilizatorService.getInstance().extrageCursanti()) {
            c.getCursuriInscrise().remove(curs);
        }

        cursuri.remove(curs.getId());
        System.out.println("Succes: Cursul \"" + titlu + "\" predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". a fost eliminat din sistem.");
    }

    public void stergeLectie(String titluCurs, String emailProfesor, String titluLectie) throws EntitateNegasitaException {
        valideazaDateIntrare(titluCurs, emailProfesor);

        Curs curs = getCurs(titluCurs, emailProfesor);
        Profesor p = (Profesor) UtilizatorService.getInstance().getUtilizatorByEmail(emailProfesor);
        if (curs == null) {
            throw new EntitateNegasitaException("Cursul \"" + titluCurs + "\" predat de profesorul " + p.getNume() + " " + p.getPrenume().charAt(0) + ". nu a fost gasit.");
        }

        Lectie deSters = null;
        for (Lectie l : curs.getLectii()) {
            if (l.getTitlu().equalsIgnoreCase(titluLectie)) {
                deSters = l;
                break;
            }
        }

        if (deSters == null) {
            throw new EntitateNegasitaException("Lectia \"" + titluLectie + "\" nu a fost gasita la acest curs.");
        }

        curs.getLectii().remove(deSters);
        System.out.println("Succes: lectia \"" + titluLectie + "\" a fost stearsa din cursul \"" + titluCurs + "\".");
    }

}
