package EjercicioFinal;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Cliente gráfico basado en Swing para el sistema de subastas.
 * <p>
 * Maneja la conexión TCP, el envío de objetos {@link MensajeApuesta} y
 * la actualización de la interfaz gráfica mediante hilos seguros.
 * </p>
 */
public class ClienteSubasta extends JFrame {

    private JTextField tfMonto;
    private JTextArea taLog;
    private JLabel lblMontoActual;
    private String usuario;

    // Objetos de red
    private Socket socket;
    private ObjectOutputStream out;
    private ObjectInputStream in;

    /**
     * Constructor del cliente. Solicita usuario e inicia la GUI y conexión.
     */
    public ClienteSubasta() {
        super("Subasta PSP - Cliente");
        this.usuario = JOptionPane.showInputDialog("Introduce tu nombre de usuario:");
        if (this.usuario == null || this.usuario.trim().isEmpty()) System.exit(0);

        inicializarGUI();
        conectarAlServidor();
    }

    /**
     * Configura los componentes visuales de la ventana (Swing).
     */
    private void inicializarGUI() {
        this.setSize(400, 400);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());

        // Panel Superior (Info actual)
        JPanel panelNorte = new JPanel(new GridLayout(2, 1));
        panelNorte.add(new JLabel("Artículo: PlayStation 5 Pro", SwingConstants.CENTER));
        lblMontoActual = new JLabel("Puja actual: 0.0 €", SwingConstants.CENTER);
        lblMontoActual.setFont(new Font("Arial", Font.BOLD, 20));
        lblMontoActual.setForeground(Color.BLUE);
        panelNorte.add(lblMontoActual);
        this.add(panelNorte, BorderLayout.NORTH);

        // Centro (Log de eventos)
        taLog = new JTextArea();
        taLog.setEditable(false);
        this.add(new JScrollPane(taLog), BorderLayout.CENTER);

        // Panel Inferior (Controles de puja)
        JPanel panelSur = new JPanel();
        tfMonto = new JTextField(10);
        JButton btnPujar = new JButton("¡Pujar!");

        btnPujar.addActionListener(e -> enviarPuja());

        panelSur.add(new JLabel("Tu oferta:"));
        panelSur.add(tfMonto);
        panelSur.add(btnPujar);
        this.add(panelSur, BorderLayout.SOUTH);

        this.setVisible(true);
    }

    /**
     * Establece la conexión con el servidor en el puerto 6000.
     * <ol>
     * <li>Abre el Socket.</li>
     * <li>Inicializa Streams (Output antes que Input).</li>
     * <li>Envía mensaje inicial de LOGIN.</li>
     * <li>Lanza hilo de escucha.</li>
     * </ol>
     */
    private void conectarAlServidor() {
        try {
            socket = new Socket("localhost", 6000);
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());

            MensajeApuesta mensajeLogin = new MensajeApuesta(MensajeApuesta.LOGIN, usuario, 0, "Me intento conectar al servidor");
            out.writeObject(mensajeLogin);
            out.flush();

            escucharServidor();

        } catch (Exception e) {
            taLog.append("Error de conexión: " + e.getMessage() + "\n");
        }
    }

    /**
     * Inicia un hilo secundario para escuchar mensajes del servidor sin bloquear la UI.
     * Utiliza {@link SwingUtilities#invokeLater(Runnable)} para actualizar los componentes Swing.
     */
    private void escucharServidor() {
        Thread hiloEscucha = new Thread(() -> {
            while (true) {
                try {
                    MensajeApuesta mensajeServer = (MensajeApuesta) in.readObject();

                    // Procesamiento según el tipo de mensaje recibido
                    if (mensajeServer.getTipo() == MensajeApuesta.LOGIN) {
                        SwingUtilities.invokeLater(() -> {
                            taLog.append(">> " + mensajeServer.getMensajeTexto() + "\n");
                        });

                    } else if (mensajeServer.getTipo() == MensajeApuesta.UPDATE) {
                        SwingUtilities.invokeLater(() -> {
                            lblMontoActual.setText("Puja actual: " + mensajeServer.getMonto() + " €");
                            taLog.append(mensajeServer.getMensajeTexto() + "\n");
                        });

                    } else if (mensajeServer.getTipo() == MensajeApuesta.RECHAZADO) {
                        SwingUtilities.invokeLater(() -> {
                            taLog.append("ERROR: " + mensajeServer.getMensajeTexto() + "\n");
                        });
                    }
                } catch (IOException | ClassNotFoundException e) {
                    SwingUtilities.invokeLater(() -> taLog.append("Conexión perdida con el servidor.\n"));
                    break; // Salir del bucle si hay error
                }
            }
        });
        hiloEscucha.start();
    }

    /**
     * Valida la entrada del usuario, crea un objeto {@link MensajeApuesta} y lo envía.
     */
    private void enviarPuja() {
        try {
            double cantidad = Double.parseDouble(tfMonto.getText());

            MensajeApuesta mensajeApuesta = new MensajeApuesta(MensajeApuesta.PUJA, usuario, cantidad, "Puja: " + cantidad);
            out.writeObject(mensajeApuesta);
            out.flush(); // Aseguramos que el mensaje sale del buffer

            tfMonto.setText(""); // Limpiar campo

        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(this, "Introduce un número válido");
        } catch (IOException e) {
            taLog.append("Error al enviar: " + e.getMessage() + "\n");
        }
    }

    public static void main(String[] args) {
        new ClienteSubasta();
    }
}