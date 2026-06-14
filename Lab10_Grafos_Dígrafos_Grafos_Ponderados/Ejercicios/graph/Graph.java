package graph;

import java.util.ArrayList;

/**
 * EJERCICIO 3 – Interfaz Graph<V, E> que define las operaciones básicas
 * de un grafo. GraphLink implementa esta interfaz.
 *
 * V: tipo de dato de los vértices
 * W: tipo de dato del peso de las aristas (Integer para grafos ponderados)
 */
public interface Graph<V extends Comparable<V>> {

    // EJERCICIO 3 – Insertar un vértice con dato v
    void insertVertex(V data);

    // EJERCICIO 3 – Insertar arista no ponderada entre origin y destination
    void insertEdge(V origin, V destination);

    // EJERCICIO 3 – Insertar arista ponderada con peso w
    void insertEdgeWeight(V origin, V destination, int weight);

    // EJERCICIO 3 – Eliminar el vértice con dato data y sus aristas
    void removeVertex(V data);

    // EJERCICIO 3 – Eliminar la arista entre origin y destination
    void removeEdge(V origin, V destination);

    // EJERCICIO 3 – Buscar si existe un vértice con dato data
    boolean searchVertex(V data);

    // EJERCICIO 3 – Buscar si existe arista entre origin y destination
    boolean searchEdge(V origin, V destination);

    // EJERCICIO 3 – Retornar lista de vértices adyacentes al vértice data
    ArrayList<V> adjacentVertices(V data);

    // EJERCICIO 3 – Recorrido en profundidad desde startData
    void DFS(V startData);

    // EJERCICIO 3 – Recorrido en anchura desde startData
    void BFS(V startData);

    // EJERCICIO 3 – Verificar si el grafo es conexo
    boolean isConexo();
}
