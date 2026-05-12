import java.util.List;

public class Controlador {
    private ArbolBST arbol;

    public Controlador(ArbolBST arbol) {
        this.arbol = arbol;
    }

    public ArbolBST getArbol() { return arbol; }

    public boolean insertar(Libro libro) {
        return arbol.insertar(libro);
    }

    public boolean eliminar(String isbn) {
        return arbol.eliminar(isbn);
    }

    public Libro buscar(String isbn) {
        return arbol.buscar(isbn);
    }

    public List<String> buscarConPasos(String isbn) {
        return arbol.buscarConPasos(isbn);
    }

    public boolean prestarLibro(String isbn) {
        return arbol.prestarLibro(isbn);
    }

    public boolean devolverLibro(String isbn) {
        return arbol.devolverLibro(isbn);
    }
}
