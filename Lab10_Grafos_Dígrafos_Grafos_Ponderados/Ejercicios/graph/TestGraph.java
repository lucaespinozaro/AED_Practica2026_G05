package graph;
import listlinked.StackLink;
import java.util.ArrayList;


/**
 * Clase de prueba que valida los métodos de Actividad 3
 * y de los Ejercicios 1, 3 y 4.
 */
public class TestGraph {

    public static void main(String[] args) {

        // ============================================================
        // ACTIVIDAD 3 – Prueba base del grafo no dirigido
        // ============================================================
        System.out.println("========================================");
        System.out.println(" ACTIVIDAD 3 – Grafo no dirigido base");
        System.out.println("========================================");

        GraphLink<String> g = new GraphLink<>();
        g.insertVertex("A");
        g.insertVertex("B");
        g.insertVertex("C");
        g.insertVertex("D");

        g.insertEdge("A", "B");
        g.insertEdge("A", "C");
        g.insertEdge("B", "D");

        System.out.println("--- Grafo inicial ---");
        System.out.println(g);

        System.out.print("DFS desde A: ");
        g.DFS("A");

        System.out.print("BFS desde A: ");
        g.BFS("A");

        System.out.println("\n--- Eliminar arista (A, C) ---");
        g.removeEdge("A", "C");
        System.out.println(g);

        System.out.println("--- Eliminar vértice B ---");
        g.removeVertex("B");
        System.out.println(g);

        // ============================================================
        // EJERCICIO 1 – Grafo ponderado: insertEdgeWeight, shortPath,
        //               isConexo, Dijkstra
        // ============================================================
        System.out.println("========================================");
        System.out.println(" EJERCICIO 1 – Grafo ponderado");
        System.out.println("========================================");

        GraphLink<String> gPonderado = new GraphLink<>();
        gPonderado.insertVertex("Arequipa");
        gPonderado.insertVertex("Cusco");
        gPonderado.insertVertex("Puno");
        gPonderado.insertVertex("Tacna");
        gPonderado.insertVertex("Moquegua");

        // EJERCICIO 1 – Aristas con peso
        gPonderado.insertEdgeWeight("Arequipa",  "Cusco",    510);
        gPonderado.insertEdgeWeight("Arequipa",  "Moquegua", 230);
        gPonderado.insertEdgeWeight("Moquegua",  "Tacna",    160);
        gPonderado.insertEdgeWeight("Cusco",     "Puno",     390);
        gPonderado.insertEdgeWeight("Puno",      "Tacna",    420);

        System.out.println("--- Grafo ponderado ---");
        System.out.println(gPonderado);

        // EJERCICIO 1 – isConexo
        System.out.println("¿Es conexo? " + gPonderado.isConexo());

        // EJERCICIO 1 – shortPath (ArrayList)
        ArrayList<String> path = gPonderado.shortPath("Arequipa", "Tacna");
        System.out.println("Ruta más corta Arequipa → Tacna (shortPath): " + path);

        // EJERCICIO 1 – Dijkstra (Stack)
        StackLink<String> stack = gPonderado.Dijkstra("Cusco", "Tacna");
        System.out.print("Ruta más corta Cusco → Tacna (Dijkstra Stack): ");
        StackLink<String> temp = new StackLink<>();
        temp.addAll(stack);
        System.out.println(temp);

        // EJERCICIO 1 – Grafo no conexo
        GraphLink<String> gNoConexo = new GraphLink<>();
        gNoConexo.insertVertex("X");
        gNoConexo.insertVertex("Y");
        gNoConexo.insertVertex("Z"); // Z queda aislado
        gNoConexo.insertEdge("X", "Y");
        System.out.println("\n¿Grafo {X-Y, Z} es conexo? " + gNoConexo.isConexo());

        // ============================================================
        // EJERCICIO 3 – searchVertex, searchEdge, adjacentVertices
        // ============================================================
        System.out.println("\n========================================");
        System.out.println(" EJERCICIO 3 – Métodos de búsqueda");
        System.out.println("========================================");

        GraphLink<String> g3 = new GraphLink<>();
        g3.insertVertex("A");
        g3.insertVertex("B");
        g3.insertVertex("C");
        g3.insertVertex("D");
        g3.insertEdge("A", "B");
        g3.insertEdge("A", "C");
        g3.insertEdge("B", "D");
        g3.insertEdge("C", "D");

        // EJERCICIO 3 – searchVertex
        System.out.println("¿Existe vértice A? " + g3.searchVertex("A"));
        System.out.println("¿Existe vértice Z? " + g3.searchVertex("Z"));

        // EJERCICIO 3 – searchEdge
        System.out.println("¿Existe arista A-B? " + g3.searchEdge("A", "B"));
        System.out.println("¿Existe arista A-D? " + g3.searchEdge("A", "D"));

        // EJERCICIO 3 – adjacentVertices
        System.out.println("Adyacentes de A: " + g3.adjacentVertices("A"));
        System.out.println("Adyacentes de D: " + g3.adjacentVertices("D"));

        // ============================================================
        // EJERCICIO 4 – isIsomorfo, isPlanar, isConexo,
        //               isAutoComplementario
        // ============================================================
        System.out.println("\n========================================");
        System.out.println(" EJERCICIO 4 – Propiedades de grafos");
        System.out.println("========================================");

        // EJERCICIO 4 – Grafo A: triángulo (K3)
        GraphLink<String> gA = new GraphLink<>();
        gA.insertVertex("1"); gA.insertVertex("2"); gA.insertVertex("3");
        gA.insertEdge("1", "2");
        gA.insertEdge("2", "3");
        gA.insertEdge("1", "3");

        // EJERCICIO 4 – Grafo B: otro triángulo (isomorfo a gA)
        GraphLink<String> gB = new GraphLink<>();
        gB.insertVertex("X"); gB.insertVertex("Y"); gB.insertVertex("Z");
        gB.insertEdge("X", "Y");
        gB.insertEdge("Y", "Z");
        gB.insertEdge("X", "Z");

        // EJERCICIO 4 – Grafo C: camino de 3 vértices (NO isomorfo a gA)
        GraphLink<String> gC = new GraphLink<>();
        gC.insertVertex("P"); gC.insertVertex("Q"); gC.insertVertex("R");
        gC.insertEdge("P", "Q");
        gC.insertEdge("Q", "R");

        System.out.println("--- isIsomorfo ---");
        System.out.println("gA (K3) isomorfo a gB (K3): " + gA.isIsomorfo(gB)); // true
        System.out.println("gA (K3) isomorfo a gC (camino): " + gA.isIsomorfo(gC)); // false

        System.out.println("\n--- isPlanar ---");
        System.out.println("gA (K3) es planar: " + gA.isPlanar());    // true (triángulo)
        System.out.println("gB (K3) es planar: " + gB.isPlanar());    // true

        // K5 – grafo completo de 5 vértices (NO planar: E=10 > 3*5-6=9)
        GraphLink<String> k5 = new GraphLink<>();
        String[] vk5 = {"v1","v2","v3","v4","v5"};
        for (String v : vk5) k5.insertVertex(v);
        for (int i = 0; i < vk5.length; i++)
            for (int j = i + 1; j < vk5.length; j++)
                k5.insertEdge(vk5[i], vk5[j]);
        System.out.println("K5 (completo 5) es planar: " + k5.isPlanar()); // false

        System.out.println("\n--- isConexo ---");
        System.out.println("gA es conexo: " + gA.isConexo());   // true
        System.out.println("gC es conexo: " + gC.isConexo());   // true
        GraphLink<String> gDisconexo = new GraphLink<>();
        gDisconexo.insertVertex("M"); gDisconexo.insertVertex("N");
        gDisconexo.insertVertex("O"); // O aislado
        gDisconexo.insertEdge("M", "N");
        System.out.println("Grafo {M-N, O aislado} es conexo: " + gDisconexo.isConexo()); // false

        System.out.println("\n--- isAutoComplementario ---");
        // P4 (camino de 4 nodos) es auto-complementario
        GraphLink<String> p4 = new GraphLink<>();
        p4.insertVertex("a"); p4.insertVertex("b");
        p4.insertVertex("c"); p4.insertVertex("d");
        p4.insertEdge("a", "b");
        p4.insertEdge("b", "c");
        p4.insertEdge("c", "d");
        System.out.println("P4 (camino 4) es auto-complementario: " + p4.isAutoComplementario()); // true

        // K3 NO es auto-complementario
        System.out.println("K3 es auto-complementario: " + gA.isAutoComplementario()); // false
    }
}
