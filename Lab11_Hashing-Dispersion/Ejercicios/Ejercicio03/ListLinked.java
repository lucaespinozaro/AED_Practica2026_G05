public class ListLinked<T extends Comparable<T>> {
    public static class Node<T> {
        public T dato;
        public Node<T> next;

        public Node(T dato) {
            if (dato == null) throw new IllegalArgumentException("Dato null");
            this.dato = dato;
            this.next = null;
        }
    }

    private Node<T> first;
    private int size;

    public ListLinked() {
        this.first = null;
        this.size = 0;
    }

    public Node<T> getFirstNode() {
        return first;
    }

    public boolean isEmptyList() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insertLast(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> nuevo = new Node<>(dato);
        if (this.isEmptyList()) {
            this.first = nuevo;
        } else {
            Node<T> aux = this.first;
            while (aux.next != null) aux = aux.next;
            aux.next = nuevo;
        }
        size++;
    }

    public void addLast(T dato) {
        insertLast(dato);
    }

    public boolean search(T dato) {
        if (dato == null || this.isEmptyList()) return false;
        Node<T> aux = this.first;
        while (aux != null) {
            if (aux.dato.compareTo(dato) == 0) return true;
            aux = aux.next;
        }
        return false;
    }

    public boolean removeNode(T dato) {
        if (dato == null || this.isEmptyList()) return false;

        if (this.first.dato.compareTo(dato) == 0) {
            this.first = this.first.next;
            size--;
            return true;
        }

        Node<T> aux = this.first;
        while (aux.next != null && aux.next.dato.compareTo(dato) != 0) {
            aux = aux.next;
        }

        if (aux.next == null) return false;

        aux.next = aux.next.next;
        size--;
        return true;
    }

    public void print() {
        if (this.isEmptyList()) {
            System.out.println("");
            return;
        }

        StringBuilder sb = new StringBuilder();
        Node<T> aux = this.first;
        while (aux != null) {
            sb.append(aux.dato.toString());
            if (aux.next != null) sb.append(" -> ");
            aux = aux.next;
        }
        System.out.println(sb.toString());
    }
}
