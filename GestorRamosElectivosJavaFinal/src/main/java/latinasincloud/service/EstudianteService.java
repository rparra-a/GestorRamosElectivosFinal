package latinasincloud.service;

import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Estudiante;
import latinasincloud.repository.IEstudianteRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service // Esta anotación es fundamental para que Spring lo detecte.
public class EstudianteService {

    // 1. Reemplazamos List<Estudiante> y contadorId por el Repositorio
    private final IEstudianteRepository estudianteRepository;

    // 💡 NUEVO: Objeto para el Hashing de Contraseñas
    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor
    public EstudianteService(IEstudianteRepository estudianteRepository, PasswordEncoder passwordEncoder) {
        this.estudianteRepository = estudianteRepository;
        this.passwordEncoder = passwordEncoder; // Asignación de PasswordEncoder
    }

    // ---------------------------------------------------
    // MÉTODOS CRUD (Usando JPA Repository)
    // ---------------------------------------------------

    // Lógica para crear el estudiante
    @Transactional // Recomendado para métodos de escritura
    public Estudiante crearEstudiante(Estudiante estudiante) {
        // CRÍTICO: Hashear la contraseña antes de guardar
        String encodedPassword = passwordEncoder.encode(estudiante.getPassword());
        estudiante.setPassword(encodedPassword);

        estudiante.setRol("Estudiante"); // Asigna el rol al crearlo
        // JPA asigna el ID automáticamente
        return estudianteRepository.save(estudiante);
    }

    // Lógica para listar los estudiantes
    public List<Estudiante> listaEstudiantes(){
        return estudianteRepository.findAll();
    }

    // Lógica para obtener por ID
    public Estudiante obtenerEstudiantePorId(int id){
        // Usamos findById y lanzamos excepción si no existe
        return estudianteRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + id));
    }

    // 💡 NUEVO: Lógica para actualizar por ID
    @Transactional
    public Estudiante actualizarEstudiante(int id, Estudiante estudianteAc) {
        // Verificar existencia y obtener la entidad (lanza 404 si no existe)
        Estudiante estudianteExistente = obtenerEstudiantePorId(id);

        // Actualizar campos comunes que pueden cambiar (Nombre y Email)
        if (estudianteAc.getNombre() != null && !estudianteAc.getNombre().isEmpty()) {
            estudianteExistente.setNombre(estudianteAc.getNombre());
        }
        if (estudianteAc.getEmail() != null && !estudianteAc.getEmail().isEmpty()) {
            estudianteExistente.setEmail(estudianteAc.getEmail());
        }

        // Actualizar contraseña solo si se proporciona una nueva
        if (estudianteAc.getPassword() != null && !estudianteAc.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(estudianteAc.getPassword());
            estudianteExistente.setPassword(encodedPassword);
        }

        // El rol ("Estudiante") se mantiene.

        return estudianteRepository.save(estudianteExistente);
    }


    // Lógica para eliminar por ID
    @Transactional // Recomendado para operaciones de eliminación
    public boolean eliminarEstudiantePorId(int id){
        // Verificar existencia y eliminar
        Estudiante estudianteEliminar = obtenerEstudiantePorId(id); // Lanza 404 si no existe
        estudianteRepository.delete(estudianteEliminar);
        return true;
    }
}