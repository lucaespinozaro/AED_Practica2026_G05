public class Prueba {
    public static void main(String[] args) {
        LinkedBST<Integer> inventario = new LinkedBST<>();
        
        try {
            int[] productosNuevos = {15, 8, 22, 5, 12, 18, 30};
            for (int codigo : productosNuevos) {
                inventario.insert(codigo);
            }

            System.out.print("PreOrder: " + inventario.preOrder());
            System.out.print("InOrder: " + inventario.inOrder()); 
            System.out.print("PostOrder: " + inventario.postOrder());

            inventario.drawBST();

            Integer productoEncontrado = inventario.search(12);
            System.out.println("Producto localizado: " + productoEncontrado);

            System.out.println("Altura del sistema: " + inventario.height(15));
            System.out.println("Productos en nodos hoja: " + inventario.countLeaves());
            System.out.println("ID Producto mínimo: " + inventario.findMinNode());
            System.out.println("ID Producto máximo: " + inventario.findMaxNode());

            java.util.ArrayList<Integer> enRango = inventario.searchRange(10, 25);
            System.out.println("Productos encontrados: " + enRango);

            inventario.printDescending();
            inventario.parenthesize();

            inventario.delete(8);
            System.out.println("Inventario actualizado: ");
            inventario.drwaBST();

            LinkedBST<Integer> inventarioSucursal = new LinkedBST<>();
            inventarioSucursal.insert(10); inventarioSucursal.insert(5); inventarioSucursal.insert(15);
            System.out.println("¿Misma área que sucursal?: " + sameArea(inventario, inventarioSucursal));
;
            System.out.println("Mínimo en sub-árbol 22: " + inventario.findMinNode(22));
            System.out.println("Máximo en sub-árbol 22: " + inventario.findMaxNode(22));

            inventario.search(999);

        } catch (ItemDuplicated | ItemNoFound | ExceptionIsEmpty e) {
            System.out.println("Notificación del Sistema: " + e.getMessage());
        } catch (Exception e) {
            System.out.println("Error Crítico: " + e.getMessage());
        }
    }
    
    public static boolean sameArea(LinkedBST<?> tree1, LinkedBST<?> tree2) {
        return tree1.areaBST() == tree2.areaBST();
    }
}
