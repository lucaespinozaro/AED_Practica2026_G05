public class TestHashO {
    public static void main(String[] args) {
        HashO<String> hashO = new HashO<>(7);

        hashO.insert(new Register<>(10, "Juan"));
        hashO.insert(new Register<>(17, "Ana"));
        hashO.insert(new Register<>(24, "Luis"));
        hashO.insert(new Register<>(31, "Rosa"));
        hashO.insert(new Register<>(5, "Pedro"));
        hashO.insert(new Register<>(12, "Carla"));

        System.out.println("--- Tabla Hash Abierta ---");
        hashO.printTable();

        System.out.println("\nBuscando clave 24...");
        Register<String> found = hashO.search(24);
        if (found != null) {
            System.out.println("Encontrado: " + found);
        } else {
            System.out.println("No encontrado");
        }

        System.out.println("\nEliminando clave 17...");
        hashO.delete(17);

        System.out.println("\n--- Tabla Hash Abierta despues de la eliminacion ---");
        hashO.printTable();
    }
}
