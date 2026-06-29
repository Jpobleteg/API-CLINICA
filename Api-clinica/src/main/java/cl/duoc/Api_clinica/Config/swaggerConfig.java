package cl.duoc.Api_clinica.Config;

import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI customOpenAPI() {

        return new OpenAPI()
                .info(new Info()
                        .title("Microservicio de Triage Clínico")
                        .version("1.0")
                        .description("API para la gestión de pacientes, clasificación de prioridad clínica y comunicación con el microservicio de Gestión de Camas.")
                        .contact(new Contact()
                                .name("Sigrid Monsalve")
                                .email("sigrid@duoc.cl"))
                        .license(new License()
                                .name("Proyecto Académico - Duoc UC")))
                .externalDocs(new ExternalDocumentation()
                        .description("Caso de estudio: Ecosistema de Contingencia para Triage Clínico y Gestión de Camas"));

    }
}