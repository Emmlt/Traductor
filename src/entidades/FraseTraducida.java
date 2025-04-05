package entidades;

public class FraseTraducida {
    private String texto;
    private String idioma;
    private String traduccion;
    private String rutaAudio; 

    public FraseTraducida(String texto, String idioma, String traduccion, String rutaAudio) {
        this.texto = texto;
        this.idioma = idioma;
        this.traduccion = traduccion;
        this.rutaAudio = rutaAudio;
    }

    public String getTexto() {
        return texto;
    }

    public String getIdioma() {
        return idioma;
    }

    public String getTraduccion() {
        return traduccion;
    }

    public String getRutaAudio() {
        return rutaAudio;
    }
}
