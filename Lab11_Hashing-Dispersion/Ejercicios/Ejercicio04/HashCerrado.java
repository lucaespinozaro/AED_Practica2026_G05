/**
 * Tabla hash cerrada con sondeo lineal, usando celdas Entry con estados
 * EMPTY / OCCUPIED / DELETED para soportar eliminacion logica.
 */
public class HashCerrado {
    private Entry[] table;
    private int size;

    public HashCerrado(int size) {
        this.size = size;
        this.table = new Entry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Entry();
        }
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

    public boolean insert(int key) {
        int start = hash(key);
        int index = start;
        int firstDeleted = -1;
        int probes = 0;

        do {
            if (table[index].getStatus() == Entry.Status.EMPTY) {
                int target = (firstDeleted != -1) ? firstDeleted : index;
                table[target].setKey(key);
                table[target].setStatus(Entry.Status.OCCUPIED);
                return true;
            }
            if (table[index].getStatus() == Entry.Status.DELETED && firstDeleted == -1) {
                firstDeleted = index; // primera celda DELETED reutilizable
            }
            if (table[index].getStatus() == Entry.Status.OCCUPIED && table[index].getKey() == key) {
                return false; // la clave ya existe
            }
            index = (index + 1) % size;
            probes++;
        } while (probes < size);

        if (firstDeleted != -1) {
            table[firstDeleted].setKey(key);
            table[firstDeleted].setStatus(Entry.Status.OCCUPIED);
            return true;
        }

        System.out.println("Error: tabla hash llena");
        return false;
    }

    /**
     * Busca la clave usando sondeo lineal. Una celda DELETED NO detiene la
     * busqueda (se sigue sondeando); solo una celda EMPTY indica que la
     * clave no puede estar mas adelante en la secuencia de sondeo.
     */
    public int search(int key) {
        int start = hash(key);
        int index = start;
        int probes = 0;

        do {
            if (table[index].getStatus() == Entry.Status.EMPTY) {
                return -1;
            }
            if (table[index].getStatus() == Entry.Status.OCCUPIED && table[index].getKey() == key) {
                return index;
            }
            index = (index + 1) % size;
            probes++;
        } while (probes < size);

        return -1;
    }

    /** Eliminacion logica: solo cambia el estado a DELETED. */
    public boolean delete(int key) {
        int index = search(key);
        if (index == -1) return false;
        table[index].setStatus(Entry.Status.DELETED);
        return true;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.println("  Indice " + i + ": " + table[i]);
        }
    }
}
