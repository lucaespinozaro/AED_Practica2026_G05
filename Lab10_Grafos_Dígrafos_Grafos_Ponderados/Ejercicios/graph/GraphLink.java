package graph;

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
    if (origin == null || destination == null) return;
    if (origin.equals(destination)) return;

    AdjList<E> v1 = findVertex(origin);
    AdjList<E> v2 = findVertex(destination);
    if (v1 == null || v2 == null) return;

    if (searchEdge(origin, destination)) return;

    v1.getEdges().addLast(new Edge<>(v2.getVertex(), weight));
    v2.getEdges().addLast(new Edge<>(v1.getVertex(), weight));
  }

  @Override
  public void removeEdge(E origin, E destination) {
    AdjList<E> v1 = findVertex(origin);
    AdjList<E> v2 = findVertex(destination);
    if (v1 == null || v2 == null) return;

    v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
    v2.getEdges().removeNode(new Edge<>(v1.getVertex()));
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
