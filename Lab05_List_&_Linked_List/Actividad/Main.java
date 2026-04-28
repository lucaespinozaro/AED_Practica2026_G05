public class Main {
    public static void main(String[] args) {
        GestorDeTareas<Tarea> gestor = new GestorDeTareas<>();
        
        gestor.agregarTarea(new Tarea("Diseñar BD", 2, "pendiente"));
        gestor.agregarTarea(new Tarea("Deploy produccion", 1, "pendiente"));
        gestor.agregarTarea(new Tarea("Documentar API", 3, "completada"));
        gestor.agregarTarea(new Tarea("Code review", 2, "pendiente"));
        gestor.agregarTarea(new Tarea("Corregir bug #42", 1, "completada"));

        System.out.println("Tareas:");
        gestor.imprimirTareas();

        gestor.eliminarTarea(new Tarea("Code review", 2, "pendiente"));

        System.out.println("\nDespues de eliminar:");
        gestor.imprimirTareas();

        boolean existe = gestor.contieneTarea(new Tarea("Diseñar BD", 2, "pendiente"));
        System.out.println("\nExiste 'Diseñar BD': " + existe);

        System.out.println("\nMas prioritaria:");
        System.out.println(gestor.obtenerTareaMasPrioritaria());

        gestor.invertirTareas();
        System.out.println("\nLista invertida:");
        gestor.imprimirTareas();

        ListLinked<Tarea> completadas = new ListLinked<>();

        Node<Tarea> aux = gestor.lista.getFirstNode();
        while (aux != null) {
            if (aux.dato.getEstado().equalsIgnoreCase("completada")) {
                completadas.insertLast(aux.dato);
            }
            aux = aux.next;
        }

        System.out.println("\nTareas completadas:");
        completadas.print();
    }
}
