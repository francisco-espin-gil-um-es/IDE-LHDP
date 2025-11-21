package ecg;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import java.io.InputStream;
import java.util.List;

/**
 * Clase auxiliar para integrar JFuzzyLogic dentro del motor de reglas Drools.
 * Esta clase es invocada DESDE LAS REGLAS (no desde Java) para evaluar riesgo
 * difuso.
 * 
 * Patrón: Helper/Utility invocado por Drools mediante función estática
 */
public class FuzzyHelper {

    private static FIS fis;
    private static FunctionBlock fb;
    private static boolean sistemaInicializado = false;

    /**
     * Inicializa el sistema fuzzy (solo se ejecuta una vez).
     * Debe ser invocado desde una regla de inicialización.
     */
    public static void inicializarSistemaFuzzy() {
        if (sistemaInicializado) {
            return;
        }

        try {
            InputStream fclStream = FuzzyHelper.class.getClassLoader()
                    .getResourceAsStream("ecg/riesgo_cardiologico.fcl");

            if (fclStream == null) {
                throw new RuntimeException("No se encontró el archivo riesgo_cardiologico.fcl en resources/ecg/");
            }

            fis = FIS.load(fclStream, true);

            if (fis == null) {
                throw new RuntimeException("Error al cargar el sistema fuzzy desde el archivo FCL");
            }

            fb = fis.getFunctionBlock("evaluacion_riesgo");

            if (fb == null) {
                throw new RuntimeException("No se encontró el FUNCTION_BLOCK 'evaluacion_riesgo' en el archivo FCL");
            }

            sistemaInicializado = true;
            System.out.println("✓ Sistema fuzzy inicializado correctamente desde Drools");

        } catch (Exception e) {
            System.err.println("✗ Error al inicializar sistema fuzzy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Evalúa el riesgo global usando el sistema fuzzy JFuzzyLogic.
     * Este método es invocado DESDE UNA REGLA DROOLS.
     * 
     * @param diagnosticos  Lista de diagnósticos con severidades calculadas
     * @param numPatologias Número de patologías detectadas (comorbilidad)
     * @return Valor de riesgo difuso (0.0-1.0), o 0.0 si hay error
     */
    public static double evaluarRiesgoFuzzy(List<Diagnostico> diagnosticos, int numPatologias) {
        if (!sistemaInicializado || fb == null) {
            System.err.println("✗ Sistema fuzzy no inicializado. Ejecute inicializarSistemaFuzzy() primero.");
            return 0.0;
        }

        try {
            // Inicializar todas las variables de entrada a 0.0
            fb.setVariable("severidad_taquicardia", 0.0);
            fb.setVariable("severidad_bradicardia", 0.0);
            fb.setVariable("severidad_iam", 0.0);
            fb.setVariable("severidad_isquemia", 0.0);
            fb.setVariable("severidad_hipocalcemia", 0.0);
            fb.setVariable("severidad_hipopotasemia", 0.0);
            fb.setVariable("num_patologias", (double) numPatologias);

            // Mapear severidades detectadas a variables FCL
            for (Diagnostico diagnostico : diagnosticos) {
                String patologia = diagnostico.getPatologia();
                double severidad = diagnostico.getSeveridad();

                // Solo procesar diagnósticos que no sean "Normal" y que tengan severidad > 0
                if ("Normal".equals(patologia) || severidad <= 0.0) {
                    continue;
                }

                switch (patologia) {
                    case "Taquicardia":
                        fb.setVariable("severidad_taquicardia", severidad);
                        break;
                    case "Bradicardia":
                        fb.setVariable("severidad_bradicardia", severidad);
                        break;
                    case "Infarto Agudo de Miocardio":
                        fb.setVariable("severidad_iam", severidad);
                        break;
                    case "Isquemia Coronaria":
                        fb.setVariable("severidad_isquemia", severidad);
                        break;
                    case "Hipocalcemia":
                        fb.setVariable("severidad_hipocalcemia", severidad);
                        break;
                    case "Hipopotasemia":
                        fb.setVariable("severidad_hipopotasemia", severidad);
                        break;
                    default:
                        System.out.println("⚠ Patología no reconocida por el sistema fuzzy: " + patologia);
                }
            }

            // Evaluar el sistema fuzzy
            fb.evaluate();

            // Obtener el resultado
            double riesgoGlobal = fb.getVariable("riesgo_global").getValue();

            return riesgoGlobal;

        } catch (Exception e) {
            System.err.println("✗ Error al evaluar sistema fuzzy: " + e.getMessage());
            e.printStackTrace();
            return 0.0;
        }
    }

    /**
     * Clasifica el nivel de riesgo según el valor numérico.
     * 
     * @param valorRiesgo Valor de riesgo (0.0-1.0)
     * @return Nivel de riesgo: "BAJO", "MEDIO", "ALTO", "CRÍTICO"
     */
    public static String clasificarNivelRiesgo(double valorRiesgo) {
        if (valorRiesgo < 0.35) {
            return "BAJO";
        } else if (valorRiesgo < 0.65) {
            return "MEDIO";
        } else if (valorRiesgo < 0.85) {
            return "ALTO";
        } else {
            return "CRÍTICO";
        }
    }

    /**
     * Genera justificación textual del riesgo.
     * 
     * @param numPatologias Número de patologías
     * @param diagnosticos  Lista de diagnósticos con severidades
     * @return Texto descriptivo
     */
    public static String generarJustificacion(int numPatologias, List<Diagnostico> diagnosticos) {
        if (numPatologias == 0) {
            return "ECG normal, sin factores de riesgo detectados.";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Detectadas ").append(numPatologias).append(" patología(s). ");

        for (Diagnostico diagnostico : diagnosticos) {
            if ("Normal".equals(diagnostico.getPatologia()) || diagnostico.getSeveridad() <= 0.0) {
                continue;
            }

            String nivel = diagnostico.getNivelSeveridad();
            sb.append(diagnostico.getPatologia()).append(" (").append(nivel.toLowerCase()).append("), ");
        }

        // Eliminar última coma
        if (sb.length() > 0 && sb.charAt(sb.length() - 2) == ',') {
            sb.setLength(sb.length() - 2);
        }

        return sb.toString();
    }
}
