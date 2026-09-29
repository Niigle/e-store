package rs.ac.ni.pmf.rwa.estore.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rs.ac.ni.pmf.rwa.estore.security.JwtAuthenticationFilter;
import rs.ac.ni.pmf.rwa.estore.security.JwtTokenUtil;
import rs.ac.ni.pmf.rwa.estore.security.RestAuthenticationEntryPoint;
import tools.jackson.databind.json.JsonMapper;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenUtil _jwtTokenUtil;
    private final UserDetailsService _userDetailsService;
    private final JsonMapper _jsonMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(final HttpSecurity http) throws Exception
    {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .exceptionHandling(ex -> ex.authenticationEntryPoint(authenticationEntryPoint()))
                .authorizeHttpRequests(auth -> auth
                        // auth i javni resursi
                        .requestMatchers("/api/v1/auth/login", "/api/v1/auth/refresh").permitAll()
                        .requestMatchers(
                                "/", "/index.html", "/assets/**", "/*.js", "/*.css",
                                "/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**",
                                "/doc", "/api-docs/**", "/favicon.ico",
                                "/actuator/health"
                        ).permitAll()

                        .requestMatchers(HttpMethod.GET, "/api/v1/products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/stores/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/store-products/**").permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/category/**").permitAll()
                        .requestMatchers("/error").permitAll()
                        .requestMatchers("/api/v1/users").permitAll()

                        .requestMatchers(HttpMethod.DELETE, "/api/v1/products/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.POST, "/api/v1/stores/**").hasRole("Admin")
                        .requestMatchers(HttpMethod.DELETE, "/api/v1/stores/**").hasRole("Admin")
                        .requestMatchers("/api/v1/category/**").hasRole("Admin")

                        .requestMatchers(HttpMethod.POST, "/api/v1/products/**").hasAnyRole("Admin", "Manager")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/products/**").hasAnyRole("Admin", "Manager")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/stores/**").hasAnyRole("Admin", "Manager")
                        .requestMatchers(HttpMethod.PUT, "/api/v1/store-products/**").hasAnyRole("Admin", "Manager")
                        .requestMatchers(HttpMethod.POST, "/api/v1/store-products/**").hasAnyRole("Admin", "Manager")

                        .requestMatchers("/api/v1/order/**").hasAnyRole("User", "Manager", "Admin")

                        .requestMatchers("/api/v1/**").authenticated()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(new JwtAuthenticationFilter(_jwtTokenUtil, _userDetailsService),
                        UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint()
    {
        return new RestAuthenticationEntryPoint(_jsonMapper);
    }
}