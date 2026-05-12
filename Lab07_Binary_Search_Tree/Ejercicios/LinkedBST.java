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

    private void validateData(E data) {
        if (data == null) throw new IllegalArgumentException("Data null");
    }

    @Override
    public void insert(E data) throws ItemDuplicated {
        validateData(data);
        this.root = insertRec(this.root, data);
    }

    private Node insertRec(Node node, E data) throws ItemDuplicated {
        if (node == null) return new Node(data);
        int cmp = data.compareTo(node.data);
        if (cmp < 0) node.left = insertRec(node.left, data);
        else if (cmp > 0) node.right = insertRec(node.right, data);
        else throw new ItemDuplicated("Item duplicated");
        return node;
    }

    @Override
    public E search(E data) throws ItemNotFound {
        validateData(data);
        Node current = this.root;
        while (current != null) {
            System.out.println("Visitando nodo: " + current.data);
            int cmp = data.compareTo(current.data);
            if (cmp == 0) return current.data;
            current = (cmp < 0) ? current.left : current.right;
        }
        throw new ItemNotFound("Item not found");
    }

    @Override
    public void delete(E data) throws ExceptionIsEmpty, ItemNotFound {
        validateData(data);
        if (isEmpty()) throw new ExceptionIsEmpty("BST empty");
        this.root = deleteRec(this.root, data);
    }

    private Node deleteRec(Node node, E data) {
        if (node == null) return null;
        int cmp = data.compareTo(node.data);
        if (cmp < 0) node.left = deleteRec(node.left, data);
        else if (cmp > 0) node.right = deleteRec(node.right, data);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = node.right;
            while (min.left != null) min = min.left;
            node.data = min.data;
            node.right = deleteRec(node.right, min.data);
        }
        return node;
    }

    public void destroyNodes() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("BST empty");
        this.root = null;
    }

    public int countAllNodes() {
        return countAllNodesRec(this.root);
    }

    private int countAllNodesRec(Node node) {
        if (node == null) {
            return 0;
        }
        return 1 + countAllNodesRec(node.left) + countAllNodesRec(node.right);
    }

    public int countNodes() {
        return countNodesRec(this.root);
    }

    private int countNodesRec(Node node) {
        if (node == null) {
            return 0;
        }

        if (node.left == null && node.right == null) {
            return 0;
        }
        return 1 + countNodesRec(node.left) + countNodesRec(node.right);
    }

    public int height(E data) {
        if (data == null) return -1;
        Node current = this.root;
        Node target = null;
        while (current != null) {
            int cmp = data.compareTo(current.data);
            if (cmp == 0) { target = current; break; }
            current = (cmp < 0) ? current.left : current.right;
        }
        if (target == null) return -1;

        int h = -1;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(target);
        while (!q.isEmpty()) {
            h++;
            int size = q.size();
            for (int i = 0; i < size; i++) {
                try {
                    Node n = q.dequeue();
                    if (n.left != null) q.enqueue(n.left);
                    if (n.right != null) q.enqueue(n.right);
                } catch (Exception e) {}
            }
        }
        return h;
    }

    public int amplitude(int nivel) {
        if (isEmpty()) return 0;
        int altura = height(this.root.data);
        if (nivel < 0) return 0;
        
        int max = 0;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(root);
        int nivelActual = 0;
        
        while (!q.isEmpty() && nivelActual <= altura) {
            int size = q.size();
            if (size > max) max = size;
            for (int i = 0; i < size; i++) {
                try {
                    Node n = q.dequeue();
                    if (n.left != null) q.enqueue(n.left);
                    if (n.right != null) q.enqueue(n.right);
                } catch (Exception e) {}
            }
            nivelActual++;
        }
        return max;
    }

    public int areaBST() {
        if (isEmpty()) return 0;
        int leaves = countLeaves();
        int h = height(root.data);
        return leaves * h;
    }

    public void parenthesize() {
        parenthesizeNode(this.root, 0);
    }

    private void parenthesizeNode(Node node, int level) {
        if (node == null) return;
        String tab = "    ".repeat(level);
        if (node.left != null || node.right != null) {
            System.out.println(tab + node.data + " (");
            parenthesizeNode(node.left, level + 1);
            parenthesizeNode(node.right, level + 1);
            System.out.println(tab + ")");
        } else {
            System.out.println(tab + node.data);
        }
    }

    public boolean isValidBST() {
        return validateBST(this.root, null, null);
    }

    private boolean validateBST(Node node, E min, E max) {
        if (node == null) return true;
        if (min != null && node.data.compareTo(min) <= 0) return false;
        if (max != null && node.data.compareTo(max) >= 0) return false;
        return validateBST(node.left, min, node.data) && validateBST(node.right, node.data, max);
    }

    public List<E> searchRange(E min, E max) {
        validateData(min);
        validateData(max);
        List<E> result = new ArrayList<>();
        searchRangeHelper(this.root, min, max, result);
        return result;
    }

    private void searchRangeHelper(Node node, E min, E max, List<E> result) {
        if (node == null) return;
        if (node.data.compareTo(min) > 0) searchRangeHelper(node.left, min, max, result);
        if (node.data.compareTo(min) >= 0 && node.data.compareTo(max) <= 0) result.add(node.data);
        if (node.data.compareTo(max) < 0) searchRangeHelper(node.right, min, max, result);
    }

    public int countLeaves() {
        if (isEmpty()) return 0;
        int count = 0;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            try {
                Node n = q.dequeue();
                if (n.left == null && n.right == null) count++;
                if (n.left != null) q.enqueue(n.left);
                if (n.right != null) q.enqueue(n.right);
            } catch (Exception e) {}
        }
        return count;
    }

    public void printDescending() {
        printDescHelper(this.root);
        System.out.println();
    }

    private void printDescHelper(Node node) {
        if (node == null) return;
        printDescHelper(node.right);
        System.out.print(node.data + " ");
        printDescHelper(node.left);
    }

    public E getMin() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty();
        Node curr = root;
        while (curr.left != null) curr = curr.left;
        return curr.data;
    }

    public E getMax() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty();
        Node curr = root;
        while (curr.right != null) curr = curr.right;
        return curr.data;
    }

    public void inOrder() { 
        inOrderRec(root); 
        System.out.println(); 
    }
    
    private void inOrderRec(Node n) { 
        if (n!=null) { 
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
        if (n!=null) { 
            System.out.print(n.data + " "); 
            preOrderRec(n.left); 
            preOrderRec(n.right); 
        } 
    }

    public void postOrder() { 
        postOrderRec(root); 
        System.out.println(); 
    }
    
    private void postOrderRec(Node n) { 
        if (n!=null) { 
            postOrderRec(n.left); 
            postOrderRec(n.right); 
            System.out.print(n.data + " "); 
        } 
    }

    public void drawBST() { System.out.println(this.toString()); }
    private String drawHelper(Node n, int l) {
        if (n == null) return;
        StringBuilder sb = new StringBuilder();
        sb.append(drawHelper(n.right, l + 1));
        
        String indent = "      ".repeat(l);
        String connector = (l > 0) ? "└── " : "";
        sb.append(indent).append(connector).append("[").append(n.data).append("]\n");
        
        sb.append(drawHelper(n.left, l + 1));
        return sb.toString();
    }

    @Override
    public String toString() {
        if (isEmpty()) return "Árbol vació";
        return drawHelper(this.root, 0);
}
