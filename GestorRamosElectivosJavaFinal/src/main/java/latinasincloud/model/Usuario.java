package latinasincloud.model;

import jakarta.persistence.*; //Corrección: Mapeo de Usuario.java para JPA
@MappedSuperclass
public abstract class Usuario {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, unique = true, length = 100) // Email debe ser único para el login
    private String email;

    @Column(nullable = false, length = 255) // Suficiente para almacenar un hash de contraseña
    private String password;

    @Column(nullable = false, length = 50)
    private String rol;

    // constructor por defecto
    public Usuario(){}

    // constructor con parámetros
    public Usuario(int id, String nombre, String email, String password, String rol) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.password = password;
        this.rol = rol;
    }

    // getters and setters encapsulamiento de los atributos
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getRol() {
        return rol;
    }

    public void setRol(String rol) {
        this.rol = rol;
    }
}
