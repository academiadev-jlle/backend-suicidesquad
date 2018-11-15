package br.com.academiadev.suicidesquad.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
    @Bean
    public Docket apis() {
        final List<Parameter> params = new ArrayList<>();
        params.add(authorizationParameter());

        return new Docket(DocumentationType.SWAGGER_2)
                .globalOperationParameters(params)
                .select()
                .apis(RequestHandlerSelectors.basePackage("br.com.academiadev.suicidesquad"))
                .paths(PathSelectors.any())
                .build()
                .apiInfo(apiInfo())
                .useDefaultResponseMessages(false);
    }

    private Parameter authorizationParameter() {
        return new ParameterBuilder()
                .name("Authorization")
                .modelRef(new ModelRef("string"))
                .parameterType("header")
                .required(false)
                .build();
    }

    private ApiInfo apiInfo() {
        return new ApiInfo(
                "Backend SuicideSquad",
                "API REST",
                "1.0",
                "N/A",
                null,
                "MIT",
                "N/A",
                new ArrayList<>()
        );
    }
}
