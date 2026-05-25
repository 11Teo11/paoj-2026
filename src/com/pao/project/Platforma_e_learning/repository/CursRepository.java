package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.Curs;
import com.pao.project.Platforma_e_learning.model.Profesor;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class CursRepository implements Repository<Curs, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Curs mapRow(ResultSet rs) throws SQLException {
        Profesor p = new Profesor(
                rs.getString("prof_prenume"),
                rs.getString("prof_nume"),
                rs.getString("profesor_email"),
                ""
        );
        p.setId(rs.getLong("profesor_id"));
        Curs c = new Curs(rs.getString("nume"), p);
        c.setId(rs.getLong("id"));
        return c;
    }

    @Override
    public void save(Curs curs) throws SQLException {
        String sql = "INSERT INTO curs (nume, profesor_id) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, curs.getNume());
            ps.setLong(2, curs.getProfesor().getId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) curs.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Curs> findById(Long id) throws SQLException {
        String sql = "SELECT c.id, c.nume, c.profesor_id, u.email as profesor_email, " +
                "u.nume as prof_nume, u.prenume as prof_prenume " +
                "FROM curs c JOIN utilizator u ON c.profesor_id = u.id " +
                "WHERE c.id = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public List<Curs> findByProfesorId(long profesorId) throws SQLException {
        String sql = "SELECT c.id, c.nume, c.profesor_id, u.email as profesor_email, " +
                "u.nume as prof_nume, u.prenume as prof_prenume " +
                "FROM curs c JOIN utilizator u ON c.profesor_id = u.id " +
                "WHERE c.profesor_id = ? ORDER BY c.nume";
        List<Curs> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, profesorId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Curs> findByCursantId(long cursantId) throws SQLException {
        String sql = "SELECT c.id, c.nume, c.profesor_id, u.email as profesor_email, " +
                "u.nume as prof_nume, u.prenume as prof_prenume " +
                "FROM curs c " +
                "JOIN utilizator u ON c.profesor_id = u.id " +
                "JOIN inscriere i ON i.curs_id = c.id " +
                "WHERE i.cursant_id = ? ORDER BY c.nume";
        List<Curs> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public List<Curs> findAll() throws SQLException {
        String sql = "SELECT c.id, c.nume, c.profesor_id, u.email as profesor_email, " +
                "u.nume as prof_nume, u.prenume as prof_prenume " +
                "FROM curs c JOIN utilizator u ON c.profesor_id = u.id " +
                "ORDER BY c.id";
        List<Curs> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Curs curs) throws SQLException {
        String sql = "UPDATE curs SET nume=?, profesor_id=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, curs.getNume());
            ps.setLong(2, curs.getProfesor().getId());
            ps.setLong(3, curs.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM curs WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}