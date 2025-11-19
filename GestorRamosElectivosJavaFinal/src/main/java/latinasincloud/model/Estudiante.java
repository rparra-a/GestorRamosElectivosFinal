package latinasincloud.model;

import jakarta.persistence.*;
import java.util.List;
import java.util.ArrayList; // Recomendado para inicializar la lista
import com.fasterxml.jackson.annotation.JsonManagedReference; // 💡 Importante para Jackson/JSON

/**
 * Entidad que representa un Estudiante, asociado a múltiples Postulaciones.
 */
@Entity
@Table(name = "estudiantes")
public class Estudiante extends Usuario{

    // CORRECCIÓN: Se eliminan campos de atributos duplicados que ya se heredan de Usuario.java
    // ----------- Se agrega extends Usuario
    // ----------- Se elimina el setter de rut, ya que no se usa el atributo como identificador al final

    // 💡 MODIFICACIÓN CLAVE: @JsonManagedReference rompe el ciclo de serialización.
    @JsonManagedReference
    @OneToMany(mappedBy = "estudiante", fetch = FetchType.LAZY, cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Postulacion> postulaciones = new ArrayList<>(); // Inicialización para evitar NullPointer

    // Constructor sin argumentos requerido por JPA
    public Estudiante() {
    }

    public List<Postulacion> getPostulaciones() {
        return postulaciones;
    }

    public void setPostulaciones(List<Postulacion> postulaciones) {
        this.postulaciones = postulaciones;
    }

    // NOTA: Los getters/setters heredados (getId, getNombre, etc.) no se repiten aquí.
}