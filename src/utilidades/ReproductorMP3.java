package utilidades;

import javazoom.jl.player.Player;
import java.io.FileInputStream;
import java.io.File;
import java.io.BufferedInputStream;

public class ReproductorMP3 {
    public static void reproducir(String ruta) {
        try {
            FileInputStream fis = new FileInputStream(ruta);
            Player player = new Player(fis);
            player.play();
        } catch (Exception e) {
            System.out.println("Error al reproducir el audio: " + e.getMessage());
        }
    }
}



