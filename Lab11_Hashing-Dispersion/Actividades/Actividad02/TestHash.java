public class TestHash {
    public static void main(String[] args) {
        HashC<String> hashC = new HashC<>(15);
        int[] keys = {34, 3, 7, 30, 11, 8, 7, 23, 41, 16, 34};
        String[] names = {"Alpha", "Beta", "Gamma", "Delta", "Epsilon", "Zeta", "Eta", "Theta", "Iota", "Kappa", "Lambda"};

        for (int i = 0; i < keys.length; i++) {
            hashC.insert(new Register<>(keys[i], names[i]));
        }

        System.out.println("--- Tabla Hash antes de eliminar ---");
        hashC.printTable();

        System.out.println("\nEliminando clave 30...");
        hashC.delete(30);

        System.out.println("\n--- Tabla Hash despues de eliminar ---");
        hashC.printTable();

        System.out.println("\nBuscando clave 23...");
        Register<String> result = hashC.search(23);
        if (result != null) {
            System.out.println("Encontrado: " + result);
        } else {
            System.out.println("Clave 23 no encontrada");
        }
    }
}
