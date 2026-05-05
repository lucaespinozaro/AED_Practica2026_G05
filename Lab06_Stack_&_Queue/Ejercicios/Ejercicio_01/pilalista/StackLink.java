package actividadPilaLista;

import actividad1.ExceptionIsEmpty;

class StackLink<E> implements Stack<E> {
    private Node<E> top;

    public StackLink() {
        this.top = null;
    }

    @Override
    public void push(E x) {
        if (x == null) {
            throw new IllegalArgumentException();
        }
        Node<E> nuevo = new Node<>(x);
        nuevo.setNext(top);
        top = nuevo;
    }

    @Override
    public E pop() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("La pila esta vacia");
        }
        E dato = top.getData();
        top = top.getNext();
        return dato;
    }

    @Override
    public E top() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("La pila esta vacia");
        }
        return top.getData();
    }

    @Override
    public boolean isEmpty() {
        return top == null;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Pila (tope -> fondo): ");
        Node<E> actual = top;
        while (actual != null) {
            sb.append(actual.getData());
            if (actual.getNext() != null) {
                sb.append(" -> ");
            }
            actual = actual.getNext();
        }
        return sb.toString();
    }
}
