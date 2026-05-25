package com.pao.project.Platforma_e_learning.service;


import com.pao.project.Platforma_e_learning.model.*;
import com.pao.project.Platforma_e_learning.repository.*;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizServiceBD {
    private static QuizServiceBD instance;
    private final QuizRepository quizRepo = new QuizRepository();
    private final IntrebareRepository intrebareRepo = new IntrebareRepository();
    private final VariantaRepository variantaRepo = new VariantaRepository();
    private final UtilizatorRepository utilizatorRepo = new UtilizatorRepository();
    private final RezultatRepository rezultatRepo = new RezultatRepository();

    private QuizServiceBD() {};

    public static QuizServiceBD getInstance(){
        if (instance == null)
            instance = new QuizServiceBD();
        return instance;
    }

    // =========================================================
    // CRUD Quiz
    // =========================================================

    public long adaugaQuiz(String titlu, NivelDificultate dificultate, long cursId) throws SQLException, IOException {
        Quiz q = new Quiz(titlu, dificultate);
        q.setCursId(cursId);
        quizRepo.save(q);
        System.out.println("Quiz adaugat cu succes. ID=" + q.getId());
        return q.getId();
    }

    public void stergeQuiz(long quizId) throws SQLException, IOException {
        if (quizRepo.findById(quizId).isEmpty())
            throw new SQLException("Quiz-ul cu id=" + quizId + " nu exista.");
        quizRepo.delete(quizId);
        System.out.println("Quiz sters cu succes. ID=" + quizId);
    }

    public List<Quiz> quizuriCurs(long cursId) throws SQLException, IOException{
        return quizRepo.findByCursId(cursId);
    }

    public Optional<Quiz> getQuizById(long id) throws SQLException, IOException {
        return quizRepo.findById(id);
    }

    // =========================================================
    // Intrebari si Variante
    // =========================================================

    public long adaugaIntrebare(String text, int punctaj, long quizId) throws SQLException, IOException {
        if (quizRepo.findById(quizId).isEmpty())
            throw new SQLException("Quiz-ul cu id=" + quizId + " nu exista.");
        Intrebare i = new Intrebare(text, punctaj);
        i.setQuizId(quizId);
        intrebareRepo.save(i);
        return i.getId();
    }

    public long adaugaVarianta(String text, boolean corecta, long intrebareId) throws SQLException, IOException {
        Varianta v = new Varianta(text, corecta);
        v.setIntrebareId(intrebareId);
        variantaRepo.save(v);
        System.out.println("Varianta adaugata cu succes. ID=" + v.getId());
        return v.getId();
    }

    public List<Intrebare> intrebariQuiz(long quizId) throws SQLException, IOException {
        return intrebareRepo.findByQuizId(quizId);
    }

    public List<Varianta> varianteIntrebare(long intrebareId) throws SQLException, IOException {
        return variantaRepo.findByIntrebareId(intrebareId);
    }

    // =========================================================
    // Rezultate
    // =========================================================

    public void inregistreazaRezultat(long cursandId, long quizId, double valoare) throws SQLException, IOException {
        if (quizRepo.findById(quizId).isEmpty())
            throw new SQLException("Quiz-ul cu id=" + quizId + " nu exista.");
        if (utilizatorRepo.findById(cursandId).isEmpty())
            throw new SQLException("Cursantul cu id=" + cursandId + " nu exista.");

        Rezultat r = new Rezultat(cursandId, quizId, valoare, LocalDate.now().toString());
        rezultatRepo.save(r);
        System.out.println("Rezultat inregistrat cu succes.");
    }

    public List<Rezultat> rezultateCursant(long cursantId) throws SQLException, IOException {
        return rezultatRepo.findByCursantId(cursantId);
    }

    public List<String> getRezultateDetaliate() throws SQLException, IOException {
        String sql = """
                SELECT u.nume as cursant_nume, u.prenume as cursant_prenume, q.titlu as quiz_titlu, c.nume as curs_nume, r.valoare
                FROM rezultat r
                JOIN utilizator u on r.cursant_id = u.id
                JOIN quiz q on r.quiz_id = q.id
                JOIN curs c ON q.curs_id = c.id
                ORDER by u.nume, r.valoare DESC
                """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance()
                .getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                rezultate.add(rs.getString("cursant_nume") + " " +
                        rs.getString("cursant_prenume") + " | " +
                        rs.getString("quiz_titlu") + " (" +
                        rs.getString("curs_nume") + ") | " +
                        "punctaj: " + rs.getDouble("valoare"));
            }
        }
        return rezultate;
    }
}
