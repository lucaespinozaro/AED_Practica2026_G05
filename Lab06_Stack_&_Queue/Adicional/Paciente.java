import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Paciente {
    private static int contadorId = 1;

    private final int id;
    private final String nombre;
    private final int edad;
    private final String sintoma;
    private final Prioridad prioridad;
    private final LocalDateTime horaIngreso;
    private String diagnostico;
    private String tratamiento;
    private LocalDateTime horaAtencion;

    public Paciente(String nombre, int edad, String sintoma, Prioridad prioridad) {
        this.id = contadorId++;
        this.nombre = nombre;
        this.edad = edad;
        this.sintoma = sintoma;
        this.prioridad = prioridad;
        this.horaIngreso = LocalDateTime.now();
    }

    public void atender(String diagnostico, String tratamiento) {
        this.diagnostico = diagnostico;
        this.tratamiento = tratamiento;
        this.horaAtencion = LocalDateTime.now();
    }

    public int getId() { return id; }
    public String getNombre() { return nombre; }
    public int getEdad() { return edad; }
    public String getSintoma() { return sintoma; }
    public Prioridad getPrioridad() { return prioridad; }
    public LocalDateTime getHoraIngreso() { return horaIngreso; }
    public String getDiagnostico() { return diagnostico; }
    public String getTratamiento() { return tratamiento; }
    public LocalDateTime getHoraAtencion() { return horaAtencion; }

    public String getHoraIngresoStr() {
        return horaIngreso.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    public String getHoraAtencionStr() {
        if (horaAtencion == null) return "—";
        return horaAtencion.format(DateTimeFormatter.ofPattern("HH:mm:ss"));
    }

    @Override
    public String toString() {
        return String.format("[%03d] %s | %s | %s", id, nombre, prioridad.getDescripcion(), sintoma);
    }
}

