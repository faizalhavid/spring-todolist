package faizal.project.todo_list.config;

import faizal.project.todo_list.services.AccountDetailsService;
import faizal.project.todo_list.utils.JWTAuthFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import static org.springframework.security.config.Customizer.withDefaults;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final AccountDetailsService accountDetailsService;
    private final JWTAuthFilter jwtAuthFilter;


    public SecurityConfig(AccountDetailsService accountDetailsService, JWTAuthFilter jwtAuthFilter) {
        this.accountDetailsService = accountDetailsService;
        this.jwtAuthFilter = jwtAuthFilter;

    }

  @Bean
SecurityFilterChain defaultSecurityFilterChain(HttpSecurity http) throws Exception {
    http.authorizeHttpRequests((requests) -> requests
            .requestMatchers("/auth/**").permitAll()
            .requestMatchers(HttpMethod.GET, "/authentication-docs/**").permitAll()
            .anyRequest().authenticated());
    http.sessionManagement((session) -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
    http.formLogin(withDefaults());
    http.httpBasic(withDefaults());
    http.csrf(AbstractHttpConfigurer::disable);
    http.addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);
    return http.build();
}

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

  @Bean
public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
    AuthenticationManagerBuilder authenticationManagerBuilder =
            http.getSharedObject(AuthenticationManagerBuilder.class);
    authenticationManagerBuilder
            .userDetailsService(accountDetailsService)
            .passwordEncoder(passwordEncoder());
    return authenticationManagerBuilder.build();
}
}
