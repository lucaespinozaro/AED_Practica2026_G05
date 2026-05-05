package actividad3;
import actividad1.ExceptionIsEmpty;

public class Test
{
  public static void main(String[] args) throws ExceptionIsEmpty {
    PriorityQueue<String, Integer> pq = new PriorityQueueLinkSort<>();
    pq.enqueue("Tarea 1", 1);
    pq.enqueue("Emergencia", 10);
    pq.enqueue("Tarea 2", 5);
        
    System.out.println(pq.toString());
    System.out.println("Al frente: " + pq.front());
    System.out.println("Al final: " + pq.back());
    System.out.println("Sacando: " + pq.dequeue());
    System.out.println("Ahora queda: " + pq.toString());
  }
}
