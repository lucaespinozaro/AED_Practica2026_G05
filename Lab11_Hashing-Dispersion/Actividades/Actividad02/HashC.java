public class HashC<E> {
    private static class Element<E> {
        Register<E> register;
        int mark;

        public Element() {
            this.register = null;
            this.mark = 0;
        }
    }

    private Element<E>[] table;
    private int size;

    @SuppressWarnings("unchecked")
    public HashC(int size) {
        this.size = size;
        this.table = new Element[size];
        for (int i = 0; i < size; i++) {
            table[i] = new Element<>();
        }
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

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
                if (forInsert && firstDeleted == -1) {
                    firstDeleted = index;
                }
            } else if (table[index].mark == 1 && table[index].register.getKey() == key) {
                return new int[]{index, probes};
            }
            index = (index + 1) % size;
            probes++;
        } while (index != baseIndex);

        if (forInsert && firstDeleted != -1) {
            return new int[]{firstDeleted, probes};
        }
        return new int[]{-1, probes};
    }

    public void insert(Register<E> reg) {
        if (reg == null) return;
        int[] probeResult = linearProbing(reg.getKey(), true);
        int targetIndex = probeResult[0];
        if (targetIndex != -1) {
            if (table[targetIndex].mark == 1 && table[targetIndex].register.getKey() == reg.getKey()) {
                table[targetIndex].register = reg;
            } else {
                table[targetIndex].register = reg;
                table[targetIndex].mark = 1;
            }
        } else {
            System.out.println("Error: Tabla hash llena");
        }
    }

    public Register<E> search(int key) {
        int[] probeResult = linearProbing(key, false);
        int index = probeResult[0];
        if (index != -1 && table[index].mark == 1) {
            return table[index].register;
        }
        return null;
    }

    public void delete(int key) {
        int[] probeResult = linearProbing(key, false);
        int index = probeResult[0];
        if (index != -1 && table[index].mark == 1) {
            table[index].mark = -1;
        }
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i].mark == 1) {
                System.out.println(i + ": " + table[i].register);
            } else if (table[i].mark == -1) {
                System.out.println(i + ": eliminado");
            } else {
                System.out.println(i + ": vacio");
            }
        }
    }
}
