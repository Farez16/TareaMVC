package MODELO;

import java.sql.*;

public class Modelo {

    private Connection conn;

    public Modelo(Connection conn) {
        this.conn = conn;
    }

    public boolean guardarEstudiante(String cedula, String nombre, String curso, String carrera, String sexo, String correo) {
        String sql = "INSERT INTO estudiantes (cedula, nombre, curso, carrera, sexo, correo) VALUES (?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ps.setString(2, nombre);
            ps.setString(3, curso);
            ps.setString(4, carrera);
            ps.setString(5, sexo);
            ps.setString(6, correo);
            ps.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.out.println("Error al guardar estudiante: " + e.getMessage());
            return false;
        }
    }

    public boolean cedulaExiste(String cedula) {
        String sql = "SELECT COUNT(*) FROM estudiantes WHERE cedula = ?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, cedula);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Error al verificar cédula: " + e.getMessage());
        }
        return false;
    }
// Buscar estudiante por cédula
public ResultSet buscarEstudiante(String cedula) {
    String sql = "SELECT * FROM estudiantes WHERE cedula = ?";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cedula);
        return ps.executeQuery();
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return null;
}

// Actualizar estudiante
public boolean actualizarEstudiante(String cedula, String nombre, String curso, String carrera, String sexo, String correo) {
    String sql = "UPDATE estudiantes SET nombre=?, curso=?, carrera=?, sexo=?, correo=? WHERE cedula=?";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, nombre);
        ps.setString(2, curso);
        ps.setString(3, carrera);
        ps.setString(4, sexo);
        ps.setString(5, correo);
        ps.setString(6, cedula);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}

// Eliminar estudiante
public boolean eliminarEstudiante(String cedula) {
    String sql = "DELETE FROM estudiantes WHERE cedula=?";
    try {
        PreparedStatement ps = conn.prepareStatement(sql);
        ps.setString(1, cedula);
        return ps.executeUpdate() > 0;
    } catch (SQLException e) {
        e.printStackTrace();
    }
    return false;
}
    public ResultSet obtenerEstudiantes() {
        String sql = "SELECT * FROM estudiantes";
        try {
            Statement stmt = conn.createStatement();
            return stmt.executeQuery(sql);
        } catch (SQLException e) {
            System.out.println("Error al obtener estudiantes: " + e.getMessage());
            return null;
        }
    }
}
