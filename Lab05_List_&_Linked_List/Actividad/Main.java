package Actividad_01;

public class Main {
    public static void main(String[] args) {
        GestorDeTareas g = new GestorDeTareas();

        g.agregarTarea(new Tarea("Estudiar", 3, "pendiente"));
        g.agregarTarea(new Tarea("Comprar", 1, "completada"));
        g.insertarPorPrioridad(new Tarea("Dormir", 2, "pendiente"));

        System.out.println(g.mostrarTareas());

        g.eliminarTarea(new Tarea("Comprar", 1, "completada"));
        System.out.println(g.mostrarTareas());

        System.out.println(g.mostrarPorEstado("pendiente"));

        g.invertirTareas();
        System.out.println(g.mostrarTareas());
    }
}