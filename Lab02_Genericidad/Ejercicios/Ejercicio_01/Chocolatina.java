public class Chocolatina {
    private String marca;

    public Chocolatina(String marca) {
        this.marca = marca;
    }

    public String getMarca() {
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
}
