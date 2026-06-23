public class Register<E> implements Comparable<Register<E>> {
    private int key;
    private E name;

    public Register(int key, E name) {
        this.key = key;
        this.name = name;
    }

    public int getKey() {
        return key;
    }

    public E getName() {
        return name;
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
        return "(" + key + ", " + name + ")";
    }
}
