package latinasincloud.service;

import latinasincloud.dto.ElectivoPreferenciaDTO;
import latinasincloud.dto.PostulacionRequestDTO;
import latinasincloud.exception.EstadoInvalidoException;
import latinasincloud.exception.RecursoNoEncontradoException;

import latinasincloud.model.Electivo;
import latinasincloud.model.Estado;
import latinasincloud.model.Estudiante;
import latinasincloud.model.Postulacion;

import latinasincloud.repository.IPostulacionRepository; // Importar Repositorio
import latinasincloud.repository.IEstudianteRepository; // Importar Repositorio
import latinasincloud.repository.IElectivoRepository; // Importar Repositorio

import org.springframework.stereotype.Service;
import jakarta.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.stream.Collectors;


@Service // Anotación para que Spring lo reconozca
public class PostulacionService {

    // 1. Reemplazamos List<Postulacion> y contadorId por Repositorios
    private final IPostulacionRepository postulacionRepository;
    private final IEstudianteRepository estudianteRepository;
    private final IElectivoRepository electivoRepository;

    // Inyección de dependencias por constructor
    public PostulacionService(
            IPostulacionRepository postulacionRepository,
            IEstudianteRepository estudianteRepository,
            IElectivoRepository electivoRepository
    ) {
        this.postulacionRepository = postulacionRepository;
        this.estudianteRepository = estudianteRepository;
        this.electivoRepository = electivoRepository;
    }
    // ---------------------------------------------------
    // MÉTODO DE NEGOCIO (Postulación con Prioridad)
    // ---------------------------------------------------

    @Transactional
    public List<Postulacion> crearPostulacionesConPrioridad(PostulacionRequestDTO request) {

        int estudianteId = request.getEstudianteId();

        // 1. Validar Estudiante
        Estudiante estudiante = estudianteRepository.findById(estudianteId)
                .orElseThrow(() -> new RecursoNoEncontradoException("Estudiante no encontrado con ID: " + estudianteId));

        // 2. Validar que tenga exactamente 3 preferencias
        if (request.getPreferencias() == null || request.getPreferencias().size() != 3) {
            throw new EstadoInvalidoException("El estudiante debe seleccionar exactamente 3 preferencias.");
        }

        List<Postulacion> nuevasPostulaciones = new ArrayList<>();
        Set<Integer> electivosSeleccionados = new HashSet<>();

        for (ElectivoPreferenciaDTO preferencia : request.getPreferencias()) {
            int electivoId = preferencia.getElectivoId();

            // 3. Validar si el electivo ya fue seleccionado
            if (!electivosSeleccionados.add(electivoId)) {
                throw new EstadoInvalidoException("El electivo con ID " + electivoId + " está duplicado en las preferencias.");
            }

            // 4. Obtener Electivo
            Electivo electivo = electivoRepository.findById(electivoId)
                    .orElseThrow(() -> new RecursoNoEncontradoException("Electivo no encontrado con ID: " + electivoId));

            // 5. Crear la Postulación
            Postulacion postulacion = new Postulacion();
            postulacion.setEstudiante(estudiante);
            postulacion.setElectivo(electivo);
            postulacion.setFechaPostulacion(LocalDateTime.now());
            postulacion.setEstado(Estado.PENDIENTE);
            postulacion.setPrioridad(preferencia.getPrioridad());

            nuevasPostulaciones.add(postulacion);
        }

        // 6. Guardar todas las postulaciones en lote
        return postulacionRepository.saveAll(nuevasPostulaciones);
    }

    // ---------------------------------------------------
    // MÉTODO DE NEGOCIO (Asignación Masiva)
    // ---------------------------------------------------

    @Transactional
    public List<Postulacion> procesarAsignaciones() {

        // 1. Obtener todas las postulaciones PENDIENTES (Se asume findByEstado(Estado) en PostulacionRepository)
        List<Postulacion> postulacionesPendientes = postulacionRepository.findByEstado(Estado.PENDIENTE);

        // Ordenar por prioridad y luego por fecha (desempate)
        postulacionesPendientes.sort(Comparator
                .comparing(Postulacion::getPrioridad)
                .thenComparing(Postulacion::getFechaPostulacion)
        );

        List<Postulacion> asignacionesFinales = new ArrayList<>();
        Set<Integer> estudiantesAceptadosId = new HashSet<>();

        // 2. Iterar por prioridades (1, 2, 3)
        for (int prioridad = 1; prioridad <= 3; prioridad++) {

            final int currentPrioridad = prioridad;
            // Filtrar las postulaciones de la prioridad actual que aún no han sido aceptadas
            List<Postulacion> postulacionesEnPrioridad = postulacionesPendientes.stream()
                    .filter(p -> p.getPrioridad() == currentPrioridad && !estudiantesAceptadosId.contains(p.getEstudiante().getId()))
                    .collect(Collectors.toList());


            for (Postulacion p : postulacionesEnPrioridad) {
                int estudianteId = p.getEstudiante().getId();
                Electivo electivo = p.getElectivo();

                // Re-verificar si el estudiante ya fue asignado
                if (estudiantesAceptadosId.contains(estudianteId)) {
                    p.setEstado(Estado.RECHAZADA);
                    continue;
                }

                // Aplicar lógica de asignación
                if (electivo.getCupos() > 0) {
                    p.setEstado(Estado.ACEPTADA);
                    electivo.setCupos(electivo.getCupos() - 1); // Disminuir cupo
                    electivoRepository.save(electivo); // Persistir el cambio de cupos
                    estudiantesAceptadosId.add(estudianteId);
                    asignacionesFinales.add(p);
                } else {
                    p.setEstado(Estado.RECHAZADA);
                }
                // Las postulaciones Pendientes son la misma lista que postulacionesPendientes.
                // Los cambios de estado (ACEPTADA/RECHAZADA) se persistirán al final.
            }
        } // Fin del bucle de prioridades

        // 5. Limpieza final: Rechazar PENDIENTES que no fueron procesadas
        postulacionesPendientes.stream()
                .filter(p -> p.getEstado() == Estado.PENDIENTE)
                .forEach(p -> p.setEstado(Estado.RECHAZADA));

        // 6. Guardar todos los cambios de estado (ACEPTADA o RECHAZADA)
        // Se guardan los estados de todas las postulaciones pendientes procesadas.
        postulacionRepository.saveAll(postulacionesPendientes);

        return asignacionesFinales;
    }

    // --- MÉTODOS CRUD BÁSICOS (Usando JPA Repository) ---

    // 1. Obtener Postulación por ID (GET)
    public Postulacion obtenerPostulacionPorId(int id) {
        return postulacionRepository.findById(id)
                .orElseThrow(() -> new RecursoNoEncontradoException("Postulación no encontrada con ID: " + id));
    }

    // 2. Listar Postulaciones (GET)
    public List<Postulacion> listaPostulaciones (){
        return postulacionRepository.findAll();
    }

    // 3. Eliminar Postulación por ID (DELETE)
    public boolean eliminarPostulacionPorId(int postulacionId) {
        // Verificar existencia y eliminar
        Postulacion postulacion = obtenerPostulacionPorId(postulacionId);
        postulacionRepository.delete(postulacion);
        return true;
    }

}
