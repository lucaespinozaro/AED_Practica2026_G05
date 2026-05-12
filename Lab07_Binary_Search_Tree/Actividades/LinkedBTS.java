public class LinkedBST<E extends Comparable<E>> implements BinarySearchTree<E> {
    private class Node {
        private E data;
        private Node left;
        private Node right;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;

    public LinkedBST() {
        this.root = null;
    }

    @Override
    public boolean isEmpty() {
        return this.root == null;
    }

    @Override
    public void insert(E data) throws ItemDuplicated {
        validateData(data);
        this.root = insertRec(this.root, data);
    }

    private Node insertRec(Node node, E data) throws ItemDuplicated {
        if (node == null) {
            return new Node(data);
        }

        int cmp = data.compareTo(node.data);

        if (cmp < 0) {
            node.left = insertRec(node.left, data);
        } else if (cmp > 0) {
            node.right = insertRec(node.right, data);
        } else {
            throw new ItemDuplicated("Item duplicated");
        }

        return node;
    }

    @Override
    public E search(E data) throws ItemNoFound {
        validateData(data);
        Node found = searchRec(this.root, data);
        if (found == null) {
            throw new ItemNoFound("El dato no existe en el BST.");
        }
        return found.data;
    }

    private Node searchRec(Node node, E data) {
        if (node == null) { return false; }
        int cmp = data.compareTo(node.data);
        if (cmp == 0) return node;
        return (cmp < 0) ? searchRec(node.left, data) : searchRec(node.right, data);
    }

    @Override
    public void delete(E data) throws ExceptionIsEmpty {
        validateData(data);

        if (isEmpty()) {
            throw new ExceptionIsEmpty("BST is empty");
        }

        this.root = deleteRec(this.root, data);
    }

    private Node delete(Node node, E data) {
        if (node == null) {
            throw new ItemNoFound("El elemento no se encuentra en el árbol.");
        }

        int cmp = data.compareTo(node.data);

        if (cmp < 0) {
            node.left = deleteRec(node.left, data);
        } else if (cmp > 0) {
            node.right = deleteRec(node.right, data);
        } else {

            if (node.left == null && node.right == null) return null;
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;

            Node successor = findMinNode(node.right);
            node.data = successor.data;
            node.right = deleteRec(node.right, successor.data);
        }
        return node;
    }

    @Override
    public String toString() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        inOrder(this.root, sb);
        return sb.toString().trim();
    }

    private void inOrder(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        inOrder(node.left, sb);
        sb.append(node.data).append(" ");
        inOrder(node.right, sb);
    }

    public String preOrder() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        preOrder(this.root, sb);
        return sb.toString().trim();
    }

    private void preOrderRec(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        sb.append(node.data).append(" ");
        preOrder(node.left, sb);
        preOrder(node.right, sb);
    }

    public String postOrder() {
        if (isEmpty()) {
            return "";
        }

        StringBuilder sb = new StringBuilder();
        postOrder(this.root, sb);
        return sb.toString().trim();
    }

    private void postOrderRec(Node node, StringBuilder sb) {
        if (node == null) {
            return;
        }

        postOrder(node.left, sb);
        postOrder(node.right, sb);
        sb.append(node.data).append(" ");
    }

    public E findMinNode() throws ItemNoFound {
        if (isEmpty()) {
            throw new ItemNoFound("BST is empty");
        }
        return findMinNode(this.root).data;
    }

    public E findMinNode(E data) throws ItemNoFound {
        validateData(data);
        search(data);
        Node subRoot = searchRec(this.root, data);
        return findMinNode(subRoot).data;
    }

    private Node findMinNode(Node node) {
        while (node.left != null) {
            node = node.left;
        }
        return node;
    }

    public E findMaxNode() throws ItemNoFound {
        if (isEmpty()) {
            throw new ItemNoFound("BST is empty");
        }
        return findMaxNode(this.root).data;
    }

    public E findMaxNode(E data) throws ItemNoFound {
        validateData(data);
        search(data);
        Node subRoot = searchRec(this.root, data);
        return findMaxNode(subRoot).data;
    }

    private Node findMaxNode(Node node) {
        while (node.right != null) {
            node = node.right;
        }
        return node;
    }

    private void validateData(E data) {
        if (data == null) {
            throw new IllegalArgumentException("Data null");
        }
    }
}
