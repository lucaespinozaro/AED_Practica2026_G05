package ejercicio2;

// EJERCICIO 2 – Red de ciudades usando JGraphT
// Requiere: jgrapht-core-*.jar en el classpath
// Descarga: https://jgrapht.org/ o Maven: org.jgrapht:jgrapht-core:1.5.2

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

import java.util.List;
import java.util.Set;

/**
 * EJERCICIO 2 – Modela una red de ciudades conectadas por carreteras
 * usando un grafo ponderado no dirigido con la librería JGraphT.
 */
public class CityNetwork {

    // EJERCICIO 2 – Grafo ponderado no dirigido con JGraphT
    private Graph<String, DefaultWeightedEdge> graph;

    public CityNetwork() {
        // EJERCICIO 2 – SimpleWeightedGraph: no dirigido, sin lazos, sin aristas múltiples
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    /**
     * EJERCICIO 2 – Agrega una ciudad (vértice) al grafo.
     */
    public void addCity(String city) {
        graph.addVertex(city);
    }

    /**
     * EJERCICIO 2 – Agrega una carretera (arista ponderada) entre dos ciudades
     * con la distancia en kilómetros indicada.
     */
    public void addRoad(String cityA, String cityB, double distanceKm) {
        if (!graph.containsVertex(cityA) || !graph.containsVertex(cityB)) {
            System.out.println("Error: una o ambas ciudades no existen en el grafo.");
            return;
        }
        DefaultWeightedEdge edge = graph.addEdge(cityA, cityB);
        if (edge != null) {
            graph.setEdgeWeight(edge, distanceKm);
        }
    }

    /**
     * EJERCICIO 2 – Muestra la lista de ciudades registradas.
     */
    public void showCities() {
        Set<String> cities = graph.vertexSet();
        System.out.println("=== Ciudades registradas (" + cities.size() + ") ===");
        for (String city : cities) {
            System.out.println("  - " + city);
        }
    }

    /**
     * EJERCICIO 2 – Muestra todas las carreteras (aristas) con su distancia.
     */
    public void showRoads() {
        Set<DefaultWeightedEdge> edges = graph.edgeSet();
        System.out.println("=== Carreteras registradas (" + edges.size() + ") ===");
        for (DefaultWeightedEdge edge : edges) {
            String source = graph.getEdgeSource(edge);
            String target = graph.getEdgeTarget(edge);
            double weight = graph.getEdgeWeight(edge);
            System.out.printf("  %s <--> %s : %.0f km%n", source, target, weight);
        }
    }

    /**
     * EJERCICIO 2 – Calcula el camino más corto entre dos ciudades
     * usando el algoritmo de Dijkstra de JGraphT y muestra el resultado.
     */
    public void shortestPath(String origin, String destination) {
        System.out.println("=== Camino más corto: " + origin + " → " + destination + " ===");

        // EJERCICIO 2 – DijkstraShortestPath de JGraphT
        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
                new DijkstraShortestPath<>(graph);

        GraphPath<String, DefaultWeightedEdge> path =
                dijkstra.getPath(origin, destination);

        if (path == null) {
            System.out.println("  No existe camino entre " + origin + " y " + destination);
            return;
        }

        List<String> vertices = path.getVertexList();
        double totalCost = path.getWeight();

        System.out.print("  Ruta: ");
        for (int i = 0; i < vertices.size(); i++) {
            System.out.print(vertices.get(i));
            if (i < vertices.size() - 1) System.out.print(" → ");
        }
        System.out.printf("%n  Costo total: %.0f km%n", totalCost);
    }

    // ---------------------------------------------------------------
    // EJERCICIO 2 – Main de prueba con las ciudades del enunciado
    // ---------------------------------------------------------------
    public static void main(String[] args) {
        CityNetwork network = new CityNetwork();

        // EJERCICIO 2 – Agregar ciudades del enunciado
        network.addCity("Arequipa");
        network.addCity("Cusco");
        network.addCity("Puno");
        network.addCity("Tacna");
        network.addCity("Moquegua");

        // EJERCICIO 2 – Agregar carreteras con distancias del enunciado
        network.addRoad("Arequipa", "Cusco",    510);
        network.addRoad("Arequipa", "Moquegua", 230);
        network.addRoad("Moquegua", "Tacna",    160);
        network.addRoad("Cusco",    "Puno",     390);
        network.addRoad("Puno",     "Tacna",    420);

        System.out.println();
        network.showCities();
        System.out.println();
        network.showRoads();
        System.out.println();

        // EJERCICIO 2 – Calcular caminos más cortos
        network.shortestPath("Arequipa", "Tacna");
        System.out.println();
        network.shortestPath("Cusco", "Tacna");
        System.out.println();
        network.shortestPath("Arequipa", "Puno");
    }
}
