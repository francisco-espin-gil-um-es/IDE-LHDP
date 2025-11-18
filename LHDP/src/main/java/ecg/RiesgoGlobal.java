package ecg;

// Hecho derivado: Riesgo global del paciente calculado mediante lógica difusa
public class RiesgoGlobal {
    private double valorRiesgo; // 0.0 a 1.0
    private String nivelRiesgo; // "BAJO", "MEDIO", "ALTO", "CRÍTICO"
    private String justificacion;

    public RiesgoGlobal(double valorRiesgo, String nivelRiesgo, String justificacion) {
        this.valorRiesgo = valorRiesgo;
        this.nivelRiesgo = nivelRiesgo;
        this.justificacion = justificacion;
    }

    public double getValorRiesgo() {
        return valorRiesgo;
    }

    public String getNivelRiesgo() {
        return nivelRiesgo;
    }

    public String getJustificacion() {
        return justificacion;
    }

    @Override
    public String toString() {
        return String.format("Riesgo Global: %.0f%% - %s\n%s",
                valorRiesgo * 100, nivelRiesgo, justificacion);
    }
}
