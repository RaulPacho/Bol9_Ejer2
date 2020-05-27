import java.util.ArrayList;
import java.util.Scanner;

import javax.swing.*;

import java.awt.Color;
import java.awt.event.*;
import java.io.*;

public class Graphics extends JFrame implements ActionListener {
    JMenuBar mnBarraMenu;
    JMenu mnArchivo, mnMovil, mnOtros;
    JMenuItem mnGrabar, mnLeer, mnReset, mnSalir, mnAcerca;
    ArrayList<JButtonEspecial> botones = new ArrayList();
    JTextField numero;
    JButtonEspecial reset;
    String numMarcado = ""; // Me quedo sin ideas para variables

    public Graphics() {
        super("Télefon");
        setLayout(null);
        int posX = 0;
        int posY = 0;

        // Items
        mnAcerca = new JMenuItem("Acerca de...");
        mnAcerca.setMnemonic('A');
        mnAcerca.addActionListener(this);

        mnGrabar = new JMenuItem("Grabar");
        mnGrabar.setMnemonic('G');
        mnGrabar.addActionListener(this);

        mnLeer = new JMenuItem("Leer");
        mnLeer.setMnemonic('L');
        mnLeer.addActionListener(this);

        mnReset = new JMenuItem("Reset");
        mnReset.setMnemonic('R');
        mnReset.addActionListener(this);

        mnSalir = new JMenuItem("Salir");
        mnSalir.setMnemonic('S');
        mnSalir.addActionListener(this);

        // Menus
        mnArchivo = new JMenu("Archivo");
        mnArchivo.setMnemonic('F');
        mnArchivo.add(mnGrabar);
        mnArchivo.add(mnLeer);

        mnMovil = new JMenu("Movil");
        mnMovil.setMnemonic('M');
        mnMovil.add(mnReset);
        mnMovil.addSeparator();
        mnMovil.add(mnSalir);

        mnOtros = new JMenu("Otros");
        mnOtros.setMnemonic('O');
        mnOtros.add(mnAcerca);

        // Barra
        mnBarraMenu = new JMenuBar();
        mnBarraMenu.add(mnArchivo);
        mnBarraMenu.add(mnMovil);
        mnBarraMenu.add(mnOtros);
        this.setJMenuBar(mnBarraMenu);

        for (int i = 1; i <= 12; i++) {
            posX += 100;
            if (i <= 9) {
                botones.add(new JButtonEspecial("" + i));
            } else {
                botones.add(new JButtonEspecial("" + (i == 10 ? "#" : i == 11 ? 0 : "*")));
            }
            botones.get(i - 1).addMouseListener(new ControlRaton());
            botones.get(i - 1).setLocation(posX, posY);
            botones.get(i - 1).setSize(50, 50);
            botones.get(i - 1).setVisible(true);
            add(botones.get(i - 1));
            if (i % 3 == 0) {
                posX = 0;
                posY += 100;
            }

        }

        numero = new JTextField(12);

        posX += 125;
        numero.setSize(200, 25);
        numero.setLocation(posX, posY);
        numero.setVisible(true);
        numero.setEditable(false);
        add(numero);

        reset = new JButtonEspecial("Reset");
        reset.addMouseListener(new ControlRaton());
        reset.setSize(100, 25);
        reset.setLocation(20, posY);
        reset.setVisible(true);
        add(reset);

    }

    private class ControlRaton extends MouseAdapter {
        @Override
        public void mouseEntered(MouseEvent e) {
            if (e.getSource() != reset && ((JButtonEspecial) e.getSource()).clickeao == false) {
                ((JButtonEspecial) e.getSource()).setBackground(Color.ORANGE);
            }
        }

        @Override
        public void mouseExited(MouseEvent e) {
            if (e.getSource() != reset && ((JButtonEspecial) e.getSource()).clickeao == false) {
                ((JButtonEspecial) e.getSource()).setBackground(new JButton().getBackground());
            }
        }

        @Override
        public void mouseClicked(MouseEvent e) {
            if (e.getSource() != reset) {
                ((JButtonEspecial) e.getSource()).setBackground(Color.PINK);
                numMarcado += "" + ((JButtonEspecial) e.getSource()).getText();
                numero.setText(numMarcado);
                ((JButtonEspecial) e.getSource()).clickeao = true;
            } else {
                for (int i = 0; i < botones.size(); i++) {
                    botones.get(i).setBackground(new JButton().getBackground());
                    botones.get(i).clickeao = false;
                }
                numMarcado = "";
                numero.setText(numMarcado);
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (e.getSource() == mnGrabar) {
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int res = fc.showSaveDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = new File(fc.getSelectedFile().getAbsolutePath());
                boolean procedemos = true;
                if (f.exists()) {
                    int respuestica = JOptionPane.showConfirmDialog(this, "El archivo ya existe\n¿Quiere reemplazarlo?",
                            "El archivo ya existe", JOptionPane.YES_NO_OPTION);
                    if (!(respuestica == JOptionPane.YES_OPTION)) {
                        procedemos = false;
                    }

                }
                if (procedemos) {
                    PrintWriter pw = null;
                    try {
                        pw = new PrintWriter(new FileWriter(f.getPath(), true));
                        pw.println(numero.getText());
                    } catch (IOException err) {
                        JOptionPane.showMessageDialog(this, "No se ha podido guardar el documento.", "Error escritura",
                                JOptionPane.ERROR_MESSAGE);
                    } finally {
                        if (pw != null) {
                            pw.close();
                        }
                    }

                }
            }
        }

        if (e.getSource() == mnLeer) {
            String agenda = "";
            JFileChooser fc = new JFileChooser();
            fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

            int res = fc.showOpenDialog(this);
            if (res == JFileChooser.APPROVE_OPTION) {
                File f = new File(fc.getSelectedFile().getAbsolutePath());
                try (Scanner lee = new Scanner(f)) {
                    while (lee.hasNext()) {
                        agenda += lee.nextLine() + "\n";
                    }
                } catch (FileNotFoundException err) {
                    JOptionPane.showMessageDialog(this, "No se ha podido abrir el documento.", "Error lectura",
                            JOptionPane.ERROR_MESSAGE);
                }

                JOptionPane.showMessageDialog(this, agenda, "Agenda", JOptionPane.INFORMATION_MESSAGE);
            }
        }

        if (e.getSource() == mnReset) {
            // Si, si, molaba una funcion pero encuentrala despues en este pedazo de biblia.
            for (int i = 0; i < botones.size(); i++) {
                botones.get(i).setBackground(new JButton().getBackground());
                botones.get(i).clickeao = false;
            }
            numMarcado = "";
            numero.setText(numMarcado);
        }

        if (e.getSource() == mnSalir) {
            System.exit(0);
        }

        if (e.getSource() == mnAcerca) {
            String leeloSiHayHuevos = "El programa ha sido obra de la malvada mente de Curro,\n un profesor de programacion querido y odiado por sus alumnos a partes iguales. \n"
                    + "El autor, por otra parte, es Raúl; alma pura donde las haya\n ha luchado contra viento y marea para sacarlo adelante.\n No le fue facil y en su viaje hizo muchos amigos y enemigos.\n"
                    + "Cuando creyo estar cerca el malvado profesor, y con esto me adelanto a la historia,\n le dijo (como si fuese la maldad encarnada) \"No me vale que guardes solo un numero\"\n"
                    + "Y nuestro valeroso estudiante tuvo que volver a emprender la aventura\nabandonando el calido abrazo de sus seres queridos, de su madre, de una compañera, de su gata...\n Con una lagrima en su corazon.\n"
                    + "No es posible imaginar esta hazaña sin comprender el despiadado numero de veces que Curro\n hace retomar un programa porque \"Falla identacion\" o \"Hay que documentarlo\"\n"
                    + "Tengan todos en cuenta la hazaña que esto supone y recen, recen por Raúl";

            Icon icono = new ImageIcon(getClass().getResource("raul.png"));

            JOptionPane.showMessageDialog(null, leeloSiHayHuevos, "Acerca de...", JOptionPane.PLAIN_MESSAGE, icono);
        }
    }

}