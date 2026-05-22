package com.pao.project.Platforma_e_learning.service;

import com.pao.project.Platforma_e_learning.exception.*;
import com.pao.project.Platforma_e_learning.model.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

public class QuizService {
    private static QuizService instance;
    private final Map<String, Quiz> quizuri;

    private QuizService(){
        this.quizuri = new HashMap<>();
    }

    public static QuizService getInstance() {
        if (instance == null) {
            instance = new QuizService();
        }
        return instance;
    }

    public void valideazaDateIntrare(String text, String mesajEroare) throws DateQuizInvalide {
        if (text == null || text.isBlank()) {
            throw new DateQuizInvalide(mesajEroare);
        }
    }

    public Quiz adaugaQuiz(String titluCurs, String emailProfesor, String titluQuiz, NivelDificultate dificultate) throws DateCursInvalide, DateQuizInvalide{
        valideazaDateIntrare(titluQuiz, "Titlul quiz-ului este obligatoriu.");

        CursService.getInstance().valideazaDateIntrare(titluCurs, emailProfesor);
        Curs c = CursService.getInstance().getCurs(titluCurs, emailProfesor);
        if (c == null)
            throw new DateCursInvalide("Cursul " + titluCurs + " predat de profesorul cu email-ul " + emailProfesor + " nu a fost gasit.");

        Quiz q = new Quiz(titluQuiz, dificultate);
        c.adaugaQuiz(q);
        quizuri.put(q.getId(), q);

        System.out.println("Succes: Quiz-ul \"" + titluQuiz + "\" a fost adaugat cu succes la cursul \"" + titluCurs + "\".");
        return q;
    }

    public void adaugaIntrebareLaQuiz(String idQuiz, Intrebare intrebare){
        valideazaDateIntrare(idQuiz, "ID-ul quiz-ului este obligatoriu.");

        Quiz quiz = quizuri.get(idQuiz);
        if (quiz == null)
            throw new DateQuizInvalide("Quiz-ul cu ID-ul furnizat nu a fost gasit.");

        if (intrebare == null)
            throw new DateQuizInvalide("Intrebarea este obligatorie.");

        quiz.adaugaIntrebare(intrebare);
        System.out.println("Succes: Intrebarea a fost adaugata cu succes la quiz");
    }

    public void inregistreazaRezultatQuiz(String emailCursant, String idQuiz, double punctaj, LocalDate dataSustinere) throws DateUtilizatorInvalide, DateRezultatInvalide, EntitateNegasitaException {
        if (punctaj < 0){
            throw  new DateRezultatInvalide("Punctajul nu poate fi negativ.");
        }

        if (emailCursant == null || emailCursant.isBlank() || !emailCursant.contains("@"))
            throw new DateUtilizatorInvalide("Email-ul cursantului este invalid.");

        Utilizator u = UtilizatorService.getInstance().getUtilizatorByEmail(emailCursant);
        if (!(u instanceof Cursant c)) {
            throw new EntitateNegasitaException("Cursantul cu email-ul " + emailCursant + " nu a fost gasit.");
        }

        valideazaDateIntrare(idQuiz, "ID-ul quiz-ului este obligatoriu.");

        Quiz quiz = quizuri.get(idQuiz);
        if (quiz == null)
            throw new DateQuizInvalide("Quiz-ul cu ID-ul furnizat nu a fost gasit.");

        double punctajCuOficiu = punctaj + 10;

        Rezultat rezultatNou = new Rezultat(c.getId(), idQuiz, punctajCuOficiu, dataSustinere);
        c.adaugaRezultat(rezultatNou);

        System.out.println("Succes: Punctajul " + punctaj + " a fost inregistrat pentru cursantul " + c.getNume() + " " + c.getPrenume().charAt(0) + " la quiz-ul \"" + quiz.getTitlu() + "\".");
    }

    public void stergeQuiz(String titluCurs, String emailProfesor, String titluQuiz) throws DateQuizInvalide {

        Curs c = CursService.getInstance().getCurs(titluCurs, emailProfesor);
        if (c == null) {
            throw new DateQuizInvalide("cursul nu exista.");
        }

        Quiz deSters = null;
        for (Quiz q : c.getQuizuri()) {
            if (q.getTitlu().equalsIgnoreCase(titluQuiz)) {
                deSters = q;
                break;
            }
        }

        if (deSters == null) {
            throw new DateQuizInvalide("testul \"" + titluQuiz + "\" nu a fost gasit la acest curs.");
        }

        c.getQuizuri().remove(deSters);
        quizuri.remove(deSters.getId());

        System.out.println("succes: quiz-ul \"" + titluQuiz + "\" a fost sters cu succes.");
    }
}