import java.util.ArrayList;

public class Cajoneria<T> {
    private ArrayList<Caja<T>> lista;
    private int tope;

    public Cajoneria(int tope) {
        this.tope = tope;
        this.lista = new ArrayList<>();
    }

    public void add(Caja<T> caja) {
        if (lista.size() < tope) {
            lista.add(caja);
        } else {
            throw new RuntimeException("No caben más cajas");
        }
    }

    public Busqueda<T> search(T objeto) {
        for (int i = 0; i < lista.size(); i++) {
            Caja<T> caja = lista.get(i);
            if (caja.getObjeto() != null && caja.getObjeto().equals(objeto)) {
                return new Busqueda<>(caja.getColor(), i, caja.getObjeto());
            }
        }
        return null;
    }

    public T delete(T objeto) {
        for (Caja<T> caja : lista) {
            if (caja.getObjeto() != null && caja.getObjeto().equals(objeto)) {
                return caja.delete();
            }
        }
        return null;
    }

    public int contar(T elemento) {
        int contador = 0;
        for (Caja<T> caja : lista) {
            if (caja.getObjeto() != null && caja.getObjeto().equals(elemento)) {
                contador++;
            }
        }
        return contador;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        int i = 1;

        for (Caja<T> caja : lista) {
            sb.append(i)
              .append(" ")
              .append(caja.getColor())
              .append(" ")
              .append(caja.getObjeto())
              .append("\n");
            i++;
        }

        return sb.toString();
    }
}
