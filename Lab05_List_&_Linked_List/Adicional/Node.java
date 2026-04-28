public class Node<T> {
    public T dato;
    public Node<T> next;

    public Node(T dato) {
        this.dato = dato;
        this.next = null;
    }
}
