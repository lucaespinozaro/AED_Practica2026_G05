import java.util.ArrayList;
import java.util.List;

public class LinkedBST<E extends Comparable<E>> implements BinarySearchTree<E> {

    public class Node {
        public E data;
        public Node left;
        public Node right;

        public Node(E data) {
            this.data = data;
            this.left = null;
            this.right = null;
        }
    }

    private Node root;
    private List<String> historialOperaciones;

    public LinkedBST() {
        this.root = null;
        this.historialOperaciones = new ArrayList<>();
    }

    public Node getRoot() { return root; }

    @Override
    public boolean isEmpty() { return this.root == null; }

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
        else throw new ItemDuplicated("Item duplicado");
        return node;
    }

    @Override
    public E search(E data) throws ItemNotFound {
        validateData(data);
        Node current = this.root;
        while (current != null) {
            int cmp = data.compareTo(current.data);
            if (cmp == 0) return current.data;
            current = (cmp < 0) ? current.left : current.right;
        }
        throw new ItemNotFound("Item no encontrado");
    }

    @Override
    public void delete(E data) throws ExceptionIsEmpty, ItemNotFound {
        validateData(data);
        if (isEmpty()) throw new ExceptionIsEmpty("BST vacío");
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

    public List<E> inOrden() {
        List<E> lista = new ArrayList<>();
        inOrdenRec(root, lista);
        return lista;
    }

    private void inOrdenRec(Node n, List<E> lista) {
        if (n == null) return;
        inOrdenRec(n.left, lista);
        lista.add(n.data);
        inOrdenRec(n.right, lista);
    }

    public List<E> preOrden() {
        List<E> lista = new ArrayList<>();
        preOrdenRec(root, lista);
        return lista;
    }

    private void preOrdenRec(Node n, List<E> lista) {
        if (n == null) return;
        lista.add(n.data);
        preOrdenRec(n.left, lista);
        preOrdenRec(n.right, lista);
    }

    public List<E> postOrden() {
        List<E> lista = new ArrayList<>();
        postOrdenRec(root, lista);
        return lista;
    }

    private void postOrdenRec(Node n, List<E> lista) {
        if (n == null) return;
        postOrdenRec(n.left, lista);
        postOrdenRec(n.right, lista);
        lista.add(n.data);
    }

    public int altura() { return alturaRec(root); }

    private int alturaRec(Node n) {
        if (n == null) return 0;
        return 1 + Math.max(alturaRec(n.left), alturaRec(n.right));
    }

    public int totalNodos() { return inOrden().size(); }

    public boolean insertar(E data) {
        try {
            insert(data);
            if (data instanceof Libro) {
                Libro l = (Libro) data;
                historialOperaciones.add("✓ INSERTAR: \"" + l.getTitulo() + "\" (ISBN: " + l.getIsbn() + ")");
            }
            return true;
        } catch (ItemDuplicated e) {
            if (data instanceof Libro) {
                Libro l = (Libro) data;
                historialOperaciones.add("✗ INSERTAR: ISBN " + l.getIsbn() + " ya existe.");
            }
            return false;
        }
    }

    public E buscar(E key) {
        try { return search(key); }
        catch (ItemNotFound e) { return null; }
    }

    public List<String> buscarConPasos(E key) {
        List<String> pasos = new ArrayList<>();
        buscarConPasosRec(root, key, pasos);
        E encontrado = buscar(key);
        if (key instanceof Libro) {
            Libro lk = (Libro) key;
            historialOperaciones.add("🔍 BUSCAR ISBN: " + lk.getIsbn() +
                    (encontrado != null ? " → Encontrado" : " → No encontrado"));
        }
        return pasos;
    }

    private boolean buscarConPasosRec(Node node, E key, List<String> pasos) {
        if (node == null) {
            pasos.add("✗ No se encontró el elemento");
            return false;
        }
        String id = node.data instanceof Libro
                ? ((Libro) node.data).getIsbn() + " \"" + ((Libro) node.data).getTitulo() + "\""
                : node.data.toString();
        pasos.add("→ Comparando con [" + id + "]");
        int cmp = key.compareTo(node.data);
        if (cmp == 0) {
            pasos.add("✓ ¡Encontrado!");
            return true;
        } else if (cmp < 0) {
            pasos.add("  < → ir a la izquierda");
            return buscarConPasosRec(node.left, key, pasos);
        } else {
            pasos.add("  > → ir a la derecha");
            return buscarConPasosRec(node.right, key, pasos);
        }
    }

    public boolean eliminar(E key) {
        E encontrado = buscar(key);
        if (encontrado == null) {
            if (key instanceof Libro) {
                historialOperaciones.add("✗ ELIMINAR: ISBN " + ((Libro) key).getIsbn() + " no encontrado.");
            }
            return false;
        }
        try {
            delete(key);
            if (encontrado instanceof Libro) {
                Libro l = (Libro) encontrado;
                historialOperaciones.add("🗑 ELIMINAR: \"" + l.getTitulo() + "\" (ISBN: " + l.getIsbn() + ")");
            }
            return true;
        } catch (Exception e) { return false; }
    }

    public boolean prestarLibro(E key) {
        E encontrado = buscar(key);
        if (encontrado == null) {
            if (key instanceof Libro) historialOperaciones.add("✗ PRÉSTAMO: ISBN " + ((Libro)key).getIsbn() + " no existe.");
            return false;
        }
        Libro l = (Libro) encontrado;
        if (!l.isDisponible()) { historialOperaciones.add("✗ PRÉSTAMO: \"" + l.getTitulo() + "\" ya está prestado."); return false; }
        l.setDisponible(false);
        historialOperaciones.add("📤 PRÉSTAMO: \"" + l.getTitulo() + "\" (ISBN: " + l.getIsbn() + ")");
        return true;
    }

    public boolean devolverLibro(E key) {
        E encontrado = buscar(key);
        if (encontrado == null) {
            if (key instanceof Libro) historialOperaciones.add("✗ DEVOLUCIÓN: ISBN " + ((Libro)key).getIsbn() + " no existe.");
            return false;
        }
        Libro l = (Libro) encontrado;
        if (l.isDisponible()) { historialOperaciones.add("✗ DEVOLUCIÓN: \"" + l.getTitulo() + "\" ya está disponible."); return false; }
        l.setDisponible(true);
        historialOperaciones.add("📥 DEVOLUCIÓN: \"" + l.getTitulo() + "\" (ISBN: " + l.getIsbn() + ")");
        return true;
    }

    public int totalDisponibles() {
        return (int) inOrden().stream()
                .filter(e -> e instanceof Libro && ((Libro) e).isDisponible()).count();
    }

    public int totalPrestados() {
        return (int) inOrden().stream()
                .filter(e -> e instanceof Libro && !((Libro) e).isDisponible()).count();
    }

    public List<String> getHistorial() { return historialOperaciones; }

    public void limpiarHistorial() { historialOperaciones.clear(); }

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

    public int amplitude() {
        if (isEmpty()) return 0;
        int max = 0;
        LinkedQueue<Node> q = new LinkedQueue<>();
        q.enqueue(root);
        while (!q.isEmpty()) {
            int size = q.size();
            if (size > max) max = size;
            for (int i = 0; i < size; i++) {
                try {
                    Node n = q.dequeue();
                    if (n.left != null) q.enqueue(n.left);
                    if (n.right != null) q.enqueue(n.right);
                } catch (Exception e) {}
            }
        }
        return max;
    }

    public boolean isValidBST() { return validateBST(root, null, null); }

    private boolean validateBST(Node node, E min, E max) {
        if (node == null) return true;
        if (min != null && node.data.compareTo(min) <= 0) return false;
        if (max != null && node.data.compareTo(max) >= 0) return false;
        return validateBST(node.left, min, node.data) && validateBST(node.right, node.data, max);
    }
}
