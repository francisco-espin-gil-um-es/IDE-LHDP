package ecg;

import java.io.IOException;

public class ECGLector {
    public ECGData leerECGDesdeArchivo(String archivo) throws IOException {
        String ruta = System.getProperty("user.dir") + "/src/main/resources/enunciado_e_inputs/" + archivo;
        ECGReader reader = new ECGReader();
        ECGData ecg = reader.leerECGDesdeArchivo(ruta);
        /*
         * // Resumen útil
         * System.out.println("\n=== Resumen ECG ===");
         * System.out.println("Ciclos detectados: " + ecg.getNumeroCiclos());
         * System.out.printf("Frecuencia estimada: %.1f pul/min\n",
         * ecg.getFrecuenciaCardiaca());
         * System.out.println("===================\n");
         */
        return ecg;
    }
}
