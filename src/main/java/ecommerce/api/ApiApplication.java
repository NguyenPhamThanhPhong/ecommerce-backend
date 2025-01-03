package ecommerce.api;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.List;
import java.util.Spliterator;
import java.util.logging.Logger;

@SpringBootApplication
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class ApiApplication {
//    public static Logger logger = Logger.getLogger(ApiApplication.class.getName());

    public static void main(String[] args) {
        SpringApplication.run(ApiApplication.class, args);
        System.out.println("Application started successfully");
    }

}
