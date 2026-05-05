public class StackLink<E> implements Stack<E> {
    private Node<E> top;

    public StackLink() {
        this.top = null;
    }

    @Override
    public void push(E x) {
        Node<E> nuevoNodo = new Node<>(x);
        nuevoNodo.setNext(top);
        top = nuevoNodo;
    }

    @Override
    public E pop() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La pila está vacía");
        E dato = top.getData();
        top = top.getNext();
        return dato;
    }

    @Override
    public E top() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("La pila está vacía");
        return top.getData();
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    public java.util.List<E> toList() {
        java.util.List<E> list = new java.util.ArrayList<>();
        Node<E> actual = top;
        while (actual != null) {
            list.add(actual.getData());
            actual = actual.getNext();
        }
        return list;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pila (tope→fondo): ");
        Node<E> actual = top;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) sb.append(" → ");
            actual = actual.getNext();
        }
        return sb.toString();
    }
}

