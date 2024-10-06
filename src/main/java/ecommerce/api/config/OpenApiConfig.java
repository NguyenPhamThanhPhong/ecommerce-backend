package ecommerce.api.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.swagger.v3.core.jackson.ModelResolver;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.Operation;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.media.DateTimeSchema;
import io.swagger.v3.oas.models.media.MediaType;
import io.swagger.v3.oas.models.media.Schema;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.media.StringSchema;
import io.swagger.v3.oas.models.parameters.Parameter;
import org.springdoc.core.customizers.OpenApiCustomizer;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import java.text.SimpleDateFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

@Configuration
public class OpenApiConfig {

    @Bean
    public GroupedOpenApi publicApi(
            @Value("${openapi.service.group}") String apiDocs,
            OpenApiCustomizer openApiCustomizer) {
        return GroupedOpenApi.builder()
                .group(apiDocs) // /v3/api-docs/api-service
                .packagesToScan(
                        "ecommerce.api.controller"
                )
                .addOpenApiCustomizer(openApiCustomizer)
                .build();
    }

    @Bean
    public ModelResolver modelResolver(ObjectMapper objectMapper) {
        return new ModelResolver(objectMapper);
    }

    @Bean
    public OpenAPI openAPI(
            @Value("${openapi.service.title}") String title,
            @Value("${openapi.service.version}") String version,
            @Value("${openapi.service.server}") String serverUrl
    ) {
        return new OpenAPI()
                .components(
                        new Components()
                                .addSchemas("Byte", new Schema<>().
                                        type("integer").format("int32").description("This is a global schema for Byte type").example(1))
                                .addSchemas("Date", new Schema<>().
                                        type("string").format("date-time").description("This is a global schema for Date type").example("2021-09-01 00:00:00"))
                )
                .info(new Info().title(title)
                        .description("API documents")
                        .license(new License().name("Apache 2.0").url("https://springdoc.org")));
    }

    @Bean
    @Primary
    public OpenApiCustomizer customOpenApiCustomizer() {
        return openApi -> {
            // SET PROPERTIES OF EACH SCHEMAS
            openApi.getPaths().forEach((path, pathItem) -> {
                pathItem.readOperations().forEach(operation -> {
                    operation.getResponses().forEach((responseCode, apiResponse) -> {
                        if (apiResponse.getContent() != null) {
                            apiResponse.getContent().forEach((mediaType, mediaTypeObject) -> {
                                if (mediaTypeObject.getSchema() != null) {

                                }
                            });
                        }
                    });
                });
            });

            for (Map.Entry<String, Schema> schema : openApi.getComponents().getSchemas().entrySet()) {
                LinkedHashMap<String, Object> propertiesMap = (LinkedHashMap<String, Object>) schema.getValue().getProperties();
                for (var entry : propertiesMap.entrySet()) {
                    if (entry.getValue() instanceof StringSchema) {
                        if (Objects.equals(((StringSchema) entry.getValue()).getFormat(), "byte")) {
                            ((StringSchema) entry.getValue()).format("int32").type("integer");
                        }
                    }
                }
            }
        };
    }

}
