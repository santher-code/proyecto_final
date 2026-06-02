import view.Vista;
import db.Conexion;
import controller.PersonajeController;

// Punto de entrada del programa
// solo crea los objetos y arranca el Controller
public class Main {
    public static void main(String[] args) {

        // 1. crea la Vista
        Vista vista = new Vista();

        // 2. crea la Conexion con MySQL
        Conexion conexion = new Conexion();

        // 3. crea el Controller pasándole Vista y Conexion (composición)
        PersonajeController controller = new PersonajeController(vista, conexion);

        // 4. arranca el programa
        controller.ejecutar();

        // 5. cierra la conexión al salir
        conexion.cerrar();
    }
}
