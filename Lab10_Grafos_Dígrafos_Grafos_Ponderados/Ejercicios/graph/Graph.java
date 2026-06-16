import java.util.ArrayList;

public interface Graph<V extends Comparable<V>> {
    void insertVertex(V data);
    void insertEdge(V origin, V destination);
    void insertEdgeWeight(V origin, V destination, int weight);
    boolean removeVertex(V data);
    boolean removeEdge(V origin, V destination);
    boolean searchVertex(V data);
    boolean searchEdge(V origin, V destination);
    ArrayList<V> adjacentVertices(V data);
    void DFS(V startData);
    void BFS(V startData);
    boolean isConexo();
}
