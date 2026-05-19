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
}
