package entidades;

import javazoom.jl.player.Player;
import java.io.FileInputStream;

public class ReproductorAudio {

    private Player player;

    public void reproducir(String rutaArchivo) {
        try {
            FileInputStream fis = new FileInputStream(rutaArchivo);
            player = new Player(fis);
            new Thread(() -> {
                try {
                    player.play();
                } catch (Exception e) {
                    System.out.println("Error al reproducir: " + e.getMessage());
                }
            }).start();
        } catch (Exception e) {
            System.out.println("No se pudo reproducir el archivo: " + e.getMessage());
        }
    }
}

