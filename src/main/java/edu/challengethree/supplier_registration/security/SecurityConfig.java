package edu.challengethree.supplier_registration.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    SecurityFilter securityFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf().disable()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .exceptionHandling()
                //If the user is not authenticated, redirect to the login page
                .authenticationEntryPoint((request, response, authException) ->
                        response.sendRedirect("/login"))
                //If the user does not have permission to access the page, redirect to the home page.
                //FOR NOW, it's useless, because I don’t have roles defined
                .accessDeniedHandler((request, response, accessDeniedException) ->
                        response.sendRedirect("/home"))
                .and()
                .authorizeRequests(authorize -> authorize
                        .antMatchers("/css/**", "/js/**", "/images/**", "/templates/**").permitAll()
                        .antMatchers(HttpMethod.GET, "/","/login", "/user-register", "/home", "/view-supplier/{id}", "/supplier-register", "/edit-supplier/{id}").permitAll()
                        .antMatchers(HttpMethod.POST, "/auth/login", "/auth/register").permitAll()
                        .anyRequest().authenticated())
                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
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
