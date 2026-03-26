package com.projetofullstack.workspace_hub.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenApi(){
        return new OpenAPI()
                .info(new Info()
                        .title("WorkSpace Hub API")
                        .version("1.0")
                        .description("Api de gestão de espaços e reservas de coworking")
                        .termsOfService("https://www.linkedin.com/in/alexspeck/")
                );
    }

}
