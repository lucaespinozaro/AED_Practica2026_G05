package graph;

// Clase base usada en Actividad 3 y todos los Ejercicios
public class Edge<E extends Comparable<E>> implements Comparable<Edge<E>> {
    private Vertex<E> destination;
    private int weight;

    public Edge(Vertex<E> destination) {
        this(destination, 1);
    }

    // --- EJERCICIO 1: constructor con peso para grafo ponderado ---
    public Edge(Vertex<E> destination, int weight) {
        this.destination = destination;
        this.weight = weight;
    }

    public Vertex<E> getDestination() {
        return destination;
    }

    // --- EJERCICIO 1: getter de peso ---
    public int getWeight() {
        return weight;
    }

    public void setDestination(Vertex<E> destination) {
        this.destination = destination;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    @Override
    public int compareTo(Edge<E> o) {
        return this.destination.getData().compareTo(o.destination.getData());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Edge)) return false;
        Edge<E> e = (Edge<E>) o;
        return this.destination.equals(e.destination);
    }

    @Override
    public int hashCode() {
        return destination.hashCode();
    }

    @Override
    public String toString() {
        // --- EJERCICIO 1: mostrar peso si no es 1 (arista ponderada) ---
        return weight != 1
                ? destination.toString() + "(" + weight + ")"
                : destination.toString();
    }
}
