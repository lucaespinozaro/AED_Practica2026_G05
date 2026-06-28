public class Ejercicio02 {
    public static void main(String[] args) {
        int[] values = {10, 17, 24, 31, 4};
        
        System.out.println("================ Prueba 1: Sondeo Lineal ================");
        HashC<String> linearTable = new HashC<>(7);
        for (int val : values) {
            int probes = linearTable.insert(new Register<>(val, "Val" + val), false);
            System.out.println("Insertado: " + val + " | Posiciones exploradas ante colision: " + probes);
        }
        System.out.println("\nEstado final de la tabla (Lineal):");
        linearTable.printTable();

        System.out.println("================ Prueba 2: Sondeo Cuadratico ================");
        HashC<String> quadraticTable = new HashC<>(7);
        for (int val : values) {
            int probes = quadraticTable.insert(new Register<>(val, "Val" + val), true);
            System.out.println("Insertado: " + val + " | Posiciones exploradas ante colision: " + probes);
        }
        System.out.println("\nEstado final de la tabla (Cuadratico):");
        quadraticTable.printTable();
    }
}
