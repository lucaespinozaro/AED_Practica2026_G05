/**
 * Tabla Hash CERRADA (Hashing Cerrado) con resolución de colisiones por
 * SONDEO LINEAL o SONDEO CUADRÁTICO, seleccionable en cada operación.
 */
public class HashC<E> {

    public static class Element<E> {
        public Register<E> register;
        public int mark; // 0 = vacío, 1 = ocupado, -1 = eliminado (tumba)
        public Element() { this.register = null; this.mark = 0; }
    }

    private Element<E>[] table;
    private int size;
    private int count;

    @SuppressWarnings("unchecked")
    public HashC(int initialSize) {
        this.size = isPrime(initialSize) ? initialSize : nextPrime(initialSize);
        this.table = new Element[this.size];
        for (int i = 0; i < this.size; i++) table[i] = new Element<>();
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
    public Element<E> getElement(int index) { return table[index]; }

    private int[] linearProbing(int key, boolean forInsert) {
        int baseIndex = hash(key);
        int index = baseIndex;
        int firstDeleted = -1;
        int probes = 0;
        do {
            if (table[index].mark == 0) {
                if (forInsert) {
                    int target = (firstDeleted != -1) ? firstDeleted : index;
                    return new int[]{target, probes};
                }
                return new int[]{-1, probes};
            }
            if (table[index].mark == -1) {
                if (forInsert && firstDeleted == -1) firstDeleted = index;
            } else if (table[index].mark == 1 && table[index].register.getKey() == key) {
                return new int[]{index, probes};
            }
            index = (index + 1) % size;
            probes++;
        } while (index != baseIndex);

        if (forInsert && firstDeleted != -1) return new int[]{firstDeleted, probes};
        return new int[]{-1, probes};
    }

    private int[] quadraticProbing(int key, boolean forInsert) {
        int baseIndex = hash(key);
        int firstDeleted = -1;
        int probes = 0;
        for (int i = 0; i < size; i++) {
            int index = (baseIndex + i * i) % size;
            if (table[index].mark == 0) {
                if (forInsert) {
                    int target = (firstDeleted != -1) ? firstDeleted : index;
                    return new int[]{target, probes};
                }
                return new int[]{-1, probes};
            }
            if (table[index].mark == -1) {
                if (forInsert && firstDeleted == -1) firstDeleted = index;
            } else if (table[index].mark == 1 && table[index].register.getKey() == key) {
                return new int[]{index, probes};
            }
            if (i > 0) probes++;
        }
        if (forInsert && firstDeleted != -1) return new int[]{firstDeleted, probes};
        return new int[]{-1, probes};
    }

    /** Devuelve la secuencia COMPLETA de índices visitados durante el sondeo (para animar). */
    public int[] probeSequence(int key, boolean isQuadratic) {
        int baseIndex = hash(key);
        java.util.List<Integer> seq = new java.util.ArrayList<>();
        if (!isQuadratic) {
            int index = baseIndex;
            do {
                seq.add(index);
                if (table[index].mark == 0) break;
                if (table[index].mark == 1 && table[index].register.getKey() == key) break;
                index = (index + 1) % size;
            } while (index != baseIndex && seq.size() <= size);
        } else {
            for (int i = 0; i < size; i++) {
                int index = (baseIndex + i * i) % size;
                seq.add(index);
                if (table[index].mark == 0) break;
                if (table[index].mark == 1 && table[index].register.getKey() == key) break;
            }
        }
        int[] arr = new int[seq.size()];
        for (int i = 0; i < seq.size(); i++) arr[i] = seq.get(i);
        return arr;
    }

    @SuppressWarnings("unchecked")
    private void rehash(boolean isQuadratic) {
        int newSize = nextPrime(size * 2);
        Element<E>[] oldTable = table;
        int oldSize = size;
        this.size = newSize;
        this.table = new Element[newSize];
        for (int i = 0; i < newSize; i++) table[i] = new Element<>();
        this.count = 0;
        for (int i = 0; i < oldSize; i++)
            if (oldTable[i].mark == 1) insertInternal(oldTable[i].register, isQuadratic);
    }

    private void insertInternal(Register<E> reg, boolean isQuadratic) {
        int[] pr = isQuadratic ? quadraticProbing(reg.getKey(), true) : linearProbing(reg.getKey(), true);
        int target = pr[0];
        if (target != -1) { table[target].register = reg; table[target].mark = 1; count++; }
    }

    /** Inserta y devuelve {indiceFinal, numeroDeSondeos}. */
    public int[] insert(Register<E> reg, boolean isQuadratic) {
        if (reg == null) return new int[]{-1, 0};
        if (loadFactor() > 0.75) rehash(isQuadratic);
        int[] pr = isQuadratic ? quadraticProbing(reg.getKey(), true) : linearProbing(reg.getKey(), true);
        int target = pr[0], probes = pr[1];
        if (target != -1) {
            if (table[target].mark == 1 && table[target].register.getKey() == reg.getKey()) {
                table[target].register = reg;
            } else {
                table[target].register = reg;
                table[target].mark = 1;
                count++;
            }
        }
        return new int[]{target, probes};
    }

    public Register<E> search(int key, boolean isQuadratic) {
        int[] pr = isQuadratic ? quadraticProbing(key, false) : linearProbing(key, false);
        int index = pr[0];
        if (index != -1 && table[index].mark == 1) return table[index].register;
        return null;
    }

    public boolean delete(int key, boolean isQuadratic) {
        int[] pr = isQuadratic ? quadraticProbing(key, false) : linearProbing(key, false);
        int index = pr[0];
        if (index != -1 && table[index].mark == 1) { table[index].mark = -1; count--; return true; }
        return false;
    }

    public void clearTable() {
        for (int i = 0; i < size; i++) { table[i].register = null; table[i].mark = 0; }
        this.count = 0;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i].mark == 1) System.out.println(i + ": " + table[i].register);
            else if (table[i].mark == -1) System.out.println(i + ": eliminado");
            else System.out.println(i + ": vacio");
        }
    }
}
