package ecg;

public class ECGData {
	private double[] voltajes;  // Array de voltajes del ECG
    private long timestamp;     // Marca de tiempo del ECG

    public ECGData(double[] voltajes, long timestamp) {
        this.voltajes = voltajes;
        this.timestamp = timestamp;
    }

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
}
