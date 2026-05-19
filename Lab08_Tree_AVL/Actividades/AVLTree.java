public class AVLTree<E extends Comparable<E>> extends BSTree<E> {
    private boolean height;

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

    public void insert(E x) throws ItemDuplicated {
        this.height = false;
        this.root = insertRec(x, (NodeAVL) this.root);
    }

    protected Node insertRec(E x, NodeAVL node) throws ItemDuplicated {
        NodeAVL fat = node;
        if (node == null) {
            this.height = true;
            fat = new NodeAVL(x);
        } else {
            int resC = node.data.compareTo(x);
            if (resC == 0) throw new ItemDuplicated(x + " ya se encuentra en el arbol...");

            if (resC > 0) {
                fat.left = insertRec(x, (NodeAVL) node.left);
                if (this.height) {
                    switch (fat.bf) {
                        case 1: 
                            fat.bf = 0; 
                            this.height = false; 
                            break;
                        case 0: 
                            fat.bf = -1; 
                            this.height = true; 
                            break;
                        case -1: 
                            fat = balanceToRight(fat); 
                            this.height = false; 
                            break;
                    }
                }
            } else {
                fat.right = insertRec(x, (NodeAVL) node.right);
                if (this.height) {
                    switch (fat.bf) {
                        case -1: 
                            fat.bf = 0; 
                            this.height = false; 
                            break;
                        case 0: 
                            fat.bf = 1; 
                            this.height = true; 
                            break;
                        case 1: 
                            fat = balanceToLeft(fat); 
                            this.height = false; 
                            break;
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
                node.bf = 0; 
                hijo.bf = 0;
                node = rotateSL(node);
                break;
            case -1:
                NodeAVL nieto = (NodeAVL) hijo.left;
                switch (nieto.bf) {
                    case -1: 
                        node.bf = 0; 
                        hijo.bf = 1; 
                        break;
                    case 0: 
                        node.bf = 0; 
                        hijo.bf = 0; 
                        break;
                    case 1: 
                        node.bf = -1; 
                        hijo.bf = 0; 
                        break;
                }
                nieto.bf = 0;
                node.right = rotateSR(hijo);
                node = rotateSL(node);
                break;
            case 0:
                node.bf = 1; 
                hijo.bf = -1;
                node = rotateSL(node);
                break;
        }
        return node;
    }

    private NodeAVL balanceToRight(NodeAVL node) {
        NodeAVL hijo = (NodeAVL) node.left;
        switch (hijo.bf) {
            case -1:
                node.bf = 0; 
                hijo.bf = 0;
                node = rotateSR(node);
                break;
            case 1:
                NodeAVL nieto = (NodeAVL) hijo.right;
                switch (nieto.bf) {
                    case 1: 
                        node.bf = 0; 
                        hijo.bf = -1; 
                        break;
                    case 0: 
                        node.bf = 0; 
                        hijo.bf = 0; 
                        break;
                    case -1: 
                        node.bf = 1; 
                        hijo.bf = 0; 
                        break;
                }
                nieto.bf = 0;
                node.left = rotateSL(hijo);
                node = rotateSR(node);
                break;
            case 0:
                node.bf = -1; 
                hijo.bf = 1;
                node = rotateSR(node);
                break;
        }
        return node;
    }

    private NodeAVL rotateSL(NodeAVL node) {
        NodeAVL p = (NodeAVL) node.right;
        node.right = p.left;
        p.left = node;
        node = p;
        return node;
    }

    private NodeAVL rotateSR(NodeAVL node) {
        NodeAVL p = (NodeAVL) node.left;
        node.left = p.right;
        p.right = node;
        node = p;
        return node;
    }

    public void remove(E x) {
        this.height = false;
        this.root = removeRec(x, (NodeAVL) this.root);
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
            case -1: 
                node.bf = 0; 
                this.height = true; 
                break;
            case 0: 
                node.bf = 1; 
                this.height = false; 
                break;
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
            case 1: 
                node.bf = 0; 
                this.height = true; 
                break;
            case 0:
                node.bf = -1; 
                this.height = false; 
                break;
            case -1:
                NodeAVL ls = (NodeAVL) node.left;
                int bfl = ls.bf;
                node = balanceToRight(node);
                this.height = (bfl != 0);
                break;
        }
        return node;
    }

    public void preOrder() {
        preOrderRec((NodeAVL) root);
        System.out.println();
    }

    private void preOrderRec(NodeAVL node) {
        if (node != null) {
            System.out.print(node.data + "(" + node.bf + ") ");
            preOrderRec((NodeAVL) node.left);
            preOrderRec((NodeAVL) node.right);
        }
    }

    public void inOrder() {
        inOrderRec((NodeAVL) root);
        System.out.println();
    }

    private void inOrderRec(NodeAVL node) {
        if (node != null) {
            inOrderRec((NodeAVL) node.left);
            System.out.print(node.data + "(" + node.bf + ") ");
            inOrderRec((NodeAVL) node.right);
        }
    }

    public void breadthFirst() {
        int h = height(root);
        for (int i = 0; i < h; i++) {
            printLevel((NodeAVL) root, i);
        }
        System.out.println();
    }

    private void printLevel(NodeAVL node, int level) {
        if (node == null) return;
        if (level == 0) System.out.print(node.data + "(" + node.bf + ") ");
        else {
            printLevel((NodeAVL) node.left, level - 1);
            printLevel((NodeAVL) node.right, level - 1);
        }
    }
}
