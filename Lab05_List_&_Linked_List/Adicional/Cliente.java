public class Cliente implements Comparable<Cliente> {
    private static int contadorGlobal = 1;

    private int numero;       // número de ticket
    private String nombre;
    private String tipoOperacion; // "Depósito", "Retiro", "Préstamo", "Consulta"
    private boolean esPreferencial;
    private long tiempoIngreso;

    public Cliente(String nombre, String tipoOperacion, boolean esPreferencial) {
        this.numero = contadorGlobal++;
        this.nombre = nombre;
        this.tipoOperacion = tipoOperacion;
        this.esPreferencial = esPreferencial;
        this.tiempoIngreso = System.currentTimeMillis();
    }

    public static void resetContador() {
        contadorGlobal = 1;
    }

    public int getNumero() { return numero; }
    public String getNombre() { return nombre; }
    public String getTipoOperacion() { return tipoOperacion; }
    public boolean isPreferencial() { return esPreferencial; }
    public long getTiempoIngreso() { return tiempoIngreso; }

    public String getEtiqueta() {
        return (esPreferencial ? "★ " : "") + "N°" + numero + " - " + nombre;
    }

    @Override
    public int compareTo(Cliente otro) {
        // Preferenciales primero, luego por número de ticket
        if (this.esPreferencial && !otro.esPreferencial) return -1;
        if (!this.esPreferencial && otro.esPreferencial) return 1;
        return Integer.compare(this.numero, otro.numero);
    }

    @Override
    public String toString() {
        return getEtiqueta();
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Cliente)) return false;
        return this.numero == ((Cliente) obj).numero;
    }
}
