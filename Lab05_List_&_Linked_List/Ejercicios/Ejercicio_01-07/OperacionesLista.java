package Ejercicio_01;

public class OperacionesLista {
    public static <T extends Comparable<T>> boolean buscarElemento(ListLinked<T> lista, T valor) {
        if (lista == null || valor == null) return false;

        Node<T> aux = lista.getFirstNode();
        while (aux != null) {
            if (aux.dato.compareTo(valor) == 0) return true;
            aux = aux.next;
        }
        return false;
    }

    public static <T extends Comparable<T>> ListLinked<T> invertirLista(ListLinked<T> lista) {
        if (lista == null) throw new IllegalArgumentException("Lista null");

        ListLinked<T> nueva = new ListLinked<>();
        Node<T> aux = lista.getFirstNode();

        while (aux != null) {
            nueva.insertFirst(aux.dato);
            aux = aux.next;
        }

        return nueva;
    }

    public static <T extends Comparable<T>> Node<T> insertarAlFinal(Node<T> head, T valor) {
        if (valor == null) throw new IllegalArgumentException("Valor null");

        Node<T> nuevo = new Node<>(valor);

        if (head == null) return nuevo;

        Node<T> aux = head;
        while (aux.next != null) aux = aux.next;

        aux.next = nuevo;
        return head;
    }

    public static <T extends Comparable<T>> int contarNodos(Node<T> head) {
        int c = 0;
        Node<T> aux = head;

        while (aux != null) {
            c++;
            aux = aux.next;
        }

        return c;
    }

    public static <T extends Comparable<T>> boolean sonIguales(ListLinked<T> lista1, ListLinked<T> lista2) {
        if (lista1 == null || lista2 == null) return false;

        Node<T> a = lista1.getFirstNode();
        Node<T> b = lista2.getFirstNode();

        while (a != null && b != null) {
            if (a.dato.compareTo(b.dato) != 0) return false;
            a = a.next;
            b = b.next;
        }

        return a == null && b == null;
    }

    public static <T extends Comparable<T>> ListLinked<T> concatenarListas(ListLinked<T> lista1, ListLinked<T> lista2) {
        if (lista1 == null || lista2 == null) throw new IllegalArgumentException("Lista null");

        ListLinked<T> nueva = new ListLinked<>();

        Node<T> aux = lista1.getFirstNode();
        while (aux != null) {
            nueva.insertLast(aux.dato);
            aux = aux.next;
        }

        aux = lista2.getFirstNode();
        while (aux != null) {
            nueva.insertLast(aux.dato);
            aux = aux.next;
        }

        return nueva;
    }
}