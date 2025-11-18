package ecg;

// Hecho derivado: Severidad difusa de una patología (0.0 = leve, 1.0 = crítica)
public class SeveridadFuzzy {
    private String patologia;
    private double severidad; // 0.0 a 1.0
    private String nivel; // "Leve", "Moderado", "Severo"

    public SeveridadFuzzy(String patologia, double severidad) {
        this.patologia = patologia;
        this.severidad = severidad;
        // Clasificación automática basada en umbrales difusos
        if (severidad < 0.4) {
            this.nivel = "Leve";
        } else if (severidad < 0.7) {
            this.nivel = "Moderado";
        } else {
            this.nivel = "Severo";
        }
    }

    public String getPatologia() {
        return patologia;
    }

    public double getSeveridad() {
        return severidad;
    }

    public String getNivel() {
        return nivel;
    }

    @Override
    public String toString() {
        return String.format("%s: %.0f%% (%s)", patologia, severidad * 100, nivel);
    }
}
