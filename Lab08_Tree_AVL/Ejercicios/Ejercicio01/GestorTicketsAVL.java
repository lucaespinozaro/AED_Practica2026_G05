public class GestorTicketsAVL {
    public static void main(String[] args) {
        AVLTree<Integer> gestor = new AVLTree<>();
        int[] ticketsAInsertar = {30, 10, 20, 40, 50, 25};
        int[] ticketsABuscar = {20, 60};
        int[] ticketsAEliminar = {10, 40, 30};

        System.out.println("=== FASE 1: INSERCIÓN DE TICKETS URGENTES ===");
        for (int ticket : ticketsAInsertar) {
            try {
                System.out.println("\n-> Insertando ticket: " + ticket);
                gestor.insert(ticket);
                System.out.print("Estado por niveles: ");
                gestor.breadthFirst();
            } catch (ItemDuplicated e) {
                System.out.println(e.getMessage());
            }
        }

        System.out.println("\n=== FASE 2: BÚSQUEDA DE TICKETS ===");
        for (int ticket : ticketsABuscar) {
            System.out.print("Buscando ticket " + ticket + "... ");
            if (gestor.search(ticket)) {
                System.out.println("ENCONTRADO en el sistema.");
            } else {
                System.out.println("NO ENCONTRADO.");
            }
        }

        System.out.println("\n=== FASE 3: ELIMINACIÓN DE TICKETS ===");
        for (int ticket : ticketsAEliminar) {
            System.out.println("\n-> Eliminando ticket: " + ticket);
            gestor.remove(ticket);
            System.out.print("Estado por niveles: ");
            gestor.breadthFirst();
        }

        System.out.println("\n=== FASE 4: VERIFICACIÓN FINAL DEL ÁRBOL ===");
        System.out.print("Recorrido Inorden Final: ");
        gestor.inOrder();
        System.out.print("Estructura por Niveles Final: ");
        gestor.breadthFirst();
    }
}
