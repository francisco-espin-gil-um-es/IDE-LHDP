package ecg;

// Hecho derivado: Intervalo PR (desde inicio de P hasta inicio de Q del mismo ciclo)
public class IntervaloPR {
    private double inicio;
    private double fin;
    private double duracion;
    private int ciclo;

    public IntervaloPR(double inicio, double fin, int ciclo) {
        this.inicio = inicio;
        this.fin = fin;
        this.duracion = fin - inicio;
        this.ciclo = ciclo;
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

    public int getCiclo() {
        return ciclo;
    }

    @Override
    public String toString() {
        return "IntervaloPR [ciclo=" + ciclo + ", duracion=" + duracion + "]";
    }
}
