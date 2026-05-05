public class ColaEnlazada<E> {
    private Node<E> inicio;
    private Node<E> fin;

    public ColaEnlazada() {
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
        if (isEmpty()) throw new RuntimeException("Cola vacía");
        E d = inicio.getData();
        inicio = inicio.getNext();
        if (inicio == null) fin = null;
        return d;
    }

    public E peek() {
        if (isEmpty()) throw new RuntimeException("Cola vacía");
        return inicio.getData();
    }

    public java.util.List<E> toList() {
        java.util.List<E> list = new java.util.ArrayList<>();
        Node<E> actual = inicio;
        while (actual != null) {
            list.add(actual.getData());
            actual = actual.getNext();
        }
        return list;
    }

    public int size() {
        int count = 0;
        Node<E> actual = inicio;
        while (actual != null) { count++; actual = actual.getNext(); }
        return count;
    }
}

