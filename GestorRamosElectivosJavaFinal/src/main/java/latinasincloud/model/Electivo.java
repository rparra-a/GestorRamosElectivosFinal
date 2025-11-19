package latinasincloud.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa un Electivo, dictado por un Profesor y asociado a Postulaciones.
 */
@Entity
@Table(name = "electivos")
public class Electivo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @Column(nullable = false)
    private int cupos; // Cupos disponibles

    // Relación ManyToOne: Muchos Electivos a Un Profesor (Clave Foránea)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "profesor_id", nullable = false) // Columna FK en la tabla 'electivos'
    private Profesor profesor;

    // Relación Bidireccional: Un Electivo puede tener muchas Postulaciones
    @OneToMany(mappedBy = "electivo", fetch = FetchType.LAZY)
    private List<Postulacion> postulaciones;

    // Constructor sin argumentos requerido por JPA
    public Electivo() {
    }

    // Getters y Setters (Asegúrate de que existan)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public int getCupos() {
        return cupos;
    }

    public void setCupos(int cupos) {
        this.cupos = cupos;
    }

    public Profesor getProfesor() {
        return profesor;
    }

    public void setProfesor(Profesor profesor) {
        this.profesor = profesor;
    }

    public List<Postulacion> getPostulaciones() {
        return postulaciones;
    }

    public void setPostulaciones(List<Postulacion> postulaciones) {
        this.postulaciones = postulaciones;
    }
}