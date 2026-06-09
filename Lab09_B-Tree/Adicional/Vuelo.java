public class Vuelo implements Comparable<Vuelo> {

    public enum Estado { EN_HORARIO, RETRASADO, EMBARCANDO, CANCELADO, ATERRIZADO }

    private String codigo;      // clave del B-Tree, ej: "LA2045"
    private String origen;
    private String destino;
    private String hora;        // "HH:MM"
    private String aerolinea;
    private String puerta;
    private Estado estado;

    public Vuelo(String codigo, String aerolinea, String origen,
                 String destino, String hora, String puerta) {
        this.codigo    = codigo.toUpperCase();
        this.aerolinea = aerolinea;
        this.origen    = origen;
        this.destino   = destino;
        this.hora      = hora;
        this.puerta    = puerta;
        this.estado    = Estado.EN_HORARIO;
    }

    public String getCodigo()    { return codigo; }
    public String getOrigen()    { return origen; }
    public String getDestino()   { return destino; }
    public String getHora()      { return hora; }
    public String getAerolinea() { return aerolinea; }
    public String getPuerta()    { return puerta; }
    public Estado getEstado()    { return estado; }
    public void   setEstado(Estado e) { this.estado = e; }

    public String estadoTexto() {
        switch (estado) {
            case EN_HORARIO:  return "En horario";
            case RETRASADO:   return "Retrasado";
            case EMBARCANDO:  return "Embarcando";
            case CANCELADO:   return "Cancelado";
            case ATERRIZADO:  return "Aterrizado";
            default:          return "—";
        }
    }

    @Override public int compareTo(Vuelo o) { return this.codigo.compareTo(o.codigo); }
    @Override public boolean equals(Object o) {
        return o instanceof Vuelo && this.codigo.equals(((Vuelo) o).codigo);
    }
    @Override public String toString() { return codigo; }
}
