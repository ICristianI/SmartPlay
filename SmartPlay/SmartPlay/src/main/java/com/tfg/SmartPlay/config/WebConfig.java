package com.tfg.SmartPlay.config;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Handler para las imágenes de fondos
        registry.addResourceHandler("/uploads/fondos/**")
                .addResourceLocations("file:uploads/fondos/");

        // Handler para las imágenes de usuarios
        registry.addResourceHandler("/uploads/usuarios/**")
                .addResourceLocations("file:uploads/usuarios/");
    }
}


