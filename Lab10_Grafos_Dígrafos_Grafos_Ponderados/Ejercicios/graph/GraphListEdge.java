import listlinked.ListLinked;

public class GraphListEdge<E extends Comparable<E>> extends AbstractGraph<E> {
    public GraphListEdge() {
        super();
    }

    @Override
    public void insertEdge(E origin, E destination) {
        insertEdgeWeight(origin, destination, 1);
    }

    @Override
    public void insertEdgeWeight(E origin, E destination, int weight) {
        if (origin == null || destination == null || origin.compareTo(destination) == 0) return;
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null || searchEdge(origin, destination)) return;

        v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
    }

    @Override
    public boolean removeVertex(E data) {
        if (data == null) return false;
        AdjList<E> targetAdj = findVertex(data);
        if (targetAdj == null) return false;

        for (int i = 0; i < graph.size(); i++) {
            graph.get(i).getEdges().removeNode(new Edge<>(targetAdj.getVertex()));
        }
        return graph.removeNode(targetAdj);
    }

    @Override
    public boolean removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        if (v1 == null) return false;
        return v1.getEdges().removeNode(new Edge<>(new Vertex<>(destination)));
    }

    @Override
    protected int countEdges() {
        int total = 0;
        for (int i = 0; i < graph.size(); i++) {
            total += graph.get(i).getEdges().size();
        }
        return total;
    }

    private int inDegree(E data) {
        int cnt = 0;
        for (int i = 0; i < graph.size(); i++) {
            AdjList<E> adj = graph.get(i);
            for (int j = 0; j < adj.getEdges().size(); j++) {
                if (adj.getEdges().get(j).getDestination().getData().compareTo(data) == 0) {
                    cnt++;
                }
            }
        }
        return cnt;
    }

    @Override
    protected ListLinked<DegreeSig> degreeSignature() {
        ListLinked<DegreeSig> sigs = new ListLinked<>();
        for (int i = 0; i < graph.size(); i++) {
            E v = graph.get(i).getVertex().getData();
            int out = graph.get(i).getEdges().size();
            int in = inDegree(v);
            sigs.addLast(new DegreeSig(out, in));
        }
        sortDegreeSigs(sigs);
        return sigs;
    }

    @Override
    protected AbstractGraph<E> buildComplement() {
        GraphListEdge<E> comp = new GraphListEdge<>();
        for (int i = 0; i < graph.size(); i++) {
            comp.insertVertex(graph.get(i).getVertex().getData());
        }
        for (int i = 0; i < graph.size(); i++) {
            E u = graph.get(i).getVertex().getData();
            for (int j = 0; j < graph.size(); j++) {
                if (i == j) continue;
                E v = graph.get(j).getVertex().getData();
                if (!searchEdge(u, v)) {
                    comp.insertEdge(u, v);
                }
            }
        }
        return comp;
    }

    @Override
    public boolean isConexo() {
        if (graph.size() == 0) return true;
        E start = graph.get(0).getVertex().getData();
        if (reachableCount(start) != graph.size()) return false;

        GraphListEdge<E> reverse = new GraphListEdge<>();
        for (int i = 0; i < graph.size(); i++) {
            reverse.insertVertex(graph.get(i).getVertex().getData());
        }
        for (int i = 0; i < graph.size(); i++) {
            E u = graph.get(i).getVertex().getData();
            for (int j = 0; j < graph.get(i).getEdges().size(); j++) {
                Edge<E> e = graph.get(i).getEdges().get(j);
                reverse.insertEdgeWeight(e.getDestination().getData(), u, e.getWeight());
            }
        }
        return reverse.reachableCount(start) == reverse.graph.size();
    }
}
