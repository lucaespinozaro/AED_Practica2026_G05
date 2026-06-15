package graph;

import listlinked.ListLinked;
import listlinked.QueueLink;
import listlinked.StackLink;

import java.util.ArrayList;

/**
 * GraphLink: Grafo no dirigido implementado con listas de adyacencia.
 * Incluye métodos de Actividad 3 + Ejercicios 1, 3 y 4.
 * (Ejercicio 2 usa JGraphT en su propia clase separada: CityNetwork.java)
 *
 * NOTA: No se usa HashMap ni ninguna clase de java.util.Map.
 * El algoritmo de Dijkstra usa ListLinked<DijkstraEntry<E>> para
 * almacenar distancias y predecesores.
 */
public class GraphLink<E extends Comparable<E>> {

    private ListLinked<AdjList<E>> graph;

    // ---------------------------------------------------------------
    // Clase interna auxiliar para Dijkstra (reemplaza los HashMap)
    // EJERCICIO 1 – Cada entrada guarda: vértice, distancia acumulada
    //               y predecesor en el camino más corto.
    // ---------------------------------------------------------------
    private static class DijkstraEntry<E> {
        E vertex;       // vértice al que corresponde esta entrada
        int dist;       // distancia mínima acumulada desde el origen
        E prev;         // predecesor en el camino más corto (null si es el origen)

        DijkstraEntry(E vertex, int dist, E prev) {
            this.vertex = vertex;
            this.dist   = dist;
            this.prev   = prev;
        }
    }

    // ---------------------------------------------------------------
    // ACTIVIDAD 3 — Estructura base del grafo
    // ---------------------------------------------------------------

    public GraphLink() {
        graph = new ListLinked<>();
    }

    /** Actividad 3 – Insertar vértice (sin duplicados). */
    public void insertVertex(E data) {
        if (data == null || findVertex(data) != null) return;
        Vertex<E> vertex = new Vertex<>(data);
        graph.addLast(new AdjList<>(vertex));
    }

    /** Actividad 3 – Buscar un AdjList por dato (uso interno). */
    private AdjList<E> findVertex(E data) {
        if (data == null) return null;
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            if (adj.getVertex().getData().equals(data)) return adj;
        }
        return null;
    }

    /** Actividad 3 – Insertar arista no dirigida (peso = 1 por defecto). */
    public void insertEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        v1.getEdges().addLast(new Edge<>(v2.getVertex()));
        v2.getEdges().addLast(new Edge<>(v1.getVertex()));
    }

    /** Actividad 3 – Eliminar vértice y todas sus aristas. */
    public void removeVertex(E data) {
        if (data == null) return;
        AdjList<E> targetAdj = findVertex(data);
        if (targetAdj == null) return;
        for (int i = 0; i < targetAdj.getEdges().size(); i++) {
            Edge<E> edge = targetAdj.getEdges().get(i);
            AdjList<E> neighborAdj = findVertex(edge.getDestination().getData());
            if (neighborAdj != null) {
                neighborAdj.getEdges().removeNode(new Edge<>(targetAdj.getVertex()));
            }
        }
        graph.removeNode(targetAdj);
    }

    /** Actividad 3 – Eliminar arista entre dos vértices. */
    public void removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
        v2.getEdges().removeNode(new Edge<>(v1.getVertex()));
    }

    /** Actividad 3 – Recorrido DFS recursivo desde un vértice. */
    public void DFS(E startData) {
        AdjList<E> start = findVertex(startData);
        if (start == null) return;
        ListLinked<E> visited = new ListLinked<>();
        dfsRecursive(start, visited);
        System.out.println();
    }

    private void dfsRecursive(AdjList<E> current, ListLinked<E> visited) {
        E data = current.getVertex().getData();
        visited.insertLast(data);
        System.out.print(data + " ");
        for (int i = 0; i < current.getEdges().size(); i++) {
            E neighbor = current.getEdges().get(i).getDestination().getData();
            if (!visited.search(neighbor)) {
                AdjList<E> next = findVertex(neighbor);
                if (next != null) dfsRecursive(next, visited);
            }
        }
    }

    /** Actividad 3 – Recorrido BFS desde un vértice. */
    public void BFS(E startData) {
        AdjList<E> startAdj = findVertex(startData);
        if (startAdj == null) return;
        ListLinked<E> visited = new ListLinked<>();
        QueueLink<AdjList<E>> queue = new QueueLink<>();
        visited.insertLast(startData);
        queue.enqueue(startAdj);
        while (!queue.isEmpty()) {
            AdjList<E> current = queue.dequeue();
            System.out.print(current.getVertex().getData() + " ");
            for (int i = 0; i < current.getEdges().size(); i++) {
                E neighbor = current.getEdges().get(i).getDestination().getData();
                if (!visited.search(neighbor)) {
                    visited.insertLast(neighbor);
                    AdjList<E> next = findVertex(neighbor);
                    if (next != null) queue.enqueue(next);
                }
            }
        }
        System.out.println();
    }

    // ---------------------------------------------------------------
    // EJERCICIO 1 — Grafo ponderado: insertEdgeWeight, shortPath,
    //               isConexo, Dijkstra
    // ---------------------------------------------------------------

    /**
     * EJERCICIO 1 – Inserta arista con peso w entre vértices v y z.
     * Permite representar grafos no dirigidos ponderados.
     */
    public void insertEdgeWeight(E origin, E destination, int weight) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
        v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
    }

    /**
     * EJERCICIO 1 – Calcula la ruta más corta entre origin y destination.
     * Retorna un ArrayList con los vértices del camino (de origen a destino).
     * Si no existe camino, retorna lista vacía.
     */
    public ArrayList<E> shortPath(E origin, E destination) {
        return dijkstraPath(origin, destination);
    }

    /**
     * EJERCICIO 1 – Devuelve true si el grafo es conexo (todos los vértices
     * son alcanzables desde el primero), false en caso contrario.
     */
    public boolean isConexo() {
        if (graph.size() == 0) return true;
        E start = graph.get(0).getVertex().getData();
        ListLinked<E> visited = new ListLinked<>();
        dfsVisited(findVertex(start), visited);
        return visited.size() == graph.size();
    }

    /** Auxiliar – DFS que solo acumula visitados sin imprimir. */
    private void dfsVisited(AdjList<E> current, ListLinked<E> visited) {
        if (current == null) return;
        visited.insertLast(current.getVertex().getData());
        for (int i = 0; i < current.getEdges().size(); i++) {
            E neighbor = current.getEdges().get(i).getDestination().getData();
            if (!visited.search(neighbor)) {
                dfsVisited(findVertex(neighbor), visited);
            }
        }
    }

    /**
     * EJERCICIO 1 – Retorna un Stack con la ruta más corta de origin a
     * destination usando Dijkstra. La cima del stack es el origen.
     */
    public StackLink<E> Dijkstra(E origin, E destination) {
        ArrayList<E> path = dijkstraPath(origin, destination);
        StackLink<E> stack = new StackLink<>();
        // Se apila en orden inverso para que la cima sea el origen
        for (int i = path.size() - 1; i >= 0; i--) {
            stack.push(path.get(i));
        }
        return stack;
    }

    /**
     * EJERCICIO 1 – Implementación interna de Dijkstra usando
     * ListLinked<DijkstraEntry<E>> en lugar de HashMap.
     *
     * Estructura de datos:
     *   - entries : ListLinked<DijkstraEntry<E>>
     *               Una entrada por cada vértice del grafo.
     *               Cada entrada guarda (vertex, dist, prev).
     *   - unvisited: ListLinked<E>
     *               Vértices aún no procesados.
     *
     * Se reemplaza Map<E,Integer> dist  →  campo dist  de DijkstraEntry
     * Se reemplaza Map<E,E>      prev  →  campo prev  de DijkstraEntry
     * Se reemplaza dist.get(v)         →  getEntry(entries, v).dist
     * Se reemplaza prev.get(v)         →  getEntry(entries, v).prev
     * Se reemplaza dist.put(v, x)      →  getEntry(entries, v).dist = x
     * Se reemplaza prev.put(v, x)      →  getEntry(entries, v).prev = x
     */
    private ArrayList<E> dijkstraPath(E origin, E destination) {
        ArrayList<E> result = new ArrayList<>();
        if (findVertex(origin) == null || findVertex(destination) == null) return result;

        // --- Reemplaza: Map<E,Integer> dist = new HashMap<>()
        //                Map<E,E>      prev = new HashMap<>()
        // Usamos una sola lista de entradas que guarda ambos valores.
        ListLinked<DijkstraEntry<E>> entries = new ListLinked<>();
        ListLinked<E> unvisited = new ListLinked<>();

        // Inicializar: distancia infinita, sin predecesor
        for (int i = 0; i < graph.size(); i++) {
            E v = graph.get(i).getVertex().getData();
            entries.addLast(new DijkstraEntry<>(v, Integer.MAX_VALUE, null));
            unvisited.insertLast(v);
        }
        // Distancia del origen = 0
        getEntry(entries, origin).dist = 0;

        while (!unvisited.isEmpty()) {
            // Obtener el vértice no visitado con menor distancia acumulada
            E u = minDistanceEntry(entries, unvisited);
            if (u == null || getEntry(entries, u).dist == Integer.MAX_VALUE) break;
            if (u.equals(destination)) break;

            unvisited.removeNode(u);

            // Relajar vecinos
            AdjList<E> adjU = findVertex(u);
            if (adjU == null) continue;
            int distU = getEntry(entries, u).dist;

            for (int i = 0; i < adjU.getEdges().size(); i++) {
                Edge<E> edge = adjU.getEdges().get(i);
                E neighbor = edge.getDestination().getData();
                if (!unvisited.search(neighbor)) continue;

                int alt = distU + edge.getWeight();
                DijkstraEntry<E> neighborEntry = getEntry(entries, neighbor);
                if (alt < neighborEntry.dist) {
                    neighborEntry.dist = alt;   // --- reemplaza: dist.put(neighbor, alt)
                    neighborEntry.prev = u;     // --- reemplaza: prev.put(neighbor, u)
                }
            }
        }

        // Reconstruir camino desde destination hacia origin siguiendo prev
        ArrayList<E> path = new ArrayList<>();
        E step = destination;
        DijkstraEntry<E> stepEntry = getEntry(entries, step);
        if (stepEntry.prev == null && !step.equals(origin)) {
            return result; // no hay camino
        }
        while (step != null) {
            path.add(0, step);
            DijkstraEntry<E> e = getEntry(entries, step);
            step = (e != null) ? e.prev : null;
        }
        return path;
    }

    /**
     * EJERCICIO 1 – Auxiliar: busca la entrada de un vértice en la lista
     * de entradas de Dijkstra. Reemplaza dist.get(v) / prev.get(v).
     */
    private DijkstraEntry<E> getEntry(ListLinked<DijkstraEntry<E>> entries, E vertex) {
        for (int i = 0; i < entries.size(); i++) {
            DijkstraEntry<E> e = entries.get(i);
            if (e.vertex.equals(vertex)) return e;
        }
        return null;
    }

    /**
     * EJERCICIO 1 – Auxiliar: recorre los no-visitados y devuelve el vértice
     * cuya entrada tenga la menor distancia acumulada.
     * Reemplaza el método minDistance que recibía Map<E,Integer>.
     */
    private E minDistanceEntry(ListLinked<DijkstraEntry<E>> entries,
                               ListLinked<E> unvisited) {
        E minVertex = null;
        int minDist  = Integer.MAX_VALUE;
        for (int i = 0; i < unvisited.size(); i++) {
            E v = unvisited.get(i);
            DijkstraEntry<E> e = getEntry(entries, v);
            if (e != null && e.dist < minDist) {
                minDist  = e.dist;
                minVertex = v;
            }
        }
        return minVertex;
    }

    // ---------------------------------------------------------------
    // EJERCICIO 3 — Métodos adicionales de la interfaz Graph:
    //               searchVertex, searchEdge, adjacentVertices
    // (insertVertex, insertEdge, removeVertex, removeEdge ya existen
    //  desde Actividad 3)
    // ---------------------------------------------------------------

    /**
     * EJERCICIO 3 – Busca si existe un vértice con el dato dado.
     * Retorna true si existe, false si no.
     */
    public boolean searchVertex(E data) {
        return findVertex(data) != null;
    }

    /**
     * EJERCICIO 3 – Busca si existe una arista entre origin y destination.
     * Retorna true si existe, false si no.
     */
    public boolean searchEdge(E origin, E destination) {
        AdjList<E> adj = findVertex(origin);
        if (adj == null) return false;
        for (int i = 0; i < adj.getEdges().size(); i++) {
            if (adj.getEdges().get(i).getDestination().getData().equals(destination)) {
                return true;
            }
        }
        return false;
    }

    /**
     * EJERCICIO 3 – Retorna un ArrayList con los datos de los vértices
     * adyacentes al vértice dado.
     */
    public ArrayList<E> adjacentVertices(E data) {
        ArrayList<E> result = new ArrayList<>();
        AdjList<E> adj = findVertex(data);
        if (adj == null) return result;
        for (int i = 0; i < adj.getEdges().size(); i++) {
            result.add(adj.getEdges().get(i).getDestination().getData());
        }
        return result;
    }

    // ---------------------------------------------------------------
    // EJERCICIO 4 — isIsomorfo, isPlanar, isConexo (ya existe desde
    //               Ejercicio 1), isAutoComplementario
    // ---------------------------------------------------------------

    /**
     * EJERCICIO 4 – Verifica si este grafo es isomorfo con otro grafo.
     * Criterio: mismo número de vértices, mismo número de aristas y
     * misma secuencia de grados ordenada de mayor a menor.
     */
    public boolean isIsomorfo(GraphLink<E> other) {
        if (this.graph.size() != other.graph.size()) return false;
        if (this.countEdges() != other.countEdges()) return false;
        // Comparar secuencias de grados ordenadas
        ListLinked<Integer> thisDeg  = degreeSequence();
        ListLinked<Integer> otherDeg = other.degreeSequence();
        if (thisDeg.size() != otherDeg.size()) return false;
        for (int i = 0; i < thisDeg.size(); i++) {
            if (!thisDeg.get(i).equals(otherDeg.get(i))) return false;
        }
        return true;
    }

    /** Auxiliar EJERCICIO 4 – Cuenta aristas totales (no dirigido → suma/2). */
    private int countEdges() {
        int total = 0;
        for (int i = 0; i < graph.size(); i++) {
            total += graph.get(i).getEdges().size();
        }
        return total / 2;
    }

    /**
     * Auxiliar EJERCICIO 4 – Retorna la secuencia de grados ordenada
     * de mayor a menor usando una ListLinked (insertion sort).
     */
    private ListLinked<Integer> degreeSequence() {
        ListLinked<Integer> degrees = new ListLinked<>();
        // Recolectar grados
        for (int i = 0; i < graph.size(); i++) {
            degrees.addLast(graph.get(i).getEdges().size());
        }
        // Ordenar de mayor a menor con insertion sort sobre la lista
        int n = degrees.size();
        for (int i = 1; i < n; i++) {
            int key = degrees.get(i);
            int j = i - 1;
            while (j >= 0 && degrees.get(j) < key) {
                degrees.set(j + 1, degrees.get(j));
                j--;
            }
            degrees.set(j + 1, key);
        }
        return degrees;
    }

    /**
     * EJERCICIO 4 – Verifica si el grafo es planar usando el criterio de Euler:
     * Un grafo simple conexo es planar si E <= 3V - 6 (para V >= 3).
     */
    public boolean isPlanar() {
        int V = graph.size();
        int E = countEdges();
        if (V < 3) return true;
        return E <= 3 * V - 6;
    }

    /**
     * EJERCICIO 4 – Verifica si el grafo es auto-complementario.
     * G es auto-complementario si su complemento G' es isomorfo a G.
     */
    public boolean isAutoComplementario() {
        return this.isIsomorfo(buildComplement());
    }

    /**
     * Auxiliar EJERCICIO 4 – Construye el grafo complemento:
     * incluye exactamente las aristas que NO están en el grafo original.
     */
    private GraphLink<E> buildComplement() {
        GraphLink<E> comp = new GraphLink<>();
        for (int i = 0; i < graph.size(); i++) {
            comp.insertVertex(graph.get(i).getVertex().getData());
        }
        for (int i = 0; i < graph.size(); i++) {
            E u = graph.get(i).getVertex().getData();
            for (int j = i + 1; j < graph.size(); j++) {
                E v = graph.get(j).getVertex().getData();
                if (!searchEdge(u, v)) {
                    comp.insertEdge(u, v);
                }
            }
        }
        return comp;
    }

    // ---------------------------------------------------------------
    // toString – Actividad 3
    // ---------------------------------------------------------------

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            sb.append(adj.getVertex()).append(" -> ");
            for (int j = 0; j < adj.getEdges().size(); j++) {
                sb.append(adj.getEdges().get(j)).append(" ");
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    /** Retorna el número de vértices del grafo. */
    public int vertexCount() {
        return graph.size();
    }
}
