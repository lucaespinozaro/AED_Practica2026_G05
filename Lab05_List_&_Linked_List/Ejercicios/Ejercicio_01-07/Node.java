package Ejercicio_01;

class Node<T extends Comparable<T>> {
    T dato;
    Node<T> next;

    public Node(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        this.dato = dato;
        this.next = null;
    }
}