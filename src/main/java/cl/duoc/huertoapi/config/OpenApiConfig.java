package cl.duoc.huertoapi.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI huertoOpenApi() {
        return new OpenAPI().info(new Info()
                .title("API Huerto Escolar")
                .version("1.0.0")
                .description("Microservicio del huerto escolar: sectores, cultivos (con su flujo de tareas) y usuarios."));
    }
}
