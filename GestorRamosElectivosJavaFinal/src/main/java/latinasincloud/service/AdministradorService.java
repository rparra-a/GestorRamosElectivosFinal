package latinasincloud.service;

import latinasincloud.config.SecurityConfig;
import latinasincloud.exception.RecursoNoEncontradoException;
import latinasincloud.model.Administrador;
import latinasincloud.model.Postulacion;
import latinasincloud.repository.IAdministradorRepository; // Importar Repositorio
import org.springframework.security.crypto.password.PasswordEncoder; // NUEVO Import para Hashing
import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.util.List;

@Service
public class AdministradorService {

    // 1. Reemplazamos List<Administrador> por Repositorios
    private final IAdministradorRepository administradorRepository;

    // Mantenemos PostulacionService para el método de negocio de asignación masiva
    private final PostulacionService postulacionService;

    // 💡 CORRECIÓN: Se agrega objeto para el Hashing de Contraseñas
    private final PasswordEncoder passwordEncoder;


    // Constructor para inyección de dependencias (ahora con Repositorios)
    public AdministradorService(
            IAdministradorRepository administradorRepository,
            PostulacionService postulacionService,
            PasswordEncoder passwordEncoder // 💡 NUEVO: Parámetro para inyección
    ) {
        this.administradorRepository = administradorRepository;
        this.postulacionService = postulacionService;
        this.passwordEncoder = passwordEncoder; // 💡 Asignación
    }

    // ---------------------------------------------------
    // MÉTODOS CRUD (Usando JPA Repository)
    // ---------------------------------------------------

    // 1. Crear Administrador (POST)
    public Administrador crearAdministrador(Administrador administrador) {
        // CRÍTICO: Hashear la contraseña antes de guardarla en la base de datos
        String encodedPassword = passwordEncoder.encode(administrador.getPassword());
        administrador.setPassword(encodedPassword);

        // JPA asigna el ID automáticamente
        return administradorRepository.save(administrador);
    }

    // 2. Listar Administradores (GET)
    public List<Administrador> listaAdministradores (){
        return administradorRepository.findAll();
    }

    // 3. Obtener Administrador por ID (GET)
    public Administrador obtenerAdministradorPorId(int id) {
        // Usamos findById y lanzamos RecursoNoEncontradoException si no existe
        return administradorRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Administrador no encontrado con ID: " + id));
    }

    // 4. Actualizar Administrador por ID (PUT)
    @Transactional
    public Administrador actualizarAdministrador(int id, Administrador administradorAc) {
        Administrador administrador = obtenerAdministradorPorId(id);

        administrador.setNombre(administradorAc.getNombre() != null && !administradorAc.getNombre().isEmpty() ? administradorAc.getNombre() : administrador.getNombre());
        //administrador.setRut(administradorAc.getRut() != null && !administradorAc.getRut().isEmpty() ? administradorAc.getRut() : administrador.getRut());

        // 💡 Manejo de actualización de contraseña (solo si se proporciona)
        if (administradorAc.getPassword() != null && !administradorAc.getPassword().isEmpty()) {
            String encodedPassword = passwordEncoder.encode(administradorAc.getPassword());
            administrador.setPassword(encodedPassword);
        }

        // Guardar y retornar
        return administradorRepository.save(administrador);
    }

    // 5. Eliminar Administrador por ID (DELETE)
    public boolean eliminarAdministradorPorId(int administradorId) {
        // Verificar existencia y eliminar
        Administrador administrador = obtenerAdministradorPorId(administradorId);
        administradorRepository.delete(administrador);
        return true;
    }

    // ---------------------------------------------------
    // MÉTODO DE NEGOCIO (Asignación Masiva)
    // ---------------------------------------------------

    /**
     * Llama al PostulacionService para realizar el proceso de asignación masiva.
     * @return Lista de postulaciones que fueron aceptadas.
     */
    @Transactional
    public List<Postulacion> realizarAsignacionMasiva() {
        // La lógica de asignación se mantiene en PostulacionService.
        List<Postulacion> asignacionesAceptadas = postulacionService.procesarAsignaciones();

        System.out.println("Proceso de asignación masiva finalizado. Total de estudiantes asignados: " + asignacionesAceptadas.size());
        return asignacionesAceptadas;
    }
}