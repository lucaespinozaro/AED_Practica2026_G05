package Adicional;

import java.util.ArrayList;
import java.util.List;

public class AVLTree<E extends Comparable<E>> extends BSTree<E> {
    private boolean height;
    private List<String> historial = new ArrayList<>();

    protected class NodeAVL extends Node {
        protected int bf;

        public NodeAVL(E data) {
            super(data);
            this.bf = 0;
        }

        @Override
        public String toString() {
            return data.toString() + "(" + bf + ")";
        }
    }

    public List<String> getHistorial() { return historial; }

    private void registrar(String msg) {
        historial.add("[" + java.time.LocalTime.now().toString().substring(0, 8) + "] " + msg);
    }

    public void insert(E x) throws ItemDuplicated {
        this.height = false;
        this.root = insertRec(x, (NodeAVL) this.root);
        registrar("INSERTAR: " + x);
    }

    protected Node insertRec(E x, NodeAVL node) throws ItemDuplicated {
        NodeAVL fat = node;
        if (node == null) {
            this.height = true;
            fat = new NodeAVL(x);
        } else {
            int resC = node.data.compareTo(x);
            if (resC == 0) throw new ItemDuplicated(x + " ya existe en la agenda.");

            if (resC > 0) {
                fat.left = insertRec(x, (NodeAVL) node.left);
                if (this.height) {
                    switch (fat.bf) {
                        case 1:  fat.bf = 0;  this.height = false; break;
                        case 0:  fat.bf = -1; this.height = true;  break;
                        case -1: fat = balanceToRight(fat); this.height = false; break;
                    }
                }
            } else {
                fat.right = insertRec(x, (NodeAVL) node.right);
                if (this.height) {
                    switch (fat.bf) {
                        case -1: fat.bf = 0; this.height = false; break;
                        case 0:  fat.bf = 1; this.height = true;  break;
                        case 1:  fat = balanceToLeft(fat); this.height = false; break;
                    }
                }
            }
        }
        return fat;
    }

    private NodeAVL balanceToLeft(NodeAVL node) {
        NodeAVL hijo = (NodeAVL) node.right;
        switch (hijo.bf) {
            case 1:
                node.bf = 0; hijo.bf = 0;
                node = rotateSL(node);
                break;
            case -1:
                NodeAVL nieto = (NodeAVL) hijo.left;
                switch (nieto.bf) {
                    case -1: node.bf = 0; hijo.bf = 1;  break;
                    case 0:  node.bf = 0; hijo.bf = 0;  break;
                    case 1:  node.bf = -1; hijo.bf = 0; break;
                }
                nieto.bf = 0;
                node.right = rotateSR(hijo);
                node = rotateSL(node);
                break;
            case 0:
                node.bf = 1; hijo.bf = -1;
                node = rotateSL(node);
                break;
        }
        return node;
    }

    private NodeAVL balanceToRight(NodeAVL node) {
        NodeAVL hijo = (NodeAVL) node.left;
        switch (hijo.bf) {
            case -1:
                node.bf = 0; hijo.bf = 0;
                node = rotateSR(node);
                break;
            case 1:
                NodeAVL nieto = (NodeAVL) hijo.right;
                switch (nieto.bf) {
                    case 1:  node.bf = 0; hijo.bf = -1; break;
                    case 0:  node.bf = 0; hijo.bf = 0;  break;
                    case -1: node.bf = 1; hijo.bf = 0;  break;
                }
                nieto.bf = 0;
                node.left = rotateSL(hijo);
                node = rotateSR(node);
                break;
            case 0:
                node.bf = -1; hijo.bf = 1;
                node = rotateSR(node);
                break;
        }
        return node;
    }

    private NodeAVL rotateSL(NodeAVL node) {
        NodeAVL p = (NodeAVL) node.right;
        node.right = p.left;
        p.left = node;
        return p;
    }

    private NodeAVL rotateSR(NodeAVL node) {
        NodeAVL p = (NodeAVL) node.left;
        node.left = p.right;
        p.right = node;
        return p;
    }

    public void remove(E x) {
        this.height = false;
        this.root = removeRec(x, (NodeAVL) this.root);
        registrar("ELIMINAR: " + x);
    }

    private NodeAVL removeRec(E x, NodeAVL node) {
        if (node == null) return null;
        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            node.left = removeRec(x, (NodeAVL) node.left);
            if (this.height) node = balanceOnRemoveLeft(node);
        } else if (cmp > 0) {
            node.right = removeRec(x, (NodeAVL) node.right);
            if (this.height) node = balanceOnRemoveRight(node);
        } else {
            if (node.left == null || node.right == null) {
                this.height = true;
                return (NodeAVL) (node.left != null ? node.left : node.right);
            } else {
                NodeAVL successor = getMin((NodeAVL) node.right);
                node.data = successor.data;
                node.right = removeRec(successor.data, (NodeAVL) node.right);
                if (this.height) node = balanceOnRemoveRight(node);
            }
        }
        return node;
    }

    private NodeAVL getMin(NodeAVL node) {
        while (node.left != null) node = (NodeAVL) node.left;
        return node;
    }

    private NodeAVL balanceOnRemoveLeft(NodeAVL node) {
        switch (node.bf) {
            case -1: node.bf = 0; this.height = true; break;
            case 0:  node.bf = 1; this.height = false; break;
            case 1:
                NodeAVL rs = (NodeAVL) node.right;
                int bfr = rs.bf;
                node = balanceToLeft(node);
                this.height = (bfr != 0);
                break;
        }
        return node;
    }

    private NodeAVL balanceOnRemoveRight(NodeAVL node) {
        switch (node.bf) {
            case 1:  node.bf = 0; this.height = true; break;
            case 0:  node.bf = -1; this.height = false; break;
            case -1:
                NodeAVL ls = (NodeAVL) node.left;
                int bfl = ls.bf;
                node = balanceToRight(node);
                this.height = (bfl != 0);
                break;
        }
        return node;
    }

    public E buscar(E x) {
        try {
            return search(x);
        } catch (ItemNotFound e) {
            return null;
        }
    }

    public List<String> buscarConPasos(E x) {
        List<String> pasos = new ArrayList<>();
        buscarPasosRec(root, x, pasos, 1);
        registrar("BUSCAR: " + x);
        return pasos;
    }

    private void buscarPasosRec(Node node, E x, List<String> pasos, int paso) {
        if (node == null) {
            pasos.add("Paso " + paso + ": NULL → No encontrado.");
            return;
        }
        NodeAVL avl = (NodeAVL) node;
        int cmp = x.compareTo(node.data);
        if (cmp == 0) {
            pasos.add("Paso " + paso + ": ✅ ENCONTRADO → " + node.data + "  [bf=" + avl.bf + "]");
        } else if (cmp < 0) {
            pasos.add("Paso " + paso + ": Visitar " + node.data + " [bf=" + avl.bf + "]  →  ir a IZQUIERDA");
            buscarPasosRec(node.left, x, pasos, paso + 1);
        } else {
            pasos.add("Paso " + paso + ": Visitar " + node.data + " [bf=" + avl.bf + "]  →  ir a DERECHA");
            buscarPasosRec(node.right, x, pasos, paso + 1);
        }
    }

    public int totalFavoritos() {
        return contarFavoritos(root);
    }

    private int contarFavoritos(Node node) {
        if (node == null) return 0;
        int cuenta = 0;
        if (node.data instanceof Contacto && ((Contacto) node.data).isFavorito()) cuenta = 1;
        return cuenta + contarFavoritos(node.left) + contarFavoritos(node.right);
    }

    public NodeAVL getRootAVL() {
        return (NodeAVL) root;
    }

    public boolean estaBalanceado() {
        return verificarBalance(root);
    }

    private boolean verificarBalance(Node node) {
        if (node == null) return true;
        int diff = Math.abs(height(node.left) - height(node.right));
        return diff <= 1 && verificarBalance(node.left) && verificarBalance(node.right);
    }
}