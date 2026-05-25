package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.Varianta;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class VariantaRepository implements Repository<Varianta, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Varianta mapRow(ResultSet rs) throws SQLException {
        Varianta v = new Varianta(
                rs.getString("text"),
                rs.getInt("corecta") == 1
        );
        v.setId(rs.getLong("id"));
        return v;
    }

    @Override
    public void save(Varianta varianta) throws SQLException {
        String sql = "INSERT INTO varianta (text, corecta, intrebare_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, varianta.getText());
            ps.setInt(2, varianta.esteCorecta() ? 1 : 0);
            ps.setLong(3, varianta.getIntrebareId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) varianta.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Varianta> findById(Long id) throws SQLException {
        String sql = "SELECT id, text, corecta FROM varianta WHERE id = ?";
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
    public List<Varianta> findAll() throws SQLException {
        String sql = "SELECT id, text, corecta FROM varianta ORDER BY id";
        List<Varianta> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Varianta> findByIntrebareId(long intrebareId) throws SQLException {
        String sql = "SELECT id, text, corecta FROM varianta WHERE intrebare_id = ? ORDER BY id";
        List<Varianta> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, intrebareId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Varianta varianta) throws SQLException {
        String sql = "UPDATE varianta SET text=?, corecta=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, varianta.getText());
            ps.setInt(2, varianta.esteCorecta() ? 1 : 0);
            ps.setLong(3, varianta.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM varianta WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}