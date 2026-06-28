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
                if (forInsert && firstDeleted == -1) {
                    firstDeleted = index;
                }
            } else if (table[index].mark == 1 && table[index].register.getKey() == key) {
                return new int[]{index, probes};
            }
            if (i > 0) probes++;
        }
        if (forInsert && firstDeleted != -1) {
            return new int[]{firstDeleted, probes};
        }
        return new int[]{-1, probes};
    }

    public int insert(Register<E> reg, boolean isQuadratic) {
        if (reg == null) return 0;
        int[] probeResult = isQuadratic ? quadraticProbing(reg.getKey(), true) : linearProbing(reg.getKey(), true);
        int targetIndex = probeResult[0];
        int probes = probeResult[1];
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
        return probes;
    }

    public void clearTable() {
        for (int i = 0; i < size; i++) {
            table[i].register = null;
            table[i].mark = 0;
        }
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i].mark == 1) {
                System.out.println("  Indice " + i + ": " + table[i].register);
            } else {
                System.out.println("  Indice " + i + ": vacio");
            }
        }
    }
}
