public class TestGraph2 {
    public static void main(String[] args) {
        GraphLink<String> g = new GraphLink<>();
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");
        g.insertVertex("E");
        g.insertVertex("F");
        g.insertVertex("G");
        g.insertVertex("H");

        g.insertEdge("C", "E");
        g.insertEdge("A", "D");
        g.insertEdge("A", "B");
        g.insertEdge("B", "E");
        g.insertEdge("A", "C");
        g.insertEdge("C", "B");
        g.insertEdge("H", "B");
        g.insertEdge("B", "G");
        g.insertEdge("C", "F");
        g.insertEdge("B", "D");
        g.insertEdge("H", "F");
        g.insertEdge("C", "G");
        g.insertEdge("A", "H");
        g.insertEdge("D", "G");
        g.insertEdge("H", "D");
        g.insertEdge("G", "E");

        System.out.println("--- Estructura de las Listas de Adyacencia ---");
        System.out.println(g);

        System.out.print("Recorrido en Profundidad (DFS) desde A: ");
        g.DFS("A");

        System.out.print("Recorrido en Anchura (BFS) desde A: ");
        g.BFS("A");
    }
}
