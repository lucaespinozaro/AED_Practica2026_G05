package listlinked;

public class ListLinked<E> {
    private Node<E> head;
    private int size;

    public ListLinked() { head = null; size = 0; }

    public void addLast(E data) {
        Node<E> n = new Node<>(data);
        if (head == null) { head = n; }
        else {
            Node<E> cur = head;
            while (cur.next != null) cur = cur.next;
            cur.next = n;
        }
        size++;
    }

    public void addFirst(E data) {
        Node<E> n = new Node<>(data);
        n.next = head; head = n; size++;
    }

    public void insertLast(E data) { addLast(data); }

    public E get(int index) {
        if (index < 0 || index >= size) return null;
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        return cur.data;
    }

    public void set(int index, E data) {
        if (index < 0 || index >= size) return;
        Node<E> cur = head;
        for (int i = 0; i < index; i++) cur = cur.next;
        cur.data = data;
    }

    public int size() { return size; }
    public boolean isEmpty() { return size == 0; }

    public boolean search(E data) {
        Node<E> cur = head;
        while (cur != null) {
            if (cur.data.equals(data)) return true;
            cur = cur.next;
        }
        return false;
    }

    public boolean removeNode(E data) {
        if (head == null) return false;
        if (head.data.equals(data)) { head = head.next; size--; return true; }
        Node<E> cur = head;
        while (cur.next != null) {
            if (cur.next.data.equals(data)) { cur.next = cur.next.next; size--; return true; }
            cur = cur.next;
        }
        return false;
    }

    public E removeFirst() {
        if (head == null) return null;
        E d = head.data; head = head.next; size--; return d;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        Node<E> cur = head;
        while (cur != null) {
            sb.append(cur.data);
            if (cur.next != null) sb.append(", ");
            cur = cur.next;
        }
        return sb.append("]").toString();
    }
}
