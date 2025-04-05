package entidades;

import java.io.*;
import java.text.Normalizer;
import java.util.*;

public class GestorFrases {

    private List<FraseTraducida> frases;

    public GestorFrases() {
        frases = new ArrayList<>();
    }

    public void cargarDesdeJSON(String rutaArchivo) {
        frases.clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(rutaArchivo))) {
            StringBuilder json = new StringBuilder();
            String linea;

            while ((linea = reader.readLine()) != null) {
                json.append(linea.trim());
            }

            String contenido = json.toString();

            int inicio = contenido.indexOf("[");
            int fin = contenido.lastIndexOf("]");

            if (inicio == -1 || fin == -1) return;

            String frasesArray = contenido.substring(inicio + 1, fin);

            List<String> bloques = new ArrayList<>();
            int nivelLlaves = 0;
            StringBuilder bloqueActual = new StringBuilder();

            for (char c : frasesArray.toCharArray()) {
                if (c == '{') nivelLlaves++;
                if (c == '}') nivelLlaves--;

                bloqueActual.append(c);

                if (nivelLlaves == 0 && bloqueActual.length() > 0) {
                    bloques.add(bloqueActual.toString());
                    bloqueActual.setLength(0);
                }
            }

            for (String bloque : bloques) {
                String texto = extraerValor(bloque, "Texto");

                int idxTrad = bloque.indexOf("\"Traducciones\"");
                if (idxTrad == -1) continue;

                String bloqueTrads = bloque.substring(bloque.indexOf("[", idxTrad) + 1, bloque.indexOf("]", idxTrad));
                String[] traducciones = bloqueTrads.split("\\},\\s*\\{");

                for (String trad : traducciones) {
                    trad = trad.replace("{", "").replace("}", "").trim();
                    String idioma = extraerValor(trad, "Idioma");
                    String textoTraducido = extraerValor(trad, "TextoTraducido");
                    String rutaAudio = generarRutaAudio(texto, idioma);
                    frases.add(new FraseTraducida(texto, idioma, textoTraducido, rutaAudio));
                }
            }

        } catch (IOException e) {
            System.out.println("Error al leer el archivo JSON: " + e.getMessage());
        }
    }

    private String extraerValor(String bloque, String clave) {
        int i = bloque.indexOf("\"" + clave + "\"");
        if (i == -1) return "";
        int start = bloque.indexOf(":", i) + 1;
        int end = bloque.indexOf(",", start);
        if (end == -1) end = bloque.length();
        String valor = bloque.substring(start, end).replace("\"", "").trim();
        return valor;
    }

    private String generarRutaAudio(String frase, String idioma) {
        if (frase == null || idioma == null) return null;

        // Normalizar y convertir a formato del archivo
        frase = Normalizer.normalize(frase, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");
        idioma = Normalizer.normalize(idioma, Normalizer.Form.NFD).replaceAll("[^\\p{ASCII}]", "");

        String fraseFormateada = convertirAPascalCase(frase);
        String idiomaFormateado = convertirAPascalCase(idioma);

        String nombreArchivo = fraseFormateada + "-" + idiomaFormateado + ".mp3";
        String ruta = "audio/" + nombreArchivo;

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            System.out.println("No se encontró: " + ruta);
            return null;
        }

        return ruta;
    }

    private String convertirAPascalCase(String texto) {
        String[] palabras = texto.replace("?", "").replace("¿", "").split("\\s+");
        StringBuilder resultado = new StringBuilder();
        for (String palabra : palabras) {
            if (!palabra.isEmpty()) {
                resultado.append(Character.toUpperCase(palabra.charAt(0)));
                if (palabra.length() > 1) {
                    resultado.append(palabra.substring(1).toLowerCase());
                }
            }
        }
        return resultado.toString();
    }

    public Set<String> obtenerFrasesDisponibles() {
        Set<String> frasesDisponibles = new HashSet<>();
        for (FraseTraducida f : frases) {
            frasesDisponibles.add(f.getTexto());
        }
        return frasesDisponibles;
    }

    public Set<String> obtenerIdiomasDisponibles() {
        Set<String> idiomas = new HashSet<>();
        for (FraseTraducida f : frases) {
            idiomas.add(f.getIdioma());
        }
        return idiomas;
    }

    public String traducir(String frase, String idioma) {
        for (FraseTraducida f : frases) {
            if (f.getTexto().equals(frase) && f.getIdioma().equals(idioma)) {
                return f.getTraduccion();
            }
        }
        return "Traducción no disponible.";
    }

    public String obtenerRutaAudio(String frase, String idioma) {
        for (FraseTraducida f : frases) {
            if (f.getTexto().equals(frase) && f.getIdioma().equals(idioma)) {
                return f.getRutaAudio();
            }
        }
        return null;
    }
}
