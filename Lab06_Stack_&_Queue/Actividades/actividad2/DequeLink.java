package actividad2;
import actividad1.ExceptionIsEmpty;
import actividad3.Node; 

public class DequeLink<E> implements Deque<E>
{
  private Node<E> first;
  private Node<E> last;

  public DequeLink()
  {
    this.first = null;
    this.last = null;
  }

  public void addFirst(E x)
  {
    Node<E> nuevo = new Node<>(x);
    if (isEmpty()) {
      first = last = nuevo;
    } else {
      nuevo.setNext(first);
      first = nuevo;
    }
  }

  public void addLast(E x)
  {
    Node<E> nuevo = new Node<>(x);
    if (isEmpty()) {
      first = last = nuevo;
    } else {
      last.setNext(nuevo);
      last = nuevo;
    }
  }

  public E removeFirst() throws ExceptionIsEmpty {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    E aux = first.getData();
    first = first.getNext();
    if (first == null) last = null;
    return aux;
  }

  public E removeLast() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    E aux = last.getData();
    if (first == last) {
      first = last = null;
    } else {
      Node<E> temp = first;
      while (temp.getNext() != last) temp = temp.getNext();
      temp.setNext(null);
      last = temp;
    }
    return aux;
  }

  public E getFirst() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    return first.getData();
  }

  public E getLast() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("Vacio");
    return last.getData();
  }

  public boolean isEmpty()
  {
    return first == null;
  }

  public String toString()
  {
    String res = "Deque: ";
    Node<E> temp = first;
    while (temp != null) {
      res += temp.getData() + " ";
      temp = temp.getNext();
    }
    return res;
  }
}
