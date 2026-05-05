package actividad3;

import actividad1.ExceptionIsEmpty;

class PriorityQueueLinkSort<E, N extends Comparable<N>> implements PriorityQueue<E, N>
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
    first = null;
    last = null;
  }

  public void enqueue(E x, N pr)
  {
    if (x == null || pr == null) throw new IllegalArgumentException();

    EntryNode nuevoDato = new EntryNode(x, pr);
    Node<EntryNode> nuevoNodo = new Node<>(nuevoDato);

    if (isEmpty() || pr.compareTo(first.getData().priority) > 0) {
      nuevoNodo.setNext(first);
      first = nuevoNodo;
      if (last == null) last = first;
    } else {
      Node<EntryNode> temp = first;

      while (temp.getNext() != null &&
             temp.getNext().getData().priority.compareTo(pr) >= 0) {
        temp = temp.getNext();
      }

      nuevoNodo.setNext(temp.getNext());
      temp.setNext(nuevoNodo);

      if (nuevoNodo.getNext() == null) last = nuevoNodo;
    }
  }

  public E dequeue() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La cola esta vacia");

    E aux = first.getData().data;
    first = first.getNext();

    if (first == null) last = null;

    return aux;
  }

  public E front() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La cola esta vacia");
    return first.getData().data;
  }

  public E back() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La cola esta vacia");
    return last.getData().data;
  }

  public boolean isEmpty()
  {
    return first == null;
  }

  public String toString()
  {
    StringBuilder sb = new StringBuilder("PriorityQueue: ");
    Node<EntryNode> temp = first;

    while (temp != null) {
      sb.append("(")
        .append(temp.getData().data)
        .append(", P:")
        .append(temp.getData().priority)
        .append(") ");
      temp = temp.getNext();
    }

    return sb.toString();
  }
}
