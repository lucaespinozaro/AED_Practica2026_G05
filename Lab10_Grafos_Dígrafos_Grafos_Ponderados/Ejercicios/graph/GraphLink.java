import listlinked.ListLinked;

public class GraphLink<E extends Comparable<E>> extends AbstractGraph<E> {
    public GraphLink() {
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
        v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
    }

    @Override
    public boolean removeVertex(E data) {
        if (data == null) return false;
        AdjList<E> targetAdj = findVertex(data);
        if (targetAdj == null) return false;

        for (int i = 0; i < targetAdj.getEdges().size(); i++) {
            Edge<E> edge = targetAdj.getEdges().get(i);
            AdjList<E> neighborAdj = findVertex(edge.getDestination().getData());
            if (neighborAdj != null) {
                neighborAdj.getEdges().removeNode(new Edge<>(targetAdj.getVertex()));
            }
        }
        return graph.removeNode(targetAdj);
    }

    @Override
    public boolean removeEdge(E origin, E destination) {
        AdjList<E> v1 = findVertex(origin);
        AdjList<E> v2 = findVertex(destination);
        if (v1 == null || v2 == null) return false;

        boolean r1 = v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
        boolean r2 = v2.getEdges().removeNode(new Edge<>(v1.getVertex()));
        return r1 && r2;
    }

    @Override
    protected int countEdges() {
        int total = 0;
        for (int i = 0; i < graph.size(); i++) {
            total += graph.get(i).getEdges().size();
        }
        return total / 2;
    }

    @Override
    protected ListLinked<DegreeSig> degreeSignature() {
        ListLinked<DegreeSig> sigs = new ListLinked<>();
        for (int i = 0; i < graph.size(); i++) {
            int deg = graph.get(i).getEdges().size();
            sigs.addLast(new DegreeSig(deg, deg));
        }
        sortDegreeSigs(sigs);
        return sigs;
    }

    @Override
    protected AbstractGraph<E> buildComplement() {
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

    @Override
    public boolean isConexo() {
        if (graph.size() == 0) return true;
        E start = graph.get(0).getVertex().getData();
        return reachableCount(start) == graph.size();
    }

    public boolean isPlanar() {
        int V = graph.size();
        int E = countEdges();
        if (V < 3) return true;
        return E <= 3 * V - 6;
    }
}
