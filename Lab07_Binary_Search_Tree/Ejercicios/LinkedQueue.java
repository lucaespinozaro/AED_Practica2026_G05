public class LinkedQueue<E> {
    private QueueNode<E> front;
    private QueueNode<E> rear;
    private int size;

    public LinkedQueue() {
        this.front = null;
        this.rear = null;
        this.size = 0;
    }

    public boolean isEmpty() {
        return this.front == null;
    }

    public int size() {
        return this.size;
    }

    public void enqueue(E data) {
        if (data == null) throw new IllegalArgumentException("Data null");
        QueueNode<E> node = new QueueNode<>(data);
        if (isEmpty()) {
            this.front = node;
            this.rear = node;
        } else {
            this.rear.next = node;
            this.rear = node;
        }
        this.size++;
    }

    public E dequeue() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("Queue empty");
        E data = this.front.data;
        this.front = this.front.next;
        if (this.front == null) this.rear = null;
        this.size--;
        return data;
    }
}

class QueueNode<E> {
    E data;
    QueueNode<E> next;
    public QueueNode(E data) {
        this.data = data;
        this.next = null;
    }
}
