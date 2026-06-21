public class Ejercicio2 {
    public static void main(String[] args) {
        int[] values = {10, 17, 24, 31, 4};

        System.out.println("=== Sondeo Lineal ===");
        HashC<String> hashLinear = new HashC<>(7);
        for (int val : values) {
            int probes = hashLinear.insert(new Register<>(val, String.valueOf(val)), false);
            System.out.println("Valor " + val + " -> Posiciones adicionales exploradas: " + probes);
        }

        System.out.println("\nEstado final Tabla Lineal:");
        hashLinear.printTable();

        System.out.println("\n=== Sondeo Cuadratico ===");
        HashC<String> hashQuad = new HashC<>(7);
        for (int val : values) {
            int probes = hashQuad.insert(new Register<>(val, String.valueOf(val)), true);
            System.out.println("Valor " + val + " -> Posiciones adicionales exploradas: " + probes);
        }

        System.out.println("\nEstado final Tabla Cuadratica:");
        hashQuad.printTable();
    }
}
