import java.util.ArrayList;

public class PowerStation<T extends Cargable> {
    private ArrayList<T> dispositivos;

    public PowerStation() {
        dispositivos = new ArrayList<T>();
    }

    public void conectar(T dispositivo) {
        dispositivos.add(dispositivo);
        System.out.println("Dispositivo conectado: " + dispositivo);
    }

    public double calcularConsumoTotal() {
        double total = 0;
        for (T d : dispositivos) {
            total += d.getConsumoVatios();
        }
        return total;
    }

    public int buscarDispositivo(T prototipo) {
        for (int i = 0; i < dispositivos.size(); i++) {
            if (dispositivos.get(i).equals(prototipo)) {
                return i + 1; 
            }
        }
        return -1;
    }

    public void mostrarReporte() {
        System.out.println("\n--- REPORTE DE ESTACION DE CARGA ---");
        System.out.printf("%-10s %-15s %-10s%n", "Posicion", "Consumo (W)", "Dispositivo");
        System.out.println("----------------------------------------------------");
        for (int i = 0; i < dispositivos.size(); i++) {
            T d = dispositivos.get(i);
            System.out.printf("%-10d %-15.1f %s%n", (i + 1), d.getConsumoVatios(), d);
        }
        System.out.println("----------------------------------------------------");
        System.out.printf("Consumo Total: %.1f W%n", calcularConsumoTotal());
    }
}
