package EjercicioAdicional;

import java.util.ArrayList;
import java.util.List;

public class ArbolBST {

    private NodoBST raiz;
    private List<String> historialOperaciones;

    public ArbolBST() {
        raiz = null;
        historialOperaciones = new ArrayList<>();
    }

    public NodoBST getRaiz() {
        return raiz;
    }

    public boolean insertar(Libro libro) {
        if (buscar(libro.getIsbn()) != null) {
            historialOperaciones.add("✗ INSERTAR: ISBN " + libro.getIsbn() + " ya existe.");
            return false;
        }
        raiz = insertarRec(raiz, libro);
        historialOperaciones.add("✓ INSERTAR: \"" + libro.getTitulo() + "\" (ISBN: " + libro.getIsbn() + ")");
        return true;
    }

    private NodoBST insertarRec(NodoBST nodo, Libro libro) {
        if (nodo == null) return new NodoBST(libro);
        int cmp = libro.getIsbn().compareTo(nodo.libro.getIsbn());
        if (cmp < 0)
            nodo.izquierdo = insertarRec(nodo.izquierdo, libro);
        else if (cmp > 0)
            nodo.derecho = insertarRec(nodo.derecho, libro);
        return nodo;
    }

    public Libro buscar(String isbn) {
        return buscarRec(raiz, isbn);
    }

    private Libro buscarRec(NodoBST nodo, String isbn) {
        if (nodo == null) return null;
        int cmp = isbn.compareTo(nodo.libro.getIsbn());
        if (cmp == 0) return nodo.libro;
        if (cmp < 0) return buscarRec(nodo.izquierdo, isbn);
        return buscarRec(nodo.derecho, isbn);
    }

    public List<String> buscarConPasos(String isbn) {
        List<String> pasos = new ArrayList<>();
        buscarConPasosRec(raiz, isbn, pasos);
        historialOperaciones.add("🔍 BUSCAR ISBN: " + isbn +
                (buscar(isbn) != null ? " → Encontrado" : " → No encontrado"));
        return pasos;
    }

    private boolean buscarConPasosRec(NodoBST nodo, String isbn, List<String> pasos) {
        if (nodo == null) {
            pasos.add("✗ No se encontró el ISBN: " + isbn);
            return false;
        }
        int cmp = isbn.compareTo(nodo.libro.getIsbn());
        pasos.add("→ Comparando con [" + nodo.libro.getIsbn() + "] \"" + nodo.libro.getTitulo() + "\"");
        if (cmp == 0) {
            pasos.add("✓ ¡Encontrado! \"" + nodo.libro.getTitulo() + "\"");
            return true;
        } else if (cmp < 0) {
            pasos.add("  " + isbn + " < " + nodo.libro.getIsbn() + " → ir a la izquierda");
            return buscarConPasosRec(nodo.izquierdo, isbn, pasos);
        } else {
            pasos.add("  " + isbn + " > " + nodo.libro.getIsbn() + " → ir a la derecha");
            return buscarConPasosRec(nodo.derecho, isbn, pasos);
        }
    }

    public boolean eliminar(String isbn) {
        Libro libro = buscar(isbn);
        if (libro == null) {
            historialOperaciones.add("✗ ELIMINAR: ISBN " + isbn + " no encontrado.");
            return false;
        }
        raiz = eliminarRec(raiz, isbn);
        historialOperaciones.add("🗑 ELIMINAR: \"" + libro.getTitulo() + "\" (ISBN: " + isbn + ")");
        return true;
    }

    private NodoBST eliminarRec(NodoBST nodo, String isbn) {
        if (nodo == null) return null;
        int cmp = isbn.compareTo(nodo.libro.getIsbn());
        if (cmp < 0) {
            nodo.izquierdo = eliminarRec(nodo.izquierdo, isbn);
        } else if (cmp > 0) {
            nodo.derecho = eliminarRec(nodo.derecho, isbn);
        } else {
            if (nodo.izquierdo == null) return nodo.derecho;
            if (nodo.derecho == null) return nodo.izquierdo;
            NodoBST sucesor = minimoNodo(nodo.derecho);
            nodo.libro = sucesor.libro;
            nodo.derecho = eliminarRec(nodo.derecho, sucesor.libro.getIsbn());
        }
        return nodo;
    }

    private NodoBST minimoNodo(NodoBST nodo) {
        while (nodo.izquierdo != null) nodo = nodo.izquierdo;
        return nodo;
    }

    public List<Libro> inOrden() {
        List<Libro> lista = new ArrayList<>();
        inOrdenRec(raiz, lista);
        return lista;
    }

    private void inOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        inOrdenRec(nodo.izquierdo, lista);
        lista.add(nodo.libro);
        inOrdenRec(nodo.derecho, lista);
    }

    public List<Libro> preOrden() {
        List<Libro> lista = new ArrayList<>();
        preOrdenRec(raiz, lista);
        return lista;
    }

    private void preOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        lista.add(nodo.libro);
        preOrdenRec(nodo.izquierdo, lista);
        preOrdenRec(nodo.derecho, lista);
    }

    public List<Libro> postOrden() {
        List<Libro> lista = new ArrayList<>();
        postOrdenRec(raiz, lista);
        return lista;
    }

    private void postOrdenRec(NodoBST nodo, List<Libro> lista) {
        if (nodo == null) return;
        postOrdenRec(nodo.izquierdo, lista);
        postOrdenRec(nodo.derecho, lista);
        lista.add(nodo.libro);
    }

    public boolean prestarLibro(String isbn) {
        Libro l = buscar(isbn);
        if (l == null) { historialOperaciones.add("✗ PRÉSTAMO: ISBN " + isbn + " no existe."); return false; }
        if (!l.isDisponible()) { historialOperaciones.add("✗ PRÉSTAMO: \"" + l.getTitulo() + "\" ya está prestado."); return false; }
        l.setDisponible(false);
        historialOperaciones.add("📤 PRÉSTAMO: \"" + l.getTitulo() + "\" (ISBN: " + isbn + ")");
        return true;
    }

    public boolean devolverLibro(String isbn) {
        Libro l = buscar(isbn);
        if (l == null) { historialOperaciones.add("✗ DEVOLUCIÓN: ISBN " + isbn + " no existe."); return false; }
        if (l.isDisponible()) { historialOperaciones.add("✗ DEVOLUCIÓN: \"" + l.getTitulo() + "\" ya está disponible."); return false; }
        l.setDisponible(true);
        historialOperaciones.add("📥 DEVOLUCIÓN: \"" + l.getTitulo() + "\" (ISBN: " + isbn + ")");
        return true;
    }

    public int altura() { return alturaRec(raiz); }
    private int alturaRec(NodoBST n) {
        if (n == null) return 0;
        return 1 + Math.max(alturaRec(n.izquierdo), alturaRec(n.derecho));
    }

    public int totalNodos() { return inOrden().size(); }

    public int totalDisponibles() {
        return (int) inOrden().stream().filter(Libro::isDisponible).count();
    }

    public int totalPrestados() {
        return (int) inOrden().stream().filter(l -> !l.isDisponible()).count();
    }

    public List<String> getHistorial() { return historialOperaciones; }

    public void limpiarHistorial() { historialOperaciones.clear(); }

    public boolean estaVacio() { return raiz == null; }
}
