/**
 * Ejercicio 5: Factor de carga y redimensionamiento de tabla hash.
 * Tabla inicial de tamano 7, sondeo lineal, h(x) = x % 7.
 */
public class Ejercicio5 {
    public static void main(String[] args) {
        System.out.println("=== Ejercicio 5: Factor de carga y redimensionamiento ===\n");

        HashCerradoDinamico hash = new HashCerradoDinamico(7);
        int[] values = {2, 9, 16, 23, 4, 11};

        for (int v : values) {
            System.out.println("Insertando " + v + " (h(" + v + ") = " + (v % 7) + ")...");
            hash.insert(v);
            System.out.printf("  Factor de carga actual: %d/%d = %.3f%n",
                    hash.getCount(), hash.getSize(), hash.loadFactor());
        }

        System.out.println("\n--- Estado final de la tabla (tamano " + hash.getSize() + ") ---");
        hash.printTable();

        System.out.println("\nPor que cambian las posiciones tras el rehashing?");
        System.out.println("  Porque la funcion hash depende directamente del tamano de la tabla");
        System.out.println("  (h(x) = x % M). Al pasar M de 7 a 17, el residuo de cada clave cambia,");
        System.out.println("  asi que cada elemento se redistribuye a una nueva posicion. En este caso");
        System.out.println("  ademas las 6 claves quedan sin colisiones en la tabla de tamano 17.");
    }
}
