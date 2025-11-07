package ecg;

import java.util.ArrayList;
import java.util.List;

public class ECGData {
    // Compatibilidad: aún mantenemos 'voltajes' por si alguna parte del código lo
    // usa.
    private double[] voltajes; // Array de voltajes del ECG (opcional)
    private long timestamp; // Marca de tiempo del ECG

    // Nueva estructura: lista de ciclos (latidos) con ondas PQRST
    private List<Ciclo> ciclos = new ArrayList<>();
    private double frecuenciaCardiaca; // pul/min calculada

    public ECGData() {
        this.timestamp = System.currentTimeMillis();
    }

    public ECGData(long timestamp) {
        this.timestamp = timestamp;
    }

    // Constructor de compatibilidad
    public ECGData(double[] voltajes, long timestamp) {
        this.voltajes = voltajes;
        this.timestamp = timestamp;
    }

    // Getters/Setters compatibilidad
    public double[] getVoltajes() {
        return voltajes;
    }

    public void setVoltajes(double[] voltajes) {
        this.voltajes = voltajes;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    // Nueva API
    public List<Ciclo> getCiclos() {
        return ciclos;
    }

    public void setCiclos(List<Ciclo> ciclos) {
        this.ciclos = ciclos;
    }

    public void addCiclo(Ciclo ciclo) {
        this.ciclos.add(ciclo);
    }

    public int getNumeroCiclos() {
        return ciclos.size();
    }

    public double getFrecuenciaCardiaca() {
        return frecuenciaCardiaca;
    }

    public void setFrecuenciaCardiaca(double frecuenciaCardiaca) {
        this.frecuenciaCardiaca = frecuenciaCardiaca;
    }
}
