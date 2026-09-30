package EjercicioFinal;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

/**
 * Servidor TCP que gestiona la subasta concurrente.
 * <p>
 * Responsabilidades:
 * 1. Aceptar conexiones de múltiples clientes.
 * 2. Mantener el estado global de la subasta (puja máxima).
 * 3. Sincronizar las pujas para evitar condiciones de carrera.
 * 4. Difundir (Broadcast) las actualizaciones a todos los clientes.
 * </p>
 */
public class ServidorSubasta {

    private final static int PUERTO = 6000;

    /** Variable compartida que almacena la puja más alta actual. */
    private static double puja_maxima;

    /** Lista compartida para guardar los flujos de salida de todos los clientes conectados. */
    private static final ArrayList<ObjectOutputStream> listaBroadcast = new ArrayList<>();

    /**
     * Punto de entrada del Servidor.
     * Inicia el socket servidor y el bucle de aceptación de clientes.
     * @param args Argumentos de línea de comandos (no usados).
     */
    public static void main(String[] args) {
        try (ServerSocket serverSocket = new ServerSocket(PUERTO)) {
            System.out.println("Servidor de subastas iniciado en puerto " + PUERTO);
            puja_maxima = 0;

            while (true) {
                Socket socket = serverSocket.accept();

                // Creamos el flujo de salida una única vez por cliente
                ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());

                // Sincronizamos la lista al añadir un nuevo cliente (Buenas prácticas de concurrencia)
                synchronized (listaBroadcast) {
                    listaBroadcast.add(oos);
                }

                // Iniciamos el hilo dedicado al cliente
                ManejoCliente manejoCliente = new ManejoCliente(socket, oos, new ObjectInputStream(socket.getInputStream()));
                manejoCliente.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Hilo encargado de gestionar la comunicación con un cliente específico.
     */
    static class ManejoCliente extends Thread {

        private Socket socket;
        private ObjectOutputStream oos;
        private ObjectInputStream ois;

        /**
         * Constructor del hilo de cliente.
         * @param socket Socket de la conexión.
         * @param oos Flujo de salida ya creado.
         * @param ois Flujo de entrada recién creado.
         */
        public ManejoCliente(Socket socket, ObjectOutputStream oos, ObjectInputStream ois) {
            this.socket = socket;
            this.oos = oos;
            this.ois = ois;
        }

        /**
         * Lógica principal del hilo.
         * 1. Lee el mensaje de LOGIN.
         * 2. Notifica a los demás de la conexión.
         * 3. Envía al cliente nuevo el estado actual de la subasta.
         * 4. Entra en bucle para procesar PUJAS.
         */
        @Override
        public void run() {
            try {
                // 1. Lectura del LOGIN
                MensajeApuesta mensajeConexion = (MensajeApuesta) ois.readObject();

                // 2. Broadcast de conexión a los demás
                MensajeApuesta mensajeConexionBroadcast = new MensajeApuesta(
                        MensajeApuesta.LOGIN,
                        mensajeConexion.getUsuario(),
                        puja_maxima,
                        "Se ha conectado: " + mensajeConexion.getUsuario()
                );

                synchronized (listaBroadcast) {
                    for (ObjectOutputStream oosBroadcast : listaBroadcast) {
                        if (oosBroadcast != oos) { // No notificar al propio usuario de su conexión
                            oosBroadcast.writeObject(mensajeConexionBroadcast);
                            oosBroadcast.flush();
                        }
                    }
                }

                // 3. Enviar estado actual AL NUEVO cliente
                MensajeApuesta estadoActual = new MensajeApuesta(
                        MensajeApuesta.UPDATE,
                        "SERVIDOR",
                        puja_maxima,
                        "Bienvenido. La puja va por: " + puja_maxima
                );
                oos.writeObject(estadoActual);
                oos.flush();

                // 4. Bucle principal
                while (true) {
                    MensajeApuesta mensajePuja = (MensajeApuesta) ois.readObject();

                    if (mensajePuja.getTipo() == MensajeApuesta.PUJA) {
                        comprobarPuja(mensajePuja.getMonto(), mensajePuja.getUsuario());
                    }
                }

            } catch (IOException | ClassNotFoundException e) {
                // El cliente se ha desconectado o hubo error de red
            } finally {
                // Limpieza de recursos crítica para evitar errores en futuros broadcasts
                try {
                    synchronized (listaBroadcast) {
                        listaBroadcast.remove(oos);
                    }
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }

        /**
         * Verifica si una puja es válida de forma sincronizada.
         * Si es válida (mayor que la actual), actualiza el estado y notifica a todos.
         * Si no, notifica solo al remitente del rechazo.
         * @param puja Cantidad ofertada.
         * @param usuario Nombre del ofertante.
         */
        public void comprobarPuja(double puja, String usuario) {
            // Sincronización a nivel de Clase para proteger la variable estática puja_maxima
            synchronized (ServidorSubasta.class) {
                if (puja > puja_maxima) {
                    puja_maxima = puja; // Actualizamos primero
                    mandarPuja(puja, usuario); // Luego notificamos
                } else {
                    rechazarPuja();
                }
            }
        }

        /**
         * Envía un mensaje de rechazo únicamente al cliente que hizo la oferta.
         */
        private void rechazarPuja() {
            MensajeApuesta mandarRechazo = new MensajeApuesta(
                    MensajeApuesta.RECHAZADO,
                    "SERVIDOR",
                    puja_maxima,
                    "La puja ha sido rechazada por ser inferior o igual a la actual."
            );
            try {
                oos.writeObject(mandarRechazo);
                oos.flush();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        /**
         * Difunde la nueva puja máxima a TODOS los clientes conectados.
         * @param puja Nueva cantidad máxima.
         * @param usuario Usuario que ganó la puja.
         */
        private void mandarPuja(double puja, String usuario) {
            MensajeApuesta mensajeMandarPuja = new MensajeApuesta(
                    MensajeApuesta.UPDATE,
                    "SERVIDOR",
                    puja,
                    "Nueva puja máxima de " + usuario + ": " + puja
            );

            try {
                synchronized (listaBroadcast) {
                    for (ObjectOutputStream oosBroadcast : listaBroadcast) {
                        oosBroadcast.writeObject(mensajeMandarPuja);
                        oosBroadcast.flush(); // Importante para envío inmediato
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}