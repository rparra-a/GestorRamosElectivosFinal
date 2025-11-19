package latinasincloud.controller;


import latinasincloud.dto.PostulacionRequestDTO;
import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Estudiante;
import latinasincloud.model.Postulacion;
import latinasincloud.service.EstudianteService;
import latinasincloud.service.PostulacionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import org.springframework.http.HttpStatus;


@RestController
@RequestMapping("/api/estudiantes")
public class EstudianteController {

    @Autowired
    private EstudianteService estudianteService;

    @Autowired
    private PostulacionService postulacionService;

    @PostMapping
    public ResponseEntity<Estudiante> crearEstudiante(@RequestBody Estudiante estudiante) {
        Estudiante nuevaEstudiante = estudianteService.crearEstudiante(estudiante);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevaEstudiante);
    }

    @GetMapping
    public ResponseEntity<List<Estudiante>> listaEstudiantes(){
        return ResponseEntity.ok(estudianteService.listaEstudiantes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Estudiante> obtenerEstudiantePorId(@PathVariable int id) {
        Estudiante estudiante = estudianteService.obtenerEstudiantePorId(id);
        if (estudiante != null) {
            return ResponseEntity.ok(estudiante);
        }
        else{
            throw new RecursoNoEncontradoException("Estudiante no encontrada con ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarEstudiantePorId(@PathVariable int id) {
        boolean eliminada = estudianteService.eliminarEstudiantePorId(id);

        if (eliminada){
            return ResponseEntity.noContent().build();
        }
        else{
            throw new RecursoNoEncontradoException("No se puede eliminar. Estudiante no encontrada con ID: " + id);
        }
    }

    // Asumo que este método SÍ debe usar el DTO, ya que es la lógica de negocio principal
    @PostMapping("/postular")
    public ResponseEntity<List<Postulacion>> postularElectivos(@RequestBody PostulacionRequestDTO postulacionRequest) {
        List<Postulacion> nuevasPostulaciones = postulacionService.crearPostulacionesConPrioridad(postulacionRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevasPostulaciones);
    }
}
