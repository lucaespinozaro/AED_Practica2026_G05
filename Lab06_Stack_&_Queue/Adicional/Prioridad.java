public enum Prioridad {
    VERDE(0, "Verde - Leve"),
    AMARILLO(1, "Amarillo - Moderado"),
    ROJO(2, "Rojo - Crítico");

    private final int nivel;
    private final String descripcion;

    Prioridad(int nivel, String descripcion) {
        this.nivel = nivel;
        this.descripcion = descripcion;
    }

    public int getNivel() { return nivel; }
    public String getDescripcion() { return descripcion; }
}
