package graph;

import listlinked.ListLinked;
import listlinked.QueueLink;
import java.util.*;

public class GraphLink<E extends Comparable<E>> {

    protected ListLinked<AdjList<E>> graph;

    public GraphLink() { this.graph = new ListLinked<>(); }

    // ── Vértices ─────────────────────────────────────────────
    public void insertVertex(E data) {
        if (data == null || findVertex(data) != null) return;
        graph.addLast(new AdjList<>(new Vertex<>(data)));
    }

    public boolean removeVertex(E data) {
        AdjList<E> target = findVertex(data);
        if (target == null) return false;
        // Eliminar aristas que apunten a este vértice en otros nodos
        for (int i = 0; i < graph.size(); i++) {
            graph.get(i).getEdges().removeNode(new Edge<>(target.getVertex()));
        }
        graph.removeNode(target);
        return true;
    }

    public boolean searchVertex(E data) { return findVertex(data) != null; }

    // ── Aristas ──────────────────────────────────────────────
    public void insertEdge(E origin, E destination) {
        insertEdgeWeight(origin, destination, 1);
    }

    public void insertEdgeWeight(E origin, E destination, int weight) {
        if (origin == null || destination == null || origin.equals(destination)) return;
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        if (searchEdge(origin, destination)) return;
        v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
        v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
    }

    public boolean removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return false;
        boolean r1 = v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
        boolean r2 = v2.getEdges().removeNode(new Edge<>(v1.getVertex()));
        return r1 && r2;
    }

    public boolean searchEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        if (v1 == null) return false;
        for (int i = 0; i < v1.getEdges().size(); i++) {
            if (v1.getEdges().get(i).getDestination().getData().equals(destination))
                return true;
        }
        return false;
    }

    public int getWeight(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        if (v1 == null) return -1;
        for (int i = 0; i < v1.getEdges().size(); i++) {
            Edge<E> e = v1.getEdges().get(i);
            if (e.getDestination().getData().equals(destination)) return e.getWeight();
        }
        return -1;
    }

    // ── Utilidades ───────────────────────────────────────────
    public AdjList<E> findVertex(E data) {
        for (int i = 0; i < graph.size(); i++) {
            if (graph.get(i).getVertex().getData().equals(data))
                return graph.get(i);
        }
        return null;
    }

    public List<E> getAllVertices() {
        List<E> list = new ArrayList<>();
        for (int i = 0; i < graph.size(); i++)
            list.add(graph.get(i).getVertex().getData());
        return list;
    }

    public List<E> getNeighbors(E data) {
        List<E> list = new ArrayList<>();
        AdjList<E> adj = findVertex(data);
        if (adj == null) return list;
        for (int i = 0; i < adj.getEdges().size(); i++)
            list.add(adj.getEdges().get(i).getDestination().getData());
        return list;
    }

    // ── BFS ──────────────────────────────────────────────────
    /** Devuelve el orden de visita BFS desde startData */
    public List<E> bfs(E startData) {
        List<E> order = new ArrayList<>();
        if (findVertex(startData) == null) return order;
        Set<E> visited = new HashSet<>();
        QueueLink<E> queue = new QueueLink<>();
        queue.enqueue(startData);
        visited.add(startData);
        while (!queue.isEmpty()) {
            E cur = queue.dequeue();
            order.add(cur);
            List<E> neighbors = getNeighbors(cur);
            Collections.sort(neighbors);
            for (E nb : neighbors) {
                if (!visited.contains(nb)) {
                    visited.add(nb);
                    queue.enqueue(nb);
                }
            }
        }
        return order;
    }

    // ── DFS ──────────────────────────────────────────────────
    /** Devuelve el orden de visita DFS desde startData */
    public List<E> dfs(E startData) {
        List<E> order = new ArrayList<>();
        if (findVertex(startData) == null) return order;
        Set<E> visited = new HashSet<>();
        dfsRecursive(startData, visited, order);
        return order;
    }

    private void dfsRecursive(E cur, Set<E> visited, List<E> order) {
        visited.add(cur);
        order.add(cur);
        List<E> neighbors = getNeighbors(cur);
        Collections.sort(neighbors);
        for (E nb : neighbors) {
            if (!visited.contains(nb)) dfsRecursive(nb, visited, order);
        }
    }

    // ── Dijkstra ─────────────────────────────────────────────
    /** Retorna la lista de vértices en la ruta más corta de origin a destination,
     *  o lista vacía si no existe. */
    public List<E> dijkstra(E origin, E destination) {
        List<E> allV = getAllVertices();
        Map<E, Integer> dist   = new HashMap<>();
        Map<E, E>       prev   = new HashMap<>();
        Set<E>          settled = new HashSet<>();

        for (E v : allV) dist.put(v, Integer.MAX_VALUE);
        dist.put(origin, 0);

        // Simple priority queue using list (ok for small graphs)
        List<E> pq = new ArrayList<>(allV);

        while (!pq.isEmpty()) {
            // Pick minimum distance vertex
            E u = null;
            int minD = Integer.MAX_VALUE;
            for (E v : pq) {
                if (dist.get(v) < minD) { minD = dist.get(v); u = v; }
            }
            if (u == null || u.equals(destination)) break;
            pq.remove(u);
            settled.add(u);

            AdjList<E> adjU = findVertex(u);
            if (adjU == null) continue;
            for (int i = 0; i < adjU.getEdges().size(); i++) {
                Edge<E> e = adjU.getEdges().get(i);
                E nb = e.getDestination().getData();
                if (settled.contains(nb)) continue;
                int newDist = dist.get(u) + e.getWeight();
                if (newDist < dist.get(nb)) {
                    dist.put(nb, newDist);
                    prev.put(nb, u);
                }
            }
        }

        // Reconstruct path
        List<E> path = new ArrayList<>();
        if (!prev.containsKey(destination) && !origin.equals(destination)) return path;
        for (E at = destination; at != null; at = prev.get(at)) path.add(0, at);
        return path;
    }

    /** Costo total de la ruta dada */
    public int pathCost(List<E> path) {
        int total = 0;
        for (int i = 0; i < path.size() - 1; i++)
            total += getWeight(path.get(i), path.get(i + 1));
        return total;
    }

    // ── Conexo ───────────────────────────────────────────────
    public boolean isConexo() {
        if (graph.size() == 0) return true;
        E start = graph.get(0).getVertex().getData();
        return bfs(start).size() == graph.size();
    }

    public int vertexCount() { return graph.size(); }

    public int edgeCount() {
        int total = 0;
        for (int i = 0; i < graph.size(); i++) total += graph.get(i).getEdges().size();
        return total / 2;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.size(); i++) sb.append(graph.get(i)).append("\n");
        return sb.toString();
    }
}
