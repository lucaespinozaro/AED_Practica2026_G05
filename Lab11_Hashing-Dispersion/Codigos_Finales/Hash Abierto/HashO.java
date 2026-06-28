public class HashO<E> {
    private ListLinked<Register<E>>[] table;
    private int size;
    private int count;

    @SuppressWarnings("unchecked")
    public HashO(int initialSize) {
        if (!isPrime(initialSize)) {
            this.size = nextPrime(initialSize);
        } else {
            this.size = initialSize;
        }
        this.table = new ListLinked[this.size];
        for (int i = 0; i < this.size; i++) {
            table[i] = new ListLinked<>();
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

    @SuppressWarnings("unchecked")
    private void rehash() {
        int newSize = nextPrime(size * 2);
        ListLinked<Register<E>>[] oldTable = table;
        int oldSize = size;
        this.size = newSize;
        this.table = new ListLinked[newSize];
        for (int i = 0; i < newSize; i++) {
            table[i] = new ListLinked<>();
        }
        this.count = 0;
        for (int i = 0; i < oldSize; i++) {
            ListLinked.Node<Register<E>> aux = oldTable[i].getFirstNode();
            while (aux != null) {
                insertInternal(aux.dato);
                aux = aux.next;
            }
        }
    }

    private void insertInternal(Register<E> reg) {
        int index = hash(reg.getKey());
        table[index].addLast(reg);
        count++;
    }

    public void insert(Register<E> reg) {
        if (reg == null) return;
        if (loadFactor() > 0.75) {
            rehash();
        }
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
    }

    public Register<E> search(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                return aux.dato;
            }
            aux = aux.next;
        }
        return null;
    }

    public void delete(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                table[index].removeNode(aux.dato);
                count--;
                return;
            }
            aux = aux.next;
        }
    }

    public int[] locate(int key) {
        int index = hash(key);
        ListLinked.Node<Register<E>> aux = table[index].getFirstNode();
        int pos = 0;
        while (aux != null) {
            if (aux.dato.getKey() == key) {
                return new int[]{index, pos};
            }
            aux = aux.next;
            pos++;
        }
        return null;
    }

    public int chainSize(int index) {
        if (index < 0 || index >= size) return 0;
        return table[index].size();
    }

    public void clearTable() {
        for (int i = 0; i < size; i++) {
            table[i] = new ListLinked<>();
        }
        this.count = 0;
    }

    public void printTable() {
        for (int i = 0; i < size; i++) {
            System.out.print(i + ": ");
            if (table[i].isEmptyList()) {
                System.out.println("vacio");
            } else {
                ListLinked.Node<Register<E>> aux = table[i].getFirstNode();
                while (aux != null) {
                    System.out.print(aux.dato + " -> ");
                    aux = aux.next;
                }
                System.out.println("null");
            }
        }
    }
}
