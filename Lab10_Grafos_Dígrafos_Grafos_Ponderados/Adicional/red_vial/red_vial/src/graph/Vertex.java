package graph;

public class Vertex<E extends Comparable<E>> {
    private E data;

    public Vertex(E data) { this.data = data; }
    public E getData() { return data; }
    public void setData(E data) { this.data = data; }

    @Override public String toString() { return data.toString(); }

    @Override @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vertex)) return false;
        return this.data.equals(((Vertex<E>) o).data);
    }

    @Override public int hashCode() { return data.hashCode(); }
}
