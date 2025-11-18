package ecg;

// Hecho derivado: Recomendación médica basada en análisis difuso
public class Recomendacion {
    private String tipo; // "URGENTE", "PRIORITARIA", "SEGUIMIENTO", "PREVENTIVA"
    private String mensaje;
    private int prioridad; // 1-4 (1=más urgente)

    public Recomendacion(String tipo, String mensaje, int prioridad) {
        this.tipo = tipo;
        this.mensaje = mensaje;
        this.prioridad = prioridad;
    }

    public String getTipo() {
        return tipo;
    }

    public String getMensaje() {
        return mensaje;
    }

    public int getPrioridad() {
        return prioridad;
    }

    @Override
    public String toString() {
        return String.format("[%s] %s", tipo, mensaje);
    }
}
