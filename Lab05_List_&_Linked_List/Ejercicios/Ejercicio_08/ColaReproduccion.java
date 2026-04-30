import java.util.List;
import java.util.Random;

class ColaReproduccion<T extends Cancion>
{
  private final ListaDoblementeEnlazada<T> lista = new ListaDoblementeEnlazada<>();
  private NodeDoble<T> actual;

  public void agregarCancion(T cancion)
  {
    if (cancion == null)
      throw new IllegalArgumentException();

    boolean estabaVacia = lista.isEmpty();
    lista.agregar(cancion);

    if (estabaVacia)
      actual = lista.getHead();
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

  public void mezclar() {
    List<NodeDoble<T>> nodos = lista.obtenerNodos();
    if (nodos.size() <= 1) return;

    Random rand = new Random();
    for (int i = nodos.size() - 1; i > 0; i--) {
      int j = rand.nextInt(i + 1);
      NodeDoble<T> aux = nodos.get(i);
      nodos.set(i, nodos.get(j));
      nodos.set(j, aux);
    }

    lista.reenlazar(nodos);
    actual = lista.getHead();
  }

  public void mostrarCola()
  {
    List<NodeDoble<T>> nodos = lista.obtenerNodos();
    for (int i = 0; i < nodos.size(); i++) {
      NodeDoble<T> nodo = nodos.get(i);
      if (nodo == actual)
        System.out.println((i + 1) + ". ► " + nodo.data);
      else
        System.out.println((i + 1) + ". " + nodo.data);
    }

    if (actual != null)
      System.out.println("► Reproduciendo ahora: " + actual.data);
  }

  public int duracionTotal()
  {
    return lista.obtenerNodos()
      .stream()
      .mapToInt(n -> n.data.getDuracionSeg())
      .sum();
  }
}
