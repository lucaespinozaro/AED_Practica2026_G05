public class RegistroProductosAVL {
    public static void main(String[] args) {
        AVLTree<Integer> almacen = new AVLTree<>();

        try {
            System.out.println("--- Sistema de Almacén AVL ---");

            System.out.println("\n-> Insertando producto 100");
            almacen.insert(100);
            System.out.println("-> Insertando producto 200");
            almacen.insert(200);
            System.out.println("-> Insertando producto 300 (Gatilla Rotación Simple Izquierda / RSL en 100)");
            almacen.insert(300);
            System.out.print("Estado del almacén (Amplitud): "); almacen.breadthFirst();

            System.out.println("\n-> Insertando producto 150");
            almacen.insert(150);
            System.out.println("-> Insertando producto 50");
            almacen.insert(50);
            System.out.println("-> Insertando producto 25 (Gatilla Rotación Simple Derecha / RSR en 100)");
            almacen.insert(25);
            System.out.print("Estado del almacén (Amplitud): "); almacen.breadthFirst();

            System.out.println("--- Recorridos del Inventario Actual ---");
            System.out.print("Recorrido Inorden (Productos Ordenados): "); almacen.inOrder();
            System.out.print("Recorrido por Amplitud (Estructura AVL): "); almacen.breadthFirst();

            System.out.println("--- Control de Disponibilidad (Búsqueda) ---");
            System.out.println("Buscando producto 150: " + (almacen.search(150) ? "Disponible" : "No Disponible"));
            System.out.println("Buscando producto 999: " + (almacen.search(999) ? "Disponible" : "No Disponible"));

            System.out.println("--- Baja de Productos (Eliminación) ---");
            System.out.println("-> Eliminando producto 150 (Nodo Hoja)");
            almacen.remove(150);
            System.out.print("Inventario resultante (Inorden): "); almacen.inOrder();

            System.out.println("\n-> Eliminando producto 200 (Nodo Raíz del subárbol)");
            almacen.remove(200);
            System.out.print("Inventario final (Inorden): "); almacen.inOrder();
            System.out.print("Estructura final (Amplitud): "); almacen.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
