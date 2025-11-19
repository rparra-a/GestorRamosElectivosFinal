package latinasincloud.config;

import latinasincloud.service.JpaUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)

                .authorizeHttpRequests(auth -> auth

                        // Permitir la creación de usuarios sin estar logueado (BOOTSTRAP)
                        .requestMatchers(HttpMethod.POST, "/api/administradores", "/api/profesores", "/api/estudiantes").permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/electivos").hasAnyRole("ADMINISTRADOR", "PROFESOR") // Crear Electivo requiere rol

                        .requestMatchers("/api/administradores/asignacion-masiva").hasRole("ADMINISTRADOR")
                        .requestMatchers(HttpMethod.POST, "/api/estudiantes/postular").hasRole("ESTUDIANTE")

                        .requestMatchers(HttpMethod.GET, "/api/administradores/**", "/api/profesores/**", "/api/estudiantes/**", "/api/electivos/**", "/api/postulaciones/**").authenticated()
                        .requestMatchers(HttpMethod.PUT, "/api/administradores/**", "/api/profesores/**").hasRole("ADMINISTRADOR")

                        .anyRequest().authenticated()
                )
                .httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider(
            JpaUserDetailsService userDetailsService,
            PasswordEncoder passwordEncoder) {

        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder);
        return provider;
    }
}