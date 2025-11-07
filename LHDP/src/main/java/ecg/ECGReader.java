package ecg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECGReader {

    // Lee y construye ECGData con Ciclos y Ondas a partir de un archivo .ecg
    public ECGData leerECGDesdeArchivo(String archivo) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(archivo))) {
            String linea;
            ECGData ecgData = new ECGData(System.currentTimeMillis());

            Pattern pattern = Pattern.compile("^([PQRST])\\(([^,]+),([^,]+),([^)]+)\\)");
            int numeroCiclo = 0;
            Ciclo cicloActual = null;

            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty() || linea.startsWith("#")) {
                    continue;
                }

                Matcher matcher = pattern.matcher(linea);
                if (!matcher.find()) {
                    System.out.println("Advertencia: Línea no reconocida: " + linea);
                    continue;
                }

                char tipo = matcher.group(1).charAt(0);
                double inicio = Double.parseDouble(matcher.group(2).trim());
                double fin = Double.parseDouble(matcher.group(3).trim());
                double pico = Double.parseDouble(matcher.group(4).trim());

                switch (tipo) {
                    case 'P':
                        numeroCiclo++;
                        cicloActual = new Ciclo(numeroCiclo);
                        cicloActual.setOndaP(new OndaP(inicio, fin, pico, numeroCiclo));
                        ecgData.addCiclo(cicloActual);
                        break;
                    case 'Q':
                        if (cicloActual != null)
                            cicloActual.setOndaQ(new OndaQ(inicio, fin, pico, numeroCiclo));
                        break;
                    case 'R':
                        if (cicloActual != null)
                            cicloActual.setOndaR(new OndaR(inicio, fin, pico, numeroCiclo));
                        break;
                    case 'S':
                        if (cicloActual != null)
                            cicloActual.setOndaS(new OndaS(inicio, fin, pico, numeroCiclo));
                        break;
                    case 'T':
                        if (cicloActual != null)
                            cicloActual.setOndaT(new OndaT(inicio, fin, pico, numeroCiclo));
                        break;
                    default:
                        // no-op
                }
            }

            // NO calculamos la frecuencia cardíaca aquí (eso es inferencia, se hace en
            // Drools)
            return ecgData;
        }
    }
}
