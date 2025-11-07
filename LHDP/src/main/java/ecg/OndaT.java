package ecg;

public class OndaT extends Onda {

    public OndaT(double inicio, double fin, double pico, int ciclo) {
        super(inicio, fin, pico, ciclo);
    }

    @Override
    public String toString() {
        return "OndaT [inicio=" + inicio + ", fin=" + fin + ", pico=" + pico + ", ciclo=" + ciclo + "]";
    }
}
