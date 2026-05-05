package actividad2;

import actividad1.ExceptionIsEmpty;

class DequeLink<E> implements Deque<E>
{
  private Node<E> first;
  private Node<E> last;

  public DequeLink()
  {
    first = null;
    last = null;
  }

  public void addFirst(E x)
  {
    if (x == null) throw new IllegalArgumentException();
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
    if (x == null) throw new IllegalArgumentException();
    Node<E> nuevo = new Node<>(x);

    if (isEmpty()) {
      first = last = nuevo;
    } else {
      last.setNext(nuevo);
      last = nuevo;
    }
  }

  public E removeFirst() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La deque esta vacia");

    E aux = first.getData();
    first = first.getNext();

    if (first == null) last = null;

    return aux;
  }

  public E removeLast() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La deque esta vacia");

    E aux = last.getData();

    if (first == last) {
      first = last = null;
    } else {
      Node<E> temp = first;
      while (temp.getNext() != last) {
        temp = temp.getNext();
      }
      temp.setNext(null);
      last = temp;
    }

    return aux;
  }

  public E getFirst() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La deque esta vacia");
    return first.getData();
  }

  public E getLast() throws ExceptionIsEmpty
  {
    if (isEmpty()) throw new ExceptionIsEmpty("La deque esta vacia");
    return last.getData();
  }

  public boolean isEmpty()
  {
    return first == null;
  }

  public String toString()
  {
    StringBuilder res = new StringBuilder("Deque: ");
    Node<E> temp = first;

    while (temp != null) {
      res.append(temp.getData()).append(" ");
      temp = temp.getNext();
    }

    return res.toString();
  }
}
