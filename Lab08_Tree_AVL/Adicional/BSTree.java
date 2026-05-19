package Adicional;

import java.util.ArrayList;
import java.util.List;

public class BSTree<E extends Comparable<E>> {

    protected class Node {
        protected E data;
        protected Node left;
        protected Node right;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }

        @Override
        public String toString() {
            return data.toString();
        }
    }

    protected Node root;

    public BSTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return root == null;
    }

    public Node getRoot() {
        return root;
    }

    protected int height(Node node) {
        if (node == null) return 0;
        int leftH  = height(node.left);
        int rightH = height(node.right);
        return 1 + Math.max(leftH, rightH);
    }

    public int altura() {
        return height(root);
    }

    public int totalNodos() {
        return contarNodos(root);
    }

    private int contarNodos(Node node) {
        if (node == null) return 0;
        return 1 + contarNodos(node.left) + contarNodos(node.right);
    }

    public List<E> inOrden() {
        List<E> lista = new ArrayList<>();
        inOrdenRec(root, lista);
        return lista;
    }

    private void inOrdenRec(Node node, List<E> lista) {
        if (node == null) return;
        inOrdenRec(node.left, lista);
        lista.add(node.data);
        inOrdenRec(node.right, lista);
    }

    public List<E> preOrden() {
        List<E> lista = new ArrayList<>();
        preOrdenRec(root, lista);
        return lista;
    }

    private void preOrdenRec(Node node, List<E> lista) {
        if (node == null) return;
        lista.add(node.data);
        preOrdenRec(node.left, lista);
        preOrdenRec(node.right, lista);
    }

    public List<E> postOrden() {
        List<E> lista = new ArrayList<>();
        postOrdenRec(root, lista);
        return lista;
    }

    private void postOrdenRec(Node node, List<E> lista) {
        if (node == null) return;
        postOrdenRec(node.left, lista);
        postOrdenRec(node.right, lista);
        lista.add(node.data);
    }

    public E search(E x) throws ItemNotFound {
        return searchRec(root, x);
    }

    private E searchRec(Node node, E x) throws ItemNotFound {
        if (node == null) throw new ItemNotFound(x + " no encontrado.");
        int cmp = x.compareTo(node.data);
        if (cmp == 0) return node.data;
        if (cmp < 0)  return searchRec(node.left, x);
        return searchRec(node.right, x);
    }
}