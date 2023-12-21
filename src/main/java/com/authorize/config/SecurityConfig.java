package com.authorize.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.hierarchicalroles.RoleHierarchy;
import org.springframework.security.access.hierarchicalroles.RoleHierarchyImpl;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
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
import org.springframework.security.web.access.expression.DefaultWebSecurityExpressionHandler;

import com.authorize.service.implementation.CustomUserDetailsService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@EnableWebSecurity
@Configuration	
public class SecurityConfig {
	
	
	protected static final String[] PUBLIC_PATHS = {
            "/v3/api-docs.yaml",
            "/v3/api-docs/**",
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/login",
            "/auth/token",
            "/auth/validate",
            "/users/**",
            "/api/fields"
    };
	
	private static final String ADMIN = "PROJECT_ADMIN";
	private static final String SUPPORTER = "FIELD_SUPPORTER";
	private static final String MANAGER = "FIELD_MANAGER";
	
    protected static final String[] VIEW_ROLES = {ADMIN,SUPPORTER,MANAGER};
    protected static final String[] DELETE_ROLES = {ADMIN,SUPPORTER};
    protected static final String[] UPDATE_ROLES = {ADMIN,SUPPORTER};
    protected static final String CREATE_ROLES = ADMIN;

    @Bean
    public static RoleHierarchy roleHierarchy() {
    	log.info("setting role hierarchy");
        RoleHierarchyImpl hierarchy = new RoleHierarchyImpl();
        hierarchy.setHierarchy("ROLE_PROJECT_ADMIN > ROLE_FIELD_SUPPORTER\n" +
                "ROLE_FIELD_SUPPORTER > ROLE_FIELD_MANAGER");
        return hierarchy;
    }

    @Bean
	public DefaultWebSecurityExpressionHandler customWebSecurityExpressionHandler() {
    	log.info("security expression handler");
	    DefaultWebSecurityExpressionHandler expressionHandler = new DefaultWebSecurityExpressionHandler();
	    expressionHandler.setRoleHierarchy(roleHierarchy());
	    return expressionHandler;
	}
    
    @Bean
    public UserDetailsService userDetailsService() {
        return new CustomUserDetailsService();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        log.info("auth config");
        return httpSecurity
                .authorizeHttpRequests(authorize -> authorize
                    .requestMatchers(PUBLIC_PATHS).permitAll()
                    .requestMatchers(HttpMethod.GET,"/api/orgs/**").hasAnyRole(VIEW_ROLES)
                    .requestMatchers(HttpMethod.POST,"/api/orgs").hasAnyRole(CREATE_ROLES)
                    .requestMatchers(HttpMethod.PUT,"/api/orgs").hasAnyRole(UPDATE_ROLES)
                    .requestMatchers(HttpMethod.DELETE,"/api/orgs").hasAnyRole(DELETE_ROLES)
                    .anyRequest()
                    .authenticated()
                )
                .httpBasic(Customizer.withDefaults())
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
//                .exceptionHandling()
//                .accessDeniedHandler( new CustomAccessDeniedHandler())
//                .and()
//                .authenticationProvider(customAuthenticationProvider()) 
                .build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
    	log.info("authentication provider");
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    	log.info("authentication configuration");
        return config.getAuthenticationManager();
    }
}