import listlinked.ListLinked;
import listlinked.QueueLink;
import listlinked.StackLink;
import java.util.ArrayList;

public abstract class AbstractGraph<E extends Comparable<E>> implements Graph<E> {
    protected ListLinked<AdjList<E>> graph;

    protected static class DegreeSig {
        final int out;
        final int in;

        DegreeSig(int out, int in) {
            this.out = out;
            this.in = in;
        }
    }

    public AbstractGraph() {
        graph = new ListLinked<>();
    }

    @Override
    public void insertVertex(E data) {
        if (data == null || findVertex(data) != null) return;
        graph.addLast(new AdjList<>(new Vertex<>(data)));
    }

    protected AdjList<E> findVertex(E data) {
        if (data == null) return null;
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            if (adj.getVertex().getData().compareTo(data) == 0) return adj;
        }
        return null;
    }

    @Override
    public boolean searchVertex(E data) {
        return findVertex(data) != null;
    }

    @Override
    public ArrayList<E> adjacentVertices(E data) {
        ArrayList<E> res = new ArrayList<>();
        AdjList<E> adj = findVertex(data);
        if (adj == null) return res;
        for (int i = 0; i < adj.getEdges().size(); i++) {
            res.add(adj.getEdges().get(i).getDestination().getData());
        }
        return res;
    }

    @Override
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

    @Override
    public void BFS(E startData) {
        AdjList<E> start = findVertex(startData);
        if (start == null) return;
        ListLinked<E> visited = new ListLinked<>();
        QueueLink<AdjList<E>> q = new QueueLink<>();
        visited.insertLast(startData);
        q.enqueue(start);
        while (!q.isEmpty()) {
            AdjList<E> cur = q.dequeue();
            if (cur == null) continue;
            System.out.print(cur.getVertex().getData() + " ");
            for (int i = 0; i < cur.getEdges().size(); i++) {
                E neighbor = cur.getEdges().get(i).getDestination().getData();
                if (!visited.search(neighbor)) {
                    visited.insertLast(neighbor);
                    AdjList<E> next = findVertex(neighbor);
                    if (next != null) q.enqueue(next);
                }
            }
        }
        System.out.println();
    }

    @Override
    public boolean searchEdge(E origin, E destination) {
        AdjList<E> adj = findVertex(origin);
        if (adj == null) return false;
        for (int i = 0; i < adj.getEdges().size(); i++) {
            if (adj.getEdges().get(i).getDestination().getData().compareTo(destination) == 0) {
                return true;
            }
        }
        return false;
    }

    public ArrayList<E> shortPath(E v, E z) {
        ArrayList<E> pathList = new ArrayList<>();
        StackLink<E> stack = Dijkstra(v, z);
        if (stack == null) return pathList;
        while (!stack.isEmpty()) {
            pathList.add(stack.pop());
        }
        return pathList;
    }

    public StackLink<E> Dijkstra(E v, E z) {
        if (findVertex(v) == null || findVertex(z) == null) return null;
        int n = graph.size();
        int[] dist = new int[n];
        boolean[] visited = new boolean[n];
        int[] prev = new int[n];

        for (int i = 0; i < n; i++) {
            dist[i] = Integer.MAX_VALUE;
            prev[i] = -1;
        }

        int startIndex = getVertexIndex(v);
        dist[startIndex] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = minDistance(dist, visited);
            if (u == -1) break;
            visited[u] = true;

            AdjList<E> uAdj = graph.get(u);
            for (int i = 0; i < uAdj.getEdges().size(); i++) {
                Edge<E> edge = uAdj.getEdges().get(i);
                int vIndex = getVertexIndex(edge.getDestination().getData());
                if (!visited[vIndex] && dist[u] != Integer.MAX_VALUE && dist[u] + edge.getWeight() < dist[vIndex]) {
                    dist[vIndex] = dist[u] + edge.getWeight();
                    prev[vIndex] = u;
                }
            }
        }

        int destIndex = getVertexIndex(z);
        if (dist[destIndex] == Integer.MAX_VALUE) return null;

        StackLink<E> stack = new StackLink<>();
        int curr = destIndex;
        while (curr != -1) {
            stack.push(graph.get(curr).getVertex().getData());
            curr = prev[curr];
        }
        return stack;
    }

    private int minDistance(int[] dist, boolean[] visited) {
        int min = Integer.MAX_VALUE, minIndex = -1;
        for (int v = 0; v < graph.size(); v++) {
            if (!visited[v] && dist[v] < min) {
                min = dist[v];
                minIndex = v;
            }
        }
        return minIndex;
    }

    private int getVertexIndex(E data) {
        for (int i = 0; i < graph.size(); i++) {
            if (graph.get(i).getVertex().getData().compareTo(data) == 0) return i;
        }
        return -1;
    }

    public boolean isIsomorfo(AbstractGraph<E> other) {
        if (other == null || this.graph.size() != other.graph.size() || this.countEdges() != other.countEdges()) return false;
        ListLinked<DegreeSig> a = this.degreeSignature();
        ListLinked<DegreeSig> b = other.degreeSignature();
        for (int i = 0; i < a.size(); i++) {
            if (a.get(i).out != b.get(i).out || a.get(i).in != b.get(i).in) return false;
        }
        return true;
    }

    public boolean isAutoComplementario() {
        AbstractGraph<E> comp = buildComplement();
        return this.isIsomorfo(comp);
    }

    protected int reachableCount(E startData) {
        AdjList<E> start = findVertex(startData);
        if (start == null) return 0;
        ListLinked<E> visited = new ListLinked<>();
        dfsCount(start, visited);
        return visited.size();
    }

    private void dfsCount(AdjList<E> current, ListLinked<E> visited) {
        if (current == null) return;
        E data = current.getVertex().getData();
        if (visited.search(data)) return;
        visited.insertLast(data);
        for (int i = 0; i < current.getEdges().size(); i++) {
            E neighbor = current.getEdges().get(i).getDestination().getData();
            dfsCount(findVertex(neighbor), visited);
        }
    }

    protected void sortDegreeSigs(ListLinked<DegreeSig> list) {
        for (int i = 1; i < list.size(); i++) {
            DegreeSig key = list.get(i);
            int j = i - 1;
            while (j >= 0 && (list.get(j).out < key.out || (list.get(j).out == key.out && list.get(j).in < key.in))) {
                list.set(j + 1, list.get(j));
                j--;
            }
            list.set(j + 1, key);
        }
    }

    protected abstract int countEdges();
    protected abstract ListLinked<DegreeSig> degreeSignature();
    protected abstract AbstractGraph<E> buildComplement();

    public int vertexCount() {
        return graph.size();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < graph.size(); i++) {
            sb.append(graph.get(i)).append("\n");
        }
        return sb.toString();
    }
}
