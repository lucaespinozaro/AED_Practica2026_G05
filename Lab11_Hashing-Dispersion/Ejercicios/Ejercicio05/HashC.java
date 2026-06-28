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
    private int count;

    @SuppressWarnings("unchecked")
    public HashC(int initialSize) {
        if (!isPrime(initialSize)) {
            this.size = nextPrime(initialSize);
        } else {
            this.size = initialSize;
        }
        this.table = new Element[this.size];
        for (int i = 0; i < this.size; i++) {
            table[i] = new Element<>();
        }
        this.count = 0;
    }

    private int hash(int key) {
        return Math.abs(key) % size;
    }

    private boolean isPrime(int n) {
        if (n < 2) return false;
        for (int i = 2; (long) i * i <= n; i++) {
            if (n % i == 0) return false;
        }
        return true;
    }

    private int nextPrime(int n) {
        int prime = n;
        if (prime % 2 == 0) prime++;
        while (!isPrime(prime)) {
            prime += 2;
        }
        return prime;
    }

    public double loadFactor() {
        return (double) count / size;
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

    @SuppressWarnings("unchecked")
    private void rehash(boolean isQuadratic) {
        int newSize = nextPrime(size * 2);
        Element<E>[] oldTable = table;
        int oldSize = size;
        this.size = newSize;
        this.table = new Element[newSize];
        for (int i = 0; i < newSize; i++) {
            table[i] = new Element<>();
        }
        this.count = 0;
        for (int i = 0; i < oldSize; i++) {
            if (oldTable[i].mark == 1) {
                insertInternal(oldTable[i].register, isQuadratic);
            }
        }
    }

    private void insertInternal(Register<E> reg, boolean isQuadratic) {
        int[] probeResult = isQuadratic ? quadraticProbing(reg.getKey(), true) : linearProbing(reg.getKey(), true);
        int targetIndex = probeResult[0];
        if (targetIndex != -1) {
            table[targetIndex].register = reg;
            table[targetIndex].mark = 1;
            count++;
        }
    }

    public int insert(Register<E> reg, boolean isQuadratic) {
        if (reg == null) return 0;
        if (loadFactor() > 0.75) {
            rehash(isQuadratic);
        }
        int[] probeResult = isQuadratic ? quadraticProbing(reg.getKey(), true) : linearProbing(reg.getKey(), true);
        int targetIndex = probeResult[0];
        int probes = probeResult[1];
        if (targetIndex != -1) {
            if (table[targetIndex].mark == 1 && table[targetIndex].register.getKey() == reg.getKey()) {
                table[targetIndex].register = reg;
            } else {
                table[targetIndex].register = reg;
                table[targetIndex].mark = 1;
                count++;
            }
        } else {
            System.out.println("Error: Tabla hash llena");
        }
        return probes;
    }

    public Register<E> search(int key, boolean isQuadratic) {
        int[] probeResult = isQuadratic ? quadraticProbing(key, false) : linearProbing(key, false);
        int index = probeResult[0];
        if (index != -1 && table[index].mark == 1) {
            return table[index].register;
        }
        return null;
    }

    public boolean delete(int key, boolean isQuadratic) {
        int[] probeResult = isQuadratic ? quadraticProbing(key, false) : linearProbing(key, false);
        int index = probeResult[0];
        if (index != -1 && table[index].mark == 1) {
            table[index].mark = -1;
            count--;
            return true;
        }
        return false;
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
