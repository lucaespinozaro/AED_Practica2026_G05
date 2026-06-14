import listlinked.ListLinked;
import listlinked.ColaEnlazada;

public class GraphLink<E extends Comparable<E>> {
    private ListLinked<AdjList<E>> graph;

    public GraphLink() {
        graph = new ListLinked<>();
    }

    public void insertVertex(E data) {
        if (data == null || findVertex(data) != null) return;
        Vertex<E> vertex = new Vertex<>(data);
        graph.addLast(new AdjList<>(vertex));
    }

    private AdjList<E> findVertex(E data) {
        if (data == null) return null;
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            if (adj.getVertex().getData().equals(data)) {
                return adj;
            }
        }
        return null;
    }

    public void insertEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        v1.getEdges().addLast(new Edge<>(v2.getVertex()));
        v2.getEdges().addLast(new Edge<>(v1.getVertex()));
    }

    public void removeVertex(E data) {
        if (data == null) return;
        AdjList<E> targetAdj = findVertex(data);
        if (targetAdj == null) return;

        for (int i = 0; i < targetAdj.getEdges().size(); i++) {
            Edge<E> edge = targetAdj.getEdges().get(i);
            E neighborData = edge.getDestination().getData();
            AdjList<E> neighborAdj = findVertex(neighborData);
            if (neighborAdj != null) {
                neighborAdj.getEdges().removeNode(new Edge<>(targetAdj.getVertex()));
            }
        }
        graph.removeNode(targetAdj);
    }

    public void removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return;
        v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
        v2.getEdges().removeNode(new Edge<>(v1.getVertex()));
    }

    public boolean isConnected(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return false;
        for (int i = 0; i < v1.getEdges().size(); i++) {
            if (v1.getEdges().get(i).getDestination().getData().equals(destination)) {
                return true;
            }
        }
        return false;
    }

    private void dfsRecursive(AdjList<E> current, ListLinked<E> visited) {
        E data = current.getVertex().getData();
        visited.insertLast(data);
        System.out.print(data + " ");
        for (int i = 0; i < current.getEdges().size(); i++) {
            Edge<E> edge = current.getEdges().get(i);
            E neighbor = edge.getDestination().getData();
            if (!visited.search(neighbor)) {
                AdjList<E> next = findVertex(neighbor);
                if (next != null) {
                    dfsRecursive(next, visited);
                }
            }
        }
    }

    public void DFS(E startData) {
        AdjList<E> start = findVertex(startData);
        if (start == null) return;
        ListLinked<E> visited = new ListLinked<>();
        dfsRecursive(start, visited);
        System.out.println();
    }

    public void BFS(E start) {
        AdjList<E> startVertex = findVertex(start);
        if (startVertex == null) return;
        ListLinked<E> visited = new ListLinked<>();
        ColaEnlazada<AdjList<E>> queue = new ColaEnlazada<>();
        visited.insertLast(start);
        queue.enqueue(startVertex);
        while (!queue.isEmpty()) {
            AdjList<E> current = queue.dequeue();
            System.out.print(current.getVertex().getData() + " ");
            for (int i = 0; i < current.getEdges().size(); i++) {
                Edge<E> edge = current.getEdges().get(i);
                E neighbor = edge.getDestination().getData();
                if (!visited.search(neighbor)) {
                    visited.insertLast(neighbor);
                    AdjList<E> nextVertex = findVertex(neighbor);
                    if (nextVertex != null) {
                        queue.enqueue(nextVertex);
                    }
                }
            }
        }
        System.out.println();
    }

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
}
