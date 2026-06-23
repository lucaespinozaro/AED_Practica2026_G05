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

    public void insert(Register<E> reg) {
        if (reg == null) return;
        int index = hash(reg.getKey());
        int start = index;
        int targetIndex = -1;
        do {
            if (table[index].register == null) {
                if (targetIndex == -1) targetIndex = index;
                break;
            }
            if (table[index].isAvailable) {
                if (targetIndex == -1) targetIndex = index;
            } else if (table[index].register.getKey() == reg.getKey()) {
                table[index].register = reg;
                return;
            }
            index = (index + 1) % size;
        } while (index != start);

        if (targetIndex != -1) {
            table[targetIndex].register = reg;
            table[targetIndex].isAvailable = false;
        } else {
            System.out.println("Error: Tabla hash llena");
        }
    }

    public Register<E> search(int key) {
        int index = hash(key);
        int start = index;
        do {
            if (table[index].register == null) {
                return null;
            }
            if (!table[index].isAvailable && table[index].register.getKey() == key) {
                return table[index].register;
            }
            index = (index + 1) % size;
        } while (index != start);
        return null;
    }

    public void delete(int key) {
        int index = hash(key);
        int start = index;
        do {
            if (table[index].register == null) {
                return;
            }
            if (!table[index].isAvailable && table[index].register.getKey() == key) {
                table[index].isAvailable = true;
                return;
            }
            index = (index + 1) % size;
        } while (index != start);
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
