package actividad1;

public interface Queue<E>
{
  void enqueue(E x);
  E dequeue() throws ExceptionIsEmpty;
  E front() throws ExceptionIsEmpty;
  boolean isEmpty();
}
