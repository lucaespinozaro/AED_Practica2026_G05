package pilalista;

import Actividad1.ExceptionIsEmpty;

public class StackLink<E> implements Stack<E> {
    private Node<E> top;

    public StackLink() {
        this.top = null;
    }

    @Override
    public void push(E x) {
        Node<E> nuevoNodo = new Node<E>(x);
        nuevoNodo.setNext(top);
        top = nuevoNodo;
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
        String resultado = "Pila (tope -> fondo): ";
        Node<E> actual = top;
        while (actual != null) {
            resultado += actual.getData();
            if (actual.getNext() != null) {
                resultado += " -> ";
            }
            actual = actual.getNext();
        }
        return resultado;
    }
}
