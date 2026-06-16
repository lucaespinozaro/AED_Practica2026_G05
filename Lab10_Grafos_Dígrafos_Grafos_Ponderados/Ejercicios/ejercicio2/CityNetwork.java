import listlinked.ListLinked;
import org.jgrapht.Graph;
import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleWeightedGraph;

/**
 * EJERCICIO 2 – Modela una red de ciudades y carreteras ponderadas.
 * Se utiliza un grafo valorado no dirigido de la librería JGraphT.
 */
public class CityNetwork {

    private Graph<String, DefaultWeightedEdge> graph;

    public CityNetwork() {
        // Se instancia un grafo no dirigido que prohíbe lazos y aristas múltiples
        this.graph = new SimpleWeightedGraph<>(DefaultWeightedEdge.class);
    }

    /**
     * Permite registrar una nueva ciudad como vértice del grafo.
     * Realiza el control de nulidad y cadenas vacías antes de la inserción.
     */
    public void addCity(String city) {
        if (city == null || city.trim().isEmpty()) {
            return;
        }
        graph.addVertex(city);
    }

    /**
     * Establece una arista ponderada (carretera) entre dos vértices (ciudades).
     * Modifica el peso por defecto de la arista para almacenar la distancia en km.
     */
    public void addRoad(String cityA, String cityB, double distanceKm) {
        if (cityA == null || cityB == null) {
            return;
        }

        // Se valida la existencia previa de ambos vértices en la estructura
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
     * Imprime en la consola el listado completo de los vértices (ciudades)
     * almacenados actualmente en el conjunto del grafo.
     */
    public void showCities() {
        System.out.println("=== Ciudades registradas (" + graph.vertexSet().size() + ") ===");
        for (String city : graph.vertexSet()) {
            System.out.println("  - " + city);
        }
    }

    /**
     * Recupera y despliega en consola todas las aristas activas.
     * Muestra de forma iterativa el origen, destino y peso asignado a cada carretera.
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
     * Resuelve y retorna la trayectoria de costo mínimo mediante el algoritmo de Dijkstra.
     * Almacena los elementos de la ruta en la estructura manual ListLinked para su salida ordenada.
     */
    public void shortestPath(String origin, String destination) {
        System.out.println("=== Camino más corto: " + origin + " → " + destination + " ===");

        if (!graph.containsVertex(origin) || !graph.containsVertex(destination)) {
            System.out.println("  No existe camino entre " + origin + " y " + destination);
            return;
        }

        DijkstraShortestPath<String, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(graph);
        GraphPath<String, DefaultWeightedEdge> path = dijkstra.getPath(origin, destination);

        if (path == null) {
            System.out.println("  No existe camino entre " + origin + " y " + destination);
            return;
        }

        // Se migran los vértices del camino hacia la estructura de lista enlazada propia
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

    public static void main(String[] args) {
        CityNetwork network = new CityNetwork();

        network.addCity("Arequipa");
        network.addCity("Cusco");
        network.addCity("Puno");
        network.addCity("Tacna");
        network.addCity("Moquegua");

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

        network.shortestPath("Arequipa", "Tacna");
        System.out.println();
        network.shortestPath("Cusco", "Tacna");
        System.out.println();
        network.shortestPath("Arequipa", "Puno");
    }
}
