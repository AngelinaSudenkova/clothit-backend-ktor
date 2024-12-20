package com.clothit.config

import com.clothit.server.service.impl.JwtServiceImpl
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.plugins.callloging.*
import io.ktor.server.plugins.cors.routing.*
import org.koin.ktor.ext.inject
import org.slf4j.LoggerFactory
import org.slf4j.event.Level


fun Application.configureSecurity() {
    val jwtService: JwtServiceImpl by inject()
    install(Authentication) {
        jwt {
            realm = jwtService.realm
            verifier(jwtService.jwtVerifier)
            validate { credential ->
                jwtService.validateToken(credential)
            }

        }
    }
}

fun Application.corsConfigure() {
    install(CORS) {
        anyHost()
        allowHeader(HttpHeaders.ContentType)
        allowHeader(HttpHeaders.Authorization)
        allowMethod(HttpMethod.Options)
        allowMethod(HttpMethod.Put)
        allowMethod(HttpMethod.Get)
        allowMethod(HttpMethod.Patch)
        allowMethod(HttpMethod.Delete)
    }
}

    fun Application.configureLogging() {
        install(CallLogging) {
            level = Level.INFO
            logger = LoggerFactory.getLogger("io.ktor")
        }
    }
