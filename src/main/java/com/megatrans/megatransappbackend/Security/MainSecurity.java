package com.megatrans.megatransappbackend.Security;

import com.megatrans.megatransappbackend.Security.jwt.JwtEntryPoint;
import com.megatrans.megatransappbackend.Security.jwt.JwtTokenFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableMethodSecurity
public class MainSecurity {

    @Autowired
    private JwtEntryPoint jwtEntryPoint;

    @Bean
    public JwtTokenFilter jwtTokenFilter() {
        return new JwtTokenFilter();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // Deshabilitar CSRF
                .cors(cors -> cors.configurationSource(corsConfigurationSource())) // Configuración de CORS
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/**").permitAll() // Rutas públicas
                        .requestMatchers("/admin/**").hasRole("ADMIN") // Rutas para ADMIN
                        .requestMatchers("/empl/**").hasRole("EMPL") // Rutas para empleados
                        .requestMatchers("/uploads/**").permitAll() // Rutas públicas para subidas
                        .requestMatchers("/qr_codes/**").permitAll() // Rutas públicas para subidas

                        .anyRequest().authenticated() // Todas las demás requieren autenticación
                )
                .exceptionHandling(exceptions ->
                        exceptions.authenticationEntryPoint(jwtEntryPoint) // Manejador de excepciones de autenticación
                )
                .sessionManagement(session ->
                        session.sessionCreationPolicy(SessionCreationPolicy.STATELESS) // Política sin estado
                )
                .addFilterBefore(jwtTokenFilter(), UsernamePasswordAuthenticationFilter.class); // Filtro JWT

        return http.build();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();

        configuration.addAllowedOrigin("http://localhost:4200");
        configuration.addAllowedOrigin("http://localhost"); // Permitir origen del frontend
        configuration.addAllowedMethod("*"); // Permitir todos los métodos HTTP
        configuration.addAllowedHeader("*"); // Permitir todos los encabezados
        configuration.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration); // Aplicar la configuración a todas las rutas
        return source;
    }
}
