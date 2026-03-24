import java.util.ArrayList;
import java.util.Iterator;

public class Bolsa<T> implements Iterable<T> {
    private ArrayList<T> lista;
    private int tope;

    public Bolsa(int tope) {
        this.tope = tope;
        this.lista = new ArrayList<>();
    }

    public void add(T objeto) {
        if (lista.size() < tope) {
            lista.add(objeto);
        } else {
            throw new RuntimeException("No caben más objetos");
        }
    }

    @Override
    public Iterator<T> iterator() {
        return lista.iterator();
    }
}
