public class Empleado implements Comparable<Empleado> {

    public enum Area { RECURSOS_HUMANOS, SISTEMAS, VENTAS, PRODUCCION, SEGURIDAD, GERENCIA }

    private int dni;            // clave de la tabla hash
    private String nombre;
    private String apellido;
    private Area area;
    private String turno;       // "Mañana" / "Tarde" / "Noche"
    private boolean activo;      // tarjeta activa / desactivada

    public Empleado(int dni, String nombre, String apellido, Area area, String turno) {
        this.dni = dni;
        this.nombre = nombre;
        this.apellido = apellido;
        this.area = area;
        this.turno = turno;
        this.activo = true;
    }

    public int getDni() { return dni; }
    public String getNombre() { return nombre; }
    public String getApellido() { return apellido; }
    public Area getArea() { return area; }
    public String getTurno() { return turno; }
    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String nombreCompleto() { return nombre + " " + apellido; }

    public String areaTexto() {
        switch (area) {
            case RECURSOS_HUMANOS: return "Recursos Humanos";
            case SISTEMAS:         return "Sistemas";
            case VENTAS:           return "Ventas";
            case PRODUCCION:       return "Producción";
            case SEGURIDAD:        return "Seguridad";
            case GERENCIA:         return "Gerencia";
            default: return "—";
        }
    }

    @Override
    public int compareTo(Empleado o) { return Integer.compare(this.dni, o.dni); }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Empleado)) return false;
        return this.dni == ((Empleado) o).dni;
    }

    @Override
    public String toString() { return "DNI " + dni + " — " + nombreCompleto(); }
}
