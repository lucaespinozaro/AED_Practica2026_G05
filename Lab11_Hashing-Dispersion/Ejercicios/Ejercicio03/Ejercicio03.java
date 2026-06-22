/**
 * Ejercicio 3: Tabla hash abierta con colisiones multiples.
 * Tabla de tamano 7, h(k) = k % 7. No usa java.util: HashO usa ListLinked propia.
 */
public class Ejercicio03 {
    public static void main(String[] args) {
        System.out.println("=== Ejercicio 3: Tabla hash abierta con colisiones multiples ===\n");

        HashO<String> hashO = new HashO<>(7);

        int[] keys = {10, 17, 24, 31, 5, 12};
        String[] names = {"Juan", "Ana", "Luis", "Rosa", "Pedro", "Carla"};

        System.out.println("Insertando registros (h(k) = k % 7):");
        for (int i = 0; i < keys.length; i++) {
            int index = keys[i] % 7;
            System.out.println("  Insertar(" + keys[i] + ", \"" + names[i] + "\") -> indice " + index);
            hashO.insert(new Register<>(keys[i], names[i]));
        }

        System.out.println("\nColisiones detectadas:");
        System.out.println("  Indice 3: 10, 17, 24 y 31 colisionan (los 4 valores son % 7 = 3)");
        System.out.println("  Indice 5: 5 y 12 colisionan (ambos son % 7 = 5)");
        System.out.println("  El resto de indices (0,1,2,4,6) quedan vacios.");

        System.out.println("\n--- Estado final de la tabla hash abierta ---");
        hashO.printTable();

        System.out.println("\n1) Buscando clave 24...");
        Register<String> found = hashO.search(24);
        int[] location = hashO.locate(24);
        if (found != null && location != null) {
            System.out.println("  Encontrado: " + found);
            System.out.println("  -> Se encuentra en el indice " + location[0] +
                    " de la tabla, en el nodo numero " + (location[1] + 1) +
                    " de su lista enlazada (posicion " + location[1] + " si se cuenta desde 0).");
        } else {
            System.out.println("  No encontrado");
        }

        System.out.println("\n2) Eliminando clave 17...");
        hashO.delete(17);

        System.out.println("\n--- Tabla hash abierta despues de la eliminacion ---");
        hashO.printTable();

        int remaining = hashO.chainSize(17 % 7);
        System.out.println("\nLa cadena del indice " + (17 % 7) + " ahora tiene " +
                remaining + " nodo(s) restante(s) (10, 24 y 31).");
    }
}
