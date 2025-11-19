package latinasincloud.model;

import jakarta.persistence.*;

/**
 * Entidad que representa a un Administrador en la base de datos.
 */
@Entity
@Table(name = "administradores")
public class Administrador extends Usuario {

    public Administrador() {
        this.setRol("Administrador");
    }

}