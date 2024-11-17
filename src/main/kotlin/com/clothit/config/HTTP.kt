package com.clothit.config

import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.openapi.*
import io.ktor.server.plugins.swagger.*
import io.ktor.server.routing.*

fun Application.swaggerConfigure() {
    routing {
        swaggerUI(path = "swagger", swaggerFile = "openapi/documentation.yaml") {
            version = "4.15.5"
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