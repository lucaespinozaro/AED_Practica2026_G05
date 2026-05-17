public class AVLTree<E extends Comparable<E>> extends BSTree<E> {    
    private boolean height;

    class NodeAVL extends Node<E> {
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

    public boolean search(E x) {
        return searchRec(this.root, x);
    }

    private boolean searchRec(Node<E> node, E x) {
        if (node == null) {
            return false;
        }
        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            return searchRec(node.left, x);
        } else if (cmp > 0) {
            return searchRec(node.right, x);
        } else {
            return true;
        }
    }

    public void insert(E x) throws ItemDuplicated {
        this.height = false;
        this.root = insert(x, (NodeAVL) this.root);
    }

    protected Node<E> insert(E x, NodeAVL node) throws ItemDuplicated {
        NodeAVL fat = node;

        if (node == null) {
            this.height = true;
            fat = new NodeAVL(x);
        } else {
            int resC = node.data.compareTo(x);
            
            if (resC == 0) {
                throw new ItemDuplicated(x + " ya se encuentra en el arbol...");
            }

            if (resC > 0) { 
                fat.left = insert(x, (NodeAVL) node.left);
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
                fat.right = insert(x, (NodeAVL) node.right);
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
                    case -1: node.bf = 0; hijo.bf = 1; break;
                    case 0:  node.bf = 0; hijo.bf = 0; break;
                    case 1:  node.bf = -1; hijo.bf = 0; break;
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
                    case 1:  node.bf = 0; hijo.bf = -1; break;
                    case 0:  node.bf = 0; hijo.bf = 0; break;
                    case -1: node.bf = 1; hijo.bf = 0; break;
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
        this.root = remove(x, (NodeAVL) this.root);
    }

    protected Node<E> remove(E x, NodeAVL node) {
        if (node == null) {
            this.height = false;
            return null;
        }

        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            node.left = remove(x, (NodeAVL) node.left);
            if (this.height) {
                node = balanceOnRemoveLeft(node);
            }
        } else if (cmp > 0) {
            node.right = remove(x, (NodeAVL) node.right);
            if (this.height) {
                node = balanceOnRemoveRight(node);
            }
        } else {
            if (node.left == null || node.right == null) {
                this.height = true;
                return (node.left != null) ? node.left : node.right;
            } else {
                NodeAVL successor = getMin((NodeAVL) node.right);
                node.data = successor.data;
                node.right = remove(successor.data, (NodeAVL) node.right);
                if (this.height) {
                    node = balanceOnRemoveRight(node);
                }
            }
        }
        return node;
    }

    private NodeAVL getMin(NodeAVL node) {
        while (node.left != null) {
            node = (NodeAVL) node.left;
        }
        return node;
    }

    private NodeAVL balanceOnRemoveLeft(NodeAVL node) {
        switch (node.bf) {
            case -1: node.bf = 0; this.height = true; break;
            case 0:  node.bf = 1; this.height = false; break;
            case 1:
                NodeAVL rightChild = (NodeAVL) node.right;
                int bfr = rightChild.bf;
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
                NodeAVL leftChild = (NodeAVL) node.left;
                int bfl = leftChild.bf;
                node = balanceToRight(node);
                this.height = (bfl != 0);
                break;
        }
        return node;
    }

    public void breadthFirst() {
        int h = height((NodeAVL) root);
        for (int i = 0; i < h; i++) {
            printGivenLevel((NodeAVL) root, i);
        }
        System.out.println();
    }

    private int height(NodeAVL node) {
        if (node == null) return 0;
        return 1 + Math.max(height((NodeAVL) node.left), height((NodeAVL) node.right));
    }

    private void printGivenLevel(NodeAVL node, int level) {
        if (node == null) return;
        if (level == 0) {
            System.out.print(node.data + "(" + node.bf + ") ");
        } else if (level > 0) {
            printGivenLevel((NodeAVL) node.left, level - 1);
            printGivenLevel((NodeAVL) node.right, level - 1);
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
}
