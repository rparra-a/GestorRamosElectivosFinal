package latinasincloud.service;

import latinasincloud.exception.RecursoNoEncontradoException; // Importar
import latinasincloud.model.Electivo;
import latinasincloud.model.Profesor;
import latinasincloud.repository.IElectivoRepository; // Importar Repositorio
import latinasincloud.repository.IProfesorRepository; // Importar Repositorio
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service // ¡Esta anotación es la clave!
public class ElectivoService {


    // 1. Reemplazamos List<Electivo> y contadorId por Repositorios
    private final IElectivoRepository electivoRepository;
    private final IProfesorRepository profesorRepository;

    // Inyección por constructor (ahora con Repositorios)
    public ElectivoService(IElectivoRepository electivoRepository, IProfesorRepository profesorRepository) {
        this.electivoRepository = electivoRepository;
        this.profesorRepository = profesorRepository;
    }

    // ---------------------------------------------------
    // MÉTODOS CRUD (Usando JPA Repository)
    // ---------------------------------------------------

    // 1. Crear Electivo (POST)
    public Electivo crearElectivo(Electivo electivo, int idProfesor) {

        // Buscar el profesor (lanza 404 si no existe)
        Profesor profesor = profesorRepository.findById(idProfesor)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con ID: " + idProfesor));

        // Asignar y guardar
        electivo.setProfesor(profesor);
        return electivoRepository.save(electivo);
    }

    // 2. Listar Electivos (GET)
    public List<Electivo> listaElectivos() {
        return electivoRepository.findAll();
    }

    // 3. Obtener Electivo por ID (GET)
    public Electivo obtenerElectivoPorId(int id) {
        // Usamos findById y lanzamos excepción si no existe
        return electivoRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Electivo no encontrado con ID: " + id));
    }

    // 4. Actualizar Electivo por ID (PUT)
    @Transactional
    public Electivo actualizarElectivo(int id, Electivo electivoAc) {
        // Obtener la entidad existente (lanza 404 si no existe)
        Electivo electivo = obtenerElectivoPorId(id);

        // Actualizar campos
        electivo.setNombre(electivoAc.getNombre() != null && !electivoAc.getNombre().isEmpty() ? electivoAc.getNombre() : electivo.getNombre());
        electivo.setDescripcion(electivoAc.getDescripcion() != null && !electivoAc.getDescripcion().isEmpty() ? electivoAc.getDescripcion() : electivo.getDescripcion());
        if (electivoAc.getCupos() >= 0) {
            electivo.setCupos(electivoAc.getCupos());
        }

        // Guardar y retornar
        return electivoRepository.save(electivo);
    }

    // 5. Eliminar Electivo por ID (DELETE)
    public boolean eliminarElectivoPorId(int electivoId) {
        // Verificar existencia y eliminar
        Electivo electivo = obtenerElectivoPorId(electivoId);
        electivoRepository.delete(electivo);
        return true;
    }
}
