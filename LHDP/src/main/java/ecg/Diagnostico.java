package ecg;

// Hecho derivado: Diagnóstico detectado por las reglas
public class Diagnostico {
    private String patologia;
    private String detalles;
    private double severidad; // 0.0 a 1.0 (calculado por reglas Drools)

    public Diagnostico(String patologia, String detalles) {
        this.patologia = patologia;
        this.detalles = detalles;
        this.severidad = 0.0; // Por defecto sin severidad
    }

    public Diagnostico(String patologia) {
        this(patologia, "");
    }

    // Constructor con severidad (para patologías con análisis difuso)
    public Diagnostico(String patologia, String detalles, double severidad) {
        this.patologia = patologia;
        this.detalles = detalles;
        this.severidad = severidad;
    }

    public String getPatologia() {
        return patologia;
    }

    public String getDetalles() {
        return detalles;
    }

    public double getSeveridad() {
        return severidad;
    }

    public void setSeveridad(double severidad) {
        this.severidad = severidad;
    }

    public String getNivelSeveridad() {
        if (severidad < 0.4) {
            return "Leve";
        } else if (severidad < 0.7) {
            return "Moderado";
        } else {
            return "Severo";
        }
    }

    @Override
    public String toString() {
        if (detalles == null || detalles.isEmpty()) {
            if (severidad > 0.0) {
                return patologia + " [Severidad: " + String.format("%.2f", severidad) + " - " + getNivelSeveridad()
                        + "]";
            }
            return patologia;
        }
        if (severidad > 0.0) {
            return patologia + " (" + detalles + ") [Severidad: " + String.format("%.2f", severidad) + " - "
                    + getNivelSeveridad() + "]";
        }
        return patologia + " (" + detalles + ")";
    }
}
