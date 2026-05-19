public class GestorTicketsAVL {
    public static void main(String[] args) {
        AVLTree<Integer> gestor = new AVLTree<>();
        
        try {
            System.out.println("--- Ejecutando Gestor de Tickets AVL ---");
            
            System.out.println("\n-> Insertando ticket 30");
            gestor.insert(30);
            System.out.println("-> Insertando ticket 10");
            gestor.insert(10);
            System.out.println("-> Insertando ticket 20 (Provoca Rotación Doble Derecha - Izquierda / RDR)");
            gestor.insert(20);
            System.out.print("Estado actual (Amplitud): "); gestor.breadthFirst();

            System.out.println("\n-> Insertando ticket 40");
            gestor.insert(40);
            System.out.println("-> Insertando ticket 50 (Provoca Rotación Simple Izquierda / RSL)");
            gestor.insert(50);
            System.out.print("Estado actual (Amplitud): "); gestor.breadthFirst();

            System.out.println("\n-> Insertando ticket 25 (Provoca Rotación Doble Izquierda - Derecha / RDL)");
            gestor.insert(25);
            System.out.print("Estado final de inserciones (Amplitud): "); gestor.breadthFirst();

            System.out.println("\n==================================================================");
            System.out.println("Evidencia de los Recorridos Configurados");
            System.out.println("==================================================================");
            System.out.print("Recorrido en Inorden (Validación BST): "); gestor.inOrder();
            System.out.print("Recorrido en Preorden: "); gestor.preOrder();
            System.out.print("Recorrido por Amplitud (BFS): "); gestor.breadthFirst();

            System.out.println("\n==================================================================");
            System.out.println("Operaciones de Búsqueda");
            System.out.println("==================================================================");
            System.out.println("Buscando ticket 20: " + (gestor.search(20) ? "Encontrado" : "No Encontrado"));
            System.out.println("Buscando ticket 60: " + (gestor.search(60) ? "Encontrado" : "No Encontrado"));

            System.out.println("\n==================================================================");
            System.out.println("Operaciones de Elimiación y Rebalanceo");
            System.out.println("==================================================================");
            
            System.out.println("-> Eliminando ticket 10 (Nodo Hoja)");
            gestor.remove(10);
            System.out.print("Amplitud post-eliminación 10: "); gestor.breadthFirst();

            System.out.println("\n-> Eliminando ticket 40 (Nodo Hoja)");
            gestor.remove(40);
            System.out.print("Amplitud post-eliminación 40: "); gestor.breadthFirst();

            System.out.println("\n-> Eliminando ticket 30 (Nodo con Dos Hijos / Rebalanceo RSR)");
            gestor.remove(30);
            System.out.print("Amplitud post-eliminación 30: "); gestor.breadthFirst();

            System.out.println("\n==================================================================");
            System.out.print("Recorrido Inorden Final del Sistema: "); gestor.inOrder();
            System.out.println("==================================================================");

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
