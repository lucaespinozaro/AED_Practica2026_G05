
public class QueueArray<E> implements Queue<E>
{
  private E[] array;
  private int front;
  private int rear;
  private int size;

  public QueueArray(int n)
  {
    array = (E[]) new Object[n];
    front = 0;
    rear = -1;
    size = 0;
  }

  public void enqueue(E x)
  {
    if (!isFull()) {
      rear = (rear + 1) % array.length;
      array[rear] = x;
      size++;
    }
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
    String res = "Cola: ";
    int count = 0;
    int i = front;
    while (count < size) {
      res += array[i] + " ";
      i = (i + 1) % array.length;
      count++;
    }
    return res;
  }
}
