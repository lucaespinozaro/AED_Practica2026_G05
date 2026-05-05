package actividad3;
import actividad1.ExceptionIsEmpty;

public class PriorityQueueLinkSort<E, N extends Comparable<N>> implements PriorityQueue<E, N>
{
  class EntryNode
  {
    E data;
    N priority;
    EntryNode(E data, N priority) {
      this.data = data;
      this.priority = priority;
    }
  }

  private Node<EntryNode> first;
  private Node<EntryNode> last;

  public PriorityQueueLinkSort()
  {
    this.first = null;
    this.last = null;
  }

  public void enqueue(E x, N pr)
  {
    EntryNode nuevoDato = new EntryNode(x, pr);
    Node<EntryNode> nuevoNodo = new Node<>(nuevoDato);
        
    if (isEmpty() || pr.compareTo(first.getData().priority) > 0) {
      nuevoNodo.setNext(first);
      first = nuevoNodo;
      if (last == null) last = first;
    } else {
      Node<EntryNode> temp = first;
      while (temp.getNext() != null && temp.getNext().getData().priority.compareTo(pr) >= 0) {
        temp = temp.getNext();
      }
      nuevoNodo.setNext(temp.getNext());
      temp.setNext(nuevoNodo);
      if (nuevoNodo.getNext() == null) last = nuevoNodo;
    }
  }

  public E dequeue() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Queue is empty");
    E aux = this.first.getData().data;
    this.first = this.first.getNext();
    if (this.first == null) this.last = null;
    return aux;
  }

  public E front() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    return first.getData().data;
  }

  public E back() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    return last.getData().data;
  }

  public boolean isEmpty()
  {
    return first == null;
  }

  public String toString() {
    String res = "PriorityQueue: ";
    Node<EntryNode> temp = first;
    while (temp != null) {
      res += "(" + temp.getData().data + ", P:" + temp.getData().priority + ") ";
      temp = temp.getNext();
    }
    return res;
  }
}
