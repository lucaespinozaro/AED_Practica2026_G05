public class ColaAtencionClinicaAVL {
    public static void main(String[] args) {
        AVLTree<Integer> cola = new AVLTree<>();

        try {
            System.out.println("--- Cola de Atención en Clínica (AVL) ---");

            System.out.println("\n-> Insertando turno 50");
            cola.insert(50);
            System.out.println("-> Insertando turno 10");
            cola.insert(10);
            System.out.println("-> Insertando turno 30 (Gatilla Rotación Doble Derecha / RDR)");
            cola.insert(30);
            System.out.print("Estado de la cola (Amplitud): "); cola.breadthFirst();

            System.out.println("\n-> Insertando turnos complementarios 40 y 60");
            cola.insert(40);
            cola.insert(60);
            System.out.print("Estado de la cola (Amplitud): "); cola.breadthFirst();

            System.out.println("--- Recorridos de Revisión de Turnos ---");
            System.out.print("Recorrido Inorden (Turnos Ordenados): "); cola.inOrder();
            System.out.print("Recorrido por Amplitud (Estructura AVL): "); cola.breadthFirst();

            System.out.println("--- Búsqueda de Pacientes por Turno ---");
            System.out.println("Buscando turno 30: " + (cola.search(30) ? "Paciente en Espera" : "No Encontrado"));
            System.out.println("Buscando turno 99: " + (cola.search(99) ? "Paciente en Espera" : "No Encontrado"));

            System.out.println("--- Atención y Eliminación de Turnos ---");
            System.out.println("-> Atendiendo y eliminando turno 10 (Nodo Hoja)");
            cola.remove(10);
            System.out.print("Estado de la cola (Inorden): "); cola.inOrder();
            System.out.print("Estado de la cola (Amplitud): "); cola.breadthFirst();

        } catch (ItemDuplicated e) {
            System.out.println(e.getMessage());
        }
    }
}
