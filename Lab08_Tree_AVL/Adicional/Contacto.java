public class Contacto implements Comparable<Contacto> {
    private String nombre;
    private String telefono;
    private String email;
    private String categoria;
    private boolean favorito;

    public Contacto(String nombre, String telefono, String email, String categoria) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.email = email;
        this.categoria = categoria;
        this.favorito = false;
    }

    @Override
    public int compareTo(Contacto otro) {
        return this.nombre.compareToIgnoreCase(otro.nombre);
    }

    public String getNombre()    { return nombre; }
    public String getTelefono()  { return telefono; }
    public String getEmail()     { return email; }
    public String getCategoria() { return categoria; }
    public boolean isFavorito()  { return favorito; }

    public void setNombre(String nombre)       { this.nombre = nombre; }
    public void setTelefono(String telefono)   { this.telefono = telefono; }
    public void setEmail(String email)         { this.email = email; }
    public void setCategoria(String categoria) { this.categoria = categoria; }
    public void setFavorito(boolean favorito)  { this.favorito = favorito; }

    @Override
    public String toString() {
        return nombre + " | " + telefono + " | " + email + " | " + categoria + (favorito ? " ★" : "");
    }
}
