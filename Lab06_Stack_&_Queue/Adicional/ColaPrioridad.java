public class ColaPrioridad<E> {
    private final int niveles;
    private final ColaEnlazada<E>[] colas;

    @SuppressWarnings("unchecked")
    public ColaPrioridad(int niveles) {
        this.niveles = niveles;
        this.colas = (ColaEnlazada<E>[]) new ColaEnlazada[niveles];
        for (int i = 0; i < niveles; i++) {
            colas[i] = new ColaEnlazada<>();
        }
    }

    public void enqueue(E x, int prioridad) {
        if (prioridad < 0 || prioridad >= niveles)
            throw new RuntimeException("Prioridad inválida");
        colas[prioridad].enqueue(x);
    }

    public E dequeue() {
        for (int i = niveles - 1; i >= 0; i--) {
            if (!colas[i].isEmpty()) return colas[i].dequeue();
        }
        throw new RuntimeException("Cola vacía");
    }

    public boolean isEmpty() {
        for (int i = 0; i < niveles; i++) {
            if (!colas[i].isEmpty()) return false;
        }
        return true;
    }

    public java.util.List<E> getByPriority(int prioridad) {
        return colas[prioridad].toList();
    }

    public int countByPriority(int prioridad) {
        return colas[prioridad].size();
    }

    public int totalSize() {
        int total = 0;
        for (int i = 0; i < niveles; i++) total += colas[i].size();
        return total;
    }
}

