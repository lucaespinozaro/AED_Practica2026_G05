public class Ejercicio05 {
    public static void main(String[] args) {
        HashC<String> hash = new HashC<>(7);
        int[] keys = {2, 9, 16, 23, 4, 11};

        System.out.println("============ Inserción de Valores y Registro de Load Factor ============");
        for (int key : keys) {
            hash.insert(new Register<>(key, "Val" + key), false);
            System.out.printf("Insertado: %2d | Factor de carga actual: %.3f%n", key, hash.loadFactor());
        }

        System.out.println("============ Estado de la Tabla antes del Rehashing (M - 7) ============");
        hash.printTable();

        System.out.println("\n>>> Insertando elemento para gatillar el rehashing...");
        hash.insert(new Register<>(99, "Trigger"), false);

        System.out.println("============ Estado de la Tabla despues del Rehashing (M - 17) ============");
        hash.printTable();
    }
}
