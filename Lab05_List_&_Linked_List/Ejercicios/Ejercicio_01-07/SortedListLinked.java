package Ejercicio_01;

public class SortedListLinked<T extends Comparable<T>> extends ListLinked<T> {
    public void insertOrden(T x) {
        if (x == null) throw new IllegalArgumentException("Dato null");
        insertOrdered(x);
    }
}