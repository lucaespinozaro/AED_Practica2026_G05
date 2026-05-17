public class RegistroProductosAVL {
    public static void main(String[] args) {
        AVLTree<Integer> almacen = new AVLTree<>();

        try {
            System.out.println("--- REGISTRO DE PRODUCTOS ---");
            System.out.println("Insertando producto 101...");
            almacen.insert(101);
            System.out.println("Insertando producto 102...");
            almacen.insert(102);
            System.out.println("Insertando producto 103...");
            almacen.insert(103);

            System.out.print("Productos en almacen (Inorden): ");
            almacen.inOrder();
            System.out.print("Estructura del arbol (Niveles): ");
            almacen.breadthFirst();

            System.out.println("\n--- BUSQUEDA DE PRODUCTOS ---");
            int[] productosABuscar = {102, 105};
            for (int codigo : productosABuscar) {
                System.out.print("Buscando codigo " + codigo + ": ");
                if (almacen.search(codigo)) {
                    System.out.println("Disponible en inventario.");
                } else {
                    System.out.println("No registrado / Agotado.");
                }
            }

            System.out.println("\n--- ELIMINACION DE PRODUCTOS ---");
            System.out.println("Eliminando producto 102 (raiz)...");
            almacen.remove(102);

            System.out.print("Productos en almacen (Inorden): ");
            almacen.inOrder();
            System.out.print("Estructura del arbol (Niveles): ");
            almacen.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
