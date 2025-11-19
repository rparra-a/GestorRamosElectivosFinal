package latinasincloud.exception;

public class EstadoInvalidoException extends RuntimeException {
    public EstadoInvalidoException(String message) {
        super(message);
    }
}

/* Esta excepción es ideal para mapear a un código de estado HTTP 400 Bad Request.
Se usaría para manejar errores de lógica de negocio o validación de datos
(por ejemplo, intentar postular a un electivo con 0 cupos,
o intentar cambiar el estado de una postulación a un estado que no le corresponde).

 */