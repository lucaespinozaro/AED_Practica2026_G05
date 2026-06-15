package graph;

import listlinked.ListLinked;

public class GraphListEdge<E extends Comparable<E>> extends AbstractGraph<E>
{
  public GraphListEdge() {
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
  }

  @Override
  public void removeEdge(E origin, E destination) {
    AdjList<E> v1 = findVertex(origin);
    AdjList<E> v2 = findVertex(destination);
    if (v1 == null || v2 == null) return;

    v1.getEdges().removeNode(new Edge<>(v2.getVertex()));
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
        if (adj.getEdges().get(j).getDestination().getData().equals(data)) {
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

    // Conectividad fuerte: desde un vértice debe alcanzar a todos,
    // y en el grafo reverso también.
    if (reachableCount(start) != graph.size()) return false;

    GraphListEdge<E> reverse = buildReverse();
    return reverse.reachableCount(start) == reverse.graph.size();
  }

  private GraphListEdge<E> buildReverse() {
    GraphListEdge<E> rev = new GraphListEdge<>();

    for (int i = 0; i < graph.size(); i++) {
      rev.insertVertex(graph.get(i).getVertex().getData());
    }

    for (int i = 0; i < graph.size(); i++) {
      E u = graph.get(i).getVertex().getData();
      for (int j = 0; j < graph.get(i).getEdges().size(); j++) {
        Edge<E> e = graph.get(i).getEdges().get(j);
        E v = e.getDestination().getData();
        rev.insertEdgeWeight(v, u, e.getWeight());
      }
    }

    return rev;
  }
}
