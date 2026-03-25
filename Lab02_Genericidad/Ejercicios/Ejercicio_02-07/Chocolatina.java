public class Chocolatina implements Comparable<Chocolatina> {
    private String marca;
    public Chocolatina(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
        return marca;
    }

    @Override
    public String toString() {
        return marca;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false; 
        if (this == obj) return true;
        if (!(obj instanceof Chocolatina)) return false;
        Chocolatina c = (Chocolatina) obj;

        if (marca == null || c.marca == null) {
            return marca == c.marca;
        }

        return marca.equals(c.marca);
    }

    @Override
    public int compareTo(Chocolatina o) {
        if (o == null) return 1;
        if (this.marca == null && o.marca == null) return 0;
        if (this.marca == null) return -1;
        if (o.marca == null) return 1;

        return this.marca.compareTo(o.marca);
    }
}
