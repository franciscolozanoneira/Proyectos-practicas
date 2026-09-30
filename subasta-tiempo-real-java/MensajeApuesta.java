package EjercicioFinal;

import java.io.Serializable;

/**
 * Clase que representa el objeto de transferencia de datos (DTO) entre el Cliente y el Servidor.
 * <p>
 * Implementa {@link Serializable} para poder viajar a través de los Sockets.
 * Contiene el tipo de mensaje, el usuario emisor/receptor, el monto de la puja y un mensaje de texto.
 * </p>
 */
public class MensajeApuesta implements Serializable {

    private static final long serialVersionUID = 1L;

    /** Constante que indica que un cliente se está conectando. */
    public static final int LOGIN = 1;

    /** Constante que indica que un cliente envía una oferta monetaria. */
    public static final int PUJA = 2;

    /** Constante que indica una actualización global del servidor a los clientes (nueva puja máxima). */
    public static final int UPDATE = 3;

    /** Constante que indica que la puja fue rechazada por ser menor a la actual. */
    public static final int RECHAZADO = 4;

    private String usuario;
    private double monto;
    private int tipo;
    private String mensajeTexto;

    /**
     * Constructor principal del mensaje.
     * * @param tipo Tipo de acción (LOGIN, PUJA, UPDATE, RECHAZADO).
     * @param usuario Nombre del usuario asociado a la acción.
     * @param monto Cantidad monetaria (0 si es LOGIN).
     * @param mensajeTexto Texto descriptivo para mostrar en los logs.
     */
    public MensajeApuesta(int tipo, String usuario, double monto, String mensajeTexto) {
        this.tipo = tipo;
        this.usuario = usuario;
        this.monto = monto;
        this.mensajeTexto = mensajeTexto;
    }

    /** @return El tipo de acción del mensaje. */
    public int getTipo() { return tipo; }

    /** @return El nombre del usuario implicado. */
    public String getUsuario() { return usuario; }

    /** @return El monto de la apuesta o precio actual. */
    public double getMonto() { return monto; }

    /** @return Mensaje descriptivo para la interfaz gráfica. */
    public String getMensajeTexto() { return mensajeTexto; }
}