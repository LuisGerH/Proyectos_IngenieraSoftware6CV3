package com.holamundo.HOLASPRING6CV3.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.security.config.http.SessionCreationPolicy;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(cors -> cors.configurationSource(corsConfigurationSource()))
            .csrf(csrf -> csrf.disable()) // Deshabilitamos CSRF para APIs REST
            .authorizeHttpRequests(auth -> auth
                .requestMatchers("/registro", "/login", "/testConnection", "/css/**", "/js/**", "/images/**").permitAll()
                .requestMatchers("/cambiar-tema").authenticated()
                .requestMatchers("/api/favoritos/**").authenticated()
                .requestMatchers("/api/recomendaciones").authenticated()
                .requestMatchers("/user/api/**").authenticated() // APIs REST requieren autenticación
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .requestMatchers("/user/**").hasAnyRole("USER","ADMIN")
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")
                .loginProcessingUrl("/procesar_login")
                .defaultSuccessUrl("/home", true)
                .failureUrl("/login?error=true")
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true")
                .permitAll()
            )
            // Configuración especial para endpoints de API REST
            .exceptionHandling(exceptions -> exceptions
                .defaultAuthenticationEntryPoint(apiAuthenticationEntryPoint())
            )
            // Configuración de sesiones
            .sessionManagement(session -> session
            .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            .maximumSessions(1)
            .maxSessionsPreventsLogin(false)
            .and()
            .sessionFixation().migrateSession()
            .cookieName("JSESSIONID")
            .cookieHttpOnly(true)
            .cookieSecure(false)
            );

        return http.build();
    }

    @Bean
    public AuthenticationEntryPoint apiAuthenticationEntryPoint() {
        return (request, response, authException) -> {
            System.out.println("Authentication entry point triggered for: " + request.getRequestURI());
            System.out.println("User authenticated: " + (request.getUserPrincipal() != null));
            System.out.println("Session ID: " + request.getSession().getId());
            
            // Si es una petición a la API REST, devolver JSON en lugar de redirigir
            if (isApiRequest(request)) {
                response.setContentType("application/json;charset=UTF-8");
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                response.getWriter().write("{\"error\":\"No autorizado\",\"code\":\"UNAUTHORIZED\",\"message\":\"Debe iniciar sesión para acceder a este recurso\"}");
            } else {
                // Para peticiones web normales, redirigir al login
                response.sendRedirect("/login");
            }
        };
    }

    private boolean isApiRequest(HttpServletRequest request) {
        String uri = request.getRequestURI();
        String acceptHeader = request.getHeader("Accept");
        String contentType = request.getHeader("Content-Type");
        
        System.out.println("Checking if API request - URI: " + uri + ", Accept: " + acceptHeader + ", Content-Type: " + contentType);
        
        // Verificar si es una petición a la API REST
        boolean isApiUri = uri.startsWith("/api/") || 
                          uri.startsWith("/user/api/") || 
                          uri.startsWith("/admin/api/");
        
        boolean hasJsonAccept = acceptHeader != null && acceptHeader.contains("application/json");
        boolean hasJsonContentType = contentType != null && contentType.contains("application/json");
        
        boolean isApi = isApiUri || hasJsonAccept || hasJsonContentType;
        System.out.println("Is API request: " + isApi);
        
        return isApi;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration configuration = new CorsConfiguration();
        configuration.setAllowedOriginPatterns(Arrays.asList("*"));
        configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(Arrays.asList("*"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(3600L); // Cache preflight response for 1 hour
        
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }
}