package org.django.tennis.security;


import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

    private final DymaUserDetailsService dymaUserDetailsService;

    public SecurityConfiguration(DymaUserDetailsService dymaUserDetailsService) {
        this.dymaUserDetailsService = dymaUserDetailsService;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(dymaUserDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);

        return new ProviderManager(authenticationProvider);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .headers(headers ->
                        headers
                                .contentSecurityPolicy(csp ->
                                        csp.policyDirectives("default-src 'self' data:;" +
                                                "style-src 'self' maxcdn.bootstrapcdn.com getbootstrap.com 'unsafe-inline';"))

                                .frameOptions(frameOptionsConfig -> frameOptionsConfig.deny())
                                .permissionsPolicy(permissions ->
                                        permissions.policy(
                                                "fullscreen=(self), geolocation=(), microphone=(), camera=()"
                                        )
                                )

                )

                .authorizeHttpRequests(authorizations ->
                        authorizations
                                .requestMatchers("/healthcheck").permitAll()
                                .requestMatchers(HttpMethod.GET, "/players/**").hasAuthority("ROLE_USER")
                                .requestMatchers(HttpMethod.PUT, "/players/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.POST, "/players/**").hasAuthority("ROLE_ADMIN")
                                .requestMatchers(HttpMethod.DELETE, "/players/**").hasAuthority("ROLE_ADMIN")
                                .anyRequest().authenticated()
                )
                .formLogin(form ->
                        form.defaultSuccessUrl("/swagger-ui/index.html#/", true)
                );

        return http.build();
    }
}
