package db;

import java.sql.Connection;
import java.sql.DriverManager;

// Esta clase es el puente entre Java y MySQL
// nadie más sabe el usuario ni la contraseña
public class Conexion {

    // Datos de conexión — ajusta el PASSWORD al tuyo
    private static final String URL      = "jdbc:mysql://localhost:3306/bd_multi";
    private static final String USUARIO  = "root";
    private static final String PASSWORD = "tuContraseña";

    private Connection conexion;

    // Abre el puente con MySQL
    public Connection getConexion() {
        try {
            conexion = DriverManager.getConnection(URL, USUARIO, PASSWORD);
        } catch (Exception e) {
            System.out.println("Error conectando a MySQL: " + e.getMessage());
        }
        return conexion;
    }

    // Cierra el puente cuando ya no se necesita
    public void cerrar() {
        try {
            if (conexion != null) conexion.close();
        } catch (Exception e) {
            System.out.println("Error cerrando conexión: " + e.getMessage());
        }
    }
}
