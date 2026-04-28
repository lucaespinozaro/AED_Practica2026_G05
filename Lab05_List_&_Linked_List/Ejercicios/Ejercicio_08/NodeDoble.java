class NodeDoble<T> {
    T data;
    NodeDoble<T> next;
    NodeDoble<T> prev;

    public NodeDoble(T data) {
        if (data == null)
            throw new IllegalArgumentException();
        this.data = data;
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
