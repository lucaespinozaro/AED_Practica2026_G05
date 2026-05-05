package actividad3;
import actividad1.ExceptionIsEmpty;

public interface PriorityQueue<E, N>
{
  void enqueue(E x, N pr);
  E dequeue() throws ExceptionIsEmpty;
  E front() throws ExceptionIsEmpty;
  E back() throws ExceptionIsEmpty;
  boolean isEmpty();
}
