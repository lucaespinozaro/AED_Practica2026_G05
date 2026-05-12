public class Libro implements Comparable<Libro> {
    private String isbn;
    private String titulo;
    private String autor;
    private int anio;
    private String categoria;
    private boolean disponible;

    public Libro(String isbn, String titulo, String autor, int anio, String categoria) {
        this.isbn = isbn;
        this.titulo = titulo;
        this.autor = autor;
        this.anio = anio;
        this.categoria = categoria;
        this.disponible = true;
    }

    @Override
    public int compareTo(Libro otro) {
        return this.isbn.compareTo(otro.isbn);
    }

    public String getIsbn() { return isbn; }
    public String getTitulo() { return titulo; }
    public String getAutor() { return autor; }
    public int getAnio() { return anio; }
    public String getCategoria() { return categoria; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public void setAutor(String autor) { this.autor = autor; }
    public void setAnio(int anio) { this.anio = anio; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    @Override
    public String toString() {
        return String.format("[%s] %s — %s (%d) | %s | %s",
                isbn, titulo, autor, anio, categoria,
                disponible ? "Disponible" : "Prestado");
    }
}
