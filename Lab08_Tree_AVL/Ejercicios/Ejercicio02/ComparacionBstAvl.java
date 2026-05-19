public class ComparacionBstAvl {
    public static void main(String[] args) {
        System.out.println("Caso de Prueba 1: Secuencia Crítica Ordenada (10, 20, 30, 40, 50, 60, 70)");

        BSTree<Integer> bst1 = new BSTree<>();
        AVLTree<Integer> avl1 = new AVLTree<>();
        int[] datos1 = {10, 20, 30, 40, 50, 60, 70};

        try {
            for (int d : datos1) {
                bst1.insert(d);
                avl1.insert(d);
            }

            System.out.println("[Árbol BST Tradicional]");
            System.out.print("Recorrido Preorden: "); bst1.preOrder();
            System.out.print("Recorrido Amplitud: "); bst1.breadthFirst();
            System.out.println("Altura alcanzada  : " + bst1.getHeight());

            System.out.println("\n[Árbol AVL Balanceado]");
            System.out.print("Recorrido Preorden: "); avl1.preOrder();
            System.out.print("Recorrido Amplitud: "); avl1.breadthFirst();
            System.out.println("Altura alcanzada  : " + avl1.getHeight());

            System.out.println("\n[Prueba de Búsqueda en Caso 21]");
            System.out.println("Búsqueda de 60 en BST: " + (bst1.search(60) ? "Encontrado" : "No Encontrado"));
            System.out.println("Búsqueda de 60 en AVL: " + (avl1.search(60) ? "Encontrado" : "No Encontrado"));

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }

        System.out.println("\nCaso de Prueba 2: Secuencia con Desbalanceo Mixto (50, 40, 30, 20, 45, 42)");

        BSTree<Integer> bst2 = new BSTree<>();
        AVLTree<Integer> avl2 = new AVLTree<>();
        int[] datos2 = {50, 40, 30, 20, 45, 42};

        try {
            for (int d : datos2) {
                bst2.insert(d);
                avl2.insert(d);
            }

            System.out.println("[Árbol BST Tradicional]");
            System.out.print("Recorrido Preorden: "); bst2.preOrder();
            System.out.print("Recorrido Amplitud: "); bst2.breadthFirst();
            System.out.println("Altura alcanzada  : " + bst2.getHeight());

            System.out.println("\n[Árbol AVL Balanceado]");
            System.out.print("Recorrido Preorden: "); avl2.preOrder();
            System.out.print("Recorrido Amplitud: "); avl2.breadthFirst();
            System.out.println("Altura alcanzada  : " + avl2.getHeight());

            System.out.println("\n[Prueba de Búsqueda en Caso 2]");
            System.out.println("Búsqueda de 99 (Inexistente) en BST: " + (bst2.search(99) ? "Encontrado" : "No Encontrado"));
            System.out.println("Búsqueda de 99 (Inexistente) en AVL: " + (avl2.search(99) ? "Encontrado" : "No Encontrado"));

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
