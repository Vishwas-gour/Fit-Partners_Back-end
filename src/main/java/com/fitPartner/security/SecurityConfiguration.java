package com.fitPartner.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

@Configuration
@EnableMethodSecurity
public class SecurityConfiguration {

    @Autowired
    private final MyOncePerRequest oncePerRequest;

    public SecurityConfiguration(MyOncePerRequest oncePerRequest) {
        this.oncePerRequest = oncePerRequest;
    }

    @Bean
    public SecurityFilterChain chain(HttpSecurity http) throws Exception {
        http
                .csrf (AbstractHttpConfigurer::disable).
                cors (cors -> cors.configurationSource (corsConfiguration ()))

                .authorizeHttpRequests (req -> req

                                .requestMatchers ("/**").permitAll ()
//                        .requestMatchers ("/auth/**").permitAll()
//                        .requestMatchers ("/user/**").hasRole ("USER")
//                        .requestMatchers ("/admin/**").hasRole ("ADMIN")
//                        .anyRequest ().authenticated ()
                )
                .addFilterBefore (oncePerRequest, UsernamePasswordAuthenticationFilter.class)
                .sessionManagement (sess -> sess.sessionCreationPolicy (SessionCreationPolicy.STATELESS));
        return http.build ();
    }

    @Bean
    public CorsConfigurationSource corsConfiguration() {
        CorsConfiguration cors = new CorsConfiguration ();
        cors.setAllowedOrigins (List.of ("http://localhost:5173",  "https://your-frontend.vercel.app"));
        cors.setAllowCredentials (true);
        cors.setAllowedHeaders (List.of ("*"));
        cors.setAllowedMethods (List.of ("POST", "PUT", "DELETE", "GET", "OPTIONS"));

        UrlBasedCorsConfigurationSource url = new UrlBasedCorsConfigurationSource ();
        url.registerCorsConfiguration ("/**", cors);
        return url;
    }

    @Bean
    public AuthenticationManager manager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager ();
    }

    @Bean
    public PasswordEncoder encoder() {
        return new BCryptPasswordEncoder ();
    }
}
