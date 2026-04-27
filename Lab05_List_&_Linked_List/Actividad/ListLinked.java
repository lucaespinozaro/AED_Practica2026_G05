public class ListLinked<T extends Comparable<T>> {
    private Node<T> first;
    private int size;

    public ListLinked() {
        this.first = null;
        this.size = 0;
    }

    public Node<T> getFirstNode() {
        return first;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }

    public void insertFirst(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> n = new Node<>(dato);
        n.next = first;
        first = n;
        size++;
    }

    public void insertLast(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> n = new Node<>(dato);
        if (isEmpty()) {
            first = n;
        } else {
            Node<T> aux = first;
            while (aux.next != null) aux = aux.next;
            aux.next = n;
        }
        size++;
    }

    public boolean search(T dato) {
        if (dato == null || isEmpty()) return false;
        Node<T> aux = first;
        while (aux != null) {
            if (aux.dato.compareTo(dato) == 0) return true;
            aux = aux.next;
        }
        return false;
    }

    public boolean remove(T dato) {
        if (dato == null || isEmpty()) return false;

        if (first.dato.compareTo(dato) == 0) {
            first = first.next;
            size--;
            return true;
        }

        Node<T> aux = first;
        while (aux.next != null && aux.next.dato.compareTo(dato) != 0) {
            aux = aux.next;
        }

        if (aux.next == null) return false;

        aux.next = aux.next.next;
        size--;
        return true;
    }

    public void insertOrdered(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> n = new Node<>(dato);

        if (isEmpty() || first.dato.compareTo(dato) >= 0) {
            n.next = first;
            first = n;
        } else {
            Node<T> aux = first;
            while (aux.next != null && aux.next.dato.compareTo(dato) < 0) {
                aux = aux.next;
            }
            n.next = aux.next;
            aux.next = n;
        }
        size++;
    }

    public void print() {
        System.out.println(printList());
    }

    public String printList() {
        if (isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        Node<T> aux = first;
        while (aux != null) {
            sb.append(aux.dato.toString());
            if (aux.next != null) sb.append(" -> ");
            aux = aux.next;
        }
        return sb.toString();
    }

    public void reverse() {
        Node<T> prev = null;
        Node<T> curr = first;
        while (curr != null) {
            Node<T> next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        first = prev;
    }
}
