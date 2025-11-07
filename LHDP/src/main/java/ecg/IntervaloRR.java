package ecg;

// Hecho derivado: Intervalo RR (distancia entre dos ondas R consecutivas)
public class IntervaloRR {
    private double inicio; // tiempo de la primera R
    private double fin; // tiempo de la segunda R
    private double duracion;
    private int cicloAnterior;
    private int cicloSiguiente;

    public IntervaloRR(double inicio, double fin, int cicloAnterior, int cicloSiguiente) {
        this.inicio = inicio;
        this.fin = fin;
        this.duracion = fin - inicio;
        this.cicloAnterior = cicloAnterior;
        this.cicloSiguiente = cicloSiguiente;
    }

    public double getInicio() {
        return inicio;
    }

    public double getFin() {
        return fin;
    }

    public double getDuracion() {
        return duracion;
    }

    public int getCicloAnterior() {
        return cicloAnterior;
    }

    public int getCicloSiguiente() {
        return cicloSiguiente;
    }

    @Override
    public String toString() {
        return "IntervaloRR [ciclos=" + cicloAnterior + "->" + cicloSiguiente + ", duracion=" + duracion + "]";
    }
}
