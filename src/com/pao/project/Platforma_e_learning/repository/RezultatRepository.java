package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.Rezultat;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class RezultatRepository implements Repository<Rezultat, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Rezultat mapRow(ResultSet rs) throws SQLException {
        return new Rezultat(
                rs.getLong("cursant_id"),
                rs.getLong("quiz_id"),
                rs.getDouble("valoare"),
                rs.getString("data")
        );
    }

    @Override
    public void save(Rezultat rezultat) throws SQLException {
        String sql = "INSERT INTO rezultat (cursant_id, quiz_id, valoare, data) VALUES (?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setLong(1, rezultat.idCursant());
            ps.setLong(2, rezultat.idQuiz());
            ps.setDouble(3, rezultat.valoare());
            ps.setString(4, rezultat.data());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Rezultat> findById(Long id) throws SQLException {
        String sql = "SELECT cursant_id, quiz_id, valoare, data FROM rezultat WHERE id = ?";
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

    @Override
    public List<Rezultat> findAll() throws SQLException {
        String sql = "SELECT cursant_id, quiz_id, valoare, data FROM rezultat ORDER BY id";
        List<Rezultat> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Rezultat> findByCursantId(long cursantId) throws SQLException {
        String sql = "SELECT cursant_id, quiz_id, valoare, data FROM rezultat WHERE cursant_id = ? ORDER BY id";
        List<Rezultat> list = new ArrayList<>();
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
    public void update(Rezultat rezultat) throws SQLException {
        throw new UnsupportedOperationException("Rezultatele nu se modifica.");
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM rezultat WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}