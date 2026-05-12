class QueueNode<E> {
    E data;
    QueueNode<E> next;

    public QueueNode(E data) {
        if (data == null) {
            throw new IllegalArgumentException("Data null");
        }

        this.data = data;
        this.next = null;
    }
}
