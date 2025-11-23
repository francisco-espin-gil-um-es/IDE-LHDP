package ecg;

/**
 * Hecho derivado: Intervalo RR promedio del ECG
 * Usado para detectar prematuridad en CVP (Contracciones Ventriculares
 * Prematuras)
 */
public class RRPromedio {
    private double valor;

    public RRPromedio(double valor) {
        this.valor = valor;
    }

    public double getValor() {
        return valor;
    }

    @Override
    public String toString() {
        return "RRPromedio [valor=" + valor + " ms]";
    }
}
