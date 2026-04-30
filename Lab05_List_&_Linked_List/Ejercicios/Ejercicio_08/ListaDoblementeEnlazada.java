import java.util.ArrayList;
import java.util.List;

class ListaDoblementeEnlazada<T>
{
  private NodeDoble<T> head;
  private NodeDoble<T> tail;

  public void agregar(T dato)
  {
    if (dato == null)
      throw new IllegalArgumentException();

    NodeDoble<T> nuevo = new NodeDoble<>(dato);

    if (head == null) {
      head = tail = nuevo;
    } else {
      tail.next = nuevo;
      nuevo.prev = tail;
      tail = nuevo;
    }
  }

  public List<NodeDoble<T>> obtenerNodos()
  {
    List<NodeDoble<T>> lista = new ArrayList<>();
    NodeDoble<T> temp = head;
    while (temp != null) {
      lista.add(temp);
      temp = temp.next;
    }
    return lista;
  }

  public void reenlazar(List<NodeDoble<T>> nodos) {
    if (nodos == null || nodos.isEmpty()) return;

    head = nodos.get(0);
    head.prev = null;

    for (int i = 1; i < nodos.size(); i++) {
      nodos.get(i - 1).next = nodos.get(i);
      nodos.get(i).prev = nodos.get(i - 1);
    }

    tail = nodos.get(nodos.size() - 1);
    tail.next = null;
  }

  public NodeDoble<T> getHead() { return head; }
  public boolean isEmpty() { return head == null; }
}
