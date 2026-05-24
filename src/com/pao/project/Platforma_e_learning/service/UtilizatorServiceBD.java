package com.pao.project.Platforma_e_learning.service;


import com.pao.project.Platforma_e_learning.model.Cursant;
import com.pao.project.Platforma_e_learning.model.Profesor;
import com.pao.project.Platforma_e_learning.model.TipUtilizator;
import com.pao.project.Platforma_e_learning.model.Utilizator;
import com.pao.project.Platforma_e_learning.repository.UtilizatorRepository;

import java.io.IOException;
import java.sql.*;
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

}
