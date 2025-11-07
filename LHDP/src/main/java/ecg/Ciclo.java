package ecg;

public class Ciclo {
    private final int numeroCiclo;
    private OndaP ondaP;
    private OndaQ ondaQ;
    private OndaR ondaR;
    private OndaS ondaS;
    private OndaT ondaT;

    public Ciclo(int numeroCiclo) {
        this.numeroCiclo = numeroCiclo;
    }

    public int getNumeroCiclo() {
        return numeroCiclo;
    }

    public OndaP getOndaP() {
        return ondaP;
    }

    public void setOndaP(OndaP ondaP) {
        this.ondaP = ondaP;
    }

    public OndaQ getOndaQ() {
        return ondaQ;
    }

    public void setOndaQ(OndaQ ondaQ) {
        this.ondaQ = ondaQ;
    }

    public OndaR getOndaR() {
        return ondaR;
    }

    public void setOndaR(OndaR ondaR) {
        this.ondaR = ondaR;
    }

    public OndaS getOndaS() {
        return ondaS;
    }

    public void setOndaS(OndaS ondaS) {
        this.ondaS = ondaS;
    }

    public OndaT getOndaT() {
        return ondaT;
    }

    public void setOndaT(OndaT ondaT) {
        this.ondaT = ondaT;
    }

    public boolean estaCompleto() {
        return ondaP != null && ondaQ != null && ondaR != null && ondaS != null && ondaT != null;
    }

    public double getDuracionTotal() {
        if (ondaP == null || ondaT == null)
            return 0.0;
        return ondaT.getFin() - ondaP.getInicio();
    }

    public double getIntervaloQT() {
        if (ondaQ == null || ondaT == null)
            return 0.0;
        return ondaT.getFin() - ondaQ.getInicio();
    }

    // Complejo QRS (duración desde inicio de Q hasta fin de S)
    public double getDuracionQRS() {
        if (ondaQ == null || ondaS == null)
            return 0.0;
        return ondaS.getFin() - ondaQ.getInicio();
    }

    // Intervalo PR (desde inicio de P hasta inicio de Q)
    public double getIntervaloPR() {
        if (ondaP == null || ondaQ == null)
            return 0.0;
        return ondaQ.getInicio() - ondaP.getInicio();
    }

    public double getIntervaloRR(Ciclo siguiente) {
        if (this.ondaR == null || siguiente == null || siguiente.getOndaR() == null)
            return 0.0;
        return siguiente.getOndaR().getInicio() - this.ondaR.getInicio();
    }

    @Override
    public String toString() {
        return "Ciclo " + numeroCiclo + " [P=" + (ondaP != null) + ", Q=" + (ondaQ != null) + ", R=" + (ondaR != null)
                + ", S=" + (ondaS != null) + ", T=" + (ondaT != null) + "]";
    }
}
