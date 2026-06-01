package model;

// HERENCIA: Mago ES UN Personaje
public class Mago extends Personaje {

    // Mago es frágil pero poderoso: vida=80, daño=35
    public Mago(String nombre) {
        super(nombre, 80, 35, "Mago");
    }

    // POLIMORFISMO: su propia versión de habilidadEspecial()
    @Override
    public String habilidadEspecial() {
        return getNombre() + " usa BOLA DE FUEGO y hace " + getDaño() * 2 + " de daño!";
    }
}
