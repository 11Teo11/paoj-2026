package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.Lectie;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class LectieRepository implements Repository<Lectie, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Lectie mapRow(ResultSet rs) throws SQLException {
        Lectie l = new Lectie(
                rs.getString("titlu"),
                rs.getString("continut")
        );
        l.setId(rs.getLong("id"));
        return l;
    }

    @Override
    public void save(Lectie lectie) throws SQLException {
        String sql = "INSERT INTO lectie (titlu, continut, curs_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, lectie.getTitlu());
            ps.setString(2, lectie.getContinut());
            ps.setLong(3, lectie.getCursId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) lectie.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Lectie> findById(Long id) throws SQLException {
        String sql = "SELECT id, titlu, continut FROM lectie WHERE id = ?";
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
    public List<Lectie> findAll() throws SQLException {
        String sql = "SELECT id, titlu, continut FROM lectie ORDER BY id";
        List<Lectie> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Lectie> findByCursId(long cursId) throws SQLException {
        String sql = "SELECT id, titlu, continut FROM lectie WHERE curs_id = ? ORDER BY id";
        List<Lectie> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Lectie lectie) throws SQLException {
        String sql = "UPDATE lectie SET titlu=?, continut=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, lectie.getTitlu());
            ps.setString(2, lectie.getContinut());
            ps.setLong(3, lectie.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM lectie WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}