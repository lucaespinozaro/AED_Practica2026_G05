public class SortedListLinked<T extends Comparable<T>> extends ListLinked<T> {
    public void insertOrden(T x) {
        if (x == null) throw new IllegalArgumentException("Dato null");

        Node<T> nuevo = new Node<>(x);
        Node<T> first = getFirstNode();

        if (first == null || first.dato.compareTo(x) >= 0) {
            nuevo.next = first;
            setFirstNode(nuevo);
            incrementSize();
            return;
        }

        Node<T> aux = first;
        while (aux.next != null && aux.next.dato.compareTo(x) < 0) {
            aux = aux.next;
        }

        nuevo.next = aux.next;
        aux.next = nuevo;
        incrementSize();
    }
}
