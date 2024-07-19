package ir.moslehi.finalprojectphase4.config;

import ir.moslehi.finalprojectphase4.exception.NotFoundException;
import ir.moslehi.finalprojectphase4.service.AdminService;
import ir.moslehi.finalprojectphase4.service.CustomerService;
import ir.moslehi.finalprojectphase4.service.ExpertService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final AdminService adminService;
    private final ExpertService expertService;
    private final CustomerService customerService;
    private final BCryptPasswordEncoder passwordEncoder;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(

                        s -> s.anyRequest().permitAll()).httpBasic(Customizer.withDefaults());

        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Autowired
    public void configureBuild(AuthenticationManagerBuilder auth) throws Exception {
        auth
                .userDetailsService(username -> {
                    try {
                        return customerService.findByEmail(username);
                    } catch (NotFoundException e) {
                        // Try next service
                    }
                    try {
                        return expertService.findByEmail(username);
                    } catch (NotFoundException e) {
                        // Try next service
                    }
                    return adminService.findByEmail(username);
                })
                .passwordEncoder(passwordEncoder);
    }
}