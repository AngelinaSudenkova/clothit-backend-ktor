package com.clothit.config

import io.github.smiley4.ktorswaggerui.SwaggerUI
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.http.content.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

fun Application.configureSwagger() {
    install(SwaggerUI) {
        swagger {

        }
        info {
            title = "Example API"
            version = "latest"
            description = "Example API for testing and demonstration purposes."
        }
        server {
            url = "http://localhost:8080"
            description = "Development Server"
        }
    }
}

fun Application.openApiConfigure()
{
    routing {
        openAPI(path="openapi", swaggerFile = "openapi/documentation.yaml")
    }
}

fun Application.serializationConfigure() {
    install(ContentNegotiation) {
        json()
    }
}