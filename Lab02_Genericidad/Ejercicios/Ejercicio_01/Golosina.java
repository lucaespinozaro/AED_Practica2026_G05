public class Golosina implements Comparable<Golosina> {
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
        if (obj == null) return false; 
        if (this == obj) return true;
        if (!(obj instanceof Golosina)) return false;
        Golosina g = (Golosina) obj;

        if (nombre == null || g.nombre == null) {
            if (nombre != g.nombre) return false;
        } else if (!nombre.equals(g.nombre)) {
            return false;
        }
        return Double.compare(peso, g.peso) == 0;
    }

    @Override
    public int compareTo(Golosina o) {
        if (o == null) return 1;
        if (this.nombre == null && o.nombre == null) return 0;
        if (this.nombre == null) return -1;
        if (o.nombre == null) return 1;

        int cmp = this.nombre.compareTo(o.nombre);
        if (cmp != 0) return cmp;

        return Double.compare(this.peso, o.peso);
    }
}
