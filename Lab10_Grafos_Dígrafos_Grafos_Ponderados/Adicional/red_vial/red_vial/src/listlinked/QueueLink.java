package listlinked;

public class QueueLink<E> {
    private Node<E> front, rear;
    private int size;

    public QueueLink() { front = rear = null; size = 0; }

    public void enqueue(E data) {
        Node<E> n = new Node<>(data);
        if (rear == null) { front = rear = n; }
        else { rear.next = n; rear = n; }
        size++;
    }

    public E dequeue() {
        if (front == null) return null;
        E d = front.data; front = front.next;
        if (front == null) rear = null;
        size--; return d;
    }

    public E peek() { return front != null ? front.data : null; }
    public boolean isEmpty() { return size == 0; }
    public int size() { return size; }
}
