package latinasincloud.repository;

import latinasincloud.model.Postulacion;
import latinasincloud.model.Estado;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;

// Interfaz para el repositorio de Postulacion.
// Extiende JpaRepository para obtener los métodos CRUD básicos.
@Repository
public interface IPostulacionRepository extends JpaRepository<Postulacion, Integer> {

    // ESTO RESUELVE EL ERROR: Spring Data JPA crea la implementación de este método
    // para buscar todas las postulaciones que coincidan con el estado dado.
    List<Postulacion> findByEstado(Estado estado);
}
