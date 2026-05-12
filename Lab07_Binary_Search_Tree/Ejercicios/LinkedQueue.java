public class LinkedQueue<E> {
    private QueueNode<E> front;
    private QueueNode<E> rear;

    public LinkedQueue() {
        this.front = null;
        this.rear = null;
    }

    public boolean isEmpty() {
        return this.front == null;
    }

    public void enqueue(E data) {
        if (data == null) {
            throw new IllegalArgumentException("Data null");
        }

        QueueNode<E> node = new QueueNode<>(data);

        if (isEmpty()) {
            this.front = node;
            this.rear = node;
            return;
        }

        this.rear.next = node;
        this.rear = node;
    }

    public E dequeue() throws ExceptionIsEmpty {
        if (isEmpty()) {
            throw new ExceptionIsEmpty("Queue empty");
        }

        E data = this.front.data;

        this.front = this.front.next;

        if (this.front == null) {
            this.rear = null;
        }

        return data;
    }
}
