public class HashC<E> {
    private static class Element<E> {
        Register<E> register;
        boolean isAvailable;

        public Element() {
            this.register = null;
            this.isAvailable = true;
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

    public int insert(Register<E> reg, boolean isQuadratic) {
        if (reg == null) return 0;
        int baseIndex = hash(reg.getKey());
        int i = 0;
        int probes = 0;
        int targetIndex = -1;

        do {
            int index = (baseIndex + (isQuadratic ? i * i : i)) % size;
            if (i > 0) probes++;

            if (table[index].register == null) {
                if (targetIndex == -1) targetIndex = index;
                break;
            }
            if (table[index].isAvailable) {
                if (targetIndex == -1) targetIndex = index;
            } else if (table[index].register.getKey() == reg.getKey()) {
                table[index].register = reg;
                return probes;
            }
            i++;
        } while (i < size);

        if (targetIndex != -1) {
            table[targetIndex].register = reg;
            table[targetIndex].isAvailable = false;
        } else {
            System.out.println("Error: Tabla hash llena");
        }
        return probes;
    }

    public Register<E> search(int key, boolean isQuadratic) {
        int baseIndex = hash(key);
        int i = 0;
        do {
            int index = (baseIndex + (isQuadratic ? i * i : i)) % size;
            if (table[index].register == null) {
                return null;
            }
            if (!table[index].isAvailable && table[index].register.getKey() == key) {
                return table[index].register;
            }
            i++;
        } while (i < size);
        return null;
    }

    public void delete(int key, boolean isQuadratic) {
        int baseIndex = hash(key);
        int i = 0;
        do {
            int index = (baseIndex + (isQuadratic ? i * i : i)) % size;
            if (table[index].register == null) {
                return;
            }
            if (!table[index].isAvailable && table[index].register.getKey() == key) {
                table[index].isAvailable = true;
                return;
            }
            i++;
        } while (i < size);
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            if (table[i].register != null && !table[i].isAvailable) {
                System.out.println(i + ": " + table[i].register);
            } else {
                System.out.println(i + ": vacio");
            }
        }
    }
}
