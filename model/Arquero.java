package model;

// HERENCIA: Arquero ES UN Personaje
public class Arquero extends Personaje {

    // Arquero es equilibrado: vida=100, daño=20
    public Arquero(String nombre) {
        super(nombre, 100, 20, "Arquero");
    }

    // POLIMORFISMO: su propia versión de habilidadEspecial()
    @Override
    public String habilidadEspecial() {
        return getNombre() + " usa FLECHA DIVINA y hace " + getDaño() * 2 + " de daño!";
    }
}
