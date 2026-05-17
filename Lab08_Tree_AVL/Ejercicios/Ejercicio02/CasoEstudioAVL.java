public class CasoEstudioAVL {
    public static void main(String[] args) {
        AVLTree<Integer> arbol = new AVLTree<>();

        try {
            System.out.println("=== FASE DE INSERCIÓN Y DESBALANCES ===");
            
            System.out.println("1. Insertando 30:");
            arbol.insert(30);
            arbol.breadthFirst();

            System.out.println("2. Insertando 20:");
            arbol.insert(20);
            arbol.breadthFirst();

            System.out.println("3. Insertando 10 (Provoca Rotación Simple Derecha - RSR):");
            arbol.insert(10);
            arbol.breadthFirst();

            System.out.println("4. Insertando 40:");
            arbol.insert(40);
            arbol.breadthFirst();

            System.out.println("5. Insertando 50 (Provoca Rotación Simple Izquierda - RSL):");
            arbol.insert(50);
            arbol.breadthFirst();

            System.out.println("6. Insertando 25 (Provoca Rotación Doble Derecha-Izquierda - RDL):");
            arbol.insert(25);
            arbol.breadthFirst();

            System.out.println("\n=== FASE DE ELIMINACIÓN Y REBALANCEO ===");

            System.out.println("7. Eliminando nodo hoja 10 (Provoca Rotación Simple Izquierda - RSL para equilibrar la raíz):");
            arbol.remove(10);
            arbol.breadthFirst();

            System.out.println("8. Eliminando nodo 40 (Reajusta factores de equilibrio sin requerir nueva rotación):");
            arbol.remove(40);
            arbol.breadthFirst();

            System.out.println("9. Eliminando nodo 30 (Requiere reemplazo por sucesor inorden y rebalanceo):");
            arbol.remove(30);
            arbol.breadthFirst();

            System.out.println("\n=== ESTADO FINAL DEL ÁRBOL AVL ===");
            System.out.print("Recorrido Inorden: ");
            arbol.inOrder();
            System.out.print("Recorrido por Niveles: ");
            arbol.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
