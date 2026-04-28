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

    public boolean isEmptyList() {
        return size == 0;
    }

    public int length() {
        return size;
    }

    public void insertFirst(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> nuevo = new Node<>(dato);
        nuevo.next = this.first;
        this.first = nuevo;
        size++;
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

    public void insertOrdered(T dato) {
        if (dato == null) throw new IllegalArgumentException("Dato null");
        Node<T> nuevo = new Node<>(dato);

        if (this.isEmptyList() || this.first.dato.compareTo(dato) >= 0) {
            nuevo.next = this.first;
            this.first = nuevo;
        } else {
            Node<T> aux = this.first;
            while (aux.next != null && aux.next.dato.compareTo(dato) < 0) {
                aux = aux.next;
            }
            nuevo.next = aux.next;
            aux.next = nuevo;
        }
        size++;
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
    
    public void reverse() {
        Node<T> prev = null;
        Node<T> curr = this.first;
        while (curr != null) {
            Node<T> next = curr.next;
            curr.next = prev;
            prev = curr;
            curr = next;
        }
        this.first = prev;
    }
}
