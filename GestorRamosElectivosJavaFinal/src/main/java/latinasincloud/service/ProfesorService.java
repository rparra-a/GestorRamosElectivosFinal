package latinasincloud.service;

import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Profesor;
import latinasincloud.repository.IProfesorRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class ProfesorService {

    // 1. Reemplazamos List<Profesor> y contadorId por el Repositorio
    private final IProfesorRepository profesorRepository;

    private final PasswordEncoder passwordEncoder;

    // Inyección de dependencias por constructor
    public ProfesorService(IProfesorRepository profesorRepository, PasswordEncoder passwordEncoder) {
        this.profesorRepository = profesorRepository;
        this.passwordEncoder = passwordEncoder; // Asignación de PasswordEncoder
    }


    // ---------------------------------------------------
    // MÉTODOS CRUD (Usando JPA Repository)
    // ---------------------------------------------------

    // 1. Crear profesor (POST)
    @Transactional // Recomendado para métodos de escritura
    public Profesor crearProfesor(Profesor profesor){
        // CRÍTICO: Hashear la contraseña antes de guardarla en la base de datos
        String encodedPassword = passwordEncoder.encode(profesor.getPassword());
        profesor.setPassword(encodedPassword);

        profesor.setRol("Profesor"); // Asignación de rol por lógica de negocio

        // JPA asigna el ID automáticamente
        return profesorRepository.save(profesor);
    }

    // 2. Listar profesor. (GET) - Corregido el nombre a "listaProfesores" para consistencia
    public List<Profesor> listaProfesores(){
        return profesorRepository.findAll();
    }

    // 3. Obtener profesor por ID. (GET)
    public Profesor obtenerProfesorPorId(int id){
        // Usamos findById y lanzamos excepción si no existe
        return profesorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Profesor no encontrado con ID: " + id));
    }

    // 4. Actualizar profesor por ID. (PUT)
    @Transactional
    public Profesor actualizarProfesor(int id, Profesor profesorActualizado) {
        // Obtener el profesor existente (lanza 404 si no existe)
        Profesor profesorExistente = obtenerProfesorPorId(id);

        // Actualiza solo los campos permitidos.
        if (profesorActualizado.getNombre() != null && !profesorActualizado.getNombre().isEmpty()) {
            profesorExistente.setNombre(profesorActualizado.getNombre());
        }
        if (profesorActualizado.getEspecialidad() != null && !profesorActualizado.getEspecialidad().isEmpty()) {
            profesorExistente.setEspecialidad(profesorActualizado.getEspecialidad());
        }

        // 💡 Manejo de actualización de contraseña (solo si se proporciona)
        if (profesorActualizado.getPassword() != null && !profesorActualizado.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(profesorActualizado.getPassword());
            profesorExistente.setPassword(encodedPassword);
        }

        // Persistir el cambio
        return profesorRepository.save(profesorExistente);
    }


    // 5. Eliminar profesor por ID. (DELETE)
    @Transactional // Recomendado para operaciones de eliminación
    public boolean eliminarProfesorPorId(int id){
        // Verificar existencia y eliminar
        Profesor profesor = obtenerProfesorPorId(id); // Lanza 404 si no existe
        profesorRepository.delete(profesor);
        return true;
    }
}