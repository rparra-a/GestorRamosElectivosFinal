package latinasincloud.repository;

import latinasincloud.model.Administrador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repositorio para la entidad Administrador.
 * Extiende JpaRepository para obtener las funcionalidades CRUD estándar.
 */
@Repository
public interface IAdministradorRepository extends JpaRepository<Administrador, Integer> {
    // Spring Data JPA provee automáticamente métodos como save(), findById(), findAll(), deleteById(), etc.
    Administrador findByEmail(String email);
}
