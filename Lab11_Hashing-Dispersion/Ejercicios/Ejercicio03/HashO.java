/**
 * Tabla hash abierta (encadenamiento). Cada celda es una lista enlazada propia (ListLinked).
 */
public class HashO<E> {
    private ListLinked<Register<E>>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashO(int size) {
        this.size = size;
        this.table = new ListLinked[size];
        for (int i = 0; i < size; i++) {
            table[i] = new ListLinked<>();
        }
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

    public void insert(Register<E> reg) {
        if (reg == null) return;
        int index = hash(reg.getKey());
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == reg.getKey()) {
                table[index].removeNode(aux.dato);
                break;
            }
            aux = aux.next;
        }
        table[index].addLast(reg);
    }

    public Register<E> search(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                return aux.dato;
            }
            aux = aux.next;
        }
        return null;
    }

    /**
     * Devuelve {indiceDeLaTabla, posicionDelNodoEnLaLista} (0-based), o null si no existe.
     * Permite responder "en que posicion de la tabla y en que nodo se encuentra".
     */
    public int[] locate(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        int pos = 0;
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                return new int[]{index, pos};
            }
            aux = aux.next;
            pos++;
        }
        return null;
    }

    /** Cantidad de nodos que quedan en la cadena de un indice dado. */
    public int chainSize(int index) {
        return table[index].size();
    }

    public void delete(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                table[index].removeNode(aux.dato);
                return;
            }
            aux = aux.next;
        }
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.print("  " + i + ": ");
            if (table[i].isEmptyList()) {
                System.out.println("vacio");
            } else {
                ListLinked.Node<Register<E>> aux = table[i].getFirstNode();
                while (aux != null) {
                    System.out.print(aux.dato + " -> ");
                    aux = aux.next;
                }
                System.out.println("null");
            }
        }
    }
}
