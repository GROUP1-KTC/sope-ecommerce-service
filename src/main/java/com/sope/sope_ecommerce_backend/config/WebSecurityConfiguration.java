package com.sope.sope_ecommerce_backend.config;

import com.sope.sope_ecommerce_backend.modules.user.service.OurUserDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class WebSecurityConfiguration {
    private final OurUserDetailsService ourUserDetailsService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    @Autowired
    public WebSecurityConfiguration(OurUserDetailsService ourUserDetailsService,
                                    JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.ourUserDetailsService = ourUserDetailsService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    /**
     * Cấu hình bảo mật chính
     */

    @Value("${api.prefix}")
    private String apiPrefix;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http,
                                           CorsConfigurationSource corsConfigurationSource) throws Exception {

        http
                // close CSRF
                .csrf(AbstractHttpConfigurer::disable)

                // config cors
                .cors(cors -> cors.configurationSource(corsConfigurationSource))

                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Configure access endpoints
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "swagger-resources",

                                "swagger-resources/**",
                                "/swagger-ui/**",
                                "/v3/api-docs/**",
                                "/swagger-ui.html",
                                apiPrefix + "/users/register",
                                apiPrefix + "/users/signin",
                                apiPrefix + "/account/refreshtoken",
                                apiPrefix + "/image",
                                apiPrefix + "/product-types",
                                apiPrefix + "/products/**",
                                apiPrefix + "/category",
                                apiPrefix + "/supplier/**",
                                apiPrefix + "/accounts/**",
                                apiPrefix + "/reviews/product/**",
                                "/ws/**"

                        )
                        .permitAll()
                        .anyRequest().authenticated() // otherwise, require authentication
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                ;

        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}