public class Laptop implements Cargable {
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
        if (this == obj) return true;
        if (!(obj instanceof Laptop)) return false;
        Laptop otro = (Laptop) obj;
        return this.marca.equals(otro.marca) && Double.compare(this.consumoVatios, otro.consumoVatios) == 0;
    }

    @Override
    public String toString() {
        return "Laptop [Marca: " + marca + " | Consumo: " + consumoVatios + "W | Bateria: " + nivelBateria + "%]";
    }
}
