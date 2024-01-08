package ua.foxminded.universitycms;

import org.modelmapper.ModelMapper;
import org.modelmapper.config.Configuration.AccessLevel;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * The {@code ApplicationConfiguration} class provides configuration for the
 * application.
 * <p>
 * This class is annotated with {@code @Configuration} to indicate that it is a
 * configuration class. It also uses {@code @ComponentScan} to specify the base
 * packages to scan for components.
 *
 * @author Serhii Bohdan
 */
@Configuration
@ComponentScan(basePackages = "ua.foxminded.universitycms")
public class ApplicationConfiguration {

    /**
     * Creates and configures a {@code ModelMapper} bean.
     * <p>
     * The configuration for the {@link ModelMapper} is set to use strict matching
     * strategies, enable field matching, skip null values, and set the field access
     * level to private.
     *
     * @return the configured {@code ModelMapper} bean
     */
    @Bean
    ModelMapper modelMapper() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setMatchingStrategy(MatchingStrategies.STRICT)
                .setFieldMatchingEnabled(true)
                .setSkipNullEnabled(true)
                .setFieldAccessLevel(AccessLevel.PRIVATE);

        return modelMapper;
    }

}