public class ColaPrioridad<E> {
    private int niveles;
    private ColaEnlazada<E>[] colas;

    @SuppressWarnings("unchecked")
    public ColaPrioridad(int niveles) {
        this.niveles = niveles;
        this.colas = (ColaEnlazada<E>[]) new ColaEnlazada[niveles];
        for (int i = 0; i < niveles; i++) {
            colas[i] = new ColaEnlazada<>();
        }
    }

    public void enqueue(E x, int prioridad) {
        if (prioridad < 0 || prioridad >= niveles) {
            throw new RuntimeException("Prioridad invalida");
        }
        colas[prioridad].enqueue(x);
    }

    public E dequeue() {
        for (int i = niveles - 1; i >= 0; i--) {
            if (!colas[i].isEmpty()) {
                return colas[i].dequeue();
            }
        }
        throw new RuntimeException("Cola vacia");
    }

    public boolean isEmpty() {
        for (int i = 0; i < niveles; i++) {
            if (!colas[i].isEmpty()) {
                return false;
            }
        }
        return true;
    }
}
