package view;

import java.util.Scanner;

public class Vista {

    private Scanner scanner = new Scanner(System.in);

    // menú actualizado con todas las opciones
    public int mostrarMenu() {
        System.out.println("\n====== RPG MANAGER ======");
        System.out.println("1. Crear personaje");
        System.out.println("2. Listar todos los personajes");
        System.out.println("3. Buscar personaje por nombre");
        System.out.println("4. Actualizar nivel");
        System.out.println("5. Eliminar personaje");
        System.out.println("6. Combate simulado");
        System.out.println("0. Salir");
        System.out.print("Elige una opción: ");
        return scanner.nextInt();
    }

    public int pedirRol() {
        System.out.println("Elige tu rol:");
        System.out.println("1. Guerrero (vida: 120, daño: 25)");
        System.out.println("2. Mago     (vida: 80,  daño: 35)");
        System.out.println("3. Arquero  (vida: 100, daño: 20)");
        System.out.print("Rol: ");
        return scanner.nextInt();
    }

    public String pedirNombre() {
        System.out.print("Digite el nombre del personaje: ");
        scanner.nextLine();
        return scanner.nextLine();
    }

    public int pedirId() {
        System.out.print("Digite el ID del personaje: ");
        return scanner.nextInt();
    }

    public void mostrarMensaje(String mensaje) {
        System.out.println(mensaje);
    }
}
