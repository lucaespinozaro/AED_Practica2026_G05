public class Smartphone implements Cargable, Comparable<Smartphone> {
    private String modelo;
    private double consumoVatios;
    private int nivelBateria;

    public Smartphone(String modelo, double consumoVatios) {
        this.modelo = modelo;
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

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public void setConsumoVatios(double consumoVatios) {
        this.consumoVatios = consumoVatios;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) return false; 
        if (this == obj) return true;
        if (!(obj instanceof Smartphone)) return false;
        Smartphone otro = (Smartphone) obj;

        if (this.modelo == null || otro.modelo == null) {
            if (this.modelo != otro.modelo) return false;
        } else if (!this.modelo.equals(otro.modelo)) {
            return false;
        }
        return Double.compare(consumoVatios, otro.consumoVatios) == 0;
    }

    @Override
    public int compareTo(Smartphone o) {
        if (o == null) return 1;
        if (this.modelo == null && o.modelo == null) return 0;
        if (this.modelo == null) return -1;
        if (o.modelo == null) return 1;

        int cmp = this.modelo.compareTo(o.modelo);
        if (cmp != 0) return cmp;
        return Double.compare(this.consumoVatios, o.consumoVatios);
    }

    @Override
    public String toString() {
        return "Smartphone [Modelo: " + modelo + " | Consumo: " + consumoVatios + "W | Bateria: " + nivelBateria + "%]";
    }
}
