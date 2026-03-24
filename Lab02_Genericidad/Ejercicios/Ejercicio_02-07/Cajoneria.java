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

    public Rastreo<T> search(T objeto) {
        for (int i = 0; i < lista.size(); i++) {
            Caja<T> caja = lista.get(i);
            if (caja.getObjeto() != null && caja.getObjeto().equals(objeto)) {
                return new Rastreo<>(caja.getColor(), i, caja.getObjeto());
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

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format("%-10s %-15s %-20s%n", "Posición", "Color Caja", "Objeto"));
        sb.append("----------------------------------------------------------\n");

        int i = 1;
        for (Caja<T> caja : lista) {
            sb.append(String.format("%-10d %-15s %-20s%n", i, caja.getColor(), caja.getObjeto()));
            i++;
        }
        return sb.toString();
    }
}
