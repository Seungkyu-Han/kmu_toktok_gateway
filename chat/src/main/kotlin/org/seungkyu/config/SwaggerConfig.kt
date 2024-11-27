package org.seungkyu.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.servers.Server
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig(
    @Value("\${swagger.url}")
    val path: String
) {

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
            .addServersItem(Server().url(path).description("API for Swagger"))
    }
}