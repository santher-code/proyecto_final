public abstract class Personaje {

    // ── ATRIBUTOS (private = nadie los toca directo) ──
    private String nombre;
    private int vida;
    private int exp;
    private int nivel;
    private int daño;

    // ── CONSTRUCTOR ──
    public Personaje(String nombre, int vida, int daño) {
        this.nombre = nombre;
        this.vida   = vida;
        this.exp    = 0;   // siempre arranca en 0
        this.nivel  = 1;   // siempre arranca en 1
        this.daño   = daño;
    }

    // ── GETTERS ──
    public String getNombre() { return nombre; }
    public int getVida()      { return vida;   }
    public int getExp()       { return exp;    }
    public int getNivel()     { return nivel;  }
    public int getDaño()      { return daño;   }

    // ── SETTERS con validación ──
    public void setVida(int vida) {
        if (vida < 0) vida = 0;  // vida nunca negativa
        this.vida = vida;
    }

    public void setDaño(int daño) {
        if (daño < 0) return;    // daño nunca negativo
        this.daño = daño;
    }

    // ── LÓGICA DE EXPERIENCIA ──
    public void ganarExperiencia(int exp) {
        this.exp += exp;
        if (this.exp >= 100) {   // sube de nivel
            this.nivel++;
            this.vida  += 15;
            this.daño  += 20;
            this.exp    = 0;     // resetea experiencia
        }
    }

    // ── MÉTODO ABSTRACTO (las hijas lo completan) ──
    public abstract String habilidadEspecial();
}
