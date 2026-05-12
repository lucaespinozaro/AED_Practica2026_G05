import java.util.List;

public class Controlador {
    private LinkedBST<Libro> arbol;

    public Controlador(LinkedBST<Libro> arbol) {
        this.arbol = arbol;
    }

    public LinkedBST<Libro> getArbol() { return arbol; }

    public boolean insertar(Libro libro) { return arbol.insertar(libro); }

    public boolean eliminar(String isbn) {
        return arbol.eliminar(new Libro(isbn, "", "", 0, ""));
    }

    public Libro buscar(String isbn) {
        return arbol.buscar(new Libro(isbn, "", "", 0, ""));
    }

    public List<String> buscarConPasos(String isbn) {
        return arbol.buscarConPasos(new Libro(isbn, "", "", 0, ""));
    }

    public boolean prestarLibro(String isbn) {
        return arbol.prestarLibro(new Libro(isbn, "", "", 0, ""));
    }

    public boolean devolverLibro(String isbn) {
        return arbol.devolverLibro(new Libro(isbn, "", "", 0, ""));
    }
}
