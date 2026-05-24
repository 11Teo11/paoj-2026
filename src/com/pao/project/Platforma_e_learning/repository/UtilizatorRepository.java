package com.pao.project.Platforma_e_learning.repository;

import com.pao.project.Platforma_e_learning.model.*;
import com.pao.project.Platforma_e_learning.util.DatabaseConnection;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class UtilizatorRepository implements Repository<Utilizator, Long> {

    private Connection getConn() throws SQLException, IOException {
        return DatabaseConnection.getInstance().getConnection();
    }

    private Utilizator mapRow(ResultSet rs) throws SQLException {
        String tip = rs.getString("tip");
        if (tip.equals("PROFESOR")) {
            Profesor p = new Profesor(
                    rs.getString("prenume"),
                    rs.getString("nume"),
                    rs.getString("email"),
                    rs.getString("departament")
            );
            p.setId(rs.getLong("id"));
            return p;
        } else {
            Cursant c = new Cursant(
                    rs.getString("prenume"),
                    rs.getString("nume"),
                    rs.getString("email")
            );
            c.setId(rs.getLong("id"));
            return c;
        }
    }

    @Override
    public void save(Utilizator u) throws SQLException {
        String sql = "INSERT INTO utilizator (prenume, nume, email, tip, departament) VALUES (?, ?, ?, ?, ?)";
        try (PreparedStatement ps = getConn().prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, u.getPrenume());
            ps.setString(2, u.getNume());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRol().toUpperCase());
            ps.setString(5, u instanceof Profesor ? ((Profesor) u).getDepartament() : null);
            ps.executeUpdate();
            try (ResultSet keys = ps.getGeneratedKeys()) {
                if (keys.next()) u.setId(keys.getLong(1));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public Optional<Utilizator> findById(Long id) throws SQLException {
        String sql = "SELECT * FROM utilizator WHERE id = ?";
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

    public Optional<Utilizator> findByEmail(String email) throws SQLException {
        String sql = "SELECT * FROM utilizator WHERE email = ?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, email);
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) return Optional.of(mapRow(rs));
                return Optional.empty();
            }
        }
        catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public List<Utilizator> findAll() throws SQLException {
        String sql = "SELECT * FROM utilizator ORDER BY id";
        List<Utilizator> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) list.add(mapRow(rs));
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Utilizator> findByName(String nume, String prenume) throws SQLException {
        String sql = "SELECT * FROM utilizator WHERE nume = ? AND prenume = ? ORDER BY email";
        List<Utilizator> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)){
            ps.setString(1, nume);
            ps.setString(2, prenume);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    public List<Utilizator> findAllByTip(String tip) throws SQLException {
        String sql = "SELECT * FROM utilizator WHERE tip = ? ORDER BY nume";
        List<Utilizator> list = new ArrayList<>();
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, tip);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) list.add(mapRow(rs));
            }
        } catch (IOException e) {
            throw new SQLException(e);
        }
        return list;
    }

    @Override
    public void update(Utilizator u) throws SQLException {
        String sql = "UPDATE utilizator SET prenume=?, nume=?, email=?, tip=?, departament=? WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setString(1, u.getPrenume());
            ps.setString(2, u.getNume());
            ps.setString(3, u.getEmail());
            ps.setString(4, u.getRol().toUpperCase());
            ps.setString(5, u instanceof Profesor ? ((Profesor) u).getDepartament() : null);
            ps.setLong(6, u.getId());
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

    @Override
    public void delete(Long id) throws SQLException {
        String sql = "DELETE FROM utilizator WHERE id=?";
        try (PreparedStatement ps = getConn().prepareStatement(sql)) {
            ps.setLong(1, id);
            ps.executeUpdate();
        } catch (IOException e) {
            throw new SQLException(e);
        }
    }

}