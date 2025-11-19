package latinasincloud.repository;
import latinasincloud.model.Profesor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Profesor.
 */
@Repository
public interface IProfesorRepository extends JpaRepository<Profesor, Integer> {
    Profesor findByEmail(String email);
}