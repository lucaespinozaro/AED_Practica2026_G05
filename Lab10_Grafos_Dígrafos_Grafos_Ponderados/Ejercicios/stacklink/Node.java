class Node<E extends Comparable<E>> {
    E dato;
    Node<E> next;

    public Node(E dato) {
        this.dato = dato;
        this.next = null;
    }
}
