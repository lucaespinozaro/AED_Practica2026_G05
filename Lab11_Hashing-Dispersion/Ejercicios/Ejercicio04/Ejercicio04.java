/**
 * Ejercicio 4: Eliminacion logica y reinsercion en hash cerrado.
 * Tabla de tamano 7, sondeo lineal, h(x) = x % 7.
 */
public class Ejercicio04 {
    public static void main(String[] args) {
        System.out.println("=== Ejercicio 4: Eliminacion logica y reinsercion en hash cerrado ===\n");

        HashCerrado hash = new HashCerrado(7);

        int[] keys = {5, 12, 19, 26};
        System.out.println("Insertando claves (h(x) = x % 7), sondeo lineal:");
        for (int k : keys) {
            hash.insert(k);
            System.out.println("  Insertar(" + k + ") -> indice base " + (k % 7));
        }

        System.out.println("\n--- Estado de la tabla tras las inserciones ---");
        hash.printTable();
        System.out.println("(Las 4 claves colisionan en el indice 5 porque 5,12,19,26 difieren");
        System.out.println(" exactamente en multiplos de 7, asi que el sondeo lineal las ubica");
        System.out.println(" en 5, 6, 0 y 1 respectivamente.)");

        System.out.println("\n1) Eliminando logicamente la clave 12...");
        hash.delete(12);
        System.out.println("--- Estado de la tabla tras eliminar 12 ---");
        hash.printTable();
        System.out.println("La celda del indice 6 queda marcada como DELETED: el objeto Entry no se");
        System.out.println("borra ni se vacia, solo cambia su estado.");

        System.out.println("\n2) Buscando la clave 19 despues de la eliminacion...");
        int pos19 = hash.search(19);
        if (pos19 != -1) {
            System.out.println("  Encontrada en el indice " + pos19 + ".");
        } else {
            System.out.println("  No encontrada.");
        }
        System.out.println("  Explicacion: la busqueda parte del indice 5, pasa por el indice 6");
        System.out.println("  (DELETED) y SIGUE sondeando hasta llegar al indice 0, donde esta el 19.");
        System.out.println("  Si la busqueda se detuviera apenas encuentra una celda DELETED, jamas");
        System.out.println("  llegaria al indice 0 y reportaria (incorrectamente) que 19 no existe.");
        System.out.println("  Por eso DELETED no detiene el sondeo: solo una celda EMPTY garantiza");
        System.out.println("  que la clave no pudo haberse insertado mas adelante en esa secuencia.");

        System.out.println("\n3) Reinsertando la clave 33...");
        hash.insert(33);
        System.out.println("--- Estado de la tabla tras reinsertar 33 ---");
        hash.printTable();
        System.out.println("La clave 33 (33 % 7 = 5) reutilizo la celda DELETED del indice 6,");
        System.out.println("demostrando que esas celdas pueden reciclarse en una insercion futura.");

        System.out.println("\nDiferencia entre eliminacion logica y fisica:");
        System.out.println("  - Logica: solo se cambia el estado a DELETED. El sondeo de OTRAS claves");
        System.out.println("    sigue funcionando bien porque la secuencia de busqueda no se interrumpe.");
        System.out.println("  - Fisica: se borraria realmente la celda (quedaria EMPTY). Es mas simple,");
        System.out.println("    pero rompe el sondeo de cualquier clave insertada despues que dependa");
        System.out.println("    de pasar por esa posicion, provocando busquedas validas que fallan.");
        System.out.println("  - Conviene usar eliminacion logica en una tabla activa con sondeo, y dejar");
        System.out.println("    la fisica para momentos en que se reconstruye toda la tabla (rehashing).");
    }
}
