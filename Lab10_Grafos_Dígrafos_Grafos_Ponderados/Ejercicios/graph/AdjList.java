package graph;

import listlinked.ListLinked;

// Clase auxiliar usada desde Actividad 3 y todos los Ejercicios
public class AdjList<E extends Comparable<E>> implements Comparable<AdjList<E>> {
    private Vertex<E> vertex;
    private ListLinked<Edge<E>> edges;

    public AdjList(Vertex<E> vertex) {
        this.vertex = vertex;
        this.edges = new ListLinked<>();
    }

    public Vertex<E> getVertex() {
        return vertex;
    }

    public ListLinked<Edge<E>> getEdges() {
        return edges;
    }

    @Override
    public int compareTo(AdjList<E> o) {
        return this.vertex.getData().compareTo(o.vertex.getData());
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AdjList)) return false;
        AdjList<E> a = (AdjList<E>) o;
        return this.vertex.equals(a.vertex);
    }

    @Override
    public int hashCode() {
        return vertex.hashCode();
    }

    @Override
    public String toString() {
        return vertex.toString() + " -> " + edges.toString();
    }
}
