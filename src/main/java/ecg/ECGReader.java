package ecg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ECGReader {

    public ECGData leerECGDesdeArchivo(String archivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(archivo));
        String linea;
        StringBuilder voltajesString = new StringBuilder();
        long timestamp = System.currentTimeMillis();  // Usamos el timestamp actual como ejemplo

        // Usaremos una expresión regular para extraer los valores dentro de los paréntesis
        Pattern pattern = Pattern.compile("[A-Za-z]+\\(([^,]+),([^,]+),([^)]+)\\)");

        while ((linea = reader.readLine()) != null) {
            // Ignorar líneas que son comentarios o vacías
            if (linea.trim().startsWith("#") || linea.trim().isEmpty()) {
                continue;  // Si la línea es un comentario o está vacía, la ignoramos
            }

            // Buscar los valores numéricos dentro de los paréntesis en cada línea
            Matcher matcher = pattern.matcher(linea);
            if (matcher.find()) {
                // Extraemos los valores dentro de los paréntesis
                try {
                    double valor = Double.parseDouble(matcher.group(3));  // El valor está en el tercer grupo
                    voltajesString.append(valor).append("\n");  // Guardamos el valor en voltajesString
                } catch (NumberFormatException e) {
                    System.out.println("Advertencia: Línea no válida para convertir a número: " + linea);
                }
            }
        }
        reader.close();

        // Convertir la cadena de voltajes a un array de double
        String[] voltajesArray = voltajesString.toString().split("\n");
        double[] voltajes = new double[voltajesArray.length];

        for (int i = 0; i < voltajesArray.length; i++) {
            try {
                voltajes[i] = Double.parseDouble(voltajesArray[i].trim());  // Convertir a double
            } catch (NumberFormatException e) {
                System.out.println("Advertencia: No se pudo convertir la línea a un número válido: " + voltajesArray[i]);
            }
        }

        return new ECGData(voltajes, timestamp);
    }
}
