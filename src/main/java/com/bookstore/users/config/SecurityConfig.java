package com.bookstore.users.config;


import com.bookstore.users.controller.AuthenticationController;
import com.bookstore.users.entity.Permission;
import com.bookstore.users.enums.PermissionTypes;
import com.bookstore.users.repository.PermissionRepository;
import com.bookstore.users.security.CustomUserDetailService;
import com.bookstore.users.security.JwtFilter;
import com.bookstore.users.security.util.JwtUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.couchbase.CouchbaseProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.intercept.AuthorizationFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {
    private final AuthenticationConfiguration authenticationConfiguration;
    private final UserDetailsService customUserDetailService;
    private final JwtUtil jwtUtil;

    String[] PUBLIC_GET_ENDPOINTS = {
            "/api/books/*",
            "/api/books/image/*",
            "/api/books/images/**"
    };
    @Bean
    public PasswordEncoder passEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception{
       return httpSecurity.cors(AbstractHttpConfigurer::disable)
               .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(request->
                        request.requestMatchers("/api/auth/**").permitAll()
                                .requestMatchers(HttpMethod.GET, PUBLIC_GET_ENDPOINTS).permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/awards/**").permitAll()
                                .requestMatchers(HttpMethod.GET, "/api/genres/**").permitAll()
                        .anyRequest()
                                .fullyAuthenticated())
                .sessionManagement(session-> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
               .addFilterBefore(jwtFilter(), UsernamePasswordAuthenticationFilter.class)
               .authenticationProvider(this.authenticationProvider())
               .build();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthProvider = new DaoAuthenticationProvider();
        daoAuthProvider.setPasswordEncoder(this.passEncoder());
        daoAuthProvider.setUserDetailsService(customUserDetailService);
        return  daoAuthProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager() throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public JwtFilter jwtFilter() {
        return new JwtFilter(jwtUtil, customUserDetailService);
    }
}
