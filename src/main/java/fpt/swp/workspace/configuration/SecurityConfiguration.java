package fpt.swp.workspace.configuration;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import java.util.Arrays;
import java.util.Collections;

@Configuration

public class SecurityConfiguration {



    @Autowired
    private ApplicationConfiguration applicationConfiguration;


    @Autowired
    JWTAuthFilter jwtAuthFilter;




    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.csrf(AbstractHttpConfigurer::disable)
                        .authorizeHttpRequests(configure -> configure
                        .requestMatchers("/api/auth/**", "/vnpay/**").permitAll()
                               .requestMatchers("/api/customer/**").hasAuthority("CUSTOMER")
                               .requestMatchers("/api/staff/**").hasAuthority("STAFF")
                               .requestMatchers("api/manager/**").hasAuthority("MANAGER")
                        .anyRequest().authenticated())
                        .sessionManagement(manager -> manager.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                        .authenticationProvider(applicationConfiguration.authenticationProvider())
                        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class) ;
        return http.build();
    }


}
