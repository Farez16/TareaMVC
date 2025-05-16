package tareamvc;

import MODELO.Modelo;
import VISTA.Formulario;
import CONTROLADOR.Controlador;
import Conexion.Conexion;
import java.sql.Connection;

public class TareaMVC {

    public static void main(String[] args) {
        // Paso 1: Establecer conexión a la base de datos
        Conexion conexion = new Conexion();
        Connection conn = conexion.getConexion();

        if (conn != null) {
            System.out.println("La base de datos esta conectada.");
        } else {
            System.out.println("No se pudo conectar a la base de datos.");
            return; // Salir si no hay conexión
        }

        // Paso 2: Crear modelo y pasarle la conexión
        Modelo modelo = new Modelo(conn);

        // Paso 3: Crear la vista (formulario)
        Formulario formulario = new Formulario();

        // Paso 4: Crear el controlador
        Controlador controlador = new Controlador(modelo, formulario);

        // Paso 5: Mostrar la interfaz gráfica
        formulario.setVisible(true);
    }
}


