package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.NivelDificultate;
import com.pao.project.Platforma_e_learning.model.Quiz;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class QuizRepository implements Repository<Quiz, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Quiz mapRow(ResultSet rs) throws SQLException {
        Quiz q = new Quiz(
                rs.getString("titlu"),
                NivelDificultate.valueOf(rs.getString("dificultate"))
        );
        q.setId(rs.getLong("id"));
        return q;
    }

    @Override
    public void save(Quiz quiz) throws SQLException {
        String sql = "INSERT INTO quiz (titlu, dificultate, curs_id) VALUES (?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, quiz.getTitlu());
            ps.setString(2, quiz.getDificultate().name());
            ps.setLong(3, quiz.getCursId());
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) quiz.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Quiz> findById(Long id) throws SQLException {
        String sql = "SELECT id, titlu, dificultate FROM quiz WHERE id = ?";
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
    public List<Quiz> findAll() throws SQLException {
        String sql = "SELECT id, titlu, dificultate FROM quiz ORDER BY id";
        List<Quiz> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Quiz> findByCursId(long cursId) throws SQLException {
        String sql = "SELECT id, titlu, dificultate FROM quiz WHERE curs_id = ? ORDER BY id";
        List<Quiz> list = new ArrayList<>();
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
    public void update(Quiz quiz) throws SQLException {
        String sql = "UPDATE quiz SET titlu=?, dificultate=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, quiz.getTitlu());
            ps.setString(2, quiz.getDificultate().name());
            ps.setLong(3, quiz.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM quiz WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }
}