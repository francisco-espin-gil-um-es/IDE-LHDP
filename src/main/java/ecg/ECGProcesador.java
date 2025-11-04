package ecg;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.IOException;
import java.util.Scanner;

public class ECGProcesador {
    public static void main(String[] args) throws IOException {
        // Inicializar el servicio de Kie
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        // Crear la sesión de reglas
        KieSession kieSession = kieContainer.newKieSession("ksession-rules");

        // Leer la ruta del archivo desde la consola
        Scanner scanner = new Scanner(System.in);
        System.out.print("Por favor, ingrese la ruta del archivo ECG: ");
        String archivoRuta = scanner.nextLine();  // Lee la ruta completa del archivo

        // Leer el archivo de ECG
        ECGLector ecgLector = new ECGLector();
        ECGData ecgData = ecgLector.leerECGDesdeArchivo(archivoRuta);

        // Insertar el hecho (ECG) en la memoria de trabajo
        kieSession.insert(ecgData);

        // Ejecutar las reglas
        kieSession.fireAllRules();

        // Finalizar la sesión
        kieSession.dispose();

        scanner.close();  // Cerrar el scanner
    }
    
    public static class ECGProcessor {
        // Método para calcular la frecuencia cardíaca
        public static double calcularFrecuenciaCardiaca(double[] voltajes) {
            int picosR = detectarPicosR(voltajes);
            double intervalo = calcularIntervalo(picosR); // Intervalo en segundos
            return 60 / intervalo;
        }

        // Detecta los picos R en el ECG (este es un método simplificado)
        public static int detectarPicosR(double[] voltajes) {
            int picos = 0;
            for (int i = 1; i < voltajes.length - 1; i++) {
                if (voltajes[i] > voltajes[i - 1] && voltajes[i] > voltajes[i + 1]) {
                    picos++;
                }
            }
            return picos;
        }

        // Calcula el intervalo entre picos (simplificado)
        public static double calcularIntervalo(int picos) {
            // Este es un cálculo simple; en realidad se debe basar en el tiempo entre picos
            return picos * 0.6;  // Este es solo un ejemplo, necesitas ajustar según tus datos
        }
    }

}
