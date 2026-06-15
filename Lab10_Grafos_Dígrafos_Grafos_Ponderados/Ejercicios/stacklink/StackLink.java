package Pilas;

public class StackLink<E extends Comparable<E>> {
    private Node<E> tope;

    public StackLink() {
        this.tope = null;
    }

    public void push(E x) {
        Node<E> n = new Node<>(x);
        n.next = tope;
        tope = n;
    }

    public E pop() {
        if (isEmpty()) {
            throw new RuntimeException("Pila vacia");
        }
        E dato = tope.dato;
        tope = tope.next;
        return dato;
    }

    public E top() {
        if (isEmpty()) {
            throw new RuntimeException("Pila vacia");
        }
        return tope.dato;
    }

    public boolean isEmpty() {
        return tope == null;
    }
}
