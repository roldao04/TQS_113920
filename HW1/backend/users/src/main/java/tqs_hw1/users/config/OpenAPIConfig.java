package tqs_hw1.users.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${USERS_API_URL:http://localhost:8081}")
    private String devUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Contact contact = new Contact();
        contact.setEmail("roldao@ua.pt");
        contact.setName("Roldão Martins");
        contact.setUrl("https://github.com/roldao04");

        License mitLicense = new License().name("MIT License").url("https://choosealicense.com/licenses/mit/");

        Info info = new Info()
                .title("Moliceiro Canteen Users API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints for user management and authentication in the Moliceiro Canteen System.")
                .termsOfService("https://github.com/roldao04/TQS_113920/blob/main/README.md")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
} 