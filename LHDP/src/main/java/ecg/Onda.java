package ecg;

public abstract class Onda {
    protected double inicio; // Tiempo de inicio de la onda en segundos
    protected double fin; // Tiempo de fin de la onda en segundos
    protected double pico; // Tiempo del pico de la onda en segundos
    protected int ciclo; // Ciclo cardíaco al que pertenece la onda

    public Onda(double inicio, double fin, double pico, int ciclo) {
        this.inicio = inicio;
        this.fin = fin;
        this.pico = pico;
        this.ciclo = ciclo;
    }

    public double getInicio() {
        return inicio;
    }

    public double getFin() {
        return fin;
    }

    public double getPico() {
        return pico;
    }

    public int getCiclo() {
        return ciclo;
    }
}
