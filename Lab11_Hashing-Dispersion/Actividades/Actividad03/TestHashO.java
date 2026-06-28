public class TestHash {
    public static void main(String[] args) {
        HashO<String> hashO = new HashO<>(5);

        hashO.insert(new Register<>(5, "Alpha"));
        hashO.insert(new Register<>(10, "Beta"));
        hashO.insert(new Register<>(15, "Gamma"));
        hashO.insert(new Register<>(3, "Delta"));
        hashO.insert(new Register<>(8, "Epsilon"));

        System.out.println("--- Tabla Hash Abierta inicial con colisiones ---");
        hashO.printTable();

        System.out.println("\nBuscando clave 10...");
        Register<String> result = hashO.search(10);
        if (result != null) {
            System.out.println("Encontrado: " + result);
        } else {
            System.out.println("Clave 10 no encontrada");
        }

        System.out.println("\nEliminando clave 10...");
        hashO.delete(10);

        System.out.println("\n--- Tabla Hash despues de eliminar la clave 10 ---");
        hashO.printTable();
    }
}
