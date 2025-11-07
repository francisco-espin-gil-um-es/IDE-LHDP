package ecg;

public class OndaP extends Onda {

    public OndaP(double inicio, double fin, double pico, int ciclo) {
        super(inicio, fin, pico, ciclo);
    }

    @Override
    public String toString() {
        return "OndaP [inicio=" + inicio + ", fin=" + fin + ", pico=" + pico + ", ciclo=" + ciclo + "]";
    }
}
