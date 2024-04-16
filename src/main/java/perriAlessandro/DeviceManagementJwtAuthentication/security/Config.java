package perriAlessandro.DeviceManagementJwtAuthentication.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.Arrays;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
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

    @Bean
    PasswordEncoder getBCrypt() {
        return new BCryptPasswordEncoder(11);
        // 11 è il NUMERO DI ROUNDS, ovvero quante volte viene eseguito l'algoritmo. Questo valore
        // è utile per poter personalizzare la velocità di esecuzione di BCrypt. Più è veloce, meno sicure
        // saranno le password, e ovviamente viceversa. Bisogna comunque tenere sempre in considerazione
        // anche il fatto che se lo rendessimo estremamente lento peggiorerebbe la UX. Bisogna trovare il
        // giusto bilanciamento tra le 2.
        // 11 significa che l'algoritmo ogni volta viene eseguito 2^11 volte cioè 2048 volte. Su un computer
        // di prestazioni medie vuol dire circa 100/200 ms
    }

    @Bean
    CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(Arrays.asList("http://localhost:3000", "https://mywonderfulfrontend.com"));
        config.setAllowedMethods(Arrays.asList("*"));
        config.setAllowedHeaders(Arrays.asList("*"));

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", config);
        // Registro la configurazione CORS appena fatta a livello globale su tutti gli endpoint del mio server

        return source;

    }
}
