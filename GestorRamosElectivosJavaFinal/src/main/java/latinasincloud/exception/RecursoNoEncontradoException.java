package latinasincloud.exception; // recordar para exception Crear un nuevo paquete 'exception'

// Extiende RuntimeException para ser una excepción no chequeada
public class RecursoNoEncontradoException extends RuntimeException {
    public RecursoNoEncontradoException(String message) {
        super(message);
    }
}


/* Esta excepción está perfectamente diseñada para mapear a un código de estado
HTTP 404 Not Found. Se utiliza cuando se intenta obtener,
actualizar o eliminar un recurso (Estudiante, Electivo, Postulacion) por ID y no existe.
 */