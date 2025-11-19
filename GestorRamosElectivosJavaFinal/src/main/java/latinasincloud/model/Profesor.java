package latinasincloud.model;


import jakarta.persistence.*;
import java.util.List;

/**
 * Entidad que representa a un Profesor, puede dictar múltiples Electivos.
 */

// CORRECIÓN: Se agrega extends Usuario
// ---------- Se eliminan duplicados de getter, setter e inicialización de atributos.
@Entity
@Table(name = "profesores")
public class Profesor extends Usuario{

    @Column(nullable = false, length = 100)
    private String especialidad;

    // Relación Bidireccional: Un Profesor puede tener muchos Electivos
    // 'mappedBy' indica el nombre del campo en la entidad Electivo que es la FK.
    @OneToMany(mappedBy = "profesor", fetch = FetchType.LAZY)
    private List<Electivo> electivosDictados;

    // Constructor sin argumentos requerido por JPA
    public Profesor() {
    }

    // Getters y Setters (Asegúrate de que existan)


    public String getEspecialidad() {
        return especialidad;
    }

    public void setEspecialidad(String especialidad) {
        this.especialidad = especialidad;
    }

    public List<Electivo> getElectivosDictados() {
        return electivosDictados;
    }

    public void setElectivosDictados(List<Electivo> electivosDictados) {
        this.electivosDictados = electivosDictados;
    }
}