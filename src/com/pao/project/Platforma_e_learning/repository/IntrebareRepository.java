package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.Intrebare;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class IntrebareRepository implements Repository<Intrebare, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Intrebare mapRow(ResultSet rs) throws SQLException {
        Intrebare i = new Intrebare(
                rs.getString("text"),
                rs.getInt("punctaj")
        );
        i.setId(rs.getLong("id"));
        return i;
    }

    @Override
    public void save(Intrebare intrebare) throws SQLException {
        String sql = "INSERT INTO intrebare (text, punctaj, quiz_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, intrebare.getText());
            ps.setInt(2, intrebare.getPunctaj());
            ps.setLong(3, intrebare.getQuizId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) intrebare.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Intrebare> findById(Long id) throws SQLException {
        String sql = "SELECT id, text, punctaj FROM intrebare WHERE id = ?";
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
    public List<Intrebare> findAll() throws SQLException {
        String sql = "SELECT id, text, punctaj FROM intrebare ORDER BY id";
        List<Intrebare> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Intrebare> findByQuizId(long quizId) throws SQLException {
        String sql = "SELECT id, text, punctaj FROM intrebare WHERE quiz_id = ? ORDER BY id";
        List<Intrebare> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, quizId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Intrebare intrebare) throws SQLException {
        String sql = "UPDATE intrebare SET text=?, punctaj=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, intrebare.getText());
            ps.setInt(2, intrebare.getPunctaj());
            ps.setLong(3, intrebare.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM intrebare WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}