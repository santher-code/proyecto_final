package controller;

import model.*;       // importa Personaje, Guerrero, Mago, Arquero
import view.Vista;
import db.Conexion;
import java.sql.*;

// CONTROLLER — orquesta Vista, Modelo y Base de Datos
// aquí vive toda la lógica y el SQL
public class PersonajeController {

    // COMPOSICIÓN: el controller TIENE una Vista y una Conexion
    private Vista     vista;
    private Conexion  conexion;

    // Constructor — recibe las dependencias
    public PersonajeController(Vista vista, Conexion conexion) {
        this.vista    = vista;
        this.conexion = conexion;
    }

    // ── EJECUTAR — el switch que decide qué hacer ─────────────────────────────
    public void ejecutar() {
        int op = -1;
        while (op != 0) {                    // repite hasta que el usuario elija salir
            op = vista.mostrarMenu();        // pide opción a la Vista

            switch (op) {
                case 1: crearPersonaje();    break;
                case 2: buscarPersonaje();   break;
                case 3: eliminarPersonaje(); break;
                case 4: combate();           break;
                case 0: vista.mostrarMensaje("¡Hasta luego!"); break;
                default: vista.mostrarMensaje("Opción no válida");
            }
        }
    }

    // ── CREAR PERSONAJE ───────────────────────────────────────────────────────
    public void crearPersonaje() {
        // 1. pide datos a la Vista
        String nombre = vista.pedirNombre();
        int    rol    = vista.pedirRol();

        // 2. crea el objeto del Modelo según el rol
        // POLIMORFISMO: variable tipo Personaje puede guardar cualquier hijo
        Personaje p = null;
        switch (rol) {
            case 1: p = new Guerrero(nombre); break;
            case 2: p = new Mago(nombre);     break;
            case 3: p = new Arquero(nombre);  break;
            default:
                vista.mostrarMensaje("Rol inválido");
                return;
        }

        // 3. guarda en MySQL con INSERT
        try {
            String sql = "INSERT INTO personaje (nombre, rol, nivel, vida, daño, exp) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";

            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, p.getNombre());  // reemplaza el primer ?
            ps.setString(2, p.getRol());     // reemplaza el segundo ?
            ps.setInt(3,    p.getNivel());   // reemplaza el tercer ?
            ps.setInt(4,    p.getVida());    // reemplaza el cuarto ?
            ps.setInt(5,    p.getDaño());    // reemplaza el quinto ?
            ps.setInt(6,    p.getExp());     // reemplaza el sexto ?
            ps.executeUpdate();              // ejecuta el INSERT

            // 4. avisa a la Vista
            vista.mostrarMensaje("¡Personaje " + p.getNombre() + " creado exitosamente!");
            vista.mostrarMensaje(p.habilidadEspecial()); // muestra su habilidad

        } catch (SQLException e) {
            vista.mostrarMensaje("Error creando personaje: " + e.getMessage());
        }
    }

    // ── BUSCAR PERSONAJE ──────────────────────────────────────────────────────
    public void buscarPersonaje() {
        // 1. pide el ID a la Vista
        int id = vista.pedirId();

        // 2. busca en MySQL con SELECT
        try {
            String sql = "SELECT * FROM personaje WHERE id_personaje = ?";

            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);                // reemplaza el ?
            ResultSet rs = ps.executeQuery(); // ejecuta y trae resultados

            // 3. muestra el resultado
            if (rs.next()) {  // si encontró el personaje
                vista.mostrarMensaje("\n====== PERSONAJE ENCONTRADO ======");
                vista.mostrarMensaje("ID:     " + rs.getInt("id_personaje"));
                vista.mostrarMensaje("Nombre: " + rs.getString("nombre"));
                vista.mostrarMensaje("Rol:    " + rs.getString("rol"));
                vista.mostrarMensaje("Nivel:  " + rs.getInt("nivel"));
                vista.mostrarMensaje("Vida:   " + rs.getInt("vida"));
                vista.mostrarMensaje("Daño:   " + rs.getInt("daño"));
                vista.mostrarMensaje("Exp:    " + rs.getInt("exp"));
            } else {
                vista.mostrarMensaje("Personaje con ID " + id + " no encontrado");
            }

        } catch (SQLException e) {
            vista.mostrarMensaje("Error buscando personaje: " + e.getMessage());
        }
    }

    // ── ELIMINAR PERSONAJE ────────────────────────────────────────────────────
    public void eliminarPersonaje() {
        // 1. pide el ID a la Vista
        int id = vista.pedirId();

        // 2. elimina en MySQL con DELETE
        try {
            String sql = "DELETE FROM personaje WHERE id_personaje = ?";

            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);        // reemplaza el ?
            int filas = ps.executeUpdate(); // ejecuta el DELETE

            // 3. avisa si encontró o no el personaje
            if (filas > 0) {
                vista.mostrarMensaje("Personaje eliminado exitosamente");
            } else {
                vista.mostrarMensaje("No existe personaje con ID " + id);
            }

        } catch (SQLException e) {
            vista.mostrarMensaje("Error eliminando personaje: " + e.getMessage());
        }
    }

    // ── COMBATE SIMULADO ──────────────────────────────────────────────────────
    public void combate() {
        // 1. pide el ID del personaje que va a combatir
        vista.mostrarMensaje("¿Qué personaje va a combatir?");
        int id = vista.pedirId();

        try {
            // 2. busca el personaje en MySQL
            String sql = "SELECT * FROM personaje WHERE id_personaje = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                vista.mostrarMensaje("Personaje no encontrado");
                return;
            }

            // 3. crea el objeto del modelo con los datos de MySQL
            String rol    = rs.getString("rol");
            String nombre = rs.getString("nombre");

            // POLIMORFISMO: crea el hijo correcto según el rol
            Personaje p;
            switch (rol) {
                case "Guerrero": p = new Guerrero(nombre); break;
                case "Mago":     p = new Mago(nombre);     break;
                default:         p = new Arquero(nombre);  break;
            }

            // sincroniza los stats con los de MySQL
            p.setVida(rs.getInt("vida"));
            p.setDaño(rs.getInt("daño"));
            p.setNivel(rs.getInt("nivel"));
            p.setExp(rs.getInt("exp"));

            // 4. simula el combate contra el MOB
            int vidaMob = 45; // el MOB tiene 45 de vida
            vista.mostrarMensaje("\n====== COMBATE INICIADO ======");
            vista.mostrarMensaje("MOB aparece con " + vidaMob + " de vida!");

            // el personaje ataca hasta que el MOB muere
            while (vidaMob > 0) {
                vidaMob -= 15;  // cada ataque hace 15 de daño
                vista.mostrarMensaje(p.getNombre() + " ataca! MOB vida: " 
                                   + Math.max(vidaMob, 0));
            }

            // 5. MOB muere → gana experiencia
            vista.mostrarMensaje("¡MOB derrotado!");
            p.ganarExperiencia(15);  // gana 15 de exp

            // 6. actualiza en MySQL con UPDATE
            String update = "UPDATE personaje SET nivel = ?, vida = ?, daño = ?, exp = ? "
                          + "WHERE id_personaje = ?";
            PreparedStatement pu = conexion.getConexion().prepareStatement(update);
            pu.setInt(1, p.getNivel());
            pu.setInt(2, p.getVida());
            pu.setInt(3, p.getDaño());
            pu.setInt(4, p.getExp());
            pu.setInt(5, id);
            pu.executeUpdate();

            vista.mostrarMensaje("Exp actual: " + p.getExp() + "/100");
            vista.mostrarMensaje("Nivel actual: " + p.getNivel());

        } catch (SQLException e) {
            vista.mostrarMensaje("Error en combate: " + e.getMessage());
        }
    }
}
