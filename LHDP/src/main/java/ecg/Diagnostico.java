package ecg;

// Hecho derivado: Diagnóstico detectado por las reglas
public class Diagnostico {
    private String patologia;
    private String detalles;

    public Diagnostico(String patologia, String detalles) {
        this.patologia = patologia;
        this.detalles = detalles;
    }

    public Diagnostico(String patologia) {
        this(patologia, "");
    }

    public String getPatologia() {
        return patologia;
    }

    public String getDetalles() {
        return detalles;
    }

    @Override
    public String toString() {
        if (detalles == null || detalles.isEmpty()) {
            return patologia;
        }
        return patologia + " (" + detalles + ")";
    }
}
