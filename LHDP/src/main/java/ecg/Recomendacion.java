package ecg;

// Hecho derivado: Recomendación médica basada en análisis difuso
public class Recomendacion {
    private String tipo; // "URGENTE", "ALTA_PRIORIDAD", "SEGUIMIENTO", "PREVENTIVO"
    private String titulo;
    private String descripcion;
    private int prioridad; // 1-4 (1=más urgente)

    // Constructor original (compatibilidad con código previo)
    public Recomendacion(String tipo, String mensaje, int prioridad) {
        this.tipo = tipo;
        this.titulo = mensaje;
        this.descripcion = "";
        this.prioridad = prioridad;
    }

    // Constructor nuevo (usado por las reglas Drools)
    public Recomendacion(String tipo, String titulo, String descripcion) {
        this.tipo = tipo;
        this.titulo = titulo;
        this.descripcion = descripcion;
        // Asignar prioridad automática según tipo
        switch (tipo) {
            case "URGENTE":
                this.prioridad = 1;
                break;
            case "ALTA_PRIORIDAD":
                this.prioridad = 2;
                break;
            case "SEGUIMIENTO":
                this.prioridad = 3;
                break;
            case "PREVENTIVO":
                this.prioridad = 4;
                break;
            default:
                this.prioridad = 5;
        }
    }

    public String getTipo() {
        return tipo;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    // Mantener getMensaje() para compatibilidad
    public String getMensaje() {
        return descripcion.isEmpty() ? titulo : descripcion;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        if (descripcion.isEmpty()) {
            return String.format("[%s] %s", tipo, titulo);
        } else {
            return String.format("[%s] %s\n%s", tipo, titulo, descripcion);
        }
    }
}
