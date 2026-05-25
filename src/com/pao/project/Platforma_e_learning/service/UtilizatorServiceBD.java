package com.pao.project.Platforma_e_learning.service;


import com.pao.project.Platforma_e_learning.model.Cursant;
import com.pao.project.Platforma_e_learning.model.Profesor;
import com.pao.project.Platforma_e_learning.model.TipUtilizator;
import com.pao.project.Platforma_e_learning.model.Utilizator;
import com.pao.project.Platforma_e_learning.repository.UtilizatorRepository;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorServiceBD {

    private static UtilizatorServiceBD instance;
    private final UtilizatorRepository repo = new UtilizatorRepository();

    private UtilizatorServiceBD() {}

    public static UtilizatorServiceBD getInstance() {
        if (instance == null)
            instance = new UtilizatorServiceBD();
        return instance;
    }


    public long inregistreazaUtilizator(String prenume, String nume, String email,
                                        TipUtilizator tip, String departament)
            throws SQLException, IOException {

        if (repo.findByEmail(email).isPresent())
            throw new SQLException("Un utilizator cu email-ul " + email + " exista deja.");

        Utilizator u;
        if (tip == TipUtilizator.PROFESOR)
            u = new Profesor(prenume, nume, email, departament);
        else
            u = new Cursant(prenume, nume, email);

        repo.save(u);
        System.out.println("Utilizator inregistrat cu succes. ID=" + u.getId());
        return u.getId();
    }

    public void stergeUtilizator(long id) throws SQLException, IOException {
        if (repo.findById(id).isEmpty()){
            throw new SQLException("Utilizatorul cu id = " + id + " nu exista.");
        }
        repo.delete(id);
    }

    public List<Utilizator> cautaDupaNume (String nume, String prenume) throws SQLException, IOException {
        return repo.findByName(nume, prenume);
    }

    public List<Utilizator> totiUtilizatorii() throws SQLException, IOException {
        return repo.findAll();
    }

    public List<Utilizator> totiCursantii() throws SQLException, IOException {
        return repo.findAllByTip("CURSANT");
    }

    public List<Utilizator> totiProfesorii() throws SQLException, IOException {
        return repo.findAllByTip("PROFESOR");
    }

    public Optional<Utilizator> getByEmail(String email) throws SQLException, IOException {
        return repo.findByEmail(email);
    }

    public List<String> topCursantiDupaPunctajMediu() throws SQLException, IOException {
        String sql = """
            SELECT u.nume, u.prenume, AVG(r.valoare) AS punctaj_mediu
            FROM utilizator u
            JOIN rezultat r ON r.cursant_id = u.id
            WHERE u.tip = 'CURSANT'
            GROUP BY u.id, u.nume, u.prenume
            ORDER BY punctaj_mediu DESC
            """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int loc = 1;
            while (rs.next()) {
                rezultate.add(loc + ". " + rs.getString("nume") + " " +
                        rs.getString("prenume") +
                        " | punctaj mediu: " + String.format("%.2f", rs.getDouble("punctaj_mediu")));
                loc++;
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return rezultate;
    }

    public List<String> topCursantiDupaNrQuizuri() throws SQLException, IOException {
        String sql = """
            SELECT u.nume, u.prenume, COUNT(r.quiz_id) AS nr_quizuri
            FROM utilizator u
            JOIN rezultat r ON r.cursant_id = u.id
            WHERE u.tip = 'CURSANT'
            GROUP BY u.id, u.nume, u.prenume
            ORDER BY nr_quizuri DESC
            """;
        List<String> rezultate = new ArrayList<>();
        try (PreparedStatement ps = DatabaseConnection.getInstance().getConnection().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            int loc = 1;
            while (rs.next()) {
                rezultate.add(loc + ". " + rs.getString("nume") + " " +
                        rs.getString("prenume") +
                        " | quizuri completate: " + rs.getLong("nr_quizuri"));
                loc++;
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return rezultate;
    }

}
