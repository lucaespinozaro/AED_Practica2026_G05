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
        if (cmp < 0) node.left = insert(node.left, data);
        else if (cmp > 0) node.right = insert(node.right, data);
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
        if (cmp < 0) node.left = delete(node.left, data);
        else if (cmp > 0) node.right = delete(node.right, data);
        else {
            if (node.left == null) return node.right;
            if (node.right == null) return node.left;
            Node min = node.right;
            while (min.left != null) min = min.left;
            node.data = min.data;
            node.right = delete(node.right, min.data);
        }
        return node;
    }

    public void destroyNodes() throws ExceptionIsEmpty {
        if (isEmpty()) throw new ExceptionIsEmpty("BST empty");
        this.root = null;
    }

    public int countAllNodes() {
        int count = 0;
        if (isEmpty()) return 0;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            try {
                Node n = q.dequeue();
                count++;
                if (n.left != null) q.enqueue(n.left);
                if (n.right != null) q.enqueue(n.right);
            } catch (Exception e) {}
        }
        return count;
    }

    public int countNodes() {
        int count = 0;
        if (isEmpty()) return 0;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            try {
                Node n = q.dequeue();
                if (n.left != null || n.right != null) count++;
                if (n.left != null) q.enqueue(n.left);
                if (n.right != null) q.enqueue(n.right);
            } catch (Exception e) {}
        }
        return count;
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
        parenthesize(this.root, 0);
    }

    private void parenthesize(Node node, int level) {
        if (node == null) return;
        String tab = "    ".repeat(level);
        if (node.left != null || node.right != null) {
            System.out.println(tab + node.data + " (");
            parenthesize(node.left, level + 1);
            parenthesize(node.right, level + 1);
            System.out.println(tab + ")");
        } else {
            System.out.println(tab + node.data);
        }
    }

    public boolean isValidBST() {
        return isValidBST(this.root, null, null);
    }

    private boolean isValidBST(Node node, E min, E max) {
        if (node == null) return true;
        if (min != null && node.data.compareTo(min) <= 0) return false;
        if (max != null && node.data.compareTo(max) >= 0) return false;
        return isValidBST(node.left, min, node.data) && isValidBST(node.right, node.data, max);
    }

    public List<E> searchRange(E min, E max) {
        validateData(min);
        validateData(max);
        List<E> result = new ArrayList<>();
        searchRange(this.root, min, max, result);
        return result;
    }

    private void searchRange(Node node, E min, E max, List<E> result) {
        if (node == null) return;
        if (node.data.compareTo(min) > 0) searchRange(node.left, min, max, result);
        if (node.data.compareTo(min) >= 0 && node.data.compareTo(max) <= 0) result.add(node.data);
        if (node.data.compareTo(max) < 0) searchRange(node.right, min, max, result);
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
        printDescending(this.root);
        System.out.println();
    }

    private void printDescending(Node node) {
        if (node == null) return;
        printDescending(node.right);
        System.out.print(node.data + " ");
        printDescending(node.left);
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

    public void inOrder() { inOrder(root); System.out.println(); }
    private void inOrder(Node n) { if (n!=null) { inOrder(n.left); System.out.print(n.data + " "); inOrder(n.right); } }

    public void preOrder() { preOrder(root); System.out.println(); }
    private void preOrder(Node n) { if (n!=null) { System.out.print(n.data + " "); preOrder(n.left); preOrder(n.right); } }

    public void postOrder() { postOrder(root); System.out.println(); }
    private void postOrder(Node n) { if (n!=null) { postOrder(n.left); postOrder(n.right); System.out.print(n.data + " "); } }

    public void drawBST() { drawBST(this.root, 0); }
    private void drawBST(Node n, int l) {
        if (n == null) return;
        drawBST(n.right, l + 1);
        System.out.println("    ".repeat(l) + "[" + n.data + "]");
        drawBST(n.left, l + 1);
    }
}
