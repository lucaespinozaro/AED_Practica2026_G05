package Actividad_01;

public class GestorDeTareas {
    private ListLinked<Tarea> lista;

    public GestorDeTareas() {
        this.lista = new ListLinked<>();
    }

    public boolean estaVacia() {
        return lista.isEmpty();
    }

    public void agregarTarea(Tarea t) {
        if (t == null) throw new IllegalArgumentException("Tarea null");
        lista.insertLast(t);
    }

    public void insertarPorPrioridad(Tarea t) {
        if (t == null) throw new IllegalArgumentException("Tarea null");
        lista.insertOrdered(t);
    }

    public boolean eliminarTarea(Tarea t) {
        if (t == null) return false;
        return lista.remove(t);
    }

    public boolean buscarTarea(Tarea t) {
        if (t == null) return false;
        return lista.search(t);
    }

    public String mostrarTareas() {
        return lista.printList();
    }

    public void invertirTareas() {
        lista.reverse();
    }

    public String mostrarPorEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) throw new IllegalArgumentException("Estado invalido");
        if (lista.isEmpty()) return "";

        String est = estado.trim();
        StringBuilder sb = new StringBuilder();

        Node<Tarea> aux = lista.getFirstNode();

        while (aux != null) {
            if (aux.dato.getEstado().equalsIgnoreCase(est)) {
                sb.append(aux.dato.toString());
                if (aux.next != null) sb.append(" -> ");
            }
            aux = aux.next;
        }

        return sb.toString();
    }
}