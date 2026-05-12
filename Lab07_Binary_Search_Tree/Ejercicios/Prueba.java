public class Prueba {
    public static void main(String[] args) {
        LinkedBST<Integer> bst = new LinkedBST<>();
        try {
            int[] values = {15, 8, 22, 5, 12, 18, 30};
            for (int v : values) bst.insert(v);

            System.out.println("Recorridos:");
            System.out.print("InOrder: "); bst.inOrder();
            System.out.print("PreOrder: "); bst.preOrder();
            System.out.print("PostOrder: "); bst.postOrder();

            System.out.println("\nBúsqueda paso a paso de 12:");
            bst.search(12);
            System.out.println("Búsqueda 21 (no existe): " + bst.height(21));

            System.out.println("\nAnálisis Básico:");
            System.out.println("Altura árbol: " + bst.height(15));
            System.out.println("Nodos hoja: " + bst.countLeaves());
            System.out.println("Mínimo: " + bst.getMin());
            System.out.println("Máximo: " + bst.getMax());

            System.out.println("\nRepresentación Paréntesis:");
            bst.parenthesize();

            System.out.println("\n¿Es BST válido?: " + bst.isValidBST());
            
            LinkedBST<Integer> bst2 = new LinkedBST<>();
            bst2.insert(10); bst2.insert(5); bst2.insert(15);
            System.out.println("¿bst y bst2 tienen misma área?: " + sameArea(bst, bst2));

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    public static boolean sameArea(LinkedBST<?> tree1, LinkedBST<?> tree2) {
        return tree1.areaBST() == tree2.areaBST();
    }
}
