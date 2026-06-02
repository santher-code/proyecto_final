package view;

import java.util.Scanner;

// VISTA — solo muestra y recibe datos del usuario
// nunca toca la base de datos ni modifica el modelo
public class Vista {

    // Scanner = el oído de Java, escucha al usuario
    private Scanner scanner = new Scanner(System.in);

    // Muestra el menú y devuelve la opción al Controller
    public int mostrarMenu() {
        System.out.println("\n====== RPG MANAGER ======");
        System.out.println("1. Crear personaje");
        System.out.println("2. Buscar personaje");
        System.out.println("3. Eliminar personaje");
        System.out.println("4. Combate simulado");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
        return scanner.nextInt();  // devuelve el número al Controller
    }

    // Pide el rol y devuelve la opción
    public int pedirRol() {
        System.out.println("Elige tu rol:");
        System.out.println("1. Guerrero (vida: 120, daño: 25)");
        System.out.println("2. Mago     (vida: 80,  daño: 35)");
        System.out.println("3. Arquero  (vida: 100, daño: 20)");
        System.out.print("Rol: ");
        return scanner.nextInt();
    }

    // Pide el nombre y lo devuelve
    public String pedirNombre() {
        System.out.print("Digite el nombre del personaje: ");
        scanner.nextLine(); // limpia el buffer del scanner
        return scanner.nextLine();
    }

    // Pide el ID y lo devuelve
    public int pedirId() {
        System.out.print("Digite el ID del personaje: ");
        return scanner.nextInt();
    }

    // Muestra cualquier mensaje
    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
