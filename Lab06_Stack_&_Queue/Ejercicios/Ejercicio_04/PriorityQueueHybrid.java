public class PriorityQueueHybrid<E> {
    private int niveles;
    private ColaOrdenada<E>[] colas;

    @SuppressWarnings("unchecked")
    public PriorityQueueHybrid(int niveles) {
        this.niveles = niveles;
        this.colas = (ColaOrdenada<E>[]) new ColaOrdenada[niveles];
        for (int i = 0; i < niveles; i++) {
            colas[i] = new ColaOrdenada<>();
        }
    }

    public void enqueue(E dato, int prioridad, int valor) {
        if (prioridad < 0 || prioridad >= niveles) {
            throw new RuntimeException("Prioridad invalida");
        }
        colas[prioridad].enqueue(dato, valor);
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
