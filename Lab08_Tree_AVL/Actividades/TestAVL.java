package avltree;

public class TestAVL
{
  public static void main(String[] args)
  {
    AVLTree<Integer> tree = new AVLTree<>();

    System.out.println("--- PRUEBAS DE INSERCIÓN Y BALANCEO ---");
    System.out.println("1. Insertando 10, 5, 2 (Provoca Rotación Simple Derecha - RSR)");
    tree.insert(10);
    tree.insert(5);
    tree.insert(2);
        
    System.out.print("Recorrido Inorden: ");
    tree.inOrder();

    System.out.println("\n2. Insertando 15, 20 (Provoca Rotación Simple Izquierda - RSL)");
    tree.insert(15);
    tree.insert(20);
    System.out.print("Recorrido Inorden: ");
    tree.inOrder();

    System.out.println("\n3. Insertando 12 (Provoca Rotación Doble Izquierda - RDL en subárbol)");
    tree.insert(12);
    System.out.print("Recorrido Inorden: ");
    tree.inOrder();

    System.out.println("\n==========================================");
    System.out.println("DEMOSTRACIÓN DE LOS NUEVOS RECORRIDOS");
    System.out.println("==========================================");

    System.out.print("EJERCICIO 6: Recorrido en Preorden (Raíz -> Izq -> Der): ");
    tree.preOrder();

    System.out.print("EJERCICIOS 4 y 5: Recorrido por Amplitud (Por Niveles Recursivo): ");
    tree.breadthFirst();
  }
}
