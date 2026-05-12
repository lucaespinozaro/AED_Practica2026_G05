public class NodoBST {
    public Libro libro;
    public NodoBST izquierdo;
    public NodoBST derecho;

    public NodoBST(Libro libro) {
        this.libro = libro;
        this.izquierdo = null;
        this.derecho = null;
    }
}
