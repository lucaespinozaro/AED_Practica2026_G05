public class Ejercicio06 {
    public static void main(String[] args) {
        System.out.println("=== Ejercicio 6: Cache de sesiones con tabla hash (encadenamiento) ===\n");
        SessionCache cache = new SessionCache(7);

        System.out.println("1) Tres usuarios inician sesion:");
        cache.login("abc123", "alice", "admin", 60000);   
        cache.login("xyz789", "bob", "user", -5000);       
        cache.login("mno456", "carol", "editor", 30000);   
        System.out.println("  alice (abc123, 60s), bob (xyz789, ya expirado), carol (mno456, 30s)");

        System.out.println("\n--- Estado de la tabla tras los logins ---");
        cache.printTable();

        System.out.println("\n2) Validando los tokens:");
        String[] tokens = {"abc123", "xyz789", "mno456"};
        for (String t : tokens) {
            Session s = cache.validate(t);
            if (s != null) {
                System.out.println("  " + t + " -> VALIDO (" + s.getUsername() + ", rol " + s.getRole() + ")");
            } else {
                System.out.println("  " + t + " -> INVALIDO o expirado");
            }
        }

        System.out.println("\n3) carol cierra sesion explicitamente (logout)...");
        cache.logout("mno456");

        System.out.println("\n--- Estado de la tabla tras el logout ---");
        cache.printTable();

        System.out.println("\n4) Ejecutando cleanExpired()...");
        cache.cleanExpired();

        System.out.println("\n--- Estado final de la tabla ---");
        cache.printTable();

        System.out.println("\nSesiones activas restantes: " + cache.countActive());

        System.out.println("\n--- Reflexion ---");
        System.out.println("¿Por que usar una tabla hash es mas eficiente que recorrer una lista enlazada");
        System.out.println("para verificar un token?");
        System.out.println("  Con una lista enlazada simple hay que recorrerla nodo por nodo (O(n) en");
        System.out.println("  el peor caso) comparando tokens uno por uno. Con la tabla hash, el token");
        System.out.println("  se transforma en un indice directo (token.hashCode() % tamano), por lo");
        System.out.println("  que en promedio la busqueda es O(1): se llega casi de inmediato a la");
        System.out.println("  cadena correcta, que normalmente tiene muy pocos elementos.");

        System.out.println("\n¿Que ventaja tiene el HashMap de Java sobre esta implementacion manual?");
        System.out.println("  HashMap redimensiona (rehashing) automaticamente cuando el factor de");
        System.out.println("  carga crece, usa una funcion hash mas elaborada para distribuir mejor");
        System.out.println("  las claves, y ya esta probada y optimizada (con variantes concurrentes");
        System.out.println("  como ConcurrentHashMap). Nuestra implementacion manual es de tamano fijo");
        System.out.println("  y, sin rehashing, su rendimiento podria degradarse con muchas sesiones.");
    }
}
