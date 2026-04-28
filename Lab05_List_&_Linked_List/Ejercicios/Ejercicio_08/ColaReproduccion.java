import java.util.*;

class ColaReproduccion<T>
{
  private NodeDoble<T> head;
  private NodeDoble<T> tail;
  private NodeDoble<T> actual;

  public void agregarCancion(T cancion)
  {
    NodeDoble<T> nuevo = new NodeDoble<>(cancion);
    if (head == null) {
      head = tail = nuevo;
      actual = head;
    } else {
      tail.next = nuevo;
      nuevo.prev = tail;
      tail = nuevo;
    }
  }

  public T reproducirSiguiente()
  {
    if (actual != null && actual.next != null) {
      actual = actual.next;
      return actual.data;
    }
    return null;
  }

  public T reproducirAnterior()
  {
    if (actual != null && actual.prev != null) {
      actual = actual.prev;
      return actual.data;
    }
    return null;
  }

  public void mezclar()
  {
    List<NodeDoble<T>> lista = new ArrayList<>();
    NodeDoble<T> temp = head;

    while (temp != null) {
      lista.add(temp);
      temp = temp.next;
    }

    Collections.shuffle(lista);

    head = lista.get(0);
    head.prev = null;

    for (int i = 1; i < lista.size(); i++) {
      lista.get(i - 1).next = lista.get(i);
      lista.get(i).prev = lista.get(i - 1);
    }

    tail = lista.get(lista.size() - 1);
    tail.next = null;
    actual = head;
  }

  public void mostrarCola()
  {
    NodeDoble<T> temp = head;
    int i = 1;

    while (temp != null) {
      System.out.println(i + ". " + temp.data);
      temp = temp.next;
      i++;
    }

    System.out.println("► Reproduciendo ahora: " + actual.data);
  }

  public int duracionTotal()
  {
    int total = 0;
    NodeDoble<T> temp = head;

    while (temp != null) {
      Cancion c = (Cancion) temp.data;
      total += c.getDuracionSeg();
      temp = temp.next;
    }

    return total;
  }
}
