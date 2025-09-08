    package com.sope.sope_ecommerce_backend.security;

    import org.springframework.context.annotation.Configuration;
    import org.springframework.http.HttpMethod;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;

    @Configuration
    public class AuthorizationRules {

        public void apply(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry auth) {
            // Public routes
            auth.requestMatchers("/", "/login-page", "/user", "/admin").permitAll();
            auth.requestMatchers("/css/**", "/js/**", "/images/**").permitAll();
            auth.requestMatchers("/swagger-ui/**", "/v3/api-docs/**", "/swagger-ui/index.html").permitAll();
            auth.requestMatchers("/api/auth/**").permitAll();

            auth.requestMatchers("/api/**").permitAll();

            // Test Socket
            auth.requestMatchers("/api/messages/**").permitAll();
            auth.requestMatchers("/ws-chat/**").permitAll();


            // User APIs
            auth.requestMatchers(HttpMethod.GET, "/api/users").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.OPTIONS, "/api/users/me").hasAnyRole("USER", "ADMIN");
            auth.requestMatchers(HttpMethod.PUT, "/api/users/**").hasAnyRole("USER", "ADMIN");

            // Admin APIs
            auth.requestMatchers("/api/admin/**").hasRole("ADMIN");

            // Default
            auth.anyRequest().authenticated();
        }
    }

