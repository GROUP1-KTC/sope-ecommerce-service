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
            auth.requestMatchers("/auth/**").permitAll();

            auth.requestMatchers("/**").permitAll();

            // Test Socket
            auth.requestMatchers("/messages/**").permitAll();
            auth.requestMatchers("/ws/**").permitAll();


            // User APIs
            auth.requestMatchers(HttpMethod.GET, "/users").hasRole("ADMIN");
            auth.requestMatchers(HttpMethod.OPTIONS, "/users/me").hasAnyRole("USER", "ADMIN");
            auth.requestMatchers(HttpMethod.PUT, "/users/**").hasAnyRole("USER", "ADMIN");

            // Admin APIs
            auth.requestMatchers("/admin/**").hasRole("ADMIN");

            // Default
            auth.anyRequest().authenticated();
        }
    }

