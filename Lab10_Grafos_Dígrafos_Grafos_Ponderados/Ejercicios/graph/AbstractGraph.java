package graph;

import listlinked.ListLinked;
import listlinked.QueueLink;

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
      if (adj.getVertex().getData().equals(data)) return adj;
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
  public void removeVertex(E data) {
    if (data == null) return;

    AdjList<E> target = findVertex(data);
    if (target == null) return;

    for (int i = 0; i < graph.size(); i++) {
      AdjList<E> adj = graph.get(i);
      adj.getEdges().removeNode(new Edge<>(target.getVertex()));
    }

    graph.removeNode(target);
  }

  @Override
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

  public boolean isIsomorfo(AbstractGraph<E> other) {
    if (other == null) return false;
    if (this.graph.size() != other.graph.size()) return false;
    if (this.countEdges() != other.countEdges()) return false;

    ListLinked<DegreeSig> a = this.degreeSignature();
    ListLinked<DegreeSig> b = other.degreeSignature();

    if (a.size() != b.size()) return false;

    for (int i = 0; i < a.size(); i++) {
      DegreeSig x = a.get(i);
      DegreeSig y = b.get(i);
      if (x.out != y.out || x.in != y.in) return false;
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

  protected void dfsCount(AdjList<E> current, ListLinked<E> visited) {
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

      while (j >= 0 && compareSig(list.get(j), key) < 0) {
        list.set(j + 1, list.get(j));
        j--;
      }
      list.set(j + 1, key);
    }
  }

  private int compareSig(DegreeSig a, DegreeSig b) {
    if (a.out != b.out) return Integer.compare(a.out, b.out);
    return Integer.compare(a.in, b.in);
  }

  protected abstract int countEdges();

  protected abstract ListLinked<DegreeSig> degreeSignature();

  protected abstract AbstractGraph<E> buildComplement();

  @Override
  public abstract void insertEdge(E origin, E destination);

  @Override
  public abstract void insertEdgeWeight(E origin, E destination, int weight);

  @Override
  public abstract void removeEdge(E origin, E destination);

  @Override
  public abstract boolean isConexo();

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

  public int vertexCount() {
    return graph.size();
  }
}
