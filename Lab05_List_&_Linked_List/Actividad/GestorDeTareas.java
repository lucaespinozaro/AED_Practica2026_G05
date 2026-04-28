public class GestorDeTareas<T extends Comparable<T>> {
    private ListLinked<T> lista;

    public GestorDeTareas() {
        this.lista = new ListLinked<>();
    }

    public boolean estaVacia() {
        return lista.isEmptyList();
    }

    public void agregarTarea(T tarea) {
        if (tarea == null) throw new IllegalArgumentException("Tarea null");
        lista.insertLast(tarea);
    }

    public boolean eliminarTarea(T tarea) {
        if (tarea == null) return false;
        return lista.removeNode(tarea);
    }

    public boolean contieneTarea(T tarea) {
        if (tarea == null) return false;
        return lista.search(tarea);
    }

    public void imprimirTareas() {
        lista.print();
    }

    public int contarTareas() {
        return lista.length();
    }

    public T obtenerTareaMasPrioritaria() {
        if (lista.isEmptyList()) return null;

        Node<T> aux = lista.getFirstNode();
        T min = aux.dato;

        while (aux != null) {
            if (aux.dato.compareTo(min) < 0) {
                min = aux.dato;
            }
            aux = aux.next;
        }
        return min;
    }

    public void invertirTareas() {
        lista.reverse();
    }

    public Node<T> getFirstNode() {
        return lista.getFirstNode();
    }
}
