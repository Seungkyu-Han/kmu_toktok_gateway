package org.seungkyu.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun openApi(): OpenAPI {

        return OpenAPI()
            .components(Components())
            .info(
                Info().apply {
                    title = "PROMPTING API"
                    description = "PROMPTING Swagger"
                    version = "1.0.0"
                }
            )
    }
}