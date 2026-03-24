public class Golosina {
    private String nombre;
    private double peso;

    public Golosina(String nombre, double peso) {
        this.nombre = nombre;
        this.peso = peso;
    }

    public String getNombre() {
        return nombre;
    }

    public double getPeso() {
        return peso;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Golosina)) return false;
        Golosina g = (Golosina) obj;
        return nombre.equals(g.nombre) && Double.compare(peso, g.peso) == 0;
    }
}
