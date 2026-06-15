public class QueueLink<E> {
    private static class Node<E> {
        E dato;
        Node<E> next;

        public Node(E dato) {
            this.dato = dato;
            this.next = null;
        }
    }

    private Node<E> inicio;
    private Node<E> fin;

    public QueueLink() {
        this.inicio = null;
        this.fin = null;
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public void enqueue(E x) {
        Node<E> n = new Node<>(x);
        if (isEmpty()) {
            inicio = fin = n;
        } else {
            fin.next = n;
            fin = n;
        }
    }

    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Cola vacia");
        }
        E d = inicio.dato;
        inicio = inicio.next;
        if (inicio == null) {
            fin = null;
        }
        return d;
    }
}
