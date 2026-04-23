import java.util.PriorityQueue;
import java.util.Comparator;
 
public class SetVectors {
    // Cola de prioridad: el subarreglo mas largo sale primero
    private PriorityQueue<Limits> pq;
 
    public SetVectors() {
        pq = new PriorityQueue<>(
            Comparator.comparingInt(Limits::length).reversed()
        );
    }
 
    public void insertar(Limits l) {
        if (l == null)
            throw new IllegalArgumentException("No se puede insertar null");
        pq.add(l);
    }
 
    public Limits mayor() {
        return pq.poll();
    } 

    public int longMayor() {
        return pq.isEmpty() ? 0 : pq.peek().length();
    }
 
    public boolean esVacio() {
        return pq.isEmpty();
    }
 
    public void destruir() {
        pq.clear();
    }
}
