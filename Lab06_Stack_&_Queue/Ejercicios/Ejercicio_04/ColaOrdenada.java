public class ColaOrdenada<E> {
    private Node<E> inicio;

    public ColaOrdenada() {
        this.inicio = null;
    }

    public boolean isEmpty() {
        return inicio == null;
    }

    public void enqueue(E dato, int valor) {
        Node<E> n = new Node<>(dato, valor);

        if (inicio == null || valor < inicio.valor) {
            n.next = inicio;
            inicio = n;
            return;
        }

        Node<E> act = inicio;
        while (act.next != null && act.next.valor <= valor) {
            act = act.next;
        }

        n.next = act.next;
        act.next = n;
    }

    public E dequeue() {
        if (isEmpty()) {
            throw new RuntimeException("Cola vacia");
        }
        E d = inicio.dato;
        inicio = inicio.next;
        return d;
    }
}
