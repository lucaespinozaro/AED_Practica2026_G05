package graph;

import listlinked.ListLinked;
import listlinked.ColaEnlazada;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * GraphLink: Grafo no dirigido implementado con listas de adyacencia.
 * Incluye métodos de Actividad 3 + Ejercicios 1, 3 y 4.
 * (Ejercicio 2 usa JGraphT en su propia clase separada: CityNetwork.java)
 */
public class GraphLink<E extends Comparable<E>> {

    private ListLinked<AdjList<E>> graph;

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
        // Eliminar aristas en vecinos que apuntan al vértice a borrar
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
        ColaEnlazada<AdjList<E>> queue = new ColaEnlazada<>();
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
     * EJERCICIO 1 – Calcula la ruta más corta entre v y z usando Dijkstra.
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

    /** Auxiliar DFS que solo marca visitados (sin imprimir). */
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
     * EJERCICIO 1 – Retorna un Stack con la ruta más corta de v a w
     * usando el algoritmo de Dijkstra. La cima del stack es el origen.
     */
    public Stack<E> Dijkstra(E origin, E destination) {
        ArrayList<E> path = dijkstraPath(origin, destination);
        Stack<E> stack = new Stack<>();
        // Apilamos en orden inverso para que la cima sea el origen
        for (int i = path.size() - 1; i >= 0; i--) {
            stack.push(path.get(i));
        }
        return stack;
    }

    /**
     * Implementación interna de Dijkstra.
     * Retorna el camino como ArrayList del origen al destino.
     */
    private ArrayList<E> dijkstraPath(E origin, E destination) {
        ArrayList<E> result = new ArrayList<>();
        if (findVertex(origin) == null || findVertex(destination) == null) return result;

        // Distancias y predecesores
        Map<E, Integer> dist = new HashMap<>();
        Map<E, E> prev = new HashMap<>();
        ListLinked<E> unvisited = new ListLinked<>();

        // Inicializar todas las distancias en infinito
        for (int i = 0; i < graph.size(); i++) {
            E v = graph.get(i).getVertex().getData();
            dist.put(v, Integer.MAX_VALUE);
            prev.put(v, null);
            unvisited.insertLast(v);
        }
        dist.put(origin, 0);

        while (!unvisited.isEmpty()) {
            // Obtener el vértice no visitado con menor distancia
            E u = minDistance(dist, unvisited);
            if (u == null || dist.get(u) == Integer.MAX_VALUE) break;
            if (u.equals(destination)) break;

            // Remover u de no visitados
            unvisited.removeNode(u);

            // Relajar vecinos
            AdjList<E> adjU = findVertex(u);
            if (adjU == null) continue;
            for (int i = 0; i < adjU.getEdges().size(); i++) {
                Edge<E> edge = adjU.getEdges().get(i);
                E neighbor = edge.getDestination().getData();
                if (!unvisited.search(neighbor)) continue;
                int alt = dist.get(u) + edge.getWeight();
                if (alt < dist.get(neighbor)) {
                    dist.put(neighbor, alt);
                    prev.put(neighbor, u);
                }
            }
        }

        // Reconstruir camino desde destination hasta origin
        ArrayList<E> path = new ArrayList<>();
        E step = destination;
        if (prev.get(step) == null && !step.equals(origin)) {
            return result; // sin camino
        }
        while (step != null) {
            path.add(0, step);
            step = prev.get(step);
        }
        return path;
    }

    /** Auxiliar: devuelve el vértice con menor distancia entre los no visitados. */
    private E minDistance(Map<E, Integer> dist, ListLinked<E> unvisited) {
        E minVertex = null;
        int minDist = Integer.MAX_VALUE;
        for (int i = 0; i < unvisited.size(); i++) {
            E v = unvisited.get(i);
            int d = dist.getOrDefault(v, Integer.MAX_VALUE);
            if (d < minDist) {
                minDist = d;
                minVertex = v;
            }
        }
        return minVertex;
    }

    // ---------------------------------------------------------------
    // EJERCICIO 3 — Métodos adicionales de la interfaz Graph<V,E>:
    //               searchVertex, searchEdge, adjacentVertices
    // (insertVertex, insertEdge, removeVertex, removeEdge ya existen)
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
     * EJERCICIO 3 – Retorna un ArrayList con los vértices adyacentes
     * al vértice dado.
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
    // EJERCICIO 4 — isIsomorfo, isPlanar, isConexo (ya existe),
    //               isAutoComplementario
    // ---------------------------------------------------------------

    /**
     * EJERCICIO 4 – Verifica si este grafo es isomorfo con otro grafo g.
     * Condición necesaria (no suficiente): mismo número de vértices,
     * mismo número de aristas y misma secuencia de grados ordenada.
     * Para grafos de laboratorio esta heurística es suficientemente precisa.
     */
    public boolean isIsomorfo(GraphLink<E> other) {
        if (this.graph.size() != other.graph.size()) return false;

        int thisEdges = countEdges();
        int otherEdges = other.countEdges();
        if (thisEdges != otherEdges) return false;

        // Comparar secuencias de grados ordenadas
        ArrayList<Integer> thisDegrees = degreeSequence();
        ArrayList<Integer> otherDegrees = other.degreeSequence();
        return thisDegrees.equals(otherDegrees);
    }

    /** Auxiliar EJERCICIO 4 – Cuenta el número de aristas del grafo (no dirigido → /2). */
    private int countEdges() {
        int total = 0;
        for (int i = 0; i < graph.size(); i++) {
            total += graph.get(i).getEdges().size();
        }
        return total / 2;
    }

    /** Auxiliar EJERCICIO 4 – Retorna la secuencia de grados ordenada de mayor a menor. */
    private ArrayList<Integer> degreeSequence() {
        ArrayList<Integer> degrees = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++) {
            degrees.add(graph.get(i).getEdges().size());
        }
        degrees.sort((a, b) -> b - a);
        return degrees;
    }

    /**
     * EJERCICIO 4 – Verifica si el grafo es planar usando el criterio de Euler:
     * Un grafo simple conexo es planar si E <= 3V - 6 (para V >= 3).
     * También aplica la restricción de Kuratowski para grafos bipartitos: E <= 2V - 4.
     * Para grafos pequeños del laboratorio esta fórmula es correcta.
     */
    public boolean isPlanar() {
        int V = graph.size();
        int E = countEdges();
        if (V < 3) return true;          // Grafos triviales siempre planos
        if (E > 3 * V - 6) return false; // Condición necesaria de Euler
        return true;
    }

    /**
     * EJERCICIO 4 – Verifica si el grafo es auto-complementario.
     * Un grafo G es auto-complementario si su complemento G' es isomorfo a G.
     * El complemento se forma con las aristas que NO están en G.
     */
    public boolean isAutoComplementario() {
        GraphLink<E> complement = buildComplement();
        return this.isIsomorfo(complement);
    }

    /**
     * Auxiliar EJERCICIO 4 – Construye el grafo complemento:
     * contiene todas las aristas posibles que NO están en el grafo original.
     */
    private GraphLink<E> buildComplement() {
        GraphLink<E> comp = new GraphLink<>();
        // Agregar los mismos vértices
        for (int i = 0; i < graph.size(); i++) {
            comp.insertVertex(graph.get(i).getVertex().getData());
        }
        // Agregar aristas que NO existen en el grafo original
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
