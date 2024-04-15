package perriAlessandro.DeviceManagementJwtAuthentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class Config {

    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        // Disabilito comportamenti di default
        httpSecurity.formLogin(http -> http.disable()); //No form di Login
        httpSecurity.csrf(http -> http.disable()); // No  protezione da CSRF (per l'applicazione media non è necessaria e complicherebbe tutta la faccenda, anche lato FE)
        httpSecurity.sessionManagement(http -> http.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        // Poi aggiungo filtri custom
        // Aggiungere/Rimuovere determinate regole di protezione per gli endpoint
        // Possiamo decidere se debba essere necessaria o meno un'autenticazione per accedere agli endpoint
        httpSecurity.authorizeHttpRequests(http -> http.requestMatchers("/**").permitAll());

        return httpSecurity.build();
    }
}
