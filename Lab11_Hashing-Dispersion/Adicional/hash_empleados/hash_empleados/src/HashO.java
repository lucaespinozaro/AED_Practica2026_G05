/**
 * Tabla Hash con resolución de colisiones por ENCADENAMIENTO (Hashing Abierto).
 * Cada posición de la tabla es una lista enlazada (ListLinked) de Registros.
 */
public class HashO<E> {
    private ListLinked<Register<E>>[] table;
    private int size;
    private int count;

    @SuppressWarnings("unchecked")
    public HashO(int initialSize) {
        this.size = isPrime(initialSize) ? initialSize : nextPrime(initialSize);
        this.table = new ListLinked[this.size];
        for (int i = 0; i < this.size; i++) table[i] = new ListLinked<>();
        this.count = 0;
    }

    private int hash(int key) { return Math.abs(key) % size; }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) if (n % i == 0) return false;
        return true;
    }

    private int nextPrime(int n) {
        int prime = n;
        if (prime % 2 == 0) prime++;
        while (!isPrime(prime)) prime += 2;
        return prime;
    }

    public int getSize() { return size; }
    public int getCount() { return count; }
    public double loadFactor() { return (double) count / size; }

    @SuppressWarnings("unchecked")
    private void rehash() {
        int newSize = nextPrime(size * 2);
        ListLinked<Register<E>>[] oldTable = table;
        int oldSize = size;
        this.size = newSize;
        this.table = new ListLinked[newSize];
        for (int i = 0; i < newSize; i++) table[i] = new ListLinked<>();
        this.count = 0;
        for (int i = 0; i < oldSize; i++) {
            ListLinked.Node<Register<E>> aux = oldTable[i].getFirstNode();
            while (aux != null) { insertInternal(aux.dato); aux = aux.next; }
        }
    }

    private void insertInternal(Register<E> reg) {
        int index = hash(reg.getKey());
        table[index].addLast(reg);
        count++;
    }

    /** Inserta y devuelve el índice donde quedó (para resaltar visualmente). */
    public int insert(Register<E> reg) {
        if (reg == null) return -1;
        if (loadFactor() > 0.75) rehash();
        int index = hash(reg.getKey());
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == reg.getKey()) {
                table[index].removeNode(aux.dato);
                count--;
                break;
            }
            aux = aux.next;
        }
        table[index].addLast(reg);
        count++;
        return index;
    }

    public Register<E> search(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) return aux.dato;
            aux = aux.next;
        }
        return null;
    }

    /** Índice donde caería una clave (para visualización aunque no exista). */
    public int indexFor(int key) { return hash(key); }

    public boolean delete(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                table[index].removeNode(aux.dato);
                count--;
                return true;
            }
            aux = aux.next;
        }
        return false;
    }

    public int chainSize(int index) {
        if (index < 0 || index >= size) return 0;
        return table[index].size();
    }

    public ListLinked<Register<E>> getChain(int index) { return table[index]; }

    public void clearTable() {
        for (int i = 0; i < size; i++) table[i] = new ListLinked<>();
        this.count = 0;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.print(i + ": ");
            if (table[i].isEmptyList()) { System.out.println("vacio"); continue; }
            ListLinked.Node<Register<E>> aux = table[i].getFirstNode();
            while (aux != null) { System.out.print(aux.dato + " -> "); aux = aux.next; }
            System.out.println("null");
        }
    }
}
