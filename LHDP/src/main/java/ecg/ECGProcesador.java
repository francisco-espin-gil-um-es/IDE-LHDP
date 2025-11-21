package ecg;

import org.kie.api.KieServices;
import org.kie.api.runtime.KieContainer;
import org.kie.api.runtime.KieSession;

import java.io.File;
import java.io.FileWriter;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.BufferedReader;
import java.io.FileReader;
import java.util.Collection;

public class ECGProcesador {
    public static void main(String[] args) throws IOException {
        // Directorios por defecto (ejecución sin parámetros)
        String baseDir = System.getProperty("user.dir");
        String dirEnt = baseDir + File.separator + "src" + File.separator + "main" + File.separator + "resources"
                + File.separator + "enunciado_e_inputs";
        String dirSal = baseDir + File.separator + "target" + File.separator + "salidas";

        // Reescritura por argumentos
        if (args.length >= 1 && args[0] != null && !args[0].isEmpty()) {
            dirEnt = args[0];
        }
        if (args.length >= 2 && args[1] != null && !args[1].isEmpty()) {
            dirSal = args[1];
        }

        File inDir = new File(dirEnt);
        File outDir = new File(dirSal);

        if (!inDir.exists() || !inDir.isDirectory()) {
            System.err.println("Directorio de entrada no válido: " + inDir.getAbsolutePath());
            return;
        }
        if (!outDir.exists()) {
            if (!outDir.mkdirs()) {
                System.err.println("No se pudo crear el directorio de salida: " + outDir.getAbsolutePath());
                return;
            }
        }

        // Inicializar el servicio de Kie (contenedor reutilizable)
        KieServices kieServices = KieServices.Factory.get();
        KieContainer kieContainer = kieServices.getKieClasspathContainer();

        // Procesar todos los ficheros .ecg del directorio de entrada
        File[] ecgFiles = inDir.listFiles((dir, name) -> name.toLowerCase().endsWith(".ecg"));
        if (ecgFiles == null || ecgFiles.length == 0) {
            System.out.println("No se encontraron ficheros .ecg en: " + inDir.getAbsolutePath());
            return;
        }

        System.out.println("Procesando " + ecgFiles.length + " fichero(s) desde: " + inDir.getAbsolutePath());
        System.out.println("Los resultados se guardarán en: " + outDir.getAbsolutePath());

        // StringBuilder para consolidar diagnósticos en todo.salida.txt
        StringBuilder todoSalida = new StringBuilder();

        for (File ecgFile : ecgFiles) {
            // Nueva sesión por fichero para aislar hechos
            KieSession kieSession = kieContainer.newKieSession("ksession-rules");
            try {
                // Leer el archivo de ECG con ruta absoluta
                ECGReader reader = new ECGReader();
                ECGData ecgData = reader.leerECGDesdeArchivo(ecgFile.getAbsolutePath());

                // Insertar el ECGData
                kieSession.insert(ecgData);

                // Insertar solo las ondas individuales (hechos básicos)
                for (Ciclo ciclo : ecgData.getCiclos()) {
                    if (ciclo.getOndaP() != null)
                        kieSession.insert(ciclo.getOndaP());
                    if (ciclo.getOndaQ() != null)
                        kieSession.insert(ciclo.getOndaQ());
                    if (ciclo.getOndaR() != null)
                        kieSession.insert(ciclo.getOndaR());
                    if (ciclo.getOndaS() != null)
                        kieSession.insert(ciclo.getOndaS());
                    if (ciclo.getOndaT() != null)
                        kieSession.insert(ciclo.getOndaT());
                }

                // Preparar fichero de salida individual (.salida.txt)
                String baseName = ecgFile.getName().replaceFirst("\\.ecg$", "");
                File outFile = new File(outDir, baseName + ".salida.txt");
                try (PrintWriter clear = new PrintWriter(new FileWriter(outFile, false))) {
                    // dejamos vacío para que empiece de cero
                }

                // Duplicar la salida de consola al fichero durante la ejecución de reglas
                PrintStream originalOut = System.out;
                try (FileOutputStream fos = new FileOutputStream(outFile, true)) {
                    PrintStream tee = new PrintStream(new MultiOutputStream(originalOut, fos), true);
                    System.setOut(tee);

                    // Ejecutar las reglas usando AGENDAS en orden
                    System.out.println("\n========== INICIANDO PROCESAMIENTO ECG ==========\n");

                    // FASE 1: Procesamiento - Calcular intervalos y frecuencia
                    System.out.println("[AGENDA: Procesamiento] Activando...");
                    kieSession.getAgenda().getAgendaGroup("procesamiento").setFocus();
                    kieSession.fireAllRules();
                    System.out.println("[AGENDA: Procesamiento] Completada\n");

                    // FASE 2: Diagnóstico - Detectar patologías
                    System.out.println("[AGENDA: Diagnóstico] Activando...");
                    kieSession.getAgenda().getAgendaGroup("diagnostico").setFocus();
                    kieSession.fireAllRules();
                    System.out.println("[AGENDA: Diagnóstico] Completada\n");

                    System.out.println("========== PROCESAMIENTO COMPLETADO ==========\n");

                    tee.flush();
                } finally {
                    System.setOut(originalOut);
                }

                // Recuperar diagnósticos insertados por las reglas (NO calculamos en Java)
                Collection<?> objects = kieSession.getObjects();
                StringBuilder diagnosticos = new StringBuilder();
                for (Object obj : objects) {
                    if (obj instanceof Diagnostico) {
                        if (diagnosticos.length() > 0) {
                            diagnosticos.append(", ");
                        }
                        diagnosticos.append(((Diagnostico) obj).getPatologia());
                    }
                }
                String diagnosticoTexto = diagnosticos.length() > 0 ? diagnosticos.toString() : "Sin diagnóstico";

                // Añadir resumen al final del fichero individual
                try (PrintWriter pw = new PrintWriter(new FileWriter(outFile, true))) {
                    pw.println();
                    pw.println("==========================================================");
                    pw.println("--- RESUMEN CLÍNICO ---");
                    pw.println("==========================================================");
                    pw.println("Fichero: " + ecgFile.getName());
                    pw.println("Ciclos detectados: " + ecgData.getNumeroCiclos());
                    pw.printf("Frecuencia cardiaca: %.2f pul/min%n", ecgData.getFrecuenciaCardiaca());
                    pw.println("Diagnóstico: " + diagnosticoTexto);
                    pw.println("==========================================================");
                }

                // Añadir al consolidado todo.salida.txt
                todoSalida.append("Fichero: ").append(ecgFile.getName()).append("\n");
                todoSalida.append("Diagnóstico: ").append(diagnosticoTexto).append("\n");
                todoSalida.append("\n");

                System.out.println("Procesado: " + ecgFile.getName() +
                        " -> salida: " + baseName + ".salida.txt");
            } finally {
                // Finalizar la sesión de este fichero
                kieSession.dispose();
            }
        }

        // Escribir el archivo consolidado todo.salida.txt
        File todoFile = new File(outDir, "todo.salida.txt");
        try (PrintWriter pw = new PrintWriter(new FileWriter(todoFile))) {
            pw.println("=== RESUMEN DE TODOS LOS FICHEROS PROCESADOS ===");
            pw.println();
            pw.print(todoSalida.toString());
        }

        // Mostrar todo.salida.txt por consola
        System.out.println("\n========================================");
        System.out.println("CONTENIDO DE todo.salida.txt:");
        System.out.println("========================================");
        try (BufferedReader br = new BufferedReader(new FileReader(todoFile))) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        }
        System.out.println("========================================");
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
            return picos * 0.6; // Este es solo un ejemplo, necesitas ajustar según tus datos
        }
    }

    // OutputStream que escribe en dos destinos (consola + fichero)
    static class MultiOutputStream extends OutputStream {
        private final OutputStream out1;
        private final OutputStream out2;

        MultiOutputStream(OutputStream out1, OutputStream out2) {
            this.out1 = out1;
            this.out2 = out2;
        }

        @Override
        public void write(int b) throws IOException {
            out1.write(b);
            out2.write(b);
        }

        @Override
        public void write(byte[] b) throws IOException {
            out1.write(b);
            out2.write(b);
        }

        @Override
        public void write(byte[] b, int off, int len) throws IOException {
            out1.write(b, off, len);
            out2.write(b, off, len);
        }

        @Override
        public void flush() throws IOException {
            out1.flush();
            out2.flush();
        }

        @Override
        public void close() throws IOException {
            // No cerramos out1 (System.out); solo el fichero
            out2.close();
        }
    }
}
