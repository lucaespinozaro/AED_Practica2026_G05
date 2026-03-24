public class Smartphone implements Cargable {
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
        if (this == obj) return true;
        if (!(obj instanceof Smartphone)) return false;
        Smartphone otro = (Smartphone) obj;
        return this.modelo.equals(otro.modelo) && Double.compare(consumoVatios, otro.consumoVarios) == 0;
    }

    @Override
    public String toString() {
        return "Smartphone [Modelo: " + modelo + " | Consumo: " + consumoVatios + "W | Bateria: " + nivelBateria + "%]";
    }
}
