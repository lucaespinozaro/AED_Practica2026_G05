public class Laptop implements Cargable, Comparable<Laptop> {
    private String marca;
    private double consumoVatios;
    private int nivelBateria;

    public Laptop(String marca, double consumoVatios) {
        this.marca = marca;
        this.consumoVatios = consumoVatios;
        this.nivelBateria = 0;
    }

    @Override
    public double getConsumoVatios() {
        return consumoVatios;
    }

    @Override
    public int getNivelBateria() {
        return nivelBateria;
    }

    @Override
    public void cargar(int cantidad) {
        nivelBateria += cantidad;
        if (nivelBateria > 100) {
            nivelBateria = 100;
        }
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public void setConsumoVatios(double consumoVatios) {
        this.consumoVatios = consumoVatios;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false; // 🔧 CORRECCIÓN
        if (this == obj) return true;
        if (!(obj instanceof Laptop)) return false;
        Laptop otro = (Laptop) obj;

        if (this.marca == null || otro.marca == null) {
            if (this.marca != otro.marca) return false;
        } else if (!this.marca.equals(otro.marca)) {
            return false;
        }
        return Double.compare(consumoVatios, otro.consumoVatios) == 0; // 🔧 CORRECCIÓN nombre
    }

    @Override
    public int compareTo(Laptop o) {
        if (o == null) return 1;
        if (this.marca == null && o.marca == null) return 0;
        if (this.marca == null) return -1;
        if (o.marca == null) return 1;

        int cmp = this.marca.compareTo(o.marca);
        if (cmp != 0) return cmp;
        return Double.compare(this.consumoVatios, o.consumoVatios);
    }

    @Override
    public String toString() {
        return "Laptop [Marca: " + marca + " | Consumo: " + consumoVatios + "W | Bateria: " + nivelBateria + "%]";
    }
}
