public class Main {
    public static void main(String[] args) {

        AVLTree<Integer> avl = new AVLTree<>();

        try {
            System.out.println("INSERTANDO NODOS:");

            avl.insert(30);
            avl.insert(20);
            avl.insert(10); 

            avl.insert(40);
            avl.insert(50);

            avl.insert(25); 

            System.out.print("Arbol AVL: ");
            avl.breadthFirst();


            System.out.println("\nELIMINANDO NODOS:");

            avl.remove(50);
            avl.remove(40);

            System.out.print("Arbol despues de eliminar: ");
            avl.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}



