package com.mustafaz.telemed.config;

import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class AppConfig {
//    // Spring Boot already by default configures Thymeleaf using these properties
//    @Bean
//    public SpringTemplateEngine templateEngine(){
//        SpringTemplateEngine templateEngine = new SpringTemplateEngine();
//
//        ClassLoaderTemplateResolver templateResolver = new ClassLoaderTemplateResolver();
//        templateResolver.setPrefix("templates/");
//        templateResolver.setSuffix(".html");
//        templateResolver.setCharacterEncoding("UTF-8");
//
//        templateEngine.setTemplateResolver(templateResolver);
//        return templateEngine;
//    }

    @Bean
    public ModelMapper modelMapperConfig() {
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration()
                .setFieldMatchingEnabled(true) // default is false
                .setFieldAccessLevel(org.modelmapper.config.Configuration.AccessLevel.PRIVATE) // default is PUBLIC
                .setMatchingStrategy(MatchingStrategies.STANDARD);
        return modelMapper;
    }
}
