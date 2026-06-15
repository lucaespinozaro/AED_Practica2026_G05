package listlinked;

public class QueueLink<E> {
    private Node<E> front;
    private Node<E> rear;
    private int size;

    public QueueLink() {
        front = null;
        rear = null;
        size = 0;
    }

    public void enqueue(E data) {
        Node<E> newNode = new Node<>(data);
        if (rear == null) {
            front = newNode;
            rear = newNode;
        } else {
            rear.next = newNode;
            rear = newNode;
        }
        size++;
    }

    public E dequeue() {
        if (front == null) return null;
        E data = front.data;
        front = front.next;
        if (front == null) rear = null;
        size--;
        return data;
    }

    public E peek() {
        return (front != null) ? front.data : null;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    public int size() {
        return size;
    }
}
