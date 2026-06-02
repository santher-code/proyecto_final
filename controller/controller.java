package controller;

import model.*;
import view.Vista;
import db.Conexion;
import java.sql.*;

public class PersonajeController {

    private Vista    vista;
    private Conexion conexion;

    public PersonajeController(Vista vista, Conexion conexion) {
        this.vista    = vista;
        this.conexion = conexion;
    }

    // ── EJECUTAR ──────────────────────────────────────────────────────────────
    public void ejecutar() {
        int op = -1;
        while (op != 0) {
            op = vista.mostrarMenu();
            switch (op) {
                case 1: crearPersonaje();      break;
                case 2: listarPersonajes();    break;  // ← NUEVO
                case 3: buscarPorNombre();     break;  // ← busca por nombre
                case 4: actualizarNivel();     break;  // ← NUEVO
                case 5: eliminarPersonaje();   break;
                case 6: combate();             break;
                case 0: vista.mostrarMensaje("¡Hasta luego!"); break;
                default: vista.mostrarMensaje("Opción no válida");
            }
        }
    }

    // ── CREAR PERSONAJE ───────────────────────────────────────────────────────
    public void crearPersonaje() {
        String nombre = vista.pedirNombre();
        int    rol    = vista.pedirRol();

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

        try {
            String sql = "INSERT INTO personaje (nombre, rol, nivel, vida, daño, exp) "
                       + "VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, p.getNombre());
            ps.setString(2, p.getRol());
            ps.setInt(3,    p.getNivel());
            ps.setInt(4,    p.getVida());
            ps.setInt(5,    p.getDaño());
            ps.setInt(6,    p.getExp());
            ps.executeUpdate();

            vista.mostrarMensaje("¡Personaje " + p.getNombre() + " creado!");
            vista.mostrarMensaje(p.habilidadEspecial());

        } catch (SQLException e) {
            vista.mostrarMensaje("Error creando personaje: " + e.getMessage());
        }
    }

    // ── LISTAR TODOS ──────────────────────────────────────────────────────────
    // SELECT sin WHERE — trae todos los personajes
    public void listarPersonajes() {
        try {
            String sql = "SELECT * FROM personaje";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ResultSet rs = ps.executeQuery();

            vista.mostrarMensaje("\n====== TODOS LOS PERSONAJES ======");
            boolean hayPersonajes = false;

            while (rs.next()) {  // recorre cada fila del resultado
                hayPersonajes = true;
                vista.mostrarMensaje("─────────────────────────────────");
                vista.mostrarMensaje("ID:     " + rs.getInt("id_personaje"));
                vista.mostrarMensaje("Nombre: " + rs.getString("nombre"));
                vista.mostrarMensaje("Rol:    " + rs.getString("rol"));
                vista.mostrarMensaje("Nivel:  " + rs.getInt("nivel"));
                vista.mostrarMensaje("Vida:   " + rs.getInt("vida"));
                vista.mostrarMensaje("Daño:   " + rs.getInt("daño"));
                vista.mostrarMensaje("Exp:    " + rs.getInt("exp") + "/100");
            }

            if (!hayPersonajes) {
                vista.mostrarMensaje("No hay personajes registrados");
            }

        } catch (SQLException e) {
            vista.mostrarMensaje("Error listando personajes: " + e.getMessage());
        }
    }

    // ── BUSCAR POR NOMBRE ─────────────────────────────────────────────────────
    public void buscarPorNombre() {
        String nombre = vista.pedirNombre();

        try {
            String sql = "SELECT * FROM personaje WHERE nombre = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setString(1, nombre);  // reemplaza el ?
            ResultSet rs = ps.executeQuery();

            if (rs.next()) {
                vista.mostrarMensaje("\n====== PERSONAJE ENCONTRADO ======");
                vista.mostrarMensaje("ID:     " + rs.getInt("id_personaje"));
                vista.mostrarMensaje("Nombre: " + rs.getString("nombre"));
                vista.mostrarMensaje("Rol:    " + rs.getString("rol"));
                vista.mostrarMensaje("Nivel:  " + rs.getInt("nivel"));
                vista.mostrarMensaje("Vida:   " + rs.getInt("vida"));
                vista.mostrarMensaje("Daño:   " + rs.getInt("daño"));
                vista.mostrarMensaje("Exp:    " + rs.getInt("exp") + "/100");
            } else {
                vista.mostrarMensaje("No existe personaje con nombre: " + nombre);
            }

        } catch (SQLException e) {
            vista.mostrarMensaje("Error buscando personaje: " + e.getMessage());
        }
    }

    // ── ACTUALIZAR NIVEL ──────────────────────────────────────────────────────
    // UPDATE directo — sube el nivel en MySQL
    public void actualizarNivel() {
        vista.mostrarMensaje("¿A qué personaje quieres subir el nivel?");
        int id = vista.pedirId();

        try {
            // verifica que el personaje existe
            String sqlBuscar = "SELECT nombre FROM personaje WHERE id_personaje = ?";
            PreparedStatement psBuscar = conexion.getConexion().prepareStatement(sqlBuscar);
            psBuscar.setInt(1, id);
            ResultSet rs = psBuscar.executeQuery();

            if (!rs.next()) {
                vista.mostrarMensaje("No existe personaje con ID: " + id);
                return;
            }

            String nombre = rs.getString("nombre");

            // UPDATE: sube nivel +1, vida +15, daño +20
            String sql = "UPDATE personaje SET nivel = nivel + 1, "
                       + "vida = vida + 15, daño = daño + 20 "
                       + "WHERE id_personaje = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            ps.executeUpdate();

            vista.mostrarMensaje("¡" + nombre + " subió de nivel!");
            vista.mostrarMensaje("Vida +15, Daño +20");

        } catch (SQLException e) {
            vista.mostrarMensaje("Error actualizando nivel: " + e.getMessage());
        }
    }

    // ── ELIMINAR PERSONAJE ────────────────────────────────────────────────────
    public void eliminarPersonaje() {
        int id = vista.pedirId();

        try {
            String sql = "DELETE FROM personaje WHERE id_personaje = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            int filas = ps.executeUpdate();

            if (filas > 0) {
                vista.mostrarMensaje("Personaje eliminado exitosamente");
            } else {
                vista.mostrarMensaje("No existe personaje con ID: " + id);
            }

        } catch (SQLException e) {
            vista.mostrarMensaje("Error eliminando personaje: " + e.getMessage());
        }
    }

    // ── COMBATE SIMULADO ──────────────────────────────────────────────────────
    public void combate() {
        vista.mostrarMensaje("¿Qué personaje va a combatir?");
        int id = vista.pedirId();

        try {
            String sql = "SELECT * FROM personaje WHERE id_personaje = ?";
            PreparedStatement ps = conexion.getConexion().prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();

            if (!rs.next()) {
                vista.mostrarMensaje("Personaje no encontrado");
                return;
            }

            // POLIMORFISMO: crea el hijo correcto según el rol
            String rol    = rs.getString("rol");
            String nombre = rs.getString("nombre");

            Personaje p;
            switch (rol) {
                case "Guerrero": p = new Guerrero(nombre); break;
                case "Mago":     p = new Mago(nombre);     break;
                default:         p = new Arquero(nombre);  break;
            }

            // sincroniza los stats con MySQL
            p.setVida(rs.getInt("vida"));
            p.setDaño(rs.getInt("daño"));
            p.setNivel(rs.getInt("nivel"));
            p.setExp(rs.getInt("exp"));

            // simula el combate
            int vidaMob = 45;
            vista.mostrarMensaje("\n====== COMBATE INICIADO ======");
            vista.mostrarMensaje("MOB aparece con " + vidaMob + " de vida!");

            while (vidaMob > 0) {
                vidaMob -= 15;
                vista.mostrarMensaje(p.getNombre() + " ataca! MOB vida: "
                                   + Math.max(vidaMob, 0));
            }

            vista.mostrarMensaje("¡MOB derrotado!");
            p.ganarExperiencia(15);

            // actualiza en MySQL
            String update = "UPDATE personaje SET nivel=?, vida=?, daño=?, exp=? "
                          + "WHERE id_personaje=?";
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

