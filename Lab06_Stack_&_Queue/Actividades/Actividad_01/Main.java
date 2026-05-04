
public class Main
{
  public static void main(String[] args) throws ExceptionIsEmpty
  {
    QueueArray<Integer> q1 = new QueueArray<>(5);
    q1.enqueue(10); q1.enqueue(20);
    System.out.println("Enteros: " + q1.toString());

    QueueArray<String> q2 = new QueueArray<>(5);
    q2.enqueue("Hola"); q2.enqueue("Mundo");
    System.out.println("Strings: " + q2.toString());
  }
}
