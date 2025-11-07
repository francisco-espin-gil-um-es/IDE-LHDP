package ecg;

// Hecho derivado: Complejo QRS (desde inicio de Q hasta fin de S del mismo ciclo)
public class ComplejoQRS {
    private double inicio;
    private double fin;
    private double duracion;
    private int ciclo;

    public ComplejoQRS(double inicio, double fin, int ciclo) {
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
        return "ComplejoQRS [ciclo=" + ciclo + ", duracion=" + duracion + "]";
    }
}
