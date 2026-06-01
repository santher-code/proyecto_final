package model;

// HERENCIA: Guerrero ES UN Personaje
// extends = hereda todos los atributos y métodos de Personaje
public class Guerrero extends Personaje {

    // Constructor — solo recibe nombre, lo demás es fijo para el Guerrero
    public Guerrero(String nombre) {
        // super() llama al constructor del padre (Personaje)
        // le pasa: nombre, vida=120, daño=25, rol="Guerrero"
        super(nombre, 120, 25, "Guerrero");
    }

    // POLIMORFISMO: llena el espacio abstracto del padre
    // @Override avisa a Java que estamos reemplazando el método del padre
    // si escribes mal el nombre, Java te grita error
    @Override
    public String habilidadEspecial() {
        // getNombre() y getDaño() vienen heredados de Personaje
        return getNombre() + " usa CORTE SUPREMO y hace " + getDaño() * 2 + " de daño!";
    }
}
