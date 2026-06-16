import listlinked.StackLink;
import java.util.ArrayList;

public class TestGraph {
    public static void main(String[] args) {
        System.out.println("========================================");
        System.out.println(" EJERCICIO 1 y 3 – Grafo Ponderado Base ");
        System.out.println("========================================");

        GraphLink<String> gPonderado = new GraphLink<>();
        gPonderado.insertVertex("Arequipa");
        gPonderado.insertVertex("Cusco");
        gPonderado.insertVertex("Puno");
        gPonderado.insertVertex("Tacna");
        gPonderado.insertVertex("Moquegua");

        gPonderado.insertEdgeWeight("Arequipa", "Cusco", 510);
        gPonderado.insertEdgeWeight("Arequipa", "Moquegua", 230);
        gPonderado.insertEdgeWeight("Moquegua", "Tacna", 160);
        gPonderado.insertEdgeWeight("Cusco", "Puno", 390);
        gPonderado.insertEdgeWeight("Puno", "Tacna", 420);

        System.out.println(gPonderado);
        System.out.println("¿Es conexo?: " + gPonderado.isConexo());

        ArrayList<String> path = gPonderado.shortPath("Arequipa", "Tacna");
        System.out.println("shortPath (ArrayList) Arequipa -> Tacna: " + path);

        StackLink<String> stack = gPonderado.Dijkstra("Cusco", "Tacna");
        System.out.println("Dijkstra (StackLink) Cusco -> Tacna: " + stack);

        System.out.println("¿Existe vértice Cusco?: " + gPonderado.searchVertex("Cusco"));
        System.out.println("¿Existe arista Cusco-Puno?: " + gPonderado.searchEdge("Cusco", "Puno"));
        System.out.println("Vértices adyacentes a Arequipa: " + gPonderado.adjacentVertices("Arequipa"));

        System.out.println("\n========================================");
        System.out.println(" EJERCICIO 4 – Propiedades e Isomorfismo ");
        System.out.println("========================================");

        GraphLink<String> gA = new GraphLink<>();
        gA.insertVertex("1"); gA.insertVertex("2"); gA.insertVertex("3");
        gA.insertEdge("1", "2"); gA.insertEdge("2", "3"); gA.insertEdge("1", "3");

        GraphLink<String> gB = new GraphLink<>();
        gB.insertVertex("X"); gB.insertVertex("Y"); gB.insertVertex("Z");
        gB.insertEdge("X", "Y"); gB.insertEdge("Y", "Z"); gB.insertEdge("X", "Z");

        System.out.println("gA es isomorfo a gB (K3): " + gA.isIsomorfo(gB));
        System.out.println("gA es planar: " + gA.isPlanar());

        GraphLink<String> p4 = new GraphLink<>();
        p4.insertVertex("a"); p4.insertVertex("b"); p4.insertVertex("c"); p4.insertVertex("d");
        p4.insertEdge("a", "b"); p4.insertEdge("b", "c"); p4.insertEdge("c", "d");
        System.out.println("P4 es auto-complementario: " + p4.isAutoComplementario());
    }
}
