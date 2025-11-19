package latinasincloud.model;
import java.time.LocalDateTime;


import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonBackReference; // 💡 ¡NUEVO IMPORT!

/**
 * Entidad que representa la Postulación de un Estudiante a un Electivo.
 */
@Entity
@Table(name = "postulaciones")
public class Postulacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    // Relación ManyToOne: Muchas Postulaciones a Un Estudiante (Clave Foránea)
    // 💡 MODIFICACIÓN CLAVE: @JsonBackReference rompe el ciclo de serialización.
    @JsonBackReference // ⬅️ Le dice a Jackson: "Ignora este campo al serializar para evitar recursión."
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "estudiante_id", nullable = false)
    private Estudiante estudiante;

    // Relación ManyToOne: Muchas Postulaciones a Un Electivo (Clave Foránea)
    // NOTA: Esta relación puede causar un bucle si Electivo tiene una lista de Postulaciones.
    // Si tienes ese problema, también debes aplicar @JsonBackReference aquí.
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "electivo_id", nullable = false)
    private Electivo electivo;

    // Columna para la prioridad: 1 (máxima), 2, 3 (mínima)
    @Column(nullable = false)
    private int prioridad;

    // Enumeración para el estado (PENDIENTE, ACEPTADA, RECHAZADA)
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private Estado estado = Estado.PENDIENTE; // Valor por defecto

    @Column(nullable = false)
    private LocalDateTime fechaPostulacion = LocalDateTime.now(); // Valor por defecto

    // Constructor sin argumentos requerido por JPA
    public Postulacion() {
    }

    // Getters y Setters (Asegúrate de que existan)

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Estudiante getEstudiante() {
        return estudiante;
    }

    public void setEstudiante(Estudiante estudiante) {
        this.estudiante = estudiante;
    }

    public Electivo getElectivo() {
        return electivo;
    }

    public void setElectivo(Electivo electivo) {
        this.electivo = electivo;
    }

    public int getPrioridad() {
        return prioridad;
    }

    public void setPrioridad(int prioridad) {
        this.prioridad = prioridad;
    }

    public Estado getEstado() {
        return estado;
    }

    public void setEstado(Estado estado) {
        this.estado = estado;
    }

    public LocalDateTime getFechaPostulacion() {
        return fechaPostulacion;
    }

    public void setFechaPostulacion(LocalDateTime fechaPostulacion) {
        this.fechaPostulacion = fechaPostulacion;
    }
}