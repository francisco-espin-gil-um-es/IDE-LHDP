package ecg;

public class OndaR extends Onda {

    public OndaR(double inicio, double fin, double pico, int ciclo) {
        super(inicio, fin, pico, ciclo);
    }

    @Override
    public String toString() {
        return "OndaR [inicio=" + inicio + ", fin=" + fin + ", pico=" + pico + ", ciclo=" + ciclo + "]";
    }
}
