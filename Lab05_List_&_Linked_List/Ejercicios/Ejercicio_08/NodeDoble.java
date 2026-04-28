class NodeDoble<T>
{
    T data;
    NodeDoble<T> next;
    NodeDoble<T> prev;

    public NodeDoble(T data)
  {
        this.data = data;
        this.next = null;
        this.prev = null;
    }
}
