package ecg;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class ECGLector {
    public ECGData leerECGDesdeArchivo(String archivo) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(archivo));
        String linea;
        StringBuilder voltajesString = new StringBuilder();
        long timestamp = System.currentTimeMillis();  // Usamos el timestamp actual como ejemplo

        while ((linea = reader.readLine()) != null) {
            // Ignorar líneas que no sean números (por ejemplo, comentarios o texto)
            if (linea.trim().startsWith("#") || linea.trim().isEmpty()) {
                continue;  // Si la línea es un comentario o está vacía, la ignoramos
            }
            voltajesString.append(linea).append("\n");
        }
        reader.close();

        // Convertir la cadena de voltajes a un array de double
        String[] voltajesArray = voltajesString.toString().split("\n");
        double[] voltajes = new double[voltajesArray.length];

        for (int i = 0; i < voltajesArray.length; i++) {
            try {
                voltajes[i] = Double.parseDouble(voltajesArray[i].trim());  // Intentar convertir solo a números válidos
            } catch (NumberFormatException e) {
                // Si ocurre un error de formato, podemos ignorar o manejar el caso
                System.out.println("Advertencia: Línea no válida para convertir a número: " + voltajesArray[i]);
            }
        }

        return new ECGData(voltajes, timestamp);
    }
}
