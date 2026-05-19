public class BSTree<E extends Comparable<E>> {
    protected Node root;

    protected class Node {
        protected E data;
        protected Node left;
        protected Node right;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    public BSTree() {
        this.root = null;
    }

    public boolean isEmpty() {
        return this.root == null;
    }

    public int height(Node node) {
        if (node == null) return 0;
        return 1 + Math.max(height(node.left), height(node.right));
    }
    
    public int getHeight() {
        return height(root);
    }
    
    public boolean search(E x) {
        return searchRec(this.root, x);
    }

    private boolean searchRec(Node node, E x) {
        if (node == null) return false;
        
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
        this.root = insertRec(this.root, x);
    }

    private Node insertRec(Node node, E x) throws ItemDuplicated {
        if (node == null) return new Node(x);
        int cmp = x.compareTo(node.data);
        if (cmp < 0) {
            node.left = insertRec(node.left, x);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, x);
        } else {
            throw new ItemDuplicated(x + " ya existe en el BST.");
        }
        return node;
    }

    public void inOrder() {
        inOrderRec(root);
        System.out.println();
    }

    private void inOrderRec(Node n) {
        if (n != null) {
            inOrderRec(n.left);
            System.out.print(n.data + " ");
            inOrderRec(n.right);
        }
    }

    public void preOrder() {
        preOrderRec(root);
        System.out.println();
    }

    private void preOrderRec(Node n) {
        if (n != null) {
            System.out.print(n.data + " ");
            preOrderRec(n.left);
            preOrderRec(n.right);
        }
    }

    public void breadthFirst() {
        int h = height(root);
        for (int i = 0; i < h; i++) {
            printLevel(root, i);
        }
        System.out.println();
    }

    private void printLevel(Node node, int level) {
        if (node == null) return;
        if (level == 0) System.out.print(node.data + " ");
        else {
            printLevel(node.left, level - 1);
            printLevel(node.right, level - 1);
        }
    }
}
