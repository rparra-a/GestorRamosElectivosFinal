package latinasincloud.controller;

import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Electivo;
import latinasincloud.service.ElectivoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/api/electivos")
public class ElectivoController {

    @Autowired // Inyección de dependencia
    private ElectivoService electivoService;

    // -------------------------------------------------------------------------
    // MÉTODOS CRUD
    // -------------------------------------------------------------------------

    /**
     * POST /api/electivos?idProfesor={id} : Crea un nuevo electivo.
     * El cuerpo contiene los datos del Electivo (nombre, descripción, cupos).
     * El ID del profesor se pasa como parámetro de consulta.
     * Retorna 201 Created.
     */
    @PostMapping
    public ResponseEntity<Electivo> crearElectivo(
            @RequestBody Electivo electivo,
            @RequestParam int idProfesor) {

        Electivo nuevoElectivo = electivoService.crearElectivo(electivo, idProfesor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoElectivo);
    }

    /**
     * GET /api/electivos : Lista todos los electivos.
     * Retorna 200 OK.
     */
    @GetMapping
    public ResponseEntity<List<Electivo>> listaElectivos(){
        return ResponseEntity.ok(electivoService.listaElectivos());
    }

    /**
     * GET /api/electivos/{id} : Obtiene un electivo por su ID.
     * Retorna 200 OK o lanza RecursoNoEncontradoException (404).
     */
    @GetMapping("/{id}")
    public ResponseEntity<Electivo> obtenerElectivoPorId(@PathVariable int id) {
        Electivo electivo = electivoService.obtenerElectivoPorId(id);
        if (electivo != null) {
            return ResponseEntity.ok(electivo);
        }
        else{
            throw new RecursoNoEncontradoException("Electivo no encontrado con ID: " + id);
        }
    }

    /**
     * PUT /api/electivos/{id} : Actualiza los datos de un electivo existente.
     * Retorna 200 OK o lanza RecursoNoEncontradoException (404).
     */
    @PutMapping("/{id}")
    public ResponseEntity<Electivo> actualizarElectivo(@PathVariable int id, @RequestBody Electivo electivo) {
        Electivo electivoActualizado = electivoService.actualizarElectivo(id, electivo);
        if (electivoActualizado != null) {
            return ResponseEntity.ok(electivoActualizado);
        } else {
            throw new RecursoNoEncontradoException("Electivo no encontrado con ID: " + id);
        }
    }


    /**
     * DELETE /api/electivos/{id} : Elimina un electivo por su ID.
     * Retorna 204 No Content o lanza RecursoNoEncontradoException (404).
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarElectivoPorId(@PathVariable int id) {
        boolean eliminado = electivoService.eliminarElectivoPorId(id);

        if (eliminado){
            return ResponseEntity.noContent().build(); // 204 No Content
        }
        else{
            throw new RecursoNoEncontradoException("No se puede eliminar. Electivo no encontrado con ID: " + id);
        }
    }
}