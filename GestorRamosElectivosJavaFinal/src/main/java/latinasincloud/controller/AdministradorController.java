package latinasincloud.controller;

import latinasincloud.model.Postulacion;
import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Administrador;
import latinasincloud.service.AdministradorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;

@RestController
@RequestMapping("/api/administradores")
public class AdministradorController {

    @Autowired
    private AdministradorService administradorService;

    @PostMapping
    public ResponseEntity<Administrador> crearAdministrador(@RequestBody Administrador administrador) {
        Administrador nuevoAdmin = administradorService.crearAdministrador(administrador);
        return ResponseEntity.status(HttpStatus.CREATED).body(nuevoAdmin);
    }

    @GetMapping
    public ResponseEntity<List<Administrador>> listaAdministradores(){
        return ResponseEntity.ok(administradorService.listaAdministradores());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Administrador> obtenerAdministradorPorId(@PathVariable int id) {
        Administrador administrador = administradorService.obtenerAdministradorPorId(id);
        if (administrador != null) {
            return ResponseEntity.ok(administrador);
        }
        else{
            throw new RecursoNoEncontradoException("Administrador no encontrado con ID: " + id);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Administrador> actualizarAdministrador(@PathVariable int id, @RequestBody Administrador administrador) {
        Administrador adminActualizado = administradorService.actualizarAdministrador(id, administrador);
        if (adminActualizado != null) {
            return ResponseEntity.ok(adminActualizado);
        } else {
            throw new RecursoNoEncontradoException("Administrador no encontrado con ID: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarAdministradorPorId(@PathVariable int id) {
        boolean eliminada = administradorService.eliminarAdministradorPorId(id);

        if (eliminada){
            return ResponseEntity.noContent().build();
        }
        else{
            throw new RecursoNoEncontradoException("No se puede eliminar. Administrador no encontrado con ID: " + id);
        }
    }

    @PostMapping("/asignacion-masiva")
    public ResponseEntity<List<Postulacion>> realizarAsignacionMasiva() {
        List<Postulacion> asignacionesAceptadas = administradorService.realizarAsignacionMasiva();
        return ResponseEntity.ok(asignacionesAceptadas);
    }
}