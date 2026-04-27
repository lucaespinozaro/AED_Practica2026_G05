public class Tarea implements Comparable<Tarea> {
    private String nombre;
    private int prioridad;
    private String estado;

    public Tarea(String nombre, int prioridad, String estado) {
        if (nombre == null || nombre.trim().isEmpty()) throw new IllegalArgumentException("Nombre invalido");
        if (prioridad < 0) throw new IllegalArgumentException("Prioridad invalida");
        if (estado == null || estado.trim().isEmpty()) throw new IllegalArgumentException("Estado invalido");
        this.nombre = nombre.trim();
        this.prioridad = prioridad;
        this.estado = estado.trim();
    }

    public int getPrioridad() {
        return prioridad;
    }

    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        if (estado == null || estado.trim().isEmpty()) throw new IllegalArgumentException("Estado invalido");
        this.estado = estado.trim();
    }

    @Override
    public int compareTo(Tarea o) {
        if (o == null) throw new IllegalArgumentException("Comparacion null");
        return Integer.compare(this.prioridad, o.prioridad);
    }

    @Override
    public String toString() {
        return "[" + nombre + ", p=" + prioridad + ", " + estado + "]";
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Tarea)) return false;
        Tarea t = (Tarea) obj;
        return nombre.equals(t.nombre) && prioridad == t.prioridad;
    }
}
