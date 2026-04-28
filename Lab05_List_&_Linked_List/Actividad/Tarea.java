public class Tarea implements Comparable<Tarea> {
    private String titulo;
    private int prioridad;
    private String estado;

    public Tarea(String titulo, int prioridad, String estado) {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("Titulo invalido");
        if (prioridad < 1 || prioridad > 3) throw new IllegalArgumentException("Prioridad invalida");
        if (estado == null || (!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("completada")))
            throw new IllegalArgumentException("Estado invalido");

        this.titulo = titulo.trim();
        this.prioridad = prioridad;
        this.estado = estado.toLowerCase();
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        if (titulo == null || titulo.trim().isEmpty()) throw new IllegalArgumentException("Titulo invalido");
        this.titulo = titulo.trim();
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        if (prioridad < 1 || prioridad > 3) throw new IllegalArgumentException("Prioridad invalida");
        this.prioridad = prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || (!estado.equalsIgnoreCase("pendiente") && !estado.equalsIgnoreCase("completada")))
            throw new IllegalArgumentException("Estado invalido");
        this.estado = estado.toLowerCase();
    }

    @Override
    public int compareTo(Tarea otra) {
        if (otra == null) throw new IllegalArgumentException("Comparacion null");
        return Integer.compare(this.prioridad, otra.prioridad);
    }

    @Override
    public String toString() {
        return "[" + titulo + " | Prioridad: " + prioridad + " | Estado: " + estado + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tarea)) return false;
        Tarea t = (Tarea) obj;
        return titulo.equals(t.titulo) && prioridad == t.prioridad;
    }
}
