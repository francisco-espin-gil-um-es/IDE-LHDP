package ecg;

public class OndaS extends Onda {

    public OndaS(double inicio, double fin, double pico, int ciclo) {
        super(inicio, fin, pico, ciclo);
    }

    @Override
    public String toString() {
        return "OndaS [inicio=" + inicio + ", fin=" + fin + ", pico=" + pico + ", ciclo=" + ciclo + "]";
    }
}
