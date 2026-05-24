package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class InscriereRepository {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    public void save(long cursantId, long cursId) throws SQLException {
        String sql = "INSERT INTO inscriere (cursant_id, curs_id) VALUES (?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            ps.setLong(2, cursId);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public void delete(long cursantId, long cursId) throws SQLException {
        String sql = "DELETE FROM inscriere WHERE cursant_id=? AND curs_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            ps.setLong(2, cursId);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public boolean exists(long cursantId, long cursId) throws SQLException {
        String sql = "SELECT 1 FROM inscriere WHERE cursant_id=? AND curs_id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            ps.setLong(2, cursId);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    public List<Long> findCursuriIdByCursant(long cursantId) throws SQLException {
        String sql = "SELECT curs_id FROM inscriere WHERE cursant_id=?";
        List<Long> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, cursantId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(rs.getLong("curs_id"));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }
}