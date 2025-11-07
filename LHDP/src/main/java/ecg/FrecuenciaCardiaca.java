package ecg;

// Hecho derivado: Frecuencia cardíaca calculada
public class FrecuenciaCardiaca {
    private double valor; // pul/min
    private String metodoCalculo; // descripción de cómo se calculó

    public FrecuenciaCardiaca(double valor, String metodoCalculo) {
        this.valor = valor;
        this.metodoCalculo = metodoCalculo;
    }

    public double getValor() {
        return valor;
    }

    public String getMetodoCalculo() {
        return metodoCalculo;
    }

    @Override
    public String toString() {
        return "FrecuenciaCardiaca [valor=" + valor + " pul/min, metodo=" + metodoCalculo + "]";
    }
}
