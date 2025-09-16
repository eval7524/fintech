package com.zerobase.fintech.config;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.tags.Tag;
import java.util.List;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class WebConfig {

  @Configuration
  public class swaggerConfig {

    @Bean
    public OpenAPI openAPI() {
      return new OpenAPI().components(new Components()).info(apiInfo())
          .tags(List.of(
              new Tag().name("Auth").description("Authentication API"),
              new Tag().name("Account").description("Account API"),
              new Tag().name("Transaction").description("Transaction API")
          ));
    }

    private Info apiInfo() {
      return new Info()
          .title("Fintech API")
          .description("핀테크 프로젝트 Swagger-UI API Document")
          .version("v1.0");
    }

  }

}
