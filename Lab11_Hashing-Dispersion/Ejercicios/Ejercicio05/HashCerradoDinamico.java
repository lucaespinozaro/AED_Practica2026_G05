/**
 * Tabla hash cerrada (sondeo lineal) que controla su factor de carga y se
 * redimensiona (rehashing) automaticamente cuando alpha = n/M supera 0.75.
 */
public class HashCerradoDinamico {
    private Slot[] table;
    private int size;
    private int count;

    public HashCerradoDinamico(int size) {
        this.size = size;
        this.count = 0;
        this.table = new Slot[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Slot();
        }
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

    public double loadFactor() {
        return (double) count / size;
    }

    public int getSize() {
        return size;
    }

    public int getCount() {
        return count;
    }

    public void insert(int key) {
        insertInternal(key);
        count++;
        if (loadFactor() > 0.75) {
            rehash();
        }
    }

    private void insertInternal(int key) {
        int start = hash(key);
        int index = start;
        int probes = 0;
        do {
            if (!table[index].isOccupied()) {
                table[index].setKey(key);
                table[index].setOccupied(true);
                return;
            }
            index = (index + 1) % size;
            probes++;
        } while (probes < size);
        System.out.println("Error: tabla llena, no se pudo insertar " + key);
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int nextPrime(int n) {
        int candidate = n;
        while (!isPrime(candidate)) {
            candidate++;
        }
        return candidate;
    }

    private void rehash() {
        System.out.printf("%n>>> Factor de carga (%.3f) supero 0.75: iniciando rehashing <<<%n",
                loadFactor());
        System.out.println("--- Estado de la tabla ANTES del rehashing (tamano " + size + ") ---");
        printTable();

        int newSize = nextPrime(size * 2);
        Slot[] oldTable = table;
        int oldSize = size;

        this.size = newSize;
        this.table = new Slot[newSize];
        for (int i = 0; i < newSize; i++) {
            table[i] = new Slot();
        }
        this.count = 0;

        for (int i = 0; i < oldSize; i++) {
            if (oldTable[i].isOccupied()) {
                insertInternal(oldTable[i].getKey());
                count++;
            }
        }

        System.out.println("--- Estado de la tabla DESPUES del rehashing (tamano " + size + ") ---");
        printTable();
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.println("  Indice " + i + ": " + table[i]);
        }
    }
}
