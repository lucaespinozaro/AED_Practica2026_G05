public class HashC<E> {
    public enum Status { EMPTY, OCCUPIED, DELETED }

    public static class Entry<E> {
        Register<E> register;
        Status status;

        public Entry() {
            this.register = null;
            this.status = Status.EMPTY;
        }
    }

    private Entry<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashC(int size) {
        this.size = size;
        this.table = new Entry[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Entry<>();
        }
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

    public int insert(Register<E> reg) {
        if (reg == null) return 0;
        int baseIndex = hash(reg.getKey());
        int index = baseIndex;
        int firstDeleted = -1;
        int probes = 0;
        do {
            if (table[index].status == Status.EMPTY) {
                int target = (firstDeleted != -1) ? firstDeleted : index;
                table[target].register = reg;
                table[target].status = Status.OCCUPIED;
                return probes;
            }
            if (table[index].status == Status.DELETED) {
                if (firstDeleted == -1) {
                    firstDeleted = index;
                }
            } else if (table[index].status == Status.OCCUPIED && table[index].register.getKey() == reg.getKey()) {
                table[index].register = reg;
                return probes;
            }
            index = (index + 1) % size;
            probes++;
        } while (index != baseIndex);

        if (firstDeleted != -1) {
            table[firstDeleted].register = reg;
            table[firstDeleted].status = Status.OCCUPIED;
            return probes;
        }
        System.out.println("Error: Tabla hash llena");
        return probes;
    }

    public Register<E> search(int key) {
        int baseIndex = hash(key);
        int index = baseIndex;
        do {
            if (table[index].status == Status.EMPTY) {
                return null;
            }
            if (table[index].status == Status.OCCUPIED && table[index].register.getKey() == key) {
                return table[index].register;
            }
            index = (index + 1) % size;
        } while (index != baseIndex);
        return null;
    }

    public boolean delete(int key) {
        int baseIndex = hash(key);
        int index = baseIndex;
        do {
            if (table[index].status == Status.EMPTY) {
                return false;
            }
            if (table[index].status == Status.OCCUPIED && table[index].register.getKey() == key) {
                table[index].status = Status.DELETED;
                return true;
            }
            index = (index + 1) % size;
        } while (index != baseIndex);
        return false;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i].status == Status.OCCUPIED) {
                System.out.println("  Indice " + i + ": OCCUPIED " + table[i].register);
            } else if (table[i].status == Status.DELETED) {
                System.out.println("  Indice " + i + ": DELETED");
            } else {
                System.out.println("  Indice " + i + ": EMPTY");
            }
        }
    }
}
