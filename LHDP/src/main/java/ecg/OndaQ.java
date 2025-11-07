package ecg;

public class OndaQ extends Onda {

    public OndaQ(double inicio, double fin, double pico, int ciclo) {
        super(inicio, fin, pico, ciclo);
    }

    @Override
    public String toString() {
        return "OndaQ [inicio=" + inicio + ", fin=" + fin + ", pico=" + pico + ", ciclo=" + ciclo + "]";
    }
}
