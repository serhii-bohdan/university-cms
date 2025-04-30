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
 * Centralized configuration class for Spring Security in the university management system.
 * <p>
 * This class configures authentication, authorization, and password encoding mechanisms for the
 * application. It uses the following Spring annotations to enable security features:
 * <ul>
 *   <li>{@link Configuration}: Designates this class as a source of bean definitions for the Spring application context.</li>
 *   <li>{@link EnableWebSecurity}: Enables core Spring Security web security features.</li>
 *   <li>{@link EnableMethodSecurity}: Activates method-level security using annotations.</li>
 * </ul>
 * The class defines a {@link SecurityFilterChain} for HTTP request authorization and a
 * {@link PasswordEncoder} for secure password hashing using BCrypt.
 *
 * @author Serhii Bohdan
 * @see Configuration
 * @see EnableWebSecurity
 * @see EnableMethodSecurity
 * @see SecurityFilterChain
 * @see PasswordEncoder
 */
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    /**
     * Base URL path for the application.
     * Defines the root path ("/") used for mapping requests to the home page or default endpoint.
     */
    private static final String BASE_PATH_URL = "/";

    /**
     * The default URL for redirecting users after successful login or logout.
     */
    private static final String DEFAULT_LOGIN_LOGOUT_SUCCESS_URL = "/ui/v1/home";

    /**
     * The BCrypt version used for password encoding.
     */
    private static final BCryptPasswordEncoder.BCryptVersion B_CRYPT_VERSION = BCryptPasswordEncoder.BCryptVersion.$2B;

    /**
     * The strength (work factor) for BCrypt password encoding, determining computational cost.
     */
    private static final int STRENGTH = 12;

    /**
     * Configures and provides a {@link PasswordEncoder} bean using BCrypt.
     * <p>
     * Creates a {@link BCryptPasswordEncoder} instance with the specified {@link #B_CRYPT_VERSION},
     * {@link #STRENGTH}, and a {@link SecureRandom} instance for enhanced security.
     *
     * @return a configured {@link BCryptPasswordEncoder} instance
     */
    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(B_CRYPT_VERSION, STRENGTH, new SecureRandom());
    }

    /**
     * Configures the {@link SecurityFilterChain} for securing HTTP requests.
     * <p>
     * Defines security rules, including:
     * <ul>
     *   <li>Permitting access to static resources ("/css/**", "/webjars/**", "/images/**").</li>
     *   <li>Permitting access to {@link #BASE_PATH_URL} and {@link #DEFAULT_LOGIN_LOGOUT_SUCCESS_URL}.</li>
     *   <li>Requiring authentication for all other requests.</li>
     *   <li>Enabling form-based login with "/login-form" page, processing at "/login", and redirecting
     *       to {@link #DEFAULT_LOGIN_LOGOUT_SUCCESS_URL} on success.</li>
     *   <li>Configuring logout at "/logout" to invalidate sessions, clear "JSESSIONID" cookie, and
     *       redirect to {@link #DEFAULT_LOGIN_LOGOUT_SUCCESS_URL}.</li>
     * </ul>
     *
     * @param httpSecurity the {@link HttpSecurity} for configuring security settings
     * @return a configured {@link SecurityFilterChain} instance
     * @throws Exception if an error occurs during configuration
     */
    @Bean
    SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.authorizeHttpRequests(auth -> auth
                .requestMatchers("/css/**", "/webjars/**", "/images/**").permitAll()
                .requestMatchers(BASE_PATH_URL, DEFAULT_LOGIN_LOGOUT_SUCCESS_URL).permitAll()
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
