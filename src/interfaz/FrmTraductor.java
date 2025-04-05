package interfaz;

import entidades.GestorFrases;
import utilidades.ReproductorMP3;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class FrmTraductor extends JFrame {

    private JComboBox<String> cboFrases;
    private JComboBox<String> cboIdiomas;
    private JButton btnTraducir;
    private JButton btnAudio;
    private JTextArea txtResultado;

    private GestorFrases gestor;

    public FrmTraductor() {
        setTitle("Traductor de frases");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gestor = new GestorFrases();
        gestor.cargarDesdeJSON("datos/FrasesTraducidas.json");


        // Panel superior: selección de frase e idioma
        JPanel panelSuperior = new JPanel(new GridLayout(2, 2));
        panelSuperior.setBorder(BorderFactory.createTitledBorder("Selecciona la frase y el idioma"));

        cboFrases = new JComboBox<>(gestor.obtenerFrasesDisponibles().toArray(new String[0]));
        cboIdiomas = new JComboBox<>(gestor.obtenerIdiomasDisponibles().toArray(new String[0]));

        panelSuperior.add(new JLabel("Frase:"));
        panelSuperior.add(cboFrases);
        panelSuperior.add(new JLabel("Idioma:"));
        panelSuperior.add(cboIdiomas);

        // Panel central: botones y resultado
        JPanel panelCentral = new JPanel(new BorderLayout());

        btnTraducir = new JButton("Traducir");
        btnAudio = new JButton("▶ Audio");

        JPanel panelBotones = new JPanel();
        panelBotones.add(btnTraducir);
        panelBotones.add(btnAudio);

        txtResultado = new JTextArea(5, 20);
        txtResultado.setEditable(false);
        txtResultado.setLineWrap(true);
        txtResultado.setWrapStyleWord(true);

        panelCentral.add(panelBotones, BorderLayout.NORTH);
        panelCentral.add(new JScrollPane(txtResultado), BorderLayout.CENTER);

        add(panelSuperior, BorderLayout.NORTH);
        add(panelCentral, BorderLayout.CENTER);

        // Acción del botón Traducir
        btnTraducir.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String frase = (String) cboFrases.getSelectedItem();
                String idioma = (String) cboIdiomas.getSelectedItem();
                String resultado = gestor.traducir(frase, idioma);
                txtResultado.setText(resultado);
            }
        });

        // Acción del botón Audio
        btnAudio.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                String frase = (String) cboFrases.getSelectedItem();
                String idioma = (String) cboIdiomas.getSelectedItem();
                String rutaAudio = gestor.obtenerRutaAudio(frase, idioma);
                if (rutaAudio != null) {
                    ReproductorMP3.reproducir(rutaAudio);
                } else {
                    JOptionPane.showMessageDialog(null, "No hay audio disponible para esta frase en el idioma seleccionado.");
                }
            }
        });

        setVisible(true);
    }
}