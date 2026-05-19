public class TestAVL {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        
        try {
            System.out.println("\n-> Inserciones Iniciales y Disparo de Rotaciones");
            
            System.out.println("\n-> Insertando 50, 40, 30");
            tree.insert(50);
            tree.insert(40);
            tree.insert(30); 
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Insertando 60, 70");
            tree.insert(60);
            tree.insert(70); 
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Insertando 35");
            tree.insert(35);
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Insertando 65");
            tree.insert(65);
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Insertando elementos adicionales: 25, 22, 80, 90, 38");
            tree.insert(25);
            tree.insert(22); 
            tree.insert(80);
            tree.insert(90); 
            tree.insert(38); 
            
            System.out.println("\n--- Estado del Árbol antes de las Eliminaciones ---");
            System.out.print("Recorrido Inorden: "); tree.inOrder();
            System.out.print("Recorrido Preorden: "); tree.preOrder();
            System.out.print("Recorrido por Amplitud: "); tree.breadthFirst();

            System.out.println("\n--- Fase de Eliminación y Verificación de Nuevas Rotaciones ---");

            System.out.println("-> Eliminando clave 22 (Nodo Hoja - No altera el balance)");
            tree.remove(22);
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Eliminando clave 35 (Nodo Hoja - Altera balance y gatilla rebalanceo)");
            tree.remove(35);
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n-> Eliminando clave 40 (Nodo Raíz con dos hijos - Reemplazo por sucesor)");
            tree.remove(40);
            System.out.print("Amplitud actual: "); tree.breadthFirst();

            System.out.println("\n--- Estado Final del Árbol AVL ---");
            System.out.print("Recorrido Inorden Final: "); tree.inOrder();
            System.out.print("Recorrido por Amplitud Final: "); tree.breadthFirst();

            System.out.println("\n-> Control de duplicados:");
            tree.insert(60);

        } catch (ItemDuplicated e) {
            System.out.println("Validación Correcta: " + e.getMessage());
        }
    }
}
d
