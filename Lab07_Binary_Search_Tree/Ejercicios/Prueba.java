public class Prueba {
    public static void main(String[] args) {
        LinkedBST<Integer> bst = new LinkedBST<>();
        try {
            int[] values = {15, 8, 22, 5, 12, 18, 30};
            for (int v : values) bst.insert(v);

            System.out.println("Recorridos:");
            bst.inOrder(); bst.preOrder(); bst.postOrder();

            System.out.println("\nBúsqueda 12: " + bst.search(12));
            
            System.out.println("\nAltura (Iterativa) de 15: " + bst.height(15));
            System.out.println("Nodos no-hoja: " + bst.countNodes());
            System.out.println("Amplitud máxima: " + bst.amplitude());
            System.out.println("Área BST (Iterativa): " + bst.areaBST());

            System.out.println("\nRepresentación Paréntesis:");
            bst.parenthesize();

            System.out.println("\n¿Es BST válido?: " + bst.isValidBST());
            System.out.print("Rango [10, 25]: "); bst.searchRange(10, 25);
            System.out.print("Descendente: "); bst.printDescending();

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
