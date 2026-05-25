package com.pao.project.Platforma_e_learning.service;


import com.pao.project.Platforma_e_learning.model.Curs;
import com.pao.project.Platforma_e_learning.model.Lectie;
import com.pao.project.Platforma_e_learning.model.Profesor;
import com.pao.project.Platforma_e_learning.repository.CursRepository;
import com.pao.project.Platforma_e_learning.repository.InscriereRepository;
import com.pao.project.Platforma_e_learning.repository.LectieRepository;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.List;
import java.util.Optional;

public class CursServiceBD {
    private static CursServiceBD instance;
    private final CursRepository cursRepo = new CursRepository();
    private final LectieRepository lectieRepo = new LectieRepository();
    private final InscriereRepository inscriereRepo = new InscriereRepository();

    private CursServiceBD() {}

    public static CursServiceBD getInstance() {
        if (instance == null)
            instance = new CursServiceBD();
        return instance;
    }

    // =========================================================
    // CRUD cursuri
    // =========================================================

    public long adaugaCurs(String nume, long profesorId) throws SQLException, IOException {
        Profesor p = new Profesor("", "", "", "");
        p.setId(profesorId);
        Curs curs = new Curs(nume, p);
        cursRepo.save(curs);
        System.out.println("Curs adaugat cu succes. ID =" + curs.getId());
        return curs.getId();
    }

    public void stergeCurs(long cursId) throws SQLException, IOException {
        Connection conn = DatabaseConnection.getInstance().getConnection();
        conn.setAutoCommit(false);
        try {
            // verifica ca cursul exista
            if (cursRepo.findById(cursId).isEmpty())
                throw new SQLException("Cursul cu id=" + cursId + " nu exista.");

            // SQL 1: sterge inscrierile
            String deleteInscrieri = "DELETE FROM inscriere WHERE curs_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteInscrieri)) {
                ps.setLong(1, cursId);
                ps.executeUpdate();
            }

            // SQL 2: sterge rezultatele quizurilor din acest curs
            String deleteRezultate = """
                DELETE FROM rezultat WHERE quiz_id IN
                (SELECT id FROM quiz WHERE curs_id = ?)
                """;
            try (PreparedStatement ps = conn.prepareStatement(deleteRezultate)) {
                ps.setLong(1, cursId);
                ps.executeUpdate();
            }

            // SQL 3: sterge quizurile
            String deleteQuizuri = "DELETE FROM quiz WHERE curs_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteQuizuri)) {
                ps.setLong(1, cursId);
                ps.executeUpdate();
            }

            // SQL 4: sterge lectiile
            String deleteLectii = "DELETE FROM lectie WHERE curs_id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteLectii)) {
                ps.setLong(1, cursId);
                ps.executeUpdate();
            }

            // SQL 5: sterge cursul
            String deleteCurs = "DELETE FROM curs WHERE id = ?";
            try (PreparedStatement ps = conn.prepareStatement(deleteCurs)) {
                ps.setLong(1, cursId);
                ps.executeUpdate();
            }

            conn.commit();
            System.out.println("[TX] Curs sters cu succes. ID=" + cursId);

        } catch (SQLException e) {
            conn.rollback();
            System.out.println("[TX] Rollback stergere curs: " + e.getMessage());
            throw e;
        } finally {
            conn.setAutoCommit(true);
        }
    }

    public List<Curs> toateCursurile() throws SQLException, IOException {
        return cursRepo.findAll();
    }

    public Optional<Curs> getCursById(long id) throws SQLException, IOException {
        return cursRepo.findById(id);
    }

    // =========================================================
    // Lectii
    // =========================================================

    public void adaugaLectie(String titlu, String continut, long cursId)
            throws SQLException, IOException {
        if (cursRepo.findById(cursId).isEmpty())
            throw new SQLException("Cursul cu id=" + cursId + " nu exista.");
        Lectie l = new Lectie(titlu, continut);
        l.setCursId(cursId);
        lectieRepo.save(l);
        System.out.println("Lectie adaugata cu succes. ID= " + l.getId());
    }

    public void stergeLectie(long id) throws SQLException, IOException {
        if (lectieRepo.findById(id).isEmpty())
            throw new SQLException("Lectia cu id=" + id + " nu exista.");
        lectieRepo.delete(id);
        System.out.println("Lectie stearsa cu succes. ID=" + id);
    }

    public List<Lectie> lectiiCurs(long cursId) throws SQLException, IOException {
        return lectieRepo.findByCursId(cursId);
    }

    // =========================================================
    // Cursanti
    // =========================================================

    public void inscrieCursant(long cursantId, long cursId)
            throws SQLException, IOException {
        if (inscriereRepo.exists(cursantId, cursId))
            throw new SQLException("Cursantul este deja inscris la acest curs.");
        inscriereRepo.save(cursantId, cursId);
        System.out.println("Inscriere realizata cu succes.");
    }

    public List<String> getCursuriCursant(long cursantId) throws SQLException, IOException {
        String sql = """
                SELECT c.nume AS curs_nume, u.nume AS prof_nume, u.prenume AS prof_prenume
                FROM curs c
                JOIN utilizator u ON c.profesor_id = u.id
                JOIN inscriere i ON i.curs_id = c.id
                WHERE i.cursant_id = ?
                ORDER BY c.nume
                """;
        List<String> rezultate = new java.util.ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    rezultate.add(rs.getString("curs_nume") + " (prof. " +
                            rs.getString("prof_nume") + " " +
                            rs.getString("prof_prenume").charAt(0) + ".)");
                }
            }
        }
        return rezultate;
    }

    public List<String> topCursuriDupaCursanti() throws SQLException, IOException {
        String sql = """
                SELECT c.nume AS curs_nume, COUNT(i.cursant_id) AS nr_cursanti
                FROM curs c
                LEFT JOIN inscriere i ON i.curs_id = c.id
                GROUP BY c.id, c.nume
                ORDER BY nr_cursanti DESC
                """;
        List<String> rezultate = new java.util.ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection()
                .prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int loc = 1;
            while (rs.next()) {
                rezultate.add(loc + ". " + rs.getString("curs_nume") +
                        " | cursanti: " + rs.getLong("nr_cursanti"));
                loc++;
            }
        }
        return rezultate;
    }

    // =========================================================
    // Profesori
    // =========================================================

    public List<Curs> getCursuriProfesor(long profesorId) throws SQLException, IOException {
        return cursRepo.findByProfesorId(profesorId);
    }


}
