package ecg;

import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import java.io.InputStream;
import java.util.Collection;

/**
 * Evaluador de riesgo cardiológico usando lógica difusa (JFuzzyLogic).
 * Lee el archivo FCL y evalúa el riesgo global basado en severidades de
 * patologías.
 */
public class EvaluadorRiesgoFuzzy {

    private FIS fis;
    private FunctionBlock fb;

    public EvaluadorRiesgoFuzzy() {
        cargarSistemaFuzzy();
    }

    private void cargarSistemaFuzzy() {
        try {
            // Cargar el archivo FCL desde resources
            InputStream fclStream = getClass().getClassLoader()
                    .getResourceAsStream("ecg/riesgo_cardiologico.fcl");

            if (fclStream == null) {
                System.err.println("ERROR: No se pudo encontrar riesgo_cardiologico.fcl");
                return;
            }

            // Crear el sistema de inferencia difusa
            fis = FIS.load(fclStream, true);

            if (fis == null) {
                System.err.println("ERROR: No se pudo cargar el sistema fuzzy desde FCL");
                return;
            }

            // Obtener el bloque de funciones
            fb = fis.getFunctionBlock("evaluacion_riesgo");

            if (fb == null) {
                System.err.println("ERROR: No se encontró el bloque 'evaluacion_riesgo' en el FCL");
            }

        } catch (Exception e) {
            System.err.println("ERROR al cargar sistema fuzzy: " + e.getMessage());
            e.printStackTrace();
        }
    }

    /**
     * Evalúa el riesgo global del paciente basándose en las severidades detectadas.
     * 
     * @param severidades Colección de SeveridadFuzzy detectadas
     * @return RiesgoGlobal calculado, o null si hay error
     */
    public RiesgoGlobal evaluarRiesgo(Collection<SeveridadFuzzy> severidades) {
        if (fb == null) {
            System.err.println("Sistema fuzzy no inicializado");
            return null;
        }

        // Inicializar todas las variables de entrada a 0.0
        fb.setVariable("severidad_taquicardia", 0.0);
        fb.setVariable("severidad_bradicardia", 0.0);
        fb.setVariable("severidad_iam", 0.0);
        fb.setVariable("severidad_isquemia", 0.0);
        fb.setVariable("severidad_hipocalcemia", 0.0);
        fb.setVariable("severidad_hipopotasemia", 0.0);
        fb.setVariable("num_patologias", severidades.size());

        // Asignar valores de severidad según patologías detectadas
        for (SeveridadFuzzy sev : severidades) {
            String patologia = sev.getPatologia().toLowerCase();
            double valor = sev.getSeveridad();

            if (patologia.contains("taquicardia")) {
                fb.setVariable("severidad_taquicardia", valor);
            } else if (patologia.contains("bradicardia")) {
                fb.setVariable("severidad_bradicardia", valor);
            } else if (patologia.contains("infarto") || patologia.contains("iam")) {
                fb.setVariable("severidad_iam", valor);
            } else if (patologia.contains("isquemia")) {
                fb.setVariable("severidad_isquemia", valor);
            } else if (patologia.contains("hipocalcemia")) {
                fb.setVariable("severidad_hipocalcemia", valor);
            } else if (patologia.contains("hipopotasemia")) {
                fb.setVariable("severidad_hipopotasemia", valor);
            }
        }

        // Evaluar el sistema difuso
        fb.evaluate();

        // Obtener el resultado
        double riesgoValor = fb.getVariable("riesgo_global").getValue();

        // Clasificar el nivel de riesgo
        String nivelRiesgo;
        if (riesgoValor < 0.35) {
            nivelRiesgo = "BAJO";
        } else if (riesgoValor < 0.65) {
            nivelRiesgo = "MEDIO";
        } else if (riesgoValor < 0.85) {
            nivelRiesgo = "ALTO";
        } else {
            nivelRiesgo = "CRÍTICO";
        }

        // Generar justificación
        StringBuilder justificacion = new StringBuilder();
        if (severidades.size() == 0) {
            justificacion.append("No se detectaron patologías.");
        } else if (severidades.size() == 1) {
            justificacion.append("Patología única detectada: ");
            justificacion.append(severidades.iterator().next().getPatologia());
        } else {
            justificacion.append("Comorbilidad: ");
            justificacion.append(severidades.size()).append(" patologías detectadas");
        }

        return new RiesgoGlobal(riesgoValor, nivelRiesgo, justificacion.toString());
    }

    /**
     * Genera recomendaciones médicas basadas en el riesgo global.
     * 
     * @param riesgo      RiesgoGlobal calculado
     * @param severidades Severidades detectadas
     * @return Array de recomendaciones
     */
    public Recomendacion[] generarRecomendaciones(RiesgoGlobal riesgo, Collection<SeveridadFuzzy> severidades) {
        java.util.List<Recomendacion> recs = new java.util.ArrayList<>();

        String nivel = riesgo.getNivelRiesgo();

        // Recomendaciones según nivel de riesgo
        switch (nivel) {
            case "CRÍTICO":
                recs.add(new Recomendacion("URGENTE",
                        "Atención médica inmediata requerida - Acudir a urgencias", 1));
                break;

            case "ALTO":
                recs.add(new Recomendacion("PRIORITARIA",
                        "Evaluación cardiológica urgente en menos de 24 horas", 2));
                break;

            case "MEDIO":
                recs.add(new Recomendacion("SEGUIMIENTO",
                        "Consulta con cardiólogo en los próximos 7 días", 3));
                break;

            case "BAJO":
                recs.add(new Recomendacion("PREVENTIVA",
                        "Control médico rutinario - Seguimiento preventivo", 4));
                break;
        }

        // Recomendaciones específicas según patologías
        for (SeveridadFuzzy sev : severidades) {
            String patologia = sev.getPatologia();

            if (patologia.contains("Infarto") && sev.getSeveridad() > 0.5) {
                recs.add(new Recomendacion("URGENTE",
                        "Posible IAM - Protocolo de infarto activado", 1));
            }

            if (patologia.contains("Hipocalcemia") || patologia.contains("Hipopotasemia")) {
                recs.add(new Recomendacion("PRIORITARIA",
                        "Control de electrolitos - Análisis de sangre recomendado", 2));
            }

            if (patologia.contains("Isquemia") && sev.getSeveridad() > 0.6) {
                recs.add(new Recomendacion("PRIORITARIA",
                        "Riesgo de progresión a IAM - Pruebas de esfuerzo recomendadas", 2));
            }
        }

        return recs.toArray(new Recomendacion[0]);
    }
}
