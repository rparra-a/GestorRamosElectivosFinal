package latinasincloud.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Clase que centraliza el manejo de excepciones para toda la aplicación.
 * Convierte excepciones personalizadas en respuestas HTTP con códigos de estado apropiados.
 */
@ControllerAdvice
public class GlobalExceptionHandler {

    // Maneja RecursoNoEncontradoException -> HTTP 404 NOT_FOUND
    @ExceptionHandler(RecursoNoEncontradoException.class)
    @ResponseStatus(HttpStatus.NOT_FOUND) // Asigna el código 404 directamente
    public ResponseEntity<String> handleRecursoNoEncontrado(RecursoNoEncontradoException ex) {
        // Retorna un objeto ResponseEntity con el mensaje de la excepción y el código 404
        return new ResponseEntity<>("Error 404: " + ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    // Maneja EstadoInvalidoException -> HTTP 400 BAD_REQUEST
    @ExceptionHandler(EstadoInvalidoException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST) // Asigna el código 400 directamente
    public ResponseEntity<String> handleEstadoInvalido(EstadoInvalidoException ex) {
        // Retorna un objeto ResponseEntity con el mensaje de la excepción y el código 400
        return new ResponseEntity<>("Error 400: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    // Opcional: Manejador genérico para cualquier otra RuntimeException no capturada (HTTP 500)
    @ExceptionHandler(RuntimeException.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ResponseEntity<String> handleGenericException(RuntimeException ex) {
        // En producción, es mejor no exponer el stack trace, solo un mensaje genérico.
        return new ResponseEntity<>("Error 500: Error interno del servidor: " + ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}



/* En lugar de que cada controller maneje bloques try-catch,
crearemos un componente que interceptará las excepciones lanzadas
por cualquier servicio o controlador y generará la respuesta HTTP.

 */