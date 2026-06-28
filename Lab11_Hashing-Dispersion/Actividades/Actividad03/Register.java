package hash;

public class Register<E> implements Comparable<Register<E>> {
    private int key;
    private E value;

    public Register(int key, E value) {
        this.key = key;
        this.value = value;
    }

    public int getKey() {
        return key;
    }

    public E getValue() {
        return value;
    }

    @Override
    public int compareTo(Register<E> other) {
        return Integer.compare(this.key, other.key);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Register<?> other = (Register<?>) obj;
        return this.key == other.key;
    }

    @Override
    public String toString() {
        return "(" + key + ", " + value + ")";
    }
}
