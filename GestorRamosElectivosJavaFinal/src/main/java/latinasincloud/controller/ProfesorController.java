package latinasincloud.controller;

import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Profesor;
import latinasincloud.service.ProfesorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/profesores")
public class ProfesorController {

    @Autowired
    private ProfesorService profesorService;

    @PostMapping
    public ResponseEntity<Profesor> crearProfesor(@RequestBody Profesor profesor) {
        Profesor nuevoProfesor = profesorService.crearProfesor(profesor);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoProfesor);
    }

    @GetMapping
    public ResponseEntity<List<Profesor>> listaProfesores(){
        // Cambia getProfesores() por listaProfesores()
        return ResponseEntity.ok(profesorService.listaProfesores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Profesor> obtenerProfesorPorId(@PathVariable int id) {
        Profesor profesor = profesorService.obtenerProfesorPorId(id);
        if (profesor != null) {
            return ResponseEntity.ok(profesor);
        }
        else{
            throw new RecursoNoEncontradoException("Profesor no encontrado con ID: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Profesor> actualizarProfesor(@PathVariable int id, @RequestBody Profesor profesor) {
        Profesor profesorActualizado = profesorService.actualizarProfesor(id, profesor);
        if (profesorActualizado != null) {
            return ResponseEntity.ok(profesorActualizado);
        } else {
            throw new RecursoNoEncontradoException("Profesor no encontrado con ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarProfesorPorId(@PathVariable int id) {
        boolean eliminada = profesorService.eliminarProfesorPorId(id);

        if (eliminada){
            return ResponseEntity.noContent().build();
        }
        else{
            throw new RecursoNoEncontradoException("No se puede eliminar. Profesor no encontrado con ID: " + id);
        }
    }
}