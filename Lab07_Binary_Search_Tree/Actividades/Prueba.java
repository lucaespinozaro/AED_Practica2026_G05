public class Prueba {
    public static void main(String[] args) {
        LinkedBST<Integer> bst = new LinkedBST<>();
        try {
            bst.insert(400);
            bst.insert(100);
            bst.insert(700);
            bst.insert(50);
            bst.insert(200);
            bst.insert(75);

            System.out.println("InOrden:");
            System.out.println(bst);

            System.out.println();

            System.out.println("PreOrden:");
            System.out.println(bst.preOrder());

            System.out.println();

            System.out.println("PostOrden:");
            System.out.println(bst.postOrder());

            System.out.println();

            System.out.println("Buscar 200:");
            System.out.println(bst.search(200));

            System.out.println();

            System.out.println("Minimo:");
            System.out.println(bst.findMinNode());

            System.out.println();

            System.out.println("Maximo:");
            System.out.println(bst.findMaxNode());

            System.out.println();

            bst.delete(100);

            System.out.println("InOrden despues de eliminar 100:");
            System.out.println(bst);

        } catch (ItemDuplicated | ItemNoFound | ExceptionIsEmpty e) {
            System.out.println(e.getMessage());
        }
    }
}
