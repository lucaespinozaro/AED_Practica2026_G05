public class Ejercicio04 {
    public static void main(String[] args) {
        HashC<String> hash = new HashC<>(7);
        int[] keys = {5, 12, 19, 26};

        for (int key : keys) {
            hash.insert(new Register<>(key, "Nombre" + key));
        }

        System.out.println("=== 1. Estado Inicial de la Tabla ===");
        hash.printTable();

        System.out.println("\nEliminando logicamente la clave 12...");
        hash.delete(12);
        System.out.println("=== Estado de la Tabla tras eliminar el 12 ===");
        hash.printTable();

        System.out.println("\n=== 2. Busqueda de la clave 19 ===");
        Register<String> found = hash.search(19);
        System.out.println("Resultado de buscar 19: " + (found != null ? "Encontrado -> " + found : "No encontrado"));

        System.out.println("\n=== 3. Reinsercion de la clave 33 ===");
        System.out.println("Insertando clave 33...");
        hash.insert(new Register<>(33, "Nombre33"));
        System.out.println("=== Estado final de la Tabla ===");
        hash.printTable();
    }
}
