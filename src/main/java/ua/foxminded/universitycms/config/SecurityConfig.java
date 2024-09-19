package ua.foxminded.universitycms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import java.security.SecureRandom;

/**
 * Centralized configuration class for Spring Security, handling authentication, authorization,
 * and password encoding within the application.
 *
 * <p>This class leverages Spring Security's annotations for streamlined setup:
 * <ul>
 *   <li>{@link Configuration}: Marks this class as a source of Spring beans.</li>
 *   <li>{@link EnableWebSecurity}: Activates core web security features.</li>
 *   <li>{@link EnableMethodSecurity}: Enables method-level security annotations.</li>
 * </ul>
 *
 * @author Serhii Bohdan
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Default URL for both successful login and logout redirects.
     */
    private static final String DEFAULT_LOGIN_LOGOUT_SUCCESS_URL = "/ui/v1/home";

    /**
     * The BCrypt version to use for password encoding.
     */
    private static final BCryptPasswordEncoder.BCryptVersion B_CRYPT_VERSION = BCryptPasswordEncoder.BCryptVersion.$2B;

    /**
     * The strength (work factor) for password encoding.
     */
    private static final int STRENGTH = 12;

    /**
     * Creates a {@link PasswordEncoder} bean using BCrypt with the specified version and strength.
     *
     * @return a {@link BCryptPasswordEncoder} instance with configured settings
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(B_CRYPT_VERSION, STRENGTH, new SecureRandom());
    }

    /**
     * Configures the {@link SecurityFilterChain} for HTTP request authorization.
     * <p>
     * This method defines access rules for various request patterns, login and logout processes:
     * <ul>
     *   <li>Permits access to static resources (CSS, webjars, images).</li>
     *   <li>Permits access to the default login and logout success URL.</li>
     *   <li>Requires authentication for all other requests.</li>
     *   <li>Configures form-based login with a custom login page and success URL.</li>
     *   <li>Configures logout handling to invalidate sessions and clear cookies.</li>
     * </ul>
     *
     * @param httpSecurity The {@link HttpSecurity} object to configure.
     * @return A configured {@link SecurityFilterChain} instance.
     * @throws Exception If an error occurs during configuration.
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/webjars/**", "/images/**").permitAll()
                .requestMatchers(DEFAULT_LOGIN_LOGOUT_SUCCESS_URL).permitAll()
                .anyRequest().authenticated())
            .formLogin(form -> form
                .loginPage("/login-form")
                .loginProcessingUrl("/login")
                .defaultSuccessUrl(DEFAULT_LOGIN_LOGOUT_SUCCESS_URL, true)
                .permitAll())
            .logout(logout -> logout
                .logoutUrl("/logout")
                .invalidateHttpSession(true)
                .deleteCookies("JSESSIONID")
                .logoutSuccessUrl(DEFAULT_LOGIN_LOGOUT_SUCCESS_URL)
                .permitAll())
            .build();
    }

}
