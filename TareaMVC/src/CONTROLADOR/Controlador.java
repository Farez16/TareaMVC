package CONTROLADOR;

import MODELO.Modelo;
import VISTA.Formulario;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;

public class Controlador {

    private Modelo modelo;
    private Formulario formulario;

    public Controlador(Modelo modelo, Formulario formulario) {
        this.modelo = modelo;
        this.formulario = formulario;

        // Botón Guardar
        this.formulario.btnGuardar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String cedula = formulario.getTxtCedula().getText().trim();
                String nombre = formulario.getTxtNombre().getText().trim();
                String curso = formulario.getTxtCurso().getText().trim();
                String carrera = formulario.getComboBoxCarrera().getSelectedItem().toString();
                String sexo = formulario.getSelectedSexo();
                String correo = formulario.getTxtCorreo().getText().trim();

                if (validarCampos(cedula, nombre, curso, carrera, sexo, correo)) return;

                if (modelo.guardarEstudiante(cedula, nombre, curso, carrera, sexo, correo)) {
                    limpiarCampos();
                    JOptionPane.showMessageDialog(formulario, "Datos guardados correctamente.");
                    cargarEstudiantesEnTabla();
                    cargarEstudiantesEnTablaEliminar();
                } else {
                    JOptionPane.showMessageDialog(formulario, "Error al guardar los datos.");
                }
            }
        });

        // Botón Salir
        this.formulario.btnSalir.addActionListener(e -> formulario.dispose());

        // Cambio de pestaña
        this.formulario.getTabPanel().addChangeListener(e -> {
            int index = formulario.getTabPanel().getSelectedIndex();
            if (index == 1) {
                cargarEstudiantesEnTabla();
                cargarEstudiantesEnTablaEliminar();
            }
        });

        // Botón Buscar
        this.formulario.getBtnBuscar().addActionListener(e -> {
            String cedula = formulario.getTxtBuscar1().getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(formulario, "Ingresa una cédula para buscar.");
                return;
            }

            ResultSet rs = modelo.buscarEstudiante(cedula);
            try {
                if (rs != null && rs.next()) {
                    formulario.getTxtNombre1().setText(rs.getString("nombre"));
                    formulario.getTxtCurso1().setText(rs.getString("curso"));
                    formulario.getComboBoxCarrera1().setSelectedItem(rs.getString("carrera"));
                    formulario.setSelectedSexo1(rs.getString("sexo"));
                    formulario.getTxtCorreo1().setText(rs.getString("correo"));
                } else {
                    JOptionPane.showMessageDialog(formulario, "Estudiante no encontrado.");
                }
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(formulario, "Error al buscar estudiante: " + ex.getMessage());
            }
        });

        // Botón Editar
        this.formulario.getBtnEditar().addActionListener(e -> {
            String cedula = formulario.getTxtBuscar1().getText().trim();
            String nombre = formulario.getTxtNombre1().getText().trim();
            String curso = formulario.getTxtCurso1().getText().trim();
            String carrera = formulario.getComboBoxCarrera1().getSelectedItem().toString();
            String sexo = formulario.getSelectedSexo1();
            String correo = formulario.getTxtCorreo1().getText().trim();

            if (cedula.isEmpty() || nombre.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
                JOptionPane.showMessageDialog(formulario, "Todos los campos son obligatorios.");
                return;
            }
            if (carrera.equals("Seleccione una opción")) {
                JOptionPane.showMessageDialog(formulario, "Selecciona una carrera válida.");
                return;
            }
            if (sexo.equals("No seleccionado")) {
                JOptionPane.showMessageDialog(formulario, "Selecciona un sexo.");
                return;
            }

            if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
                JOptionPane.showMessageDialog(formulario, "Correo electrónico no válido.");
                return;
            }
            if (!nombre.matches("[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+")) {
                JOptionPane.showMessageDialog(formulario, "El nombre solo debe contener letras.");
                return;
            }
            if (!curso.matches("[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+")) {
                JOptionPane.showMessageDialog(formulario, "El curso solo debe contener letras.");
                return;
            }

            if (modelo.actualizarEstudiante(cedula, nombre, curso, carrera, sexo, correo)) {
                JOptionPane.showMessageDialog(formulario, "Estudiante actualizado correctamente.");
                formulario.limpiarCamposEditar();
                cargarEstudiantesEnTabla();
                cargarEstudiantesEnTablaEliminar();
            } else {
                JOptionPane.showMessageDialog(formulario, "No se pudo actualizar el estudiante.");
            }
        });

        // Botón Eliminar
        this.formulario.getBtnEliminar().addActionListener(e -> {
            String cedula = formulario.getTxtBuscar1().getText().trim();
            if (cedula.isEmpty()) {
                JOptionPane.showMessageDialog(formulario, "Ingresa una cédula para eliminar.");
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(formulario, "¿Estás seguro de eliminar este estudiante?", "Confirmar eliminación", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                if (modelo.eliminarEstudiante(cedula)) {
                    JOptionPane.showMessageDialog(formulario, "Estudiante eliminado correctamente.");
                    formulario.limpiarCamposEditar();
                    cargarEstudiantesEnTabla();
                    cargarEstudiantesEnTablaEliminar();
                } else {
                    JOptionPane.showMessageDialog(formulario, "No se encontró el estudiante.");
                }
            }
        });
    }

    // Métodos auxiliares

    private void limpiarCampos() {
        formulario.getTxtCedula().setText("");
        formulario.getTxtNombre().setText("");
        formulario.getTxtCurso().setText("");
        formulario.getGrupoSexo().clearSelection();
        formulario.getComboBoxCarrera().setSelectedIndex(0);
        formulario.getTxtCorreo().setText("");
    }

    private boolean validarCampos(String cedula, String nombre, String curso, String carrera, String sexo, String correo) {
        if (cedula.isEmpty() || nombre.isEmpty() || curso.isEmpty() || correo.isEmpty()) {
            JOptionPane.showMessageDialog(formulario, "Todos los campos son obligatorios.");
            return true;
        }
        if (carrera.equals("Seleccione una opción")) {
            JOptionPane.showMessageDialog(formulario, "Por favor selecciona una carrera válida.");
            return true;
        }
        if (sexo.equals("No seleccionado")) {
            JOptionPane.showMessageDialog(formulario, "Por favor selecciona un sexo.");
            return true;
        }
        if (!cedulaValidaEcuatoriana(cedula)) {
            JOptionPane.showMessageDialog(formulario, "La cédula ingresada no es válida.");
            return true;
        }
        if (modelo.cedulaExiste(cedula)) {
            JOptionPane.showMessageDialog(formulario, "La persona ya está registrada.");
            return true;
        }
        if (!nombre.matches("[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+")) {
            JOptionPane.showMessageDialog(formulario, "El nombre solo debe contener letras.");
            return true;
        }
        if (!curso.matches("[a-zA-ZÁÉÍÓÚáéíóúñÑ ]+")) {
            JOptionPane.showMessageDialog(formulario, "El curso solo debe contener letras.");
            return true;
        }
        if (!correo.matches("^[\\w.-]+@[\\w.-]+\\.[a-zA-Z]{2,}$")) {
            JOptionPane.showMessageDialog(formulario, "Correo electrónico no válido.");
            return true;
        }
        return false;
    }

    private boolean cedulaValidaEcuatoriana(String cedula) {
        if (cedula == null || !cedula.matches("\\d{10}")) return false;

        int provincia = Integer.parseInt(cedula.substring(0, 2));
        int tercerDigito = Integer.parseInt(cedula.substring(2, 3));

        if (provincia < 1 || provincia > 24 || tercerDigito >= 6) return false;

        int[] coef = {2, 1, 2, 1, 2, 1, 2, 1, 2};
        int suma = 0;

        for (int i = 0; i < 9; i++) {
            int num = Character.getNumericValue(cedula.charAt(i)) * coef[i];
            if (num >= 10) num -= 9;
            suma += num;
        }

        int digitoVerificador = (10 - (suma % 10)) % 10;
        return digitoVerificador == Character.getNumericValue(cedula.charAt(9));
    }

    public void cargarEstudiantesEnTabla() {
        DefaultTableModel modeloTabla = (DefaultTableModel) formulario.getTableReportes().getModel();
        modeloTabla.setRowCount(0);

        ResultSet rs = modelo.obtenerEstudiantes();
        try {
            while (rs != null && rs.next()) {
                Object[] fila = {
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("curso"),
                    rs.getString("carrera"),
                    rs.getString("sexo"),
                    rs.getString("correo")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar los estudiantes en la tabla: " + e.getMessage());
        }
    }

    public void cargarEstudiantesEnTablaEliminar() {
        DefaultTableModel modeloTabla = (DefaultTableModel) formulario.getTablaEliminar1().getModel();
        modeloTabla.setRowCount(0);

        ResultSet rs = modelo.obtenerEstudiantes();
        try {
            while (rs != null && rs.next()) {
                Object[] fila = {
                    rs.getString("cedula"),
                    rs.getString("nombre"),
                    rs.getString("curso"),
                    rs.getString("carrera"),
                    rs.getString("sexo"),
                    rs.getString("correo")
                };
                modeloTabla.addRow(fila);
            }
        } catch (SQLException e) {
            System.out.println("Error al cargar datos en TablaEliminar1: " + e.getMessage());
        }
    }
}
