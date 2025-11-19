package latinasincloud.service;

import latinasincloud.model.Administrador;
import latinasincloud.model.Estudiante;
import latinasincloud.model.Profesor;
import latinasincloud.repository.IAdministradorRepository;
import latinasincloud.repository.IEstudianteRepository;
import latinasincloud.repository.IProfesorRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class JpaUserDetailsService implements UserDetailsService {

    private final IAdministradorRepository adminRepository;
    private final IEstudianteRepository estudianteRepository;
    private final IProfesorRepository profesorRepository;

    public JpaUserDetailsService(
            IAdministradorRepository adminRepository,
            IEstudianteRepository estudianteRepository,
            IProfesorRepository profesorRepository) {
        this.adminRepository = adminRepository;
        this.estudianteRepository = estudianteRepository;
        this.profesorRepository = profesorRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {

        // 1. Buscar en Administradores
        Administrador admin = adminRepository.findByEmail(email);
        if (admin != null) {
            return User.withUsername(admin.getEmail())
                    .password(admin.getPassword()) // Contraseña hasheada de la DB
                    .roles("ADMINISTRADOR")
                    .build();
        }

        // 2. Buscar en Estudiantes
        Estudiante estudiante = estudianteRepository.findByEmail(email);
        if (estudiante != null) {
            return User.withUsername(estudiante.getEmail())
                    .password(estudiante.getPassword()) // Contraseña hasheada de la DB
                    .roles("ESTUDIANTE")
                    .build();
        }

        // 3. Buscar en Profesores
        Profesor profesor = profesorRepository.findByEmail(email);
        if (profesor != null) {
            return User.withUsername(profesor.getEmail())
                    .password(profesor.getPassword()) // Contraseña hasheada de la DB
                    .roles("PROFESOR")
                    .build();
        }

        // Si no se encuentra en ninguna tabla
        throw new UsernameNotFoundException("Usuario no encontrado con el email: " + email);
    }
}