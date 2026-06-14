public class TestGraph {
    public static void main(String[] args) {
        GraphLink<String> g = new GraphLink<>();

        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");

        g.insertEdge("A", "B");
        g.insertEdge("A", "C");
        g.insertEdge("B", "D");

        System.out.println("--- Grafo Inicial ---");
        System.out.println(g);

        System.out.print("Recorrido DFS desde A: ");
        g.DFS("A");

        System.out.print("Recorrido BFS desde A: ");
        g.BFS("A");

        System.out.println("\n--- Eliminando arista (A, C) ---");
        g.removeEdge("A", "C");
        System.out.println(g);

        System.out.println("--- Eliminando vértice B (Eficiente) ---");
        g.removeVertex("B");
        System.out.println(g);
    }
}
