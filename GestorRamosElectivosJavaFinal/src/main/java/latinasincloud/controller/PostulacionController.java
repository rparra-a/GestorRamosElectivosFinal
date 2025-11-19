package latinasincloud.controller;

import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Postulacion;
import latinasincloud.service.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/postulaciones")
public class PostulacionController {

    @Autowired // Inyección de dependencia
    private PostulacionService postulacionService;

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD
    // -------------------------------------------------------------------------

    /**
     * POST /api/postulaciones?estudianteId={id}&electivoId={id} : Crea una nueva postulación.
     * Los IDs del Estudiante y Electivo se pasan como parámetros de consulta.
     * Retorna 201 Created.
     */

    /**
     * GET /api/postulaciones : Lista todas las postulaciones.
     * Retorna 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Postulacion>> listaPostulaciones(){
        return ResponseEntity.ok(postulacionService.listaPostulaciones());
    }

    /**
     * GET /api/postulaciones/{id} : Obtiene una postulación por su ID.
     * Retorna 200 OK o lanza RecursoNoEncontradoException (404).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Postulacion> obtenerPostulacionPorId(@PathVariable int id) {
        Postulacion postulacion = postulacionService.obtenerPostulacionPorId(id);
        if (postulacion != null) {
            return ResponseEntity.ok(postulacion);
        }
        else{
            throw new RecursoNoEncontradoException("Postulación no encontrada con ID: " + id);
        }
    }

    /**
     * DELETE /api/postulaciones/{id} : Elimina una postulación por su ID.
     * Retorna 204 No Content o lanza RecursoNoEncontradoException (404).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarPostulacionPorId(@PathVariable int id) {
        boolean eliminada = postulacionService.eliminarPostulacionPorId(id);

        if (eliminada){
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        else{
            throw new RecursoNoEncontradoException("No se puede eliminar. Postulación no encontrada con ID: " + id);
        }
    }
}