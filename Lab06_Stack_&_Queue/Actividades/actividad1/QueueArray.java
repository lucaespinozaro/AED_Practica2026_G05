package actividad1;

class QueueArray<E> implements Queue<E>
{
  private E[] array;
  private int front;
  private int rear;
  private int size;

  public QueueArray(int n)
  {
    if (n <= 0) throw new IllegalArgumentException();
    array = (E[]) new Object[n];
    front = 0;
    rear = -1;
    size = 0;
  }

  public void enqueue(E x)
  {
    if (x == null) throw new IllegalArgumentException();
    if (isFull()) throw new IllegalStateException("La cola esta llena");
    rear = (rear + 1) % array.length;
    array[rear] = x;
    size++;
  }

  public E dequeue() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La cola esta vacia");
    E aux = array[front];
    front = (front + 1) % array.length;
    size--;
    return aux;
  }

  public E front() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La cola esta vacia");
    return array[front];
  }

  public boolean isEmpty()
  {
    return size == 0;
  }

  public boolean isFull()
  {
    return size == array.length;
  }

  public String toString()
  {
    StringBuilder res = new StringBuilder("Cola: ");
    int count = 0;
    int i = front;
    while (count < size) {
      res.append(array[i]).append(" ");
      i = (i + 1) % array.length;
      count++;
    }
    return res.toString();
  }
}
