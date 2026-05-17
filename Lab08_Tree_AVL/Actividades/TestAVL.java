public class TestAVL {
    public static void main(String[] args) {
        AVLTree<Integer> tree = new AVLTree<>();
        System.out.println("--- EJECUTANDO CASOS DE PRUEBA EXIGIDOS (MÍNIMO 8 OPERACIONES) ---");
        
        try {
            System.out.println("\n-> Insertando 50, 40, 30 (Gatilla RSR en 50)");
            tree.insert(50);
            tree.insert(40);
            tree.insert(30); 
            System.out.print("Preorden actual: "); tree.preOrder();

            System.out.println("\n-> Insertando 60, 70 (Gatilla RSL en 50)");
            tree.insert(60);
            tree.insert(70); 
            System.out.print("Preorden actual: "); tree.preOrder();

            System.out.println("\n-> Insertando 35 (Gatilla RDR en subárbol izquierdo)");
            tree.insert(35);
            System.out.print("Preorden actual: "); tree.preOrder();

            System.out.println("\n-> Insertando 65 (Gatilla RDL en subárbol derecho)");
            tree.insert(65);
            System.out.print("Preorden actual: "); tree.preOrder();

            System.out.println("\n-> Insertando elementos adicionales para forzar el segundo set de balances (25, 22, 80, 90, 38)");
            tree.insert(25);
            tree.insert(22); 
            tree.insert(80);
            tree.insert(90); 
            tree.insert(38); 
            
            System.out.println("\n==========================================");
            System.out.println("Resultados de los Recorridos Finales");
            System.out.println("==========================================");

            System.out.print("Recorrido Inorden (Validación BST ordenado): ");
            tree.inOrder();

            System.out.print("EJERCICIO 6: Recorrido en Preorden: ");
            tree.preOrder();

            System.out.print("EJERCICIOS 4 y 5: Recorrido por Amplitud (Por Niveles Base 0): ");
            tree.breadthFirst();

            System.out.println("\n-> Intentando insertar un nodo duplicado (60) para verificar validación:");
            tree.insert(60);

        } catch (ItemDuplicated e) {
            System.out.println("Validación Correcta: " + e.getMessage());
        }
    }
}
