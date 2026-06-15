package ejercicio2;

// EJERCICIO 2 – Red de ciudades usando JGraphT
// Requiere: jgrapht-core-*.jar en el classpath

import graph.GraphLink;
import listlinked.ListLinked;

import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * EJERCICIO 2 – Modela una red de ciudades conectadas por carreteras
 * usando un grafo ponderado no dirigido con la librería JGraphT.
 */
public class CityNetwork {

  // Grafo ponderado no dirigido con JGraphT
  private Graph<String, DefaultWeightedEdge> graph;

  public CityNetwork() {
    // SimpleWeightedGraph: no dirigido, sin lazos, sin aristas múltiples
    this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
  }

  /**
   * Agrega una ciudad (vértice) al grafo.
   */
  public void addCity(String city) {
    if (city == null || city.trim().isEmpty()) {
      return;
    }
    graph.addVertex(city);
  }

  /**
   * Agrega una carretera (arista ponderada) entre dos ciudades
   * con la distancia en kilómetros indicada.
   */
  public void addRoad(String cityA, String cityB, double distanceKm) {
    if (cityA == null || cityB == null) {
      return;
    }

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
   * Muestra la lista de ciudades registradas.
   */
  public void showCities() {
    System.out.println("=== Ciudades registradas (" + graph.vertexSet().size() + ") ===");
    for (String city : graph.vertexSet()) {
      System.out.println("  - " + city);
    }
  }

  /**
   * Muestra todas las carreteras (aristas) con su distancia.
   */
  public void showRoads() {
    System.out.println("=== Carreteras registradas (" + graph.edgeSet().size() + ") ===");
    for (DefaultWeightedEdge edge : graph.edgeSet()) {
      String source = graph.getEdgeSource(edge);
      String target = graph.getEdgeTarget(edge);
      double weight = graph.getEdgeWeight(edge);
      System.out.printf("  %s <--> %s : %.0f km%n", source, target, weight);
    }
  }

  /**
   * Calcula el camino más corto entre dos ciudades usando Dijkstra
   * y lo muestra usando ListLinked.
   */
  public void shortestPath(String origin, String destination) {
    System.out.println("=== Camino más corto: " + origin + " → " + destination + " ===");

    if (!graph.containsVertex(origin) || !graph.containsVertex(destination)) {
      System.out.println("  No existe camino entre " + origin + " y " + destination);
      return;
    }

    DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra =
      new DijkstraShortestPath<>(graph);

    GraphPath<String, DefaultWeightedEdge> path =
      dijkstra.getPath(origin, destination);

    if (path == null) {
      System.out.println("  No existe camino entre " + origin + " y " + destination);
      return;
    }

    ListLinked<String> ruta = new ListLinked<>();
    for (String city : path.getVertexList()) {
      ruta.addLast(city);
    }

    System.out.print("  Ruta: ");
    for (int i = 0; i < ruta.size(); i++) {
      System.out.print(ruta.get(i));
      if (i < ruta.size() - 1) {
        System.out.print(" → ");
      }
    }

    System.out.printf("%n  Costo total: %.0f km%n", path.getWeight());
  }

  // ---------------------------------------------------------------
  // Main de prueba con las ciudades del enunciado
  // ---------------------------------------------------------------
  public static void main(String[] args) {
    CityNetwork network = new CityNetwork();

    // Agregar ciudades del enunciado
    network.addCity("Arequipa");
    network.addCity("Cusco");
    network.addCity("Puno");
    network.addCity("Tacna");
    network.addCity("Moquegua");

    // Agregar carreteras con distancias del enunciado
    network.addRoad("Arequipa", "Cusco", 510);
    network.addRoad("Arequipa", "Moquegua", 230);
    network.addRoad("Moquegua", "Tacna", 160);
    network.addRoad("Cusco", "Puno", 390);
    network.addRoad("Puno", "Tacna", 420);

    System.out.println();
    network.showCities();
    System.out.println();
    network.showRoads();
    System.out.println();

    // Calcular caminos más cortos
    network.shortestPath("Arequipa", "Tacna");
    System.out.println();
    network.shortestPath("Cusco", "Tacna");
    System.out.println();
    network.shortestPath("Arequipa", "Puno");
  }
}
