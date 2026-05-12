public class Prueba {
    public static void main(String[] args) {
        LinkedBST<Integer> inventario = new LinkedBST<>();
        try {
            int[] codigos = {15, 8, 22, 5, 12, 18, 30};
            for (int cod : codigos) {
                inventario.insert(cod);
            }
            System.out.println("Productos insertados correctamente.");
            inventario.inOrder();

            System.out.print("Productos en el rango [10, 25]: ");
            inventario.searchRange(10, 25);

            System.out.println("Número de productos en nodos hoja: " + inventario.countLeaves());

            System.out.print("Inventario en orden descendente: ");
            inventario.printDescending();
            
            System.out.println("Altura total (iterativa): " + inventario.height(15));
            System.out.println("Amplitud máxima del árbol: " + inventario.amplitude());
            System.out.println("Área total del BST: " + inventario.areaBST());
            System.out.println("¿Es una estructura BST válida?: " + inventario.isValidBST());
            
            System.out.println("\nVisualización Parentética:");
            inventario.parenthesize();

        } catch (Exception e) {
            System.out.println("Error en la gestión: " + e.getMessage());
        }
    }
}
